package uk.ac.starlink.task;

/**
 * Parameter value representing a boolean value.
 * Permissible string values are true, false, yes and no (case insensitive).
 *
 * @author   Mark Taylor
 * @since    9 Aug 2005
 */
public class BooleanParameter extends Parameter<Boolean> {

    /**
     * Constructs a new boolean parameter.
     *
     * @param  name  parameter name
     */
    public BooleanParameter( String name ) {
        super( name, Boolean.class, false );
        setUsage( "true|false" );
        setNullPermitted( false );
    }

    /**
     * Returns the value of this parameter as a boolean.
     *
     * @param   env  execution environment
     * @return  boolean value
     * @throws  NullPointerException if the value is null, only possible
     *          if isNullPermitted is true (not by default)
     */
    public boolean booleanValue( Environment env ) throws TaskException {
        return objectValue( env ).booleanValue();
    }

    /**
     * Sets the default as a boolean value.
     *
     * @param   dflt  default value
     */
    public void setDefault( boolean dflt ) {
        setDefault( dflt ? "true" : "false" );
    }

    public Boolean stringToObject( Environment env, String stringval )
            throws ParameterValueException {
        if ( "TRUE".equalsIgnoreCase( stringval ) ||
             "YES".equalsIgnoreCase( stringval ) ) {
            return Boolean.TRUE;
        }
        else if ( "FALSE".equalsIgnoreCase( stringval ) ||
                  "NO".equalsIgnoreCase( stringval ) ) {
            return Boolean.FALSE;
        }
        else {
            throw new ParameterValueException( this, stringval +
                                               " is not true/false/yes/no" );
        }
    }
}
