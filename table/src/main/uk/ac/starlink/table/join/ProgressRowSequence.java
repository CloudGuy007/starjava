package uk.ac.starlink.table.join;

import java.io.IOException;
import uk.ac.starlink.table.RowSequence;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.WrapperRowSequence;

/**
 * RowSequence which logs progress to a {@link ProgressIndicator}.
 * Has to contain a couple methods extra to the <tt>RowSequence</tt>
 * interface to make it behave properly.
 *
 * @author   Mark Taylor (Starlink)
 * @since    6 Aug 2004
 */
public class ProgressRowSequence extends WrapperRowSequence {

    private final double nrow_;
    private final ProgressIndicator indicator_;
    private boolean closed;
    private long lrow_;

    /**
     * Constructs a new ProgressRowSequence.
     *
     * @param  table  table to get the base row sequence from
     * @param  indicator  indicator to be informed about progress
     * @param  stage    string describing this stage of the process
     */
    public ProgressRowSequence( StarTable table, ProgressIndicator indicator, 
                                String stage ) throws IOException {
        super( table.getRowSequence() );
        nrow_ = table.getRowCount();
        indicator_ = indicator;
        indicator_.startStage( stage );
    }

    /**
     * Invokes {@link #next} and also updates the progress indicator.
     */
    public void nextProgress() throws IOException, InterruptedException {
        next();
        indicator_.setLevel( lrow_ / nrow_ );
    }

    public void next() throws IOException {
        super.next();
        lrow_++;
    }

    /**
     * Indicates that progress is at an end.  Must be called to end the
     * progress indicator's stage.
     */
    public void close() throws IOException {
        if ( ! closed ) {
            indicator_.endStage();
            closed = true;
        }
        super.close();
    }
}
