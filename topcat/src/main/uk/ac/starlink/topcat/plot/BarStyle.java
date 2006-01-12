package uk.ac.starlink.topcat.plot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.Arrays;

/**
 * Defines a style for plotting a bar in a histogram.
 *
 * @author   Mark Taylor
 * @since    16 Nov 2005
 */
public class BarStyle extends DefaultStyle {

    private final Form form_;
    private final Placement placement_;

    /** Bar form using open rectangles. */
    public static final Form FORM_OPEN = new Form( "open" ) {
        public void drawBar( Graphics g, int x, int y, int width, int height ) {
            Graphics2D g2 = (Graphics2D) g;
            int thickness = ( g2.getStroke() instanceof BasicStroke )
                          ? (int) ((BasicStroke) g2.getStroke()).getLineWidth()
                          : 1;
            int x0 = x + thickness / 2;
            int y0 = y + height;
            int x1 = x + width - 1 - ( ( thickness + 1 ) / 2 );
            int y1 = y;
            int[] xp = new int[] { x0, x0, x1, x1, };
            int[] yp = new int[] { y0, y1, y1, y0, };

            // This doesn't seem to be honouring the JOIN policy.  Why????
            g2.drawPolyline( xp, yp, 4 );
        }
    };

    /** Bar form using filled rectangles. */
    public static final Form FORM_FILLED = new Form( "filled" ) {
        public void drawBar( Graphics g, int x, int y, int width, int height ) {
            g.fillRect( x, y, Math.max( width - 1, 1 ), height );
        }
    };

    /** Bar form using filled 3d rectangles. */
    public static final Form FORM_FILLED3D = new Form( "filled3D" ) {
        public void drawBar( Graphics g, int x, int y, int width, int height ) {
            g.fill3DRect( x, y, Math.max( width - 1, 1 ), height, true );
        }
    };

    /** Bar form drawing only the tops of the bars. */
    public static final Form FORM_TOP = new Form( "top" ) {
        public void drawBar( Graphics g, int x, int y, int width, int height ) {
            g.drawLine( x, y, x + width, y );
        }
        public void drawEdge( Graphics g, int x, int y1, int y2 ) {
            g.drawLine( x, y1, x, y2 );
        }
    };

    /** Bar form using 1-d spikes. */
    public static final Form FORM_SPIKE = new Form( "spike" ) {
        public void drawBar( Graphics g, int x, int y, int width, int height ) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setStroke( getStroke( g2.getStroke(), BasicStroke.CAP_ROUND,
                                     BasicStroke.JOIN_MITER ) );
            int xpos = x + width / 2;
            g2.drawLine( xpos, y, xpos, y + height );
        }
    };

    /** Placement which puts bars next to each other. */
    public static final Placement PLACE_ADJACENT = new Placement( "adjacent" ) {
        public int[] getXRange( int lo, int hi, int iseq, int nseq ) {
            int gap = ( hi - lo - 2 ) / nseq;
            int rlo = lo + 1 + gap * iseq;
            int rhi = rlo + gap + 1;
            return new int[] { rlo, rhi };
        }
    };

    /** Placement which puts bars in the same X region. */
    public static final Placement PLACE_OVER = new Placement( "over" ) {
        public int[] getXRange( int lo, int hi, int iseq, int nseq ) {
            return new int[] { lo, hi };
        }
    };

    /**
     * Constructor.
     *
     * @param   color  initial colour
     * @param   form    bar form
     * @param   placement  bar placement
     */
    public BarStyle( Color color, Form form, Placement placement ) {
        super( color, Arrays.asList( new Object[] { form, placement } ) );
        form_ = form;
        placement_ = placement;
    }

    /**
     * Draws a bar for inclusion in a histogram.
     *
     * @param   g  graphics context
     * @param   xlo  lower bound in X direction
     * @param   xhi  upper bound in X direction
     * @param   ylo  lower bound in Y direction
     * @param   yhi  upper bound in Y direction
     * @param   iseq  index of the set being plotted
     * @param   nseq  number of sets being plotted for this bar
     */
    public void drawBar( Graphics g, int xlo, int xhi, int ylo, int yhi,
                         int iseq, int nseq ) {
        Graphics2D g2 = (Graphics2D) g;
        Color col = g.getColor();
        Stroke str = g2.getStroke();
        g.setColor( getColor() );
        g2.setStroke( getStroke( BasicStroke.CAP_SQUARE,
                                 BasicStroke.JOIN_MITER) );
        int[] xRange = placement_.getXRange( xlo, xhi, iseq, nseq );
        int x = xRange[ 0 ];
        int width = xRange[ 1 ] - xRange[ 0 ];
        form_.drawBar( g, xRange[ 0 ], ylo,
                          xRange[ 1 ] - xRange[ 0 ], yhi - ylo );
        g.setColor( col );
        g2.setStroke( str );
    }

    /**
     * Draws the edge of a bar.  This can be invoked to draw the boundary
     * between one bar and its immediate neighbour; the edge described 
     * by the call's parameters is not the edge of the block representing
     * the bar's data, but the edge between the current bar and its
     * neighbour on one side or the other, so it may go up or down from
     * the Y value.  For many bar styles this will be a no-op.
     *
     * @param   g    graphics context
     * @param   x    x position of the edge
     * @param   y1   one y value for the edge
     * @param   y2   other y value for the edge
     * @param   iseq  index of the set being plotted
     * @param   nseq  number of sets being plotted for this bar
     */
    public void drawEdge( Graphics g, int x, int y1, int y2,
                          int iseq, int nseq ) {
        Graphics2D g2 = (Graphics2D) g;
        Color col = g.getColor();
        Stroke str = g2.getStroke();
        g.setColor( getColor() );
        g2.setStroke( getStroke( BasicStroke.CAP_SQUARE,
                                 BasicStroke.JOIN_MITER ) );
        form_.drawEdge( g, x, y1, y2 );
        g.setColor( col );
        g2.setStroke( str );
    }

    /**
     * Returns the form of this style.
     *
     * @return  bar form
     */
    public Form getForm() {
        return form_;
    }

    /**
     * Returns the placement of this style.
     *
     * @return  bar placement
     */
    public Placement getPlacement() {
        return placement_;
    }

    public void drawLegend( Graphics g, int x, int y ) {
        drawEdge( g, x - 4, y - 6, y + 6, 0, 3 );
        drawBar( g, x - 4, x + 4, y - 6, y + 6, 0, 3 );
        drawEdge( g, x + 4, y - 6, y + 6, 0, 3 );
    }

    /**
     * Describes the form of a bar style, that is what each bar looks like.
     */
    public static abstract class Form {
        private final String name_;

        protected Form( String name ) {
            name_ = name;
        }

        /**
         * Draws a bar.  The whole region described by 
         * <code>x</code>, <code>y</code>,
         * <code>width</code> and <code>height</code> is available for 
         * drawing in.
         *
         * @param  g   graphics context
         * @param  x   left X coordinate of region (lowest X value permitted)
         * @param  y   lower Y coordinate of region (lowest Y value permitted)
         * @param  width  width of region
         *                (x+width is highest X value permitted)
         * @param  height height of region
         *                (y+height is highest Y value permitted)
         */
        public abstract void drawBar( Graphics g, int x, int y,
                                                  int width, int height );

        /**
         * Draws the edge of a bar.  This can be invoked to draw the boundary
         * between one bar and its immediate neighbour; the edge described
         * by the call's parameters is not the edge of the block representing
         * the bar's data, but the edge between the current bar and its
         * neighbour on one side or the other, so it may go up or down from
         * the Y value.
         *
         * <p>The default implementation does nothing, which is correct for
         * many forms.
         *
         * @param   g    graphics context
         * @param   x    x position of the edge
         * @param   y1   one y value of the edge
         * @param   y2   other y value of the edge
         */
        public void drawEdge( Graphics g, int x, int y1, int y2 ) {
            // no action
        }

        public String toString() {
            return name_;
        }
    }

    /**
     * Describes bar placement, that is how multiple bars covering the same
     * data range are to be arranged.
     */
    public static abstract class Placement {
        private final String name_;

        protected Placement( String name ) {
            name_ = name;
        }

        /**
         * Returns the range of X coordinates to be used for plotting a bar.
         *
         * @param  lo  lower bound of total range for data region
         * @param  hi  upper bound of total range for data region + 1
         * @param  iseq  index of the bar to be plotted in the returned range
         * @param  nseq  total number of bars which will be plotted in 
         *               the data region
         * @return  2-element arrage giving (lower bound, upper bound+1) of
         *          the region the plotted bar should occupy
         */
        public abstract int[] getXRange( int lo, int hi, int iseq, int nseq );

        public String toString() {
            return name_;
        }
    }
}
