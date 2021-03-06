/* ********************************************************
 * This file automatically generated by MatrixMap.pl.
 *                   Do not edit.                         *
 **********************************************************/

package uk.ac.starlink.ast;


/**
 * Java interface to the AST MatrixMap class
 *  - map coordinates by multiplying by a matrix. 
 * A MatrixMap is form of Mapping which performs a general linear
 * transformation. Each set of input coordinates, regarded as a
 * column-vector, are pre-multiplied by a matrix (whose elements
 * are specified when the MatrixMap is created) to give a new
 * column-vector containing the output coordinates. If appropriate,
 * the inverse transformation may also be performed.
 * <h3>Licence</h3>
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public Licence as
 * published by the Free Software Foundation; either version 2 of
 * the Licence, or (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be
 * useful,but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR
 * PURPOSE. See the GNU General Public Licence for more details.
 * <p>
 * You should have received a copy of the GNU General Public Licence
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street,Fifth Floor, Boston, MA
 * 02110-1301, USA
 * 
 * 
 * @see  <a href='http://star-www.rl.ac.uk/cgi-bin/htxserver/sun211.htx/?xref_MatrixMap'>AST MatrixMap</a>  
 */
public class MatrixMap extends Mapping {

    /* Private native construction function - invokes the underlying 
     * construction function in the AST library. */
    private native void construct( int nin, int nout, int form, 
                                   double[] matrix );

    /**
     * Creates a MatrixMap using a fully specified matrix.
     *
     * @param  nin    the number of input coordinates
     * @param  nout   the number of output coordinates
     * @param  fullmatrix the matrix defining the transformation.
     *                <code>fullmatrix</code> must have <code>nout</code>
     *                elements, each of which is an array of doubles
     *                with <code>nin</code> elements.
     * @throws AstException  if there is an error in the AST library, or
     *                       if the supplied matrix is the wrong shape
     */
    public MatrixMap( int nin, int nout, double[][] fullmatrix ) {
        double[] matrix;

        /* Set up arguments for generic constructor, validating matrix shape. */
        if ( fullmatrix.length == nout ) {
            matrix = new double[ nin * nout ];
            for ( int i = 0; i < nout; i++ ) {
                if ( fullmatrix[ i ].length == nin ) {
                    System.arraycopy( fullmatrix[ i ], 0, 
                                      matrix, nin * i, nin );
                }
                else { 
                    throw new AstException( 
                        "construction matrix is the wrong shape" );
                }
            }
        }
        else {
            throw new AstException( "construction matrix is the wrong shape" );
        }

        /* Call the generic constructor. */
        construct( nin, nout, 0, matrix );
    }

    /**
     * Creates a MatrixMap using a diagonal matrix.  All off-diagonal
     * elements are considered equal to zero.
     *
     * @param  nin    the number of input coordinates
     * @param  nout   the number of output coordinates
     * @param  diag   the diagonal elements of the matrix defining the 
     *                transformation.  Must have at least 
     *                <code>min(nin,nout)</code> elements.
     * @throws AstException  if there is an error in the AST library, or
     *                       if the supplied matrix is the wrong shape
     */
    public MatrixMap( int nin, int nout, double[] diag ) {
        double[] matrix;

        /* Set up arguments for generic constructor, validating matrix shape. */
        if ( diag.length >= nin || diag.length >= nout ) {
            matrix = diag;
        }
        else {
            throw new AstException( "construction matrix is the wrong shape" );
        }

        /* Call the generic constructor. */
        construct( nin, nout, 1, matrix );
    }

    /**
     * Creates a MatrixMap using a unit matrix.
     *
     * @param  nin    the number of input coordinates
     * @param  nout   the number of output coordinates
     * @throws AstException  if there is an error in the AST library
     */
    public MatrixMap( int nin, int nout ) {
        construct( nin, nout, 2, (double[]) null );
    }
}
