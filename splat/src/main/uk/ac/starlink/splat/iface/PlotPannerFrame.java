package uk.ac.starlink.splat.iface;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import uk.ac.starlink.splat.iface.images.ImageHolder;
import uk.ac.starlink.splat.plot.PlotControl;
import uk.ac.starlink.splat.util.Utilities;

/**
 * Create a panner window that displays a smaller copy of the complete
 * contents of a viewport managed by a PlotControl object.  A red
 * rectangle is displayed that outlines the viewport extent. The
 * rectangle can be "picked up" and moved to scroll the viewport
 * contents.
 *
 * @since $Date$
 * @since 13-JUN-2001
 * @author Peter W. Draper
 * @version $Id$
 * @copyright Copyright (C) 2001 Central Laboratory of the Research Councils
 * @see PlotControl, PlotControlFrame, Plot, PlotPanner.
 */
public class PlotPannerFrame extends JFrame
{
    /**
     * The associated PlotControl object.
     */
    protected PlotControl plot = null;

    /**
     * The PlotPanner.
     */
    protected PlotPanner panner = new PlotPanner();

    /**
     *  Menubar and various menus and items that it contains.
     */
    protected JMenuBar menuBar = new JMenuBar();
    protected JMenu fileMenu = new JMenu();
    protected JMenuItem closeFileMenu = new JMenuItem();
    protected JMenu helpMenu = new JMenu();
    protected JMenuItem helpMenuAbout = new JMenuItem();

    /**
     * Content pane of frame.
     */
    protected JPanel contentPane = null;

    /**
     * Create an instance.
     *
     * @param plot the PlotControl object that is managing the
     *             spectral view.
     */
    public PlotPannerFrame( PlotControl plot )
    {
        setPlot( plot );
        initUI();
        initMenus();
        initFrame();
    }

    /**
     * Set the PlotControl object that is have its scroll managed.
     *
     * @param plot a PlotControl object.
     */
    public void setPlot( PlotControl plot )
    {
        this.plot = plot;
        panner.setViewport( plot.getViewport() );
        panner.setPlotControl( plot );
        setTitle();
    }
    
    /**
     * Get the PlotControl object that is having its scroll managed.
     *
     * @return the PlotControl object.
     */
    public PlotControl getPlot()
    {
        return plot;
    }
    
    /**
     * Initialise the main part of the user interface.
     */
    protected void initUI()
    {
        //  At present a single PlotPannel goes in the center.
        contentPane = (JPanel) getContentPane();
        contentPane.setLayout( new BorderLayout() );
        contentPane.add( panner, BorderLayout.CENTER );
    }

    /**
     * Initialise frame properties (disposal, menus etc.).
     */
    protected void initFrame() 
    {
        setDefaultCloseOperation( JFrame.HIDE_ON_CLOSE );
        setSize( new Dimension( plot.getWidth(), 100 ) );
        setVisible( true );
    }

   /**
     * Initialise the menu bar and related actions.
     */
    protected void initMenus()
    {
        //  Add the menuBar.
        setJMenuBar( menuBar );

        //  Create the File menu.
        fileMenu.setText( "File" );
        menuBar.add( fileMenu );

        //  Add an action to close the window.
        ImageIcon closeImage = new ImageIcon(
            ImageHolder.class.getResource( "close.gif" ) );
        CloseAction closeAction = new CloseAction( "Close", closeImage );
        fileMenu.add( closeAction );

        //  Add the window help.
        HelpFrame.createHelpMenu( "panner-window", "Help on window", 
                                  menuBar, null );
    }

    /**
     * Set the title of the frame (matched associated PlotControl object).
     */
    protected void setTitle()
    {
        setTitle( Utilities.getTitle( "Panner for " + plot.getName() ) );
    }

    /**
     *  Close the window.
     */
    protected void closeWindowEvent()
    {
        this.hide();
    }

    /**
     * Inner class defining Action for closing window and keeping fit.
     */
    protected class CloseAction extends AbstractAction
    {
        public CloseAction( String name, Icon icon ) {
            super( name, icon );
        }
        public void actionPerformed( ActionEvent ae ) {
            closeWindowEvent();
        }
    }
}
