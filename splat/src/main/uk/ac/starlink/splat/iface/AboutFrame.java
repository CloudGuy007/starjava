/*
 * Copyright (C) 2001-2003 Central Laboratory of the Research Councils
 *
 *  History:
 *     07-JUL-2001 (Peter W. Draper):
 *       Original version.
 */
package uk.ac.starlink.splat.iface;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import uk.ac.starlink.splat.iface.images.ImageHolder;
import uk.ac.starlink.splat.util.Utilities;

/**
 * Class that displays a dialog window with the "about" information
 * for the application.
 * <p>
 * To use this get an Action:
 * <pre>
 *    Action aboutAction = AboutFrame.getAction( this );
 * </pre>
 * and add this to a help button in the menu bar.
 *
 * @author Peter W. Draper
 * @version $Id$
 */
public class AboutFrame extends JDialog
    implements ActionListener
{
    protected JButton okButton = new JButton();
    protected static ImageIcon splatImage =
        new ImageIcon( ImageHolder.class.getResource( "hsplat.gif" ) );

    protected String description =
        "<html>" +
           "<h2 align=center> <font color=red>" +
              Utilities.getFullDescription() + 
           "</font></h2>" +
           "<p align=center>Version: " + 
              Utilities.getReleaseVersion() + 
           "</p>" +
           "<p align=center> " + 
              Utilities.getCopyright() + 
           "</p>" +
           "<p align=center> Authors:<i> " + 
              Utilities.getAuthors() + 
           "</i></p>" +
           "<p align=center> Licensing:<i> " + 
              Utilities.getLicense() + 
           "</i></p>" +
        "</html>";

    /**
     * Create an instance. Use the getAction() method not this.
     *
     * @param parent parent Frame (can be null).
     */
    protected AboutFrame( Frame parent )
    {
        super( parent );
        enableEvents( AWTEvent.WINDOW_EVENT_MASK );
        try {
            initUI();
            setSize( new Dimension( 360, 300 ) );
        }
        catch( Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the user interface components.
     */
    protected void initUI() throws Exception
    {
        setTitle( "About " + Utilities.getReleaseName() );
        JPanel mainPane = (JPanel) getContentPane();

        mainPane.setLayout( new BorderLayout() );
        mainPane.setBorder( BorderFactory.createEtchedBorder() );

        JEditorPane htmlArea = new JEditorPane();
        JLabel image = new JLabel();

        htmlArea.setContentType( "text/html" );
        htmlArea.setBackground( image.getBackground() );
        htmlArea.setText( description );
        htmlArea.setEditable( false );

        image.setIcon( splatImage );
        image.setVerticalAlignment( SwingConstants.TOP );

        okButton.setText( "OK" );
        okButton.addActionListener( this );

        mainPane.add( image, BorderLayout.WEST );
        mainPane.add( htmlArea, BorderLayout.CENTER );

        JPanel southPane = new JPanel();
        mainPane.add( southPane, BorderLayout.SOUTH );

        southPane.add( Box.createHorizontalGlue() );
        southPane.add( okButton );
        southPane.add( Box.createHorizontalGlue() );
    }


    /**
     * Looks out for window closing events.
     */
    protected void processWindowEvent(WindowEvent e)
    {
        if ( e.getID() == WindowEvent.WINDOW_CLOSING ) {
            cancel();
        }
        super.processWindowEvent( e );
    }


    /**
     * Close the window.
     */
    protected void cancel()
    {
        dispose();
    }

    /**
     * Implements ActionListener interface. Closing window when requested.
     */
    public void actionPerformed( ActionEvent e )
    {
        if ( e.getSource() == okButton ) {
            cancel();
        }
    }

    /**
     * Image for action icons.
     */
    protected static ImageIcon aboutImage =
        new ImageIcon( ImageHolder.class.getResource( "about.gif" ) );

    /**
     *  Inner class defining Action for about help dialog.
     */
    protected static class AboutAction extends AbstractAction
    {
        Frame parent = null;
        public AboutAction( Frame parent ) {
            super( "About " + Utilities.getReleaseName(), aboutImage );
            this.parent = parent;
            putValue( SHORT_DESCRIPTION, "Find out about program" );
        }
        public void actionPerformed( ActionEvent ae ) {
            helpAboutEvent( parent );
        }
    }

    /**
     *  Help "about" requested.
     */
    protected static void helpAboutEvent( Frame parent )
    {
        AboutFrame dlg = new AboutFrame( parent );
        dlg.setLocationRelativeTo( parent );
        dlg.setModal( true );
        dlg.show();
    }

    /**
     * Return an action for display an about window. Needs a parent
     * window (i.e. the JFrame that contains the help button).
     */
    public static Action getAction( Frame parent )
    {
        return new AboutAction( parent );
    }
}
