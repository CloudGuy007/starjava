package uk.ac.starlink.ttools.task;

import uk.ac.starlink.ttools.join.MatchMapper;

/**
 * Permforms a multi-table crossmatch.
 *
 * @author   Mark Taylor
 * @since    20 Nov 2007
 */
public class TableMatchN extends VariableMapperTask {
    public TableMatchN() {
        super( "Crossmatches multiple tables using flexible criteria",
               new ChoiceMode(), true, new MatchMapper(), true );
    }
}
