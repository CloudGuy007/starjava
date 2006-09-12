package uk.ac.starlink.util;

import java.io.PrintStream;
import java.util.LinkedList;

/**
 * Provides methods for writing XML output to a stream.
 *
 * @author   Mark Taylor
 * @since    17 Mar 2006
 */
public class XmlWriter {

    private PrintStream out_;
    private int level_;
    private LinkedList stack_;

    /**
     * Constructs a new writer which outputs to <code>System.out</code>.
     */
    public XmlWriter() {
        this( System.out );
    }

    /**
     * Constructs a new writer which writes to a given print stream.
     *
     * @param   out  destination stream
     */
    public XmlWriter( PrintStream out ) {
        out_ = out;
        stack_ = new LinkedList();
    }

    /**
     * Writes an XML declaration.
     * Only call this before any other output.
     */
    public void writeDeclaration() {
        out_.println( "<?xml version='1.0' encoding='utf-8'?>" );
    }

    /**
     * Outputs a start element tag with no attributes.
     *
     * @param  elName  name of the element
     */
    public void startElement( String elName ) {
        startElement( elName, "" );
    }

    /**
     * Outputs a start element tag with a given list of attributes.
     * The supplied attribute list is exactly as it will be inserted into
     * the output, so it must start with a space (if it's not empty) and
     * any relevant escaping must have been done.
     *
     * @param  elName  name of the element
     * @param  attList  literal string giving the attribute list
     */
    public void startElement( String elName, String attList ) {
        out_.println( getIndent( level_++ ) + "<" + elName + attList + ">" );
        stack_.add( elName );
        assert level_ == stack_.size();
    }

    /**
     * Outputs an end element tag.
     *
     * @param   elName  name of the element
     * @throws  IllegalArgumentException  if that element's not ready to finish
     */
    public void endElement( String elName ) {
        String openElName = (String) stack_.removeLast();
        if ( ! openElName.equals( elName ) ) {
            throw new IllegalArgumentException( "Start/end tag mismatch: " +
                                                elName + " != " + openElName );
        }
        out_.println( getIndent( --level_ ) + "</" + elName + ">" );
    }

    /**
     * Writes a whole element with given attribute list and content.
     * The supplied attribute list and content strings are 
     * exactly as they will be inserted into
     * the output, so it must start with a space (if it's not empty) and
     * any relevant escaping must have been done.
     *
     * @param   elName  name of the element
     * @param  attList  literal string giving the attribute list
     * @param  content  literal string giving the element content
     */
    public void addElement( String elName, String attList, String content ) {
        if ( content != null && content.trim().length() > 0  ) {
            out_.println( getIndent( level_ )
                        + "<" + elName + attList + ">"
                        + content
                        + "</" + elName + ">" );
        }
        else {
            out_.println( getIndent( level_ )
                        + "<" + elName + attList + "/>" );
        }
    }

    /**
     * Outputs a literal string in the output.
     *
     * @param  txt  literal text
     */
    public void print( String txt ) {
        out_.print( txt );
    }

    /**
     * Sets the destination stream for this writer.
     *
     * @param  out  new destination stream
     */
    public void setOut( PrintStream out ) {
        out_ = out;
    }

    /**
     * Outputs a literal string in the output followed by a newline character.
     *
     * @param  txt  literal text
     */
    public void println( String txt ) {
        out_.println( txt );
    }

    /**
     * Returns the current element nesting level.
     *
     * @return   nesting level (0 at start and end of document)
     */
    public int getLevel() {
        return level_;
    }

    /**
     * Turns a name,value pair into an attribute assignment suitable for
     * putting in an XML start tag.
     * The resulting string starts with, but does not end with, whitespace.
     * Any necessary escaping of the strings is taken care of.
     *
     * @param  name  the attribute name
     * @param  value  the attribute value
     * @return  string of the form ' name="value"'
     */
    public static String formatAttribute( String name, String value ) {
        if ( value == null || value.length() == 0 ) {
            return "";
        }
        return new StringBuffer()
            .append( ' ' )
            .append( name )
            .append( '=' )
            .append( '"' )
            .append( value.replaceAll( "&", "&amp;" )
                          .replaceAll( "<", "&lt;" )
                          .replaceAll( ">", "&gt;" )
                          .replaceAll( "\"", "&quot;" ) )
            .append( '"' )
            .toString();
    }

    /**
     * Performs necessary special character escaping for text which
     * will be written as XML CDATA.
     *
     * @param   text  the input text
     * @return  <tt>text</tt> but with XML special characters escaped
     */
    public static String formatText( String text ) {
        int leng = text.length();
        StringBuffer sbuf = new StringBuffer( leng );
        for ( int i = 0; i < leng; i++ ) {
            char c = text.charAt( i );
            switch ( c ) {
                case '<':
                    sbuf.append( "&lt;" );
                    break;
                case '>':
                    sbuf.append( "&gt;" );
                    break;
                case '&':
                    sbuf.append( "&amp;" );
                    break;
                default:
                    sbuf.append( c );
            }
        }
        return sbuf.toString();
    }

    /** 
     * Returns the indentation string associated with a given level.
     * This is a couple of spaces for each level.
     *
     * @return  level
     */
    public String getIndent( int level ) {
        StringBuffer sbuf = new StringBuffer();
        for ( int i = 0; i < level; i++ ) {
            sbuf.append( "  " );
        }
        return sbuf.toString();
    }
}
