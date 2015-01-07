package uk.ac.starlink.topcat.plot2;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import uk.ac.starlink.ttools.plot2.SurfaceFactory;
import uk.ac.starlink.ttools.plot2.config.ConfigKey;

/**
 * Abstract superclass for simple Cartesian plots.
 * This mostly just handles the axis labelling GUI, and leaves subclasses
 * to adjust the details of the other configuration options.
 *
 * @author   Mark Taylor
 * @since    14 Mar 2013
 */
public abstract class CartesianAxisController<P,A> extends AxisController<P,A> {

    private final AutoConfigSpecifier labelSpecifier_;

    /**
     * Constructor.
     *
     * @param  surfFact  plot surface factory
     * @param  navHelpId  help ID for navigator actions, if any
     * @param  hasFrameInsets  whether the Size control
     *                         supports insets or just external dimensions
     * @param  axisLabelKeys  config keys for axis labels
     * @param  stack   control stack, used to get default axis label strings
     */
    public CartesianAxisController( SurfaceFactory<P,A> surfFact,
                                    String navHelpId, boolean hasFrameInsets,
                                    final ConfigKey<String>[] axisLabelKeys,
                                    ControlStack stack ) {
        super( surfFact, navHelpId, hasFrameInsets );
        final int ndim = axisLabelKeys.length;

        /* Set up a specifier component to get axis label values.
         * Each specifier has an automatic default. */
        labelSpecifier_ = new AutoConfigSpecifier( axisLabelKeys );
        final String[] axisNames = new String[ ndim ];
        for ( int id = 0; id < ndim; id++ ) {
            axisNames[ id ] = axisLabelKeys[ id ].getDefaultValue();
        }

        /* Fix it so that the default values for the axis labels are
         * taken from coordinate labels (table column names or expressions)
         * for data layers contributing to the plot.  These have to be
         * updated when the content of the plot control stack changes. */
        final ControlStackModel stackModel = stack.getStackModel();
        ActionListener stackListener = new ActionListener() {
            public void actionPerformed( ActionEvent evt ) {
                adjustDefaultAxisLabels();
            }
            private void adjustDefaultAxisLabels() {
                String[] axLabels = getAxisLabels( getLeadControl() );
                for ( int id = 0; id < ndim; id++ ) {
                    labelSpecifier_.getAutoSpecifier( axisLabelKeys[ id ] )
                                   .setAutoValue( axLabels[ id ] );
                }
            }
            private LayerControl getLeadControl() {
                LayerControl[] controls = stackModel.getLayerControls( true );
                return controls.length > 0 ? controls[ 0 ] : null;
            }
            private String[] getAxisLabels( LayerControl control ) {
                if ( control == null ) {
                    return axisNames;
                }
                String[] labels = new String[ ndim ];
                for ( int id = 0; id < ndim; id++ ) {
                    String label = control.getCoordLabel( axisNames[ id ] );
                    labels[ id ] = label == null ? axisNames[ id ] : label;
                }
                return labels;
            }
        };
        stackModel.addPlotActionListener( stackListener );
        stackListener.actionPerformed( null );
    }

    /**
     * Adds the axis label configuration tab set up by this component.
     * It's not done in the constructor so that subclasses can decide
     * where it goes in terms of the other config tabs.
     */
    protected void addLabelsTab() {
        getMainControl().addSpecifierTab( "Labels", labelSpecifier_ );
    }

    /**
     * Returns the specifier used for axis labels.
     *
     * @return  axis label specifier
     */
    public AutoConfigSpecifier getLabelSpecifier() {
        return labelSpecifier_;
    }

    @Override
    protected boolean forceClearRange( P oldProfile, P newProfile ) {

        /* If log scaling of some axes has changed, we unconditionally
         * re-range.  This might not always be the best thing to do
         * (could be improved), but as it stands if not we run the risk
         * of trying to plot negative logarithmic values, which will
         * cause an exception in the plotting. */
        return logChanged( oldProfile, newProfile );
    }

    /**
     * Indicates whether the scaling has changed to or from logarithmic
     * for any of the cartesian axes between two profiles.
     * If so, it's going to be necessary to rescale, since attempting
     * a log plot with negative values would fail.
     *
     * @param  prof1  first profile
     * @param  prof2  second profile
     * @return  true iff some of the axes are log in prof1 and linear in prof2
     *               or vice versa
     */
    protected abstract boolean logChanged( P prof1, P prof2 );
}
