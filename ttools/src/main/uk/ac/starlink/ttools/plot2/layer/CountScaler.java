package uk.ac.starlink.ttools.plot2.layer;

import uk.ac.starlink.ttools.plot2.Scaler;
import uk.ac.starlink.ttools.plot2.Scaling;

/**
 * Scales integer count values according to a supplied Scaling object.
 *
 * @author   Mark Taylor
 * @since    26 Jan 2015
 */
public class CountScaler {

    private final Scaler scaler_;
    private final double outFactor_;
    private final int nLookup_;
    private final int[] lookup_;
    private final static int MAX_LOOKUP = 256;

    /**
     * Constructor.
     *
     * @param  scaling  provides basic scaling behaviour
     * @param  maxIn  defines input value range 0..maxIn
     * @param  maxOut  defines output value range 0..maxOut
     */
    public CountScaler( Scaling scaling, int maxIn, int maxOut ) {
        scaler_ = scaling.createScaler( 1, maxIn );
        outFactor_ = maxOut - 0.1;

        /* Prepare a lookup table for the first few integer input values. */
        nLookup_ = Math.min( maxIn, MAX_LOOKUP );
        lookup_ = new int[ nLookup_ ];
        for ( int i = 0; i < nLookup_; i++ ) {
            lookup_[ i ] = calculateScaledCount( i );
        }
    }

    /**
     * Scales in input whole number to an output whole number.
     * Zero maps to zero, other values map according to this object's Scaling.
     *
     * @param   count  unscaled value, in range 0..maxIn
     * @return  scaled count, in range 0..maxOut
     */
    public int scaleCount( int count ) {
        return count < nLookup_ ? lookup_[ count ]
                                : calculateScaledCount( count );
    }

    /**
     * Calculates the scaled value.
     * Zero maps to zero, other values map according to this object's Scaling.
     *
     * @param   count  unscaled value, in range 0..maxIn
     * @return  scaled count, in range 0..maxOut
     */
    private int calculateScaledCount( int count ) {
        return count == 0
             ? 0
             : 1 + (int) ( scaler_.scaleValue( count ) * outFactor_ );
    }
}
