package uk.ac.starlink.ttools.plot2.geom;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Arrays;
import javax.swing.Icon;
import uk.ac.starlink.ttools.plot2.Axis;
import uk.ac.starlink.ttools.plot2.BasicTicker;
import uk.ac.starlink.ttools.plot2.Captioner;
import uk.ac.starlink.ttools.plot2.PlotUtil;
import uk.ac.starlink.ttools.plot2.Surface;
import uk.ac.starlink.ttools.plot2.Tick;
import uk.ac.starlink.ttools.plot2.Ticker;
import uk.ac.starlink.ttools.plot2.config.ConfigMap;

/**
 * Surface implementation for flat 2-d plotting.
 *
 * @author   Mark Taylor
 * @since    19 Feb 2013
 */
public class PlaneSurface implements Surface, PlanarSurface {

    private final double dxlo_;
    private final double dxhi_;
    private final double dylo_;
    private final double dyhi_;
    private final boolean xlog_;
    private final boolean ylog_;
    private final boolean xflip_;
    private final boolean yflip_;
    private final int gxlo_;
    private final int gxhi_;
    private final int gylo_;
    private final int gyhi_;
    private final Tick[] xticks_;
    private final Tick[] yticks_;
    private final String xlabel_;
    private final String ylabel_;
    private final Captioner captioner_;
    private final Color gridcolor_;
    private final Color axlabelcolor_;
    private final Axis xAxis_;
    private final Axis yAxis_;

    /**
     * Constructor.
     *
     * @param  gxlo   graphics X coordinate lower bound
     * @param  gxhi   graphics X coordinate upper bound
     * @param  gylo   graphics Y coordinate lower bound
     * @param  gyhi   graphics Y coordinate upper bound
     * @param  dxlo   data X coordinate lower bound
     * @param  dxhi   data X coordinate upper bound
     * @param  dylo   data Y coordinate lower bound
     * @param  dyhi   data Y coordinate upper bound
     * @param  xlog   whether to use logarithmic scaling on X axis
     * @param  ylog   whether to use logarithmic scaling on Y axis
     * @param  xflip  whether to invert direction of X axis
     * @param  yflip  whether to invert direction of Y axis
     * @param  xticks  array of tickmark objects for X axis
     * @param  yticks  array of tickmark objects for Y axis
     * @param  xlabel  text for labelling X axis
     * @param  ylabel  text for labelling Y axis
     * @param  captioner  text renderer for axis labels etc, or null if absent
     * @param  gridcolor  colour of grid lines, or null if not plotted
     * @param  axlabelcolor  colour of axis labels
     */
    public PlaneSurface( int gxlo, int gxhi, int gylo, int gyhi,
                         double dxlo, double dxhi, double dylo, double dyhi,
                         boolean xlog, boolean ylog,
                         boolean xflip, boolean yflip,
                         Tick[] xticks, Tick[] yticks,
                         String xlabel, String ylabel, Captioner captioner,
                         Color gridcolor, Color axlabelcolor ) {
        gxlo_ = gxlo;
        gxhi_ = gxhi;
        gylo_ = gylo;
        gyhi_ = gyhi;
        dxlo_ = dxlo;
        dxhi_ = dxhi;
        dylo_ = dylo;
        dyhi_ = dyhi;
        xlog_ = xlog;
        ylog_ = ylog;
        xflip_ = xflip;
        yflip_ = yflip;
        xticks_ = xticks;
        yticks_ = yticks;
        xlabel_ = xlabel;
        ylabel_ = ylabel;
        captioner_ = captioner;
        gridcolor_ = gridcolor;
        axlabelcolor_ = axlabelcolor;
        xAxis_ = Axis.createAxis( gxlo_, gxhi_, dxlo_, dxhi_, xlog_, xflip_ );
        yAxis_ = Axis.createAxis( gylo_, gyhi_, dylo_, dyhi_, ylog_,
                                  yflip_ ^ PlaneAxisAnnotation.INVERT_Y );
        assert this.equals( this );
    }

    public Rectangle getPlotBounds() {
        return new Rectangle( gxlo_, gylo_, gxhi_ - gxlo_, gyhi_ - gylo_ );
    }

    public Insets getPlotInsets( boolean withScroll ) {
        return createAxisAnnotation().getPadding( withScroll );
    }

    /**
     * Returns 2.
     */
    public int getDataDimCount() {
        return 2;
    }

    public boolean dataToGraphics( double[] dpos, boolean visibleOnly,
                                   Point2D.Double gp ) {
        double gx = xAxis_.dataToGraphics( dpos[ 0 ] );
        double gy = yAxis_.dataToGraphics( dpos[ 1 ] );
        if ( ! visibleOnly ||
             ( gx >= gxlo_ && gx < gxhi_ && gy >= gylo_ && gy < gyhi_ ) ) {
            gp.x = gx;
            gp.y = gy;
            return true;
        }
        else {
            return false;
        }
    }

    public boolean dataToGraphicsOffset( double[] dpos0, Point2D.Double gpos0,
                                         double[] dpos1, boolean visibleOnly,
                                         Point2D.Double gpos1 ) {
        return dataToGraphics( dpos1, visibleOnly, gpos1 );
    }

    public double[] graphicsToData( Point2D gp, Iterable<double[]> dposIt ) {
        return new double[] { xAxis_.graphicsToData( gp.getX() ),
                              yAxis_.graphicsToData( gp.getY() ) };
    }

    public String formatPosition( double[] dpos ) {
        return new StringBuilder()
            .append( formatPosition( xAxis_, dpos[ 0 ] ) )
            .append( ", " )
            .append( formatPosition( yAxis_, dpos[ 1 ] ) )
            .toString();
    }

    public Captioner getCaptioner() {
        return captioner_;
    }

    public void paintBackground( Graphics g ) {
        Color color0 = g.getColor();
        g.setColor( Color.WHITE );
        g.fillRect( gxlo_, gylo_, gxhi_ - gxlo_, gyhi_ - gylo_ );
        if ( gridcolor_ != null ) {
            g.setColor( gridcolor_ );
            for ( int it = 0; it < xticks_.length; it++ ) {
                Tick tick = xticks_[ it ];
                if ( tick.getLabel() != null ) {
                    int gx = (int) xAxis_.dataToGraphics( tick.getValue() );
                    g.drawLine( gx, gylo_, gx, gyhi_ );
                }
            }
            for ( int it = 0; it < yticks_.length; it++ ) {
                Tick tick = yticks_[ it ];
                if ( tick.getLabel() != null ) {
                    int gy = (int) yAxis_.dataToGraphics( tick.getValue() );
                    g.drawLine( gxlo_, gy, gxhi_, gy );
                }
            }
        }
        g.setColor( color0 );
    }

    public void paintForeground( Graphics g ) {
        if ( axlabelcolor_ != null ) {
            Graphics2D g2 = (Graphics2D) g;
            Color color0 = g2.getColor();
            g2.setColor( axlabelcolor_ );
            createAxisAnnotation().drawLabels( g );

            /* Boundary. */
            g2.drawRect( gxlo_, gylo_, gxhi_ - gxlo_, gyhi_ - gylo_ );

            /* Restore. */
            g2.setColor( color0 );
        }
    }

    /**
     * Returns approximate config to recreate this surface's aspect.
     *
     * @return  approximate aspect config
     */
    ConfigMap getAspectConfig() {
        ConfigMap config = new ConfigMap();
        config.putAll( PlotUtil.configLimits( PlaneSurfaceFactory.XMIN_KEY,
                                              PlaneSurfaceFactory.XMAX_KEY,
                                              dxlo_, dxhi_, gxhi_ - gxlo_ ) );
        config.putAll( PlotUtil.configLimits( PlaneSurfaceFactory.YMIN_KEY,
                                              PlaneSurfaceFactory.YMAX_KEY,
                                              dylo_, dyhi_, gyhi_ - gylo_ ) );
        return config;
    }

    public double[][] getDataLimits() {
        return new double[][] { { dxlo_, dxhi_ }, { dylo_, dyhi_ } };
    }

    public boolean[] getLogFlags() {
        return new boolean[] { xlog_, ylog_ };
    }

    public boolean[] getFlipFlags() {
        return new boolean[] { xflip_, yflip_ };
    }

    public boolean[] getTimeFlags() {
        return new boolean[] { false, false };
    }

    public Axis[] getAxes() {
        return new Axis[] { xAxis_, yAxis_ };
    }

    /**
     * Returns a plot aspect representing a view of this surface zoomed
     * in some or all dimensions around the given central position.
     *
     * @param  pos  reference graphics position
     * @param  xZoom  X axis zoom factor
     * @param  yZoom  Y axis zoom factor
     * @return  new aspect
     */
    PlaneAspect zoom( Point2D pos, double xZoom, double yZoom ) {
        return new PlaneAspect(
            xAxis_.dataZoom( xAxis_.graphicsToData( pos.getX() ), xZoom ),
            yAxis_.dataZoom( yAxis_.graphicsToData( pos.getY() ), yZoom ) );
    }

    /**
     * Returns a plot aspect representing a view of this surface panned
     * such that the data that used to appear at one graphics coordinate
     * now appears at another.
     *
     * @param   pos0  source graphics position
     * @param   pos1  destination graphics position
     * @param   xFlag  true iff panning will operate in X direction
     * @param   yFlag  true iff panning will operate in Y direction
     * @return  new aspect, or null
     */
    PlaneAspect pan( Point2D pos0, Point2D pos1,
                     boolean xFlag, boolean yFlag ) {
        if ( xFlag || yFlag ) {
            return new PlaneAspect(
                xFlag ? xAxis_.dataPan( xAxis_.graphicsToData( pos0.getX() ),
                                        xAxis_.graphicsToData( pos1.getX() ) )
                      : new double[] { dxlo_, dxhi_ },
                yFlag ? yAxis_.dataPan( yAxis_.graphicsToData( pos0.getY() ),
                                        yAxis_.graphicsToData( pos1.getY() ) )
                      : new double[] { dylo_, dyhi_ } );
        }
        else {
            return null;
        }
    }

    /**
     * Returns a plot aspect in which the given data position is centred.
     *
     * @param  dpos  data position to end up central
     * @param  xFlag  true to center in X direction
     * @param  yFlag  true to center in Y direction
     * @return  new aspect
     */
    PlaneAspect center( double[] dpos, boolean xFlag, boolean yFlag ) {
        Point2D.Double gp = new Point2D.Double();
        return dataToGraphics( dpos, false, gp ) && PlotUtil.isPointFinite( gp )
             ? pan( gp, new Point2D.Double( ( gxlo_ + gxhi_ ) * 0.5,
                                            ( gylo_ + gyhi_ ) * 0.5 ),
                    xFlag, yFlag )
             : null;
    }

    /**
     * Returns a plot aspect covering the data region which is currently
     * covered by a given rectangle in graphics coordinates.
     *
     * @param   frame  rectangle in current graphics coordinates
     *                 giving data space region of interest
     */
    PlaneAspect reframe( Rectangle frame ) {
        Point gp1 = new Point( frame.x, frame.y );
        Point gp2 = new Point( frame.x + frame.width, frame.y + frame.height );
        double[] dpos1 = graphicsToData( gp1, null );
        double[] dpos2 = graphicsToData( gp2, null );
        return new PlaneAspect( PlotUtil.orderPair( dpos1[ 0 ], dpos2[ 0 ] ),
                                PlotUtil.orderPair( dpos1[ 1 ], dpos2[ 1 ] ) );
    }

    /**
     * Returns an axis annotation object for this surface.
     *
     * @return   axis annotation
     */
    private AxisAnnotation createAxisAnnotation() {
        return new PlaneAxisAnnotation( gxlo_, gxhi_, gylo_, gyhi_,
                                        xAxis_, yAxis_, xticks_, yticks_,
                                        xlabel_, ylabel_, captioner_,
                                        true, true );
    }

    @Override
    public boolean equals( Object o ) {
        if ( o instanceof PlaneSurface ) {
            PlaneSurface other = (PlaneSurface) o;
            return this.xlog_ == other.xlog_
                && this.ylog_ == other.ylog_
                && this.xflip_ == other.xflip_
                && this.yflip_ == other.yflip_
                && this.dxlo_ == other.dxlo_
                && this.dxhi_ == other.dxhi_
                && this.dylo_ == other.dylo_
                && this.dyhi_ == other.dyhi_
                && this.gxlo_ == other.gxlo_
                && this.gxhi_ == other.gxhi_
                && this.gylo_ == other.gylo_
                && this.gyhi_ == other.gyhi_
                && Arrays.equals( this.xticks_, other.xticks_ )
                && Arrays.equals( this.yticks_, other.yticks_ )
                && PlotUtil.equals( this.xlabel_, other.xlabel_ )
                && PlotUtil.equals( this.ylabel_, other.ylabel_ )
                && this.captioner_.equals( other.captioner_ )
                && PlotUtil.equals( this.gridcolor_, other.gridcolor_ )
                && PlotUtil.equals( this.axlabelcolor_, other.axlabelcolor_ );
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int code = 23023;
        code = 23 * code + ( xlog_  ? 1 : 0 )
                         + ( ylog_  ? 2 : 0 )
                         + ( xflip_ ? 4 : 0 )
                         + ( yflip_ ? 8 : 0 );
        code = 23 * code + Float.floatToIntBits( (float) dxlo_ );
        code = 23 * code + Float.floatToIntBits( (float) dxhi_ );
        code = 23 * code + Float.floatToIntBits( (float) dylo_ );
        code = 23 * code + Float.floatToIntBits( (float) dyhi_ );
        code = 23 * code + gxlo_;
        code = 23 * code + gxhi_;
        code = 23 * code + gylo_;
        code = 23 * code + gyhi_;
        code = 23 * code + Arrays.hashCode( xticks_ );
        code = 23 * code + Arrays.hashCode( yticks_ );
        code = 23 * code + PlotUtil.hashCode( xlabel_ );
        code = 23 * code + PlotUtil.hashCode( ylabel_ );
        code = 23 * code + captioner_.hashCode();
        code = 23 * code + PlotUtil.hashCode( gridcolor_ );
        code = 23 * code + PlotUtil.hashCode( axlabelcolor_ );
        return code;
    }

    /**
     * Utility method to create a PlaneSurface from available requirements.
     * It works out actual data coordinate bounds and tickmarks, and
     * then invokes the constructor.
     *
     * @param  plotBounds  rectangle which the plot data should occupy
     * @param  aspect  surface view configuration
     * @param  xlog   whether to use logarithmic scaling on X axis
     * @param  ylog   whether to use logarithmic scaling on Y axis
     * @param  xflip  whether to invert direction of X axis
     * @param  yflip  whether to invert direction of Y axis
     * @param  xlabel  text for labelling X axis
     * @param  ylabel  text for labelling Y axis
     * @param  captioner  text renderer for axis labels etc
     * @param  xyfactor   ratio (X axis unit length)/(Y axis unit length),
     *                    or NaN to use whatever bounds shape and
     *                    axis limits give you
     * @param  grid   whether to draw grid lines
     * @param  xcrowd  crowding factor for tick marks on X axis;
     *                 1 is normal
     * @param  ycrowd  crowding factor for tick marks on Y axis;
     *                 1 is normal
     * @param  minor   whether to paint minor tick marks on axes
     * @param  gridcolor  colour of grid lines, if plotted
     * @param  axlabelcolor  colour of axis labels
     * @return  new plot surface
     */
    public static PlaneSurface createSurface( Rectangle plotBounds,
                                              PlaneAspect aspect,
                                              boolean xlog, boolean ylog,
                                              boolean xflip, boolean yflip,
                                              String xlabel, String ylabel,
                                              Captioner captioner,
                                              double xyfactor, boolean grid,
                                              double xcrowd, double ycrowd,
                                              boolean minor, Color gridcolor,
                                              Color axlabelcolor ) {
        int gxlo = plotBounds.x;
        int gxhi = plotBounds.x + plotBounds.width;
        int gylo = plotBounds.y;
        int gyhi = plotBounds.y + plotBounds.height;
        double dxlo = aspect.getXMin();
        double dxhi = aspect.getXMax();
        double dylo = aspect.getYMin();
        double dyhi = aspect.getYMax();
        Ticker xTicker = xlog ? BasicTicker.LOG : BasicTicker.LINEAR;
        Ticker yTicker = ylog ? BasicTicker.LOG : BasicTicker.LINEAR;
        Tick[] xticks = xTicker.getTicks( dxlo, dxhi, minor, captioner,
                                          PlaneAxisAnnotation.X_ORIENT,
                                          plotBounds.width, xcrowd );
        Tick[] yticks = yTicker.getTicks( dylo, dyhi, minor, captioner,
                                          PlaneAxisAnnotation.Y_ORIENT,
                                          plotBounds.height, ycrowd );
        gridcolor = grid ? gridcolor : null;

        /* Fixed ratio of X/Y data scales.  Interpret this by ensuring that
         * all of both requested data ranges is included, and one of them is
         * extended if necessary to accommodate the extra graphics space.
         * Only makes sense if both linear or both log. */
        if ( xyfactor > 0 && xlog == ylog ) {
            boolean log = xlog;
            double gx = gxhi - gxlo;
            double gy = gyhi - gylo;
            double dx = log ? Math.log( dxhi / dxlo ) : dxhi - dxlo;
            double dy = log ? Math.log( dyhi / dylo ) : dyhi - dylo;
            double fadj = xyfactor * ( gy / dy ) / ( gx / dx );
            if ( fadj > 1 ) {
                double dyadj = dy * ( 1 * fadj - 1 );
                if ( log ) {
                    dylo *= Math.exp( -0.5 * dyadj );
                    dyhi *= Math.exp( +0.5 * dyadj );
                }
                else {
                    dylo += -0.5 * dyadj;
                    dyhi += +0.5 * dyadj;
                }
            }
            else {
                double dxadj = dx * ( 1 / fadj - 1 );
                if ( log ) {
                    dxlo *= Math.exp( -0.5 * dxadj );
                    dxhi *= Math.exp( +0.5 * dxadj );
                }
                else {
                    dxlo += -0.5 * dxadj;
                    dxhi += +0.5 * dxadj;
                }
            }
            dx = log ? Math.log( dxhi / dxlo ) : dxhi - dxlo;
            dy = log ? Math.log( dyhi / dylo ) : dyhi - dylo;
            assert Math.abs( xyfactor * ( gy / dy ) / ( gx / dx ) - 1 ) < 1e-6;
            xticks = xTicker.getTicks( dxlo, dxhi, minor, captioner,
                                       PlaneAxisAnnotation.X_ORIENT,
                                       plotBounds.width, 1 );
            yticks = yTicker.getTicks( dylo, dyhi, minor, captioner,
                                       PlaneAxisAnnotation.Y_ORIENT,
                                       plotBounds.height, 1 );
        }
        return new PlaneSurface( gxlo, gxhi, gylo, gyhi, dxlo, dxhi, dylo, dyhi,
                                 xlog, ylog, xflip, yflip, xticks, yticks,
                                 xlabel, ylabel, captioner, gridcolor,
                                 axlabelcolor );
    }

    /**
     * Formats a coordinate value for presentation to the user.
     *
     * @param   axis  axis on which value appears
     * @param   dpos  data coordinate value
     * @return   formatted data coordinate string
     */
    public static String formatPosition( Axis axis, double dpos ) {

        /* This could be implemented better.
         * It would be better if the precision determination, and hence
         * the number of digits before and after the decimal point and
         * in the exponent, were determined by the axis (bounds and
         * scaling) rather than by the value.  That would be fairly
         * easy for linear axes, a bit harder for logarithmic. */

        /* Work out pixel size in data coordinates by looking at the
         * data position of a point two pixels away. */
        double dp2 = axis.graphicsToData( axis.dataToGraphics( dpos ) + 2 );
        return PlotUtil.formatNumber( dpos, Math.abs( dp2 - dpos ) / 2. );
    }
}
