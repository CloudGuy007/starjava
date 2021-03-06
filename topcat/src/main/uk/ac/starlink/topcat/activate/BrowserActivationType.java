package uk.ac.starlink.topcat.activate;

import java.awt.Desktop;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComboBox;
import uk.ac.starlink.table.ColumnData;
import uk.ac.starlink.table.gui.LabelledComponentStack;
import uk.ac.starlink.topcat.AbstractHtmlPanel;
import uk.ac.starlink.topcat.HtmlWindow;
import uk.ac.starlink.topcat.Outcome;
import uk.ac.starlink.topcat.Safety;
import uk.ac.starlink.topcat.TopcatUtils;
import uk.ac.starlink.ttools.calc.WebMapper;
import uk.ac.starlink.util.gui.ShrinkWrapper;

/**
 * ActivationType for displaying a resource in a browser.
 *
 * @author   Mark Taylor
 * @since    30 Jan 2018
 */
public class BrowserActivationType implements ActivationType {

    public String getName() {
        return "View in Web Browser";
    }

    public String getDescription() {
        return "Load an associated resource into a web browser";
    }

    public ActivatorConfigurator createConfigurator( TopcatModelInfo tinfo ) {
        return new BrowserColumnConfigurator( tinfo, createBrowserList() );
    }

    public Suitability getSuitability( TopcatModelInfo tinfo ) {
        return tinfo.tableHasFlag( ColFlag.HTML ) ||
               tinfo.tableHasFlag( ColFlag.WEBREF )
             ? Suitability.SUGGESTED
             : tinfo.getUrlSuitability();
    }

    /**
     * Implementation that displays a URL in a general purpose web browser.
     */
    private static class BrowserColumnConfigurator
                         extends UrlColumnConfigurator {
        private final JComboBox<WebMapper> mapperChooser_;
        private final JComboBox<Browser> browserChooser_;
        private static final String WEBMAPPER_KEY = "webMapper";
        private static final String BROWSER_KEY = "browser";

        /**
         * Constructor.
         *
         * @param  topcat model information
         * @param  browsers  available browser implementations
         */
        BrowserColumnConfigurator( TopcatModelInfo tinfo, Browser[] browsers ) {
            super( tinfo, "Web Page",
                   new ColFlag[] { ColFlag.HTML, ColFlag.WEBREF,
                                   ColFlag.URL, } );
            setLocationLabel( "Resource Identifier" );
            mapperChooser_ = new JComboBox<>();
            for ( WebMapper mapper : WebMapper.getMappers() ) {
                mapperChooser_.addItem( mapper );
            }
            mapperChooser_.addActionListener( getActionForwarder() );

            browserChooser_ = new JComboBox<>();
            for ( Browser b : browsers ) {
                browserChooser_.addItem( b );
            }
            browserChooser_.addActionListener( getActionForwarder() );

            LabelledComponentStack stack = new LabelledComponentStack();
            stack.addLine( "Identifier Type", mapperChooser_ );
            stack.addLine( "Browser", browserChooser_ );
            getQueryPanel().add( stack );
        }

        protected Activator createActivator( ColumnData cdata ) {
            final WebMapper mapper =
                mapperChooser_.getItemAt( mapperChooser_.getSelectedIndex() );
            final Browser browser =
                browserChooser_.getItemAt( browserChooser_.getSelectedIndex() );
            final String label = getWindowLabel( cdata );
            return new LocationColumnActivator( cdata, false ) {
                protected Outcome activateLocation( String loc, long lrow ) {
                    assert loc != null && loc.trim().length() > 0;
                    URL url = mapper.toUrl( loc );
                    return url == null
                         ? Outcome.failure( "Not " + mapper.getName()
                                          + ": " + loc )
                         : browser.browse( url, label );
                }
            };
        }

        protected String getConfigMessage( ColumnData cdata ) {
            return browserChooser_.getSelectedItem() instanceof Browser
                 ? null
                 : "no browser??";
        }

        public Safety getSafety() {
            return Safety.UNSAFE;
        }

        public ConfigState getState() {
            ConfigState state = getUrlState();
            state.saveSelection( WEBMAPPER_KEY, mapperChooser_ );
            state.saveSelection( BROWSER_KEY, browserChooser_ );
            return state;
        }

        public void setState( ConfigState state ) {
            setUrlState( state );
            state.restoreSelection( WEBMAPPER_KEY, mapperChooser_ );
            state.restoreSelection( BROWSER_KEY, browserChooser_ );
        }
    }

    /**
     * Returns a new list of browsers that can be used.
     *
     * @return  browsers
     */
    private final Browser[] createBrowserList() {
        List<Browser> list = new ArrayList<Browser>();
        if ( AbstractHtmlPanel.hasJavaFx() ) {
            list.add( new BasicBrowser( "JavaFX browser" ) {
                AbstractHtmlPanel createHtmlPanel() {
                    AbstractHtmlPanel panel =
                        AbstractHtmlPanel.createFxPanel();
                    if ( panel == null ) {
                        assert false; 
                        panel = AbstractHtmlPanel.createSwingPanel();
                    }
                    return panel;
                }
            } );
        }
        list.add( new BasicBrowser( "basic browser" ) {
            AbstractHtmlPanel createHtmlPanel() {
                return AbstractHtmlPanel.createSwingPanel();
            }
        } );
        Desktop desktop = TopcatUtils.getBrowserDesktop();
        if ( desktop != null ) {
            list.add( new DesktopBrowser( desktop ) );
        }
        return list.toArray( new Browser[ 0 ] );
    }

    /**
     * Browser implementation that uses java.awt.Desktop.
     */
    private static class DesktopBrowser extends Browser {
        private final Desktop desktop_;

        /**
         * Constructor.
         *
         * @param  desktop  desktop instance, assumed to support URI browsing
         */
        DesktopBrowser( Desktop desktop ) {
            super( "system browser" );
            desktop_ = desktop;
        }
        public Outcome browse( URL url, String label ) {
            try {
                desktop_.browse( url.toURI() );
            }
            catch ( Throwable e ) {
                return Outcome.failure( e );
            }
            return Outcome.success( url.toString() );
        }
    }

    /**
     * Internally implemented HTML display window.
     */
    private static abstract class BasicBrowser extends Browser {
        private HtmlWindow htmlWin_;
        BasicBrowser( String name ) {
            super( name );
        }
        public Outcome browse( URL url, String label ) {
            HtmlWindow win = getHtmlWindow( label );
            try {
                win.setURL( url );
            }
            catch ( Throwable e ) {
                return Outcome.failure( e );
            }
            return Outcome.success( url.toString() );
        }
        private HtmlWindow getHtmlWindow( String label ) {
            if ( htmlWin_ == null ) {
                htmlWin_ = new HtmlWindow( null, createHtmlPanel() );
            }
            htmlWin_.setTitle( label );
            if ( ! htmlWin_.isShowing() ) {
                htmlWin_.setVisible( true );
            }
            return htmlWin_;
        }

        /**
         * Returns an AbstractHtmlPanel implementation.
         *
         * @return  html panel, not null
         */
        abstract AbstractHtmlPanel createHtmlPanel();
    }

    /**
     * Browser functionality interface
     */
    private static abstract class Browser {
        final String name_;

        /**
         * Constructor.
         *
         * @param  name  browser display name
         */
        Browser( String name ) {
            name_ = name;
        }

        /**
         * Attempts to display the given URL in a browser window.
         *
         * @param  url  resource to display
         * @param  label   label for window
         * @return   action outcome
         */
        public abstract Outcome browse( URL url, String label );

        @Override
        public String toString() {
            return name_;
        }
    }
}
