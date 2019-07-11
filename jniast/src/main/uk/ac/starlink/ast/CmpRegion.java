/* ********************************************************
 * This file automatically generated by CmpRegion.pl.
 *                   Do not edit.                         *
 **********************************************************/

package uk.ac.starlink.ast;


/**
 * Java interface to the AST CmpRegion class
 *  - a combination of two regions within a single Frame. 
 * A CmpRegion is a Region which allows two component
 * Regions (of any class) to be combined to form a more complex
 * Region. This combination may be performed a boolean AND, OR
 * or XOR (exclusive OR) operator. If the AND operator is
 * used, then a position is inside the CmpRegion only if it is
 * inside both of its two component Regions. If the OR operator is
 * used, then a position is inside the CmpRegion if it is inside
 * either (or both) of its two component Regions. If the XOR operator
 * is used, then a position is inside the CmpRegion if it is inside
 * one but not both of its two component Regions. Other operators can
 * be formed by negating one or both component Regions before using
 * them to construct a new CmpRegion.
 * <p>
 * The two component Region need not refer to the same coordinate
 * Frame, but it must be possible for the
 * astConvert
 * function to determine a Mapping between them (an error will be
 * reported otherwise when the CmpRegion is created). For instance,
 * a CmpRegion may combine a Region defined within an ICRS SkyFrame
 * with a Region defined within a Galactic SkyFrame. This is
 * acceptable because the SkyFrame class knows how to convert between
 * these two systems, and consequently the
 * astConvert
 * function will also be able to convert between them. In such cases,
 * the second component Region will be mapped into the coordinate Frame
 * of the first component Region, and the Frame represented by the
 * CmpRegion as a whole will be the Frame of the first component Region.
 * <p>
 * Since a CmpRegion is itself a Region, it can be used as a
 * component in forming further CmpRegions. Regions of arbitrary
 * complexity may be built from simple individual Regions in this
 * way.
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
 * @see  <a href='http://star-www.rl.ac.uk/cgi-bin/htxserver/sun211.htx/?xref_CmpRegion'>AST CmpRegion</a>  
 */
public class CmpRegion extends Region {

    /** Constant indicating AND-type region combination. */
    public static final int AST__AND = getAstConstantI( "AST__AND" );

    /** Constant indicating OR-type region combination. */
    public static final int AST__OR = getAstConstantI( "AST__OR" );
    /** 
     * Create a CmpRegion.   
     * This function creates a new CmpRegion and optionally initialises
     * its attributes.
     * <p>
     * A CmpRegion is a Region which allows two component
     * Regions (of any class) to be combined to form a more complex
     * Region. This combination may be performed a boolean AND, OR
     * or XOR (exclusive OR) operator. If the AND operator is
     * used, then a position is inside the CmpRegion only if it is
     * inside both of its two component Regions. If the OR operator is
     * used, then a position is inside the CmpRegion if it is inside
     * either (or both) of its two component Regions. If the XOR operator
     * is used, then a position is inside the CmpRegion if it is inside
     * one but not both of its two component Regions. Other operators can
     * be formed by negating one or both component Regions before using
     * them to construct a new CmpRegion.
     * <p>
     * The two component Region need not refer to the same coordinate
     * Frame, but it must be possible for the
     * astConvert
     * function to determine a Mapping between them (an error will be
     * reported otherwise when the CmpRegion is created). For instance,
     * a CmpRegion may combine a Region defined within an ICRS SkyFrame
     * with a Region defined within a Galactic SkyFrame. This is
     * acceptable because the SkyFrame class knows how to convert between
     * these two systems, and consequently the
     * astConvert
     * function will also be able to convert between them. In such cases,
     * the second component Region will be mapped into the coordinate Frame
     * of the first component Region, and the Frame represented by the
     * CmpRegion as a whole will be the Frame of the first component Region.
     * <p>
     * Since a CmpRegion is itself a Region, it can be used as a
     * component in forming further CmpRegions. Regions of arbitrary
     * complexity may be built from simple individual Regions in this
     * way.
     * <h3>Notes</h3>
     * <br> - If one of the supplied Regions has an associated uncertainty,
     * that uncertainty will also be used for the returned CmpRegion.
     * If both supplied Regions have associated uncertainties, the
     * uncertainty associated with the first Region will be used for the
     * returned CmpRegion.
     * <br> - Deep copies are taken of the supplied Regions. This means that
     * any subsequent changes made to the component Regions using the
     * supplied pointers will have no effect on the CmpRegion.
     * <br> - A null Object pointer (AST__NULL) will be returned if this
     * function is invoked with the AST error status set, or if it
     * should fail for any reason.
     * @param  region1  Pointer to the first component Region.
     * 
     * @param  region2  Pointer to the second component Region. This Region will be
     * transformed into the coordinate Frame of the first region before
     * use. An error will be reported if this is not possible.
     * 
     * @param  oper  The boolean operator with which to combine the two Regions. This
     * must be one of the symbolic constants AST__AND, AST__OR or AST__XOR.
     * 
     * @throws  AstException  if an error occurred in the AST library
    */
    public CmpRegion( Region region1, Region region2, int oper ) {
        construct( region1, region2, oper );
    }
    private native void construct( Region region1, Region region2, int oper );

}
