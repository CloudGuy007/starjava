package uk.ac.starlink.ttools.task;

import uk.ac.starlink.table.ColumnData;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.Tables;
import uk.ac.starlink.ttools.QuickTable;
import uk.ac.starlink.ttools.TableTestCase;

public class TableCatTest extends TableTestCase {

    final StarTable t1_ = new QuickTable( 2, new ColumnData[] {
        col( "index", new int[] { 1, 2 } ),
        col( "name", new String[] { "milo", "theo", } ),
    } );

    final StarTable t2_ = new QuickTable( 3, new ColumnData[] {
        col( "ix", new int[] { 1, 2, 3, } ),
        col( "atkname", new String[] { "charlotte", "jonathon", "gerald", } ),
    } );

    public TableCatTest( String name ) {
        super( name );
    }

    public void test2() throws Exception {

        MapEnvironment env2 = new MapEnvironment()
                             .setValue( "in1", t1_ )
                             .setValue( "in2", t2_ );
        new TableCat2().createExecutable( env2 ).execute();
        StarTable out2 = env2.getOutputTable( "omode" );

        MapEnvironment envN = new MapEnvironment()
                             .setValue( "in", new StarTable[] { t1_, t2_, } );
        new TableCat().createExecutable( envN ).execute();
        StarTable outN = envN.getOutputTable( "omode" );

        Tables.checkTable( out2 );
        Tables.checkTable( outN );

        assertSameData( out2, outN );

        assertArrayEquals( new String[] { "index", "name" },
                           getColNames( out2 ) );
        assertArrayEquals( box( new int[] { 1, 2, 1, 2, 3, } ),
                           getColData( out2, 0 ) );
        assertArrayEquals( new Object[] { "milo", "theo",
                                          "charlotte", "jonathon", "gerald",  },
                           getColData( out2, 1 ) );
    }

    public void testFilter() throws Exception {
        MapEnvironment env2 = new MapEnvironment()
                             .setValue( "in2", t2_ )
                             .setValue( "in1", t1_ )
                             .setValue( "icmd1", "tail 1" )
                             .setValue( "icmd2", "tail 1" )
                             .setValue( "ocmd", "keepcols '2 1'" );
        new TableCat2().createExecutable( env2 ).execute();
        StarTable out2 = env2.getOutputTable( "omode" );
        assertArrayEquals( box( new int[] { 2, 3, } ),
                           getColData( out2, 1 ) );

        MapEnvironment envN = new MapEnvironment()
                             .setValue( "in",
                                        new StarTable[] { t1_, t2_, t1_, } )
                             .setValue( "icmd", "tail 1" )
                             .setValue( "ocmd", "keepcols '2 1'" );
        new TableCat().createExecutable( envN ).execute();
        StarTable outN = envN.getOutputTable( "omode" );
        assertArrayEquals( box( new int[] { 2, 3, 2 } ),
                           getColData( outN, 1 ) );
    }
}
