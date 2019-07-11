/* ********************************************************
 * This file automatically generated by Box.pl.
 *                   Do not edit.                         *
 **********************************************************/

package uk.ac.starlink.ast;


/**
 * Java interface to the AST Box class
 *  - a box region with sides parallel to the axes of a Frame. 
 * The Box class implements a Region which represents a box with sides
 * parallel to the axes of a Frame (i.e. an area which encloses a given
 * range of values on each axis). A Box is similar to an Interval, the
 * only real difference being that the Interval class allows some axis
 * limits to be unspecified. Note, a Box will only look like a box if
 * the Frame geometry is approximately flat. For instance, a Box centred
 * close to a pole in a SkyFrame will look more like a fan than a box
 * (the Polygon class can be used to create a box-like region close to a
 * pole).
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
 * @see  <a href='http://star-www.rl.ac.uk/cgi-bin/htxserver/sun211.htx/?xref_Box'>AST Box</a>  
 */
public class Box extends Region {
    /** 
     * Create a Box.   
     * This function creates a new Box and optionally initialises its
     * attributes.
     * <p>
     * The Box class implements a Region which represents a box with sides
     * parallel to the axes of a Frame (i.e. an area which encloses a given
     * range of values on each axis). A Box is similar to an Interval, the
     * only real difference being that the Interval class allows some axis
     * limits to be unspecified. Note, a Box will only look like a box if
     * the Frame geometry is approximately flat. For instance, a Box centred
     * close to a pole in a SkyFrame will look more like a fan than a box
     * (the Polygon class can be used to create a box-like region close to a
     * pole).
     * <h3>Notes</h3>
     * <br> - A null Object pointer (AST__NULL) will be returned if this
     * function is invoked with the AST error status set, or if it
     * should fail for any reason.
     * <h3>Status Handling</h3>
     * The protected interface to this function includes an extra
     * parameter at the end of the parameter list descirbed above. This
     * parameter is a pointer to the integer inherited status
     * variable: "int *status".
     * 
     * @param  frame  A pointer to the Frame in which the region is defined. A deep
     * copy is taken of the supplied Frame. This means that any
     * subsequent changes made to the Frame using the supplied pointer
     * will have no effect the Region.
     * 
     * @param  form  Indicates how the box is described by the remaining parameters.
     * A value of zero indicates that the box is specified by a centre
     * position and a corner position. A value of one indicates that the
     * box is specified by a two opposite corner positions.
     * 
     * @param  point1  An array of double, with one element for each Frame axis
     * (Naxes attribute). If
     * "form"
     * is zero, this array should contain the coordinates at the centre of
     * the box.
     * If "form"
     * is one, it should contain the coordinates at the corner of the box
     * which is diagonally opposite the corner specified by
     * "point2".
     * 
     * @param  point2  An array of double, with one element for each Frame axis
     * (Naxes attribute) containing the coordinates at any corner of the
     * box.
     * 
     * @param  unc  An optional pointer to an existing Region which specifies the
     * uncertainties associated with the boundary of the Box being created.
     * The uncertainty in any point on the boundary of the Box is found by
     * shifting the supplied "uncertainty" Region so that it is centred at
     * the boundary point being considered. The area covered by the
     * shifted uncertainty Region then represents the uncertainty in the
     * boundary position. The uncertainty is assumed to be the same for
     * all points.
     * <p>
     * If supplied, the uncertainty Region must be of a class for which
     * all instances are centro-symetric (e.g. Box, Circle, Ellipse, etc.)
     * or be a Prism containing centro-symetric component Regions. A deep
     * copy of the supplied Region will be taken, so subsequent changes to
     * the uncertainty Region using the supplied pointer will have no
     * effect on the created Box. Alternatively,
     * a NULL Object pointer
     * may be supplied, in which case a default uncertainty is used
     * equivalent to a box 1.0E-6 of the size of the Box being created.
     * <p>
     * The uncertainty Region has two uses: 1) when the
     * astOverlap
     * function compares two Regions for equality the uncertainty
     * Region is used to determine the tolerance on the comparison, and 2)
     * when a Region is mapped into a different coordinate system and
     * subsequently simplified (using
     * astSimplify),
     * the uncertainties are used to determine if the transformed boundary
     * can be accurately represented by a specific shape of Region.
     * 
     * @throws  AstException  if an error occurred in the AST library
    */
    public Box( Frame frame, int form, double[] point1, double[] point2, Region unc ) {
        construct( frame, form, point1, point2, unc );
    }
    private native void construct( Frame frame, int form, double[] point1, double[] point2, Region unc );

}
