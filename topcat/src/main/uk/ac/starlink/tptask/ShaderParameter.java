package uk.ac.starlink.tptask;

import java.util.ArrayList;
import java.util.List;
import uk.ac.starlink.task.ChoiceParameter;
import uk.ac.starlink.task.Environment;
import uk.ac.starlink.task.TaskException;
import uk.ac.starlink.tplot.Shader;
import uk.ac.starlink.tplot.Shaders;

/**
 * Parameter for choosing {@link uk.ac.starlink.tplot.Shader} objects.
 *
 * @author   Mark Taylor
 * @since    7 Aug 2008
 */
public class ShaderParameter extends ChoiceParameter {

    /** List of shaders.  Order is significant; the first absolute entry in
     *  this list is the default if there is only one shader, and if there
     *  are multiple ones the first few non-absolute ones are the defaults. */
    private static final Shader[] SHADERS = new Shader[] {
        Shaders.LUT_RAINBOW,
        Shaders.LUT_PASTEL,
        Shaders.LUT_STANDARD,
        Shaders.LUT_HEAT,
        Shaders.LUT_COLOR,
        Shaders.FIX_HUE,
        Shaders.WHITE_BLACK,
        Shaders.RED_BLUE,
        Shaders.HSV_H,
        Shaders.HSV_S,
        Shaders.HSV_V,
        Shaders.FIX_INTENSITY,
        Shaders.FIX_RED,
        Shaders.FIX_GREEN,
        Shaders.FIX_BLUE,
        Shaders.FIX_Y,
        Shaders.FIX_U,
        Shaders.FIX_V,
        Shaders.TRANSPARENCY,
    };

    /** List of shaders with isAbsolute() true. */
    private static final Shader[] ABS_SHADERS;

    /** List of shaders with isAbsolute() false. */
    private static final Shader[] MOD_SHADERS;
    static {
        List absList = new ArrayList();
        List modList = new ArrayList();
        for ( int i = 0; i < SHADERS.length; i++ ) {
            Shader shader = SHADERS[ i ];
            ( shader.isAbsolute() ? absList : modList ).add( shader );
        }
        ABS_SHADERS = (Shader[]) absList.toArray( new Shader[ 0 ] );
        MOD_SHADERS = (Shader[]) modList.toArray( new Shader[ 0 ] );
    }

    /**
     * Constructor.
     *
     * @param  name  parameter name
     */
    public ShaderParameter( String name ) {
        super( name );
        for ( int i = 0; i < SHADERS.length; i++ ) {
            Shader shader = SHADERS[ i ];
            addOption( shader, getShaderName( shader ) );
        }
        setPrompt( "Shader defining how aux axes are coloured" );
        setDescription( new String[] {
            "<p>Determines how data from auxiliary axes will be displayed.",
            "Generally this is some kind of colour ramp.",
            "These are the available <i>colour fixing</i> options:",
            formatShaderList( ABS_SHADERS ),
            "and these are the available <i>colour modifying</i> options:",
            formatShaderList( MOD_SHADERS ),
            "</p>",
        } );
    }

    public String getUsage() {
        return "<shader-name>";
    }

    /**
     * Returns the value of this parameter as a Shader object.
     *
     * @param  env  execution environment
     * @return   shader
     */
    public Shader shaderValue( Environment env ) throws TaskException {
        return (Shader) objectValue( env );
    }

    /**
     * Returns some suitable default values for a set of related
     * ShaderParameters.
     *
     * @param  count  number of parameters to get defaults for
     * @return  array of default values, one for each parameter
     */
    public static String[] getDefaultValues( int count ) {
        if ( count == 1 ) {
            return new String[] { getShaderName( ABS_SHADERS[ 0 ] ) };
        }
        else {
            String[] dflts = new String[ count ];
            for ( int i = 0; i < count; i++ ) {
                dflts[ i ] =
                    getShaderName( MOD_SHADERS[ i % MOD_SHADERS.length ] );
            }
            return dflts;
        }
    }

    /**
     * Returns the string used within the paraeter system to identify a 
     * shader selection.
     *
     * @param  shader  shader
     * @return  string identifier
     */
    private static String getShaderName( Shader shader ) {
        return shader.getName().replaceAll( " ", "_" );
    }

    /**
     * Returns a string which is an XML formatted list of a given set of
     * shaders.
     *
     * @return   XML list string
     */
    private static String formatShaderList( Shader[] shaders ) {
        StringBuffer sbuf = new StringBuffer()
            .append( "<ul>" )
            .append( '\n' );
        for ( int i = 0; i < shaders.length; i++ ) {
            sbuf.append( "<li>" )
                .append( "<code>" )
                .append( getShaderName( shaders[ i ] ) )
                .append( "</code>" )
                .append( "</li>" )
                .append( '\n' );
        }
        sbuf.append( "</ul>" )
            .append( '\n' );
        return sbuf.toString();
    }
}
