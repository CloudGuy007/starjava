package uk.ac.starlink.topcat;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.BitSet;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ListSelectionModel;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumnModel;
import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.gui.StarJTable;
import uk.ac.starlink.table.gui.StarTableColumn;
import uk.ac.starlink.table.gui.TableRowHeader;


/**
 * Browser window for viewing the data in a table. 
 * This provides a JTable view on a TopcatModel.
 *
 * @author   Mark Taylor (Starlink)
 * @since    19 Feb 2004
 */
public class TableViewerWindow extends TopcatViewWindow
                               implements TableModelListener, 
                                          TableColumnModelListener {
    private final TopcatModel tcModel;
    private final PlasticStarTable dataModel;
    private final ViewerTableModel viewModel;
    private final TableColumnModel columnModel;
    private final OptionsListModel subsets;
    private final ColumnList columnList;

    private JTable jtab;
    private TableRowHeader rowHead;
    private JScrollPane scrollpane;
    private JProgressBar progBar;
    private Action includeAct;
    private Action excludeAct;
    private boolean selfHighlighting;

    private static int MAX_COLUMN_WIDTH = 300; 
    private static int MAX_SAMPLE_ROWS = 800;

    /**
     * Constructs a new TableViewer to view a given table.
     * The given table must provide random access.
     *      
     * @param  startab   a star table, or <tt>null</tt>
     * @param  parent    a window for positioning relative to; the new
     *         one will generally come out a bit lower and to the right
     *         of <tt>sibling</tt>.  May be <tt>null</tt>
     * @throws  IllegalArgumentException  if <tt>!startab.isRandom()</tt>
     */
    public TableViewerWindow( final TopcatModel tcModel, Component parent ) {
        super( tcModel, "Table Browser", parent );
        this.tcModel = tcModel;
        this.dataModel = tcModel.getDataModel();
        this.viewModel = tcModel.getViewModel();
        this.columnModel = tcModel.getColumnModel();
        this.subsets = tcModel.getSubsets();
        this.columnList = tcModel.getColumnList();

        /* Set up the JTable. */
        jtab = new JTable(); 
        jtab.setCellSelectionEnabled( false );
        jtab.setColumnSelectionAllowed( false );
        jtab.setRowSelectionAllowed( true );
        jtab.setModel( viewModel );
        jtab.setColumnModel( columnModel );

        /* Place the JTable. */
        scrollpane = new SizingScrollPane( jtab );
        getMainArea().add( scrollpane, BorderLayout.CENTER );

        /* Place a progress bar. */
        progBar = placeProgressBar();
    
        /* Set up row header panel. */
        rowHead = new TableRowHeader( jtab ) {
            public int rowNumber( int irow ) {
                return ((int) viewModel.getBaseRow( irow )) + 1;
            }
        };
        scrollpane.setRowHeaderView( rowHead );

        /* Make sure the viewer window is updated when the TableModel
         * or the TableColumnModel changes changes (for instance
         * change of table shape). */
        viewModel.addTableModelListener( this );
        columnModel.addColumnModelListener( this );
    
        /* Actions for new subsets from the row selection. */
        includeAct = new ViewerAction( "Subset From Selected Rows",
                                       ResourceIcon.INCLUDE_ROWS,
                                       "Define a new row subset containing " +
                                       "all selected rows" );
        excludeAct = new ViewerAction( "Subset From Unselected Rows",
                                       ResourceIcon.EXCLUDE_ROWS,
                                       "Define a new row subset containing " +
                                       "all visible unselected rows" );

        /* Configure a listener for row selection events. */
        final ListSelectionModel selectionModel = jtab.getSelectionModel();
        ListSelectionListener selList = new ListSelectionListener() {
            long lastActive = -1;
            public void valueChanged( ListSelectionEvent evt ) {
                boolean hasSelection = ! selectionModel.isSelectionEmpty();

                /* Configure event availability. */
                includeAct.setEnabled( hasSelection );
                excludeAct.setEnabled( hasSelection );

                /* If the selection consists of a single row, construe this
                 * as a row activation request. */
                if ( hasSelection && ! evt.getValueIsAdjusting() ) {
                    int first = selectionModel.getMinSelectionIndex();
                    if ( selectionModel.getMaxSelectionIndex() == first ) {
                        long active = viewModel.getBaseRow( first );
                        if ( active != lastActive ) {
                            lastActive = active;

                            /* Unless this call was initiated by a call to
                             * rowHighlight, message the topcat model that 
                             * row has been highlighted. */
                            if ( ! selfHighlighting ) {
                                selfHighlighting = true;
                                tcModel.highlightRow( active );
                                selfHighlighting = false;
                            }
                        }
                    }
                }
            }
        };
        selectionModel.addListSelectionListener( selList );
        selList.valueChanged( null );

        /* Configure a listener for column popup menus. */
        MouseListener mousey = new MouseAdapter() {
            public void mousePressed( MouseEvent evt ) {
                maybeShowPopup( evt );
            }
            public void mouseReleased( MouseEvent evt ) {
                maybeShowPopup( evt );
            }
            private void maybeShowPopup( MouseEvent evt ) {
                if ( evt.isPopupTrigger() ) {
                    int jcol = jtab.columnAtPoint( evt.getPoint() );
                    if ( evt.getComponent() == rowHead ) {
                        jcol = -1;
                    }
                    if ( jcol >= -1 ) {
                        JPopupMenu popper = columnPopup( jcol );
                        if ( popper != null ) {
                            popper.show( evt.getComponent(),
                                         evt.getX(), evt.getY() );
                        }
                    }
                }
            }
        };
        jtab.addMouseListener( mousey );
        jtab.getTableHeader().addMouseListener( mousey );
        rowHead.addMouseListener( mousey );

        /* Set the view up right. */
        scrollpane.getViewport().setViewPosition( new Point( 0, 0 ) );
        StarJTable.configureColumnWidths( jtab, MAX_COLUMN_WIDTH,
                                          MAX_SAMPLE_ROWS );

        /* Add actions to the toolbar. */
        getToolBar().add( includeAct );
        getToolBar().add( excludeAct );
        getToolBar().addSeparator();

        /* Add a subsets menu. */
        JMenu subsetMenu = new JMenu( "Subsets" );
        subsetMenu.setMnemonic( KeyEvent.VK_S );
        subsetMenu.add( includeAct );
        subsetMenu.add( excludeAct );
        Action applysubsetAct = new AbstractAction() {
            public void actionPerformed( ActionEvent evt ) {
                int index = evt.getID();
                tcModel.applySubset( (RowSubset) subsets.get( index ) );
            }
        };
        JMenu applysubsetMenu =
            subsets.makeJMenu( "Apply Subset", applysubsetAct );
        subsetMenu.add( applysubsetMenu );
        getJMenuBar().add( subsetMenu );

        /* Add help information. */
        addHelp( "TableViewerWindow" );

        /* Display. */
        pack();
        setVisible( true );
    }

    /**
     * Displays a given row in a highlighted fashion.
     *
     * <p>This may disturb the state of the viewer somewhat - the current
     * implementation sets the current table selection to the single row
     * indicated.
     *
     * @param  lrow  index in the data model (not the view model) of the
     *               row to be highlighted
     */
    public void highlightRow( long lrow ) {

        /* Maintain a flag to ensure that this doesn't cause a call to 
         * the topcat model's highlightRow method, which would in turn 
         * call this one, causing infinite recursion. */
        if ( selfHighlighting ) {
            return;
        }
        selfHighlighting = true;

        /* Get ready. */
        jtab.clearSelection();

        /* Check if the view currently on display contains the requested row. */
        if ( viewModel.getSubset().isIncluded( lrow ) ) {

            /* Get the view row corresponding to the requested table row. */
            int viewRow = viewModel.getViewRow( lrow );

            /* It can't be -1 since we've just checked it's in the current 
             * subset. */
            assert viewRow >= 0;

            /* Set the JTable's selection to contain just this row. */
            jtab.addRowSelectionInterval( viewRow, viewRow );

            /* Arrange for the row to be visible in the middle of the 
             * scrollpane's viewport. */
            Rectangle viewRect = jtab.getCellRect( viewRow, 0, false );
            int yMid = viewRect.y + viewRect.height / 2;
            JScrollBar yBar = scrollpane.getVerticalScrollBar();
            yBar.setValue( yMid - yBar.getVisibleAmount() / 2 );
        }
        selfHighlighting = false;
    }

    /**
     * Returns a popup menu for a given column.  If the dummy column
     * index -1 is used, a popup suitable for the row header column
     * will be returned.
     *
     * @param  jcol the data model column to which the menu applies
     */
    private JPopupMenu columnPopup( final int jcol ) {
        JPopupMenu popper = new JPopupMenu();
        final StarTableColumn tcol = 
            (StarTableColumn) columnModel.getColumn( jcol );
        ColumnInfo colInfo = tcol.getColumnInfo();
        String colName = colInfo.getName();
        final Component parent = this;

        /* Action to replace current column. */
        Action replacecolAct =
            new BasicAction( "Replace Column", ResourceIcon.MODIFY,
                             "Replace " + colName + 
                             " with new synthetic column" ) {
                public void actionPerformed( ActionEvent evt ) {
                    SyntheticColumnQueryWindow
                       .replaceColumnDialog( tcModel, tcol, parent );
                }
            };
        replacecolAct.setEnabled( TopcatUtils.canJel() );
        popper.add( replacecolAct );

        /* Action to append a new column here. */
        Action addcolAct = 
            new BasicAction( "New Synthetic Column", ResourceIcon.ADD,
                             "Add new synthetic column after " + colName ) {
                public void actionPerformed( ActionEvent evt ) {
                    new SyntheticColumnQueryWindow( tcModel, jcol + 1, parent );
                }
            };
        addcolAct.setEnabled( TopcatUtils.canJel() );
        popper.add( addcolAct );

        /* Actions to sort on current column. */
        if ( jcol >= 0 ) {
            if ( Comparable.class
                           .isAssignableFrom( colInfo.getContentClass() ) ) {
                popper.add( tcModel
                           .getSortAction( new SortOrder( tcol ), true ) );
                popper.add( tcModel
                           .getSortAction( new SortOrder( tcol ), false ) );
            }
        }

        /* Action to hide the current column. */
        if ( jcol >= 0 ) {
            Action hidecolAct = 
                new BasicAction( "Hide Column", ResourceIcon.HIDE,
                                 "Hide column " + colName + " from view" ) {
                    public void actionPerformed( ActionEvent evt ) {
                        columnModel.removeColumn( tcol );
                    }
                };
            popper.add( hidecolAct );
        }

        return popper;
    }

    /**
     * Returns a BitSet in which bit <i>i</i> is set if a table view row
     * corresponding to row <i>i</i> of this viewer's data model has
     * been selected in the GUI.  The BitSet has the same number of bits
     * as the data model has rows.
     *
     * @return  new bit vector
     */
    private BitSet getSelectedRowFlags() {
        int nrow = (int) dataModel.getRowCount();
        BitSet bits = new BitSet( nrow );
        int[] selected = jtab.getSelectedRows();
        int nsel = selected.length;
        int[] rowMap = viewModel.getRowMap();
        if ( rowMap == null ) {
            for ( int i = 0; i < nsel; i++ ) {
                bits.set( selected[ i ] );
            }
        }
        else {
            for ( int i = 0; i < nsel; i++ ) {
                bits.set( rowMap[ selected[ i ] ] );
            }
        }
        return bits;
    }

    /**
     * Returns a BitSet in which bit <i>i</i> is set if a table view row
     * corresponding to row <i>i</i> of this viewer's data model 
     * (a) is currently active (part of the current subset) and
     * (b) has not been selected in the GUI.  The BitSet has the same
     * number of bits as the data model has rows.
     *
     * @return  new bit vector
     */
    private BitSet getUnselectedRowFlags() {
        int nrow = (int) dataModel.getRowCount();
        BitSet bits = new BitSet( nrow );
        int nactive = jtab.getRowCount();
        ListSelectionModel selModel = jtab.getSelectionModel();
        int[] rowMap = viewModel.getRowMap();
        if ( rowMap == null ) {
            for ( int i = 0; i < nactive; i++ ) {
                if ( ! selModel.isSelectedIndex( i ) ) {
                    bits.set( i );
                }
            }
        }
        else {
            assert rowMap.length == nactive;
            for ( int i = 0; i < nactive; i++ ) {
                if ( ! selModel.isSelectedIndex( i ) ) {
                    bits.set( rowMap[ i ] );
                }
            }
        }
        return bits;
    }
    

    /*
     * Implementation of TableModelListener interface.
     */
    public void tableChanged( TableModelEvent evt ) {
        if ( evt.getSource() == viewModel ) {
            // configureTitle();
        }
    }

    /*
     * Implementation of TableColumnModelListener interface.
     */
    public void columnAdded( TableColumnModelEvent evt ) {
        if ( evt.getSource() == columnModel ) {
            // configureTitle();
            StarJTable.configureColumnWidth( jtab, MAX_COLUMN_WIDTH, 
                                             MAX_SAMPLE_ROWS,
                                             evt.getToIndex() );
        }
    }                      
    public void columnRemoved( TableColumnModelEvent evt ) {
        if ( evt.getSource() == columnModel ) {
            // configureTitle();
        }   
    }           
    public void columnMarginChanged( ChangeEvent evt ) {}
    public void columnMoved( TableColumnModelEvent evt ) {}
    public void columnSelectionChanged( ListSelectionEvent evt ) {}


    /** 
     * TableViewerWindow specific actions.
     */
    private class ViewerAction extends BasicAction {
        public ViewerAction( String name, Icon icon, String shortdesc ) {
            super( name, icon, shortdesc );
        }

        public void actionPerformed( ActionEvent evt ) {
            if ( this == includeAct || this == excludeAct ) {
                boolean exclude = this == excludeAct;
                String name = tcModel
                             .enquireSubsetName( getEventWindow( evt ) );
                if ( name != null ) {
                    BitSet bits = exclude ? getUnselectedRowFlags()
                                          : getSelectedRowFlags();
                    RowSubset rset = new BitsRowSubset( name, bits );
                    tcModel.addSubset( rset );
                    tcModel.applySubset( rset );
                }
            }
            else {
                assert false;
            }
        }
    }
}
