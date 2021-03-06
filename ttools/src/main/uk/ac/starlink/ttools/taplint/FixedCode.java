package uk.ac.starlink.ttools.taplint;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Enumerates known ReportCode instances.
 * A short description is provided for each instance.
 * Note the description is not definitive, the actual message passed
 * through the reporting system in association with the code is the one
 * that should actually be passed to the user.
 *
 * <p>This class exists for taplint clients that want to have a static
 * idea of the ReportCode instances that may be reported by the taplint
 * framework.  Note it does <em>not</em> provide a complete list of
 * all the ReportCodes, since other ReportCode sublclasses may be used.
 * But it is expected to be the large majority.
 *
 * <p>The name of each enum element has a fixed form, <code>T_LLLL</code>,
 * where <code>T</code> is code.getType().getChar()
 *   and <code>LLLL</code> is code.getLabel().
 *
 * @author   Mark Taylor
 * @since    11 Jun 2014
 */
public enum FixedCode implements ReportCode {

    E_A2XI( "Non-ADQL2 declared as ADQL2" ),
    E_ADQX( "ADQL not declared" ),
    E_BAPH( "Incorrect starting phase" ),
    E_BMIM( "Illegal MIME type" ),
    E_CERR( "Error reading TAP_SCHEMA.columns data" ),
    E_CINT( "Wrong type for TAP_SCHEMA.columns column" ),
    E_CLDR( "Declared columns absent in result" ),
    E_CLIO( "Error reading TAP_SCHEMA.columns table" ),
    E_CLOG( "Non-boolean value" ),
    E_CLRD( "Unexpected columns in result" ),
    E_CNAM( "Query/result column name mismatch" ),
    E_CNID( "Column name not ADQL identifier" ),
    E_CPIF( "No TAP capability std interface declared" ),
    E_CPIO( "Capabilities metadata read error" ),
    E_CPSX( "Capabilities metadata parse failure" ),
    E_CPT1( "No unique TAP capability element" ),
    E_CPTV( "TAP version declaration mismatch" ),
    E_CRSV( "Column name ADQL reserved word" ),
    E_CTNO( "Continuation document not found" ),
    E_CTYP( "Declared/result type mismatch" ),
    E_CUCD( "UCD mismatch" ),
    E_CUNI( "Unit mismatch" ),
    E_CUTP( "Utype mismatch" ),
    E_DCER( "Document read error" ),
    E_DECO( "Non-303 response to job deletion" ),
    E_DEHT( "Bad HTTP job connection" ),
    E_DEMO( "Job deletion failure" ),
    E_DENO( "Non-404 response for deleted job" ),
    E_DEOP( "Unavailable job URL" ),
    E_DFIO( "Duff query failed" ),
    E_DFSF( "Duff query result parse failure" ),
    E_DNST( "Missing QUERY_STATUS from duff query" ),
    E_DQUM( "QUERY_STATUS inconsistency" ),
    E_DQUS( "Unknown value for QUERY_STATUS" ),
    E_DSUC( "Non-error return from duff query" ),
    E_ELDF( "Wrong top-level element" ),
    E_EST1( "Multiple QUERY_STATUS INFOs" ),
    E_EURL( "HTTP error" ),
    E_EXCH( "Bad continuation href attribute" ),
    E_EXDH( "Examples declaration/presence mismatch" ),
    E_EXCR( "Bad continuation resource attribute" ),
    E_EXIO( "Error reading examples document" ),
    E_EXPA( "Badly formed XML examples document" ),
    E_EXVC( "Incorrect examples RDFa vocab attribute" ),
    E_FKIO( "Error reading TAP_SCHEMA.keys table" ),
    E_FKLK( "Foreign key link broken" ),
    E_FKNT( "Foreign key target table absent" ),
    E_FLIO( "Table metadata read error" ),
    E_FLSX( "Table metadata parse failure" ),
    E_GEOX( "Unknown geometry function" ),
    E_GMIM( "Content-Type mismatch" ),
    E_GONM( "Missing mandatory resource" ),
    E_GPER( "Example generic-parameter markup wrong" ),
    E_HNUL( "Illegal NULLs in ObsCore column" ),
    E_HTDE( "HTTP DELETE failure" ),
    E_HTOF( "Unavailable job URL" ),
    E_IFMT( "Non-integer result" ),
    E_ILOP( "ObsCore value not in required set" ),
    E_ILPH( "Illegal job phase" ),
    E_IOER( "Error reading document" ),
    E_JBIO( "Job read error" ),
    E_JBSP( "Job parse error" ),
    E_JDDE( "Destruction time mismatch" ),
    E_JDED( "Execution duration mismatch" ),
    E_JDID( "Job ID mismatch" ),
    E_JDIO( "Error reading job document" ),
    E_JDNO( "Missing job document" ),
    E_JDPH( "Info/phase phase mismatch" ),
    E_JDSX( "Error parsing job document" ),
    E_KCIO( "Error reading TAP_SCHEMA.key_columns table" ),
    E_KEYX( "Unknown standard language feature key" ),
    E_LVER( "Some ADQL variants fail" ),
    E_MCOL( "Query/result column count mismatch" ),
    E_MUPM( "Missing mandatory upload declaration" ),
    E_NAKT( "Table elements outside of any Schema" ),
    E_NFND( "Non-OK HTTP response" ),
    E_NO11( "Multi-cell result for COUNT" ),
    E_NOHT( "Non-HTTP job URL" ),
    E_NONM( "Non-numeric value for COUNT" ),
    E_NOOF( "No output formats defined" ),
    E_NOQL( "No query languages declared" ),
    E_NOST( "Missing QUERY_STATUS" ),
    E_NOVO( "Missing examples RDFa vocab attribute" ),
    E_NREC( "Maxrec limit exceeded" ),
    E_NRER( "COUNT error" ),
    E_NROW( "Row limit exceeded" ),
    E_OCOL( "Missing required ObsCore column" ),
    E_OVNO( "Overflow not signalled" ),
    E_PADU( "Duplicate parameter" ),
    E_PAMM( "Bad job parameter value" ),
    E_PANO( "Nameless parameter" ),
    E_PANZ( "Non-blank job parameter" ),
    E_PHUR( "Incorrect phase" ),
    E_POER( "POST failure" ),
    E_PORE( "POST error" ),
    E_PTXT( "Non plain-text RDFa property content" ),
    E_QERR( "Query failed" ),
    E_QERX( "Query result parse error" ),
    E_QFAA( "Query failure" ),
    E_QST1( "Multiple pre-table QUERY_STATUS INFOs" ),
    E_QST2( "Multiple post-table QUERY_STATUS INFOs" ),
    E_RALC( "RDFa Lite rules violated" ),
    E_RANG( "ObsCore values out of range" ),
    E_RDPH( "Job phase read failure" ),
    E_RRES( "RESOURCE not marked 'results'" ),
    E_RRTO( "Row limit exceeded" ),
    E_RUPH( "Bad phase for started job" ),
    E_SCIO( "Error reading TAP_SCHEMA.schemas table" ),
    E_SPPA( "Spurious query parameter breaks query" ),
    E_TADH( "Tables endpoint declaration/presence mismatch" ),
    E_TBIO( "Error reading TAP_SCHEMA.tables table" ),
    E_TMCD( "Upload result column value mismatch" ),
    E_TMCN( "Upload result column name mismatch" ),
    E_TMCX( "Upload result column xtype mismatch" ),
    E_TMNC( "Upload result column count mismatch" ),
    E_TMNR( "Upload result row count mismatch" ),
    E_TNTN( "Table name not ADQL table_name" ),
    E_TOVO( "Multiple examples RDFa vocab attributes" ),
    E_TRSV( "Table name ADQL reserved word" ),
    E_TSC0( "Missing TAP_SCHEMA column" ),
    E_TSCT( "Wrong type for TAP_SCHEMA column" ),
    E_TSAZ( "TAP_SCHEMA arraysize syntax error" ),
    E_TSSZ( "TAP_SCHEMA size/arraysize inconsistency" ),
    E_TST0( "Missing TAP_SCHEMA table" ),
    E_TSTD( "TAP_SCHEMA column not flagged Std" ),
    E_TSLN( "Missing TAP 1.1 TAP_SCHEMA foreign key" ),
    E_TSNL( "Null values in non-nullable column" ),
    E_TTOO( "Multiple result TABLEs" ),
    E_TYPX( "ObsCore datatype incompatibilty" ),
    E_UDFE( "Bad UDF declaration" ),
    E_UKTB( "Unknown table referenced in example" ),
    E_UPBD( "Unknown upload suffix" ),
    E_UPER( "Upload error" ),
    E_VOCT( "Bad Content-Type for VOTable" ),
    E_VTIO( "Query result read error" ),
    E_VVLO( "VOTable version unsupported by TAP" ),
    E_XID0( "Missing id attribute on example" ),
    E_XOFK( "Unknown standard output format" ),
    E_XPRM( "Bad values for example parameters" ),
    E_XRS0( "Missing resource attribute on example" ),
    E_XRS1( "Resource/id attribute mismatch on example" ),

    W_A2MN( "No ADQL2 declaration" ),
    W_A2MX( "Non-standard ADQL2 declaration" ),
    W_AD2X( "ADQL2 not declared" ),
    W_CCAS( "Capitalisation mismatch" ),
    W_CDET( "Partial column metadata from /tables" ),
    W_CEUK( "Unknown Content-Encoding" ),
    W_CEZZ( "Compression" ),
    W_CIDX( "Index flag mismatch" ),
    W_CLUN( "Columns content" ),
    W_CPAN( "Deprecated declaration of anonymous resource" ),
    W_CPI2( "No unique std interface in capability" ),
    W_CPS2( "Duplicated security method ID" ),
    W_CPSM( "Unknown security method ID" ),
    W_CPUL( "Non-standard access URL for standard interface" ),
    W_CPUR( "Service and declared TAP base URL mismatch" ),
    W_CTYP( "Datatype possible mismatch" ),
    W_CUCD( "UCD mismatch" ),
    W_CULF( "Custom language feature type" ),
    W_CUNI( "Unit mismatch" ),
    W_CUTP( "UType mismatch" ),
    W_DMDC( "DataModel not declared" ),
    W_DMSS( "DataModel subset undeclared" ),
    W_DQU2( "Unknown value for post-table QUERY_STATUS" ),
    W_EX00( "No examples in examples document" ),
    W_EXUL( "Undeclared query language" ),
    W_EXVC( "Potentially problematic examples RDFa vocab attribute" ),
    W_EXVL( "Example query does not validate" ),
    W_FKUN( "Keys content" ),
    W_FLUN( "Key columns content" ),
    W_FTYP( "Foreign key type mismatch" ),
    W_GONO( "Missing optional resource" ),
    W_HSTB( "Non-duff return from duff query" ),
    W_HURL( "Redirect to non-HTTP URL" ),
    W_IODM( "Incorrect ObsCore ID" ),
    W_LVAN( "Language has empty version string" ),
    W_MLTR( "Multiple references to continuation document" ),
    W_NMID( "Id attribute is not XML Name" ),
    W_NOCT( "Missing Content-Type header" ),
    W_NOMS( "No error message text" ),
    W_NSOP( "ObsCore value not in suggested set" ),
    W_QERR( "Query failed" ),
    W_QTYP( "Query/result column type possible mismatch" ),
    W_RDIO( "Resource read error" ),
    W_SPPA( "Spurious query parameter changes result" ),
    W_TADH( "Tables endpoint declaration/presence mismatch" ),
    W_TBNF( "Tables endpoint missing" ),
    W_TBUN( "Tables content" ),
    W_TFMT( "Non-ISO-8601 result" ),
    W_TSDL( "Bad time format in table data" ),
    W_TSZ1( "Single-element array declared" ),
    W_TYPI( "ObsCore datatype mismatch" ),
    W_UNPH( "UNKNOWN phase" ),
    W_UNSC( "Foreign schema used in validation" ),
    W_UPCS( "Custom upload method" ),
    W_VUWS( "Unknown UWS version" ),
    W_WODM( "Incorrect ObsCore ID" ),
    W_ZRES( "Resolver not used?" ),

    F_CAIO( "Bad capabilities service URL" ),
    F_CAP0( "No capabilities available" ),
    F_CAPC( "XML parser error" ),
    F_DTIO( "Unexpected table read error" ),
    F_EXNO( "No examples document" ),
    F_EXRD( "Trouble reading example element" ),
    F_GONE( "Table metadata absent" ),
    F_INTR( "Interrupted" ),
    F_MURL( "Bad URL" ),
    F_NODT( "Metadata lacks column/key detail" ),
    F_NOTB( "No obscore table" ),
    F_NOTM( "Earlier metadata stages not completed" ),
    F_NOUP( "No upload methods listed" ),
    F_SXER( "Unexpected SAX parse error" ),
    F_TBLA( "Table has no name" ),
    F_TIOF( "Error reading result table" ),
    F_TRND( "Table randomisation failure" ),
    F_UNEX( "Unexpected error (validator bug?)" ),
    F_UTF8( "Unknown UTF8 encoding" ),
    F_XENT( "DTD entity trouble" ),
    F_XURL( "Bad document URL" ),
    F_XVAL( "Validator preparation error" ),
    F_ZCOL( "No columns known for tests" ),

    I_CDET( "No column metadata from /tables" ),
    I_CJOB( "Job created" ),
    I_CURL( "Reading capability metadata" ),
    I_DMID( "Data model declared" ),
    I_DQUR( "QUERY_STATUS redundancy" ),
    I_DUFF( "Duff query" ),
    I_EURL( "Reading examples document" ),
    I_EXA2( "Assume default query language" ),
    I_EXMP( "Example found" ),
    I_JOFI( "Job finished immediately" ),
    I_NODM( "No ObsCore tests" ),
    I_OCCP( "No ObsCore tests" ),
    I_POPA( "Job POSTed" ),
    I_QGET( "Query GET URL" ),
    I_QJOB( "Submitted query URL" ),
    I_QSUB( "Submitted query" ),
    I_SCHM( "Use TAP_SCHEMA for metadata" ),
    I_TAPV( "TAP validation version" ),
    I_TMAX( "Table test count" ),
    I_TSNS( "Non-standard TAP_SCHEMA columns" ),
    I_TURL( "Reading table metadata" ),
    I_VURL( "Validation" ),
    I_VUWS( "UWS job document version" ),
    I_VVNL( "Undeclared VOTable version" ),
    I_VVUN( "Unknown VOTable version" ),

    S_COLS( "ObsCore columns" ),
    S_FLGO( "Non-standard column flags" ),
    S_FLGS( "Standard column flags" ),
    S_QNUM( "Query count" ),
    S_QTIM( "Query time" ),
    S_SUMM( "Table metadata" ),
    S_VALI( "Validation" ),
    S_XNUM( "Example count summary" ),
    S_XVAL( "Example validation summary" );

    private final String description_;

    /**
     * Constructor.
     *
     * @param  description  short description
     */
    FixedCode( String description ) {
        description_ = description;
    }

    public ReportType getType() {
        return ReportType.forChar( name().charAt( 0 ) );
    }

    public String getLabel() {
        return name().substring( 2 );
    }

    /**
     * Returns a short textual description of the use of this code.
     * It may not be very precise; if the message put through the reporting
     * system is available, that should be used in preference.
     *
     * @return  description
     */
    public String getDescription() {
        return description_;
    }
}
