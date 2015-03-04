package uk.ac.starlink.ttools.plot2.layer;

import java.util.Random;
import uk.ac.starlink.util.TestCase;

public class KernelTest extends TestCase {

    private Random rand_ = new Random( 235089454L );

    public void testKernels() {
        Kernel1dShape[] kshapes = Kernel1dShape.getStandardOptions();
        for ( Kernel1dShape kshape : Kernel1dShape.getStandardOptions() ) {
            for ( double width : new double[] { 0.0, 1.0, 4.25, 12.8 } ) {
                checkExactNormKernel( kshape.createKernel( width ) );
            }
            checkUnitKernel( kshape.createKernel( 0 ) );
        }
    }

    private void checkExactNormKernel( Kernel1d kernel ) {
        checkNormKernel( kernel, kernel.getExtent() );
    }

    private void checkNormKernel( Kernel1d kernel, double ext ) {
        int extent = (int) Math.ceil( ext );
        for ( int ns = 1; ns < 20; ns++ ) {
            double sum = 0;
            int np = ns + extent * 2;
            double[] in = new double[ np ];
            int nd = rand_.nextInt( 10 );
            for ( int id = 0; id < nd; id++ ) {
                in[ extent + rand_.nextInt( ns ) ]++;
                sum++;
            }
            double[] out = kernel.convolve( in );
            assertEquals( in.length, out.length );
            double tin = 0;
            double tout = 0;
            for ( int ip = 0; ip < np; ip++ ) {
                tin += in[ ip ];
                tout += out[ ip ];
            }
            assertEquals( sum, tin );
            assertEquals( sum, tout, sum * 1e-10 );
        }
    }

    private void checkUnitKernel( Kernel1d kernel ) {
        for ( int ns = 1; ns < 20; ns++ ) {
            double[] in = new double[ ns ];
            int nd = rand_.nextInt( 10 );
            for ( int id = 0; id < nd; id++ ) {
                in[ rand_.nextInt( ns ) ]++;
            }
            double[] out = kernel.convolve( in );
            assertArrayEquals( in, out );
        }
    }
}