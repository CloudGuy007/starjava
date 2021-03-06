package uk.ac.starlink.topcat;

import java.util.ArrayList;
import java.util.List;

/**
 * TopcatListener implementation which forwards events to other
 * TopcatListeners.
 *
 * @author   Mark Taylor
 * @since    3 Nov 2005
 */
public class TopcatForwarder implements TopcatListener {

    private final List<TopcatListener> listeners_ =
        new ArrayList<TopcatListener>();

    /**
     * Adds a new listener.
     *
     * @param  listener listener to add
     */
    public void addTopcatListener( TopcatListener listener ) {
        listeners_.add( listener );
    }

    /**
     * Removes a listener which was previously added.
     *
     * @param  listener  listener to remove
     */
    public void removeTopcatListener( TopcatListener listener ) {
        listeners_.remove( listener );
    }

    public void modelChanged( TopcatEvent evt ) {
        for ( TopcatListener l : listeners_ ) {
            l.modelChanged( evt );
        }
    }
}
