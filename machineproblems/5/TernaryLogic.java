/** TernaryLogic.java -- classes that describe a ternary logic system
 *  @author Sam Eakin
 *  @author Douglas Jones
 *  @version mp4, 2017-04-13
 *
 *  This code borrows heavily from TernaryLogic.java version mp3, 2017-03-12.
 *  ScanSupport and Simulation are from RoadNetwork.java version 2017-03-31.
 *  Changes to ScanSupport are from the posted solution to Homework 7.
 *  The gate simulation mechanism comes from the posted solution to Homework 8.
 */

import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.PriorityQueue;

/** TernaryLogic -- main program that reads and writes a ternary logic system
 *  @see Wire
 *  @see Gate
 *  @see Errors
 *  @see #main
 */
public class TernaryLogic {
	// lists of roads and intersectins
	static LinkedList <Wire> wires
		= new LinkedList <Wire> ();
	static LinkedList <Gate> gates
		= new LinkedList <Gate> ();

	/** utility method to look up an gate by name
	 *  @param s is the name of the gate, a string
	 *  @return is the Gate object with that name
	 */
	public static Gate findGate( String s ) {
		for ( Gate g: gates ) {
			if (g.name.equals( s )) return g;
		}
		return null;
	}

	/** read a ternary logic system
	 */
	public static void initializeTernary( Scanner sc ) {
		while (sc.hasNext()) {
			// until we hit the end of the file
			String command = ScanSupport.nextName( sc );
			if ("gate".equals( command )) {
				Gate g = Gate.newGate( sc );
				if (g != null) gates.add( g );

			} else if ("wire".equals( command )) {
				wires.add( new Wire( sc ) );

			} else if ("".equals( command )) { // blank or comment
				// line holding -- ends up here!
				ScanSupport.lineEnd( sc, () -> "Line" );

			} else {
				Errors.warn(
					"Command '" + command +
					"' is not gate or wire"
				);
				sc.nextLine(); // skip the rest of the error
			}
		}
	}

        /** check the sanity of the network
         */
        public static void checkNetwork() {
                for ( Gate g: gates ) {
                        g.check();
                }
                // we could also go through the wires,
		// but there's nothing to check there.
        }

	/** write out a ternary logic system
	 */
	public static void writeTernary() {
		for ( Gate g: gates ) {
			System.out.println( g.toString() );
		}
		for ( Wire w: wires ) {
			System.out.println( w.toString() );
		}
	}

	/** main program that reads and writes a road network
	 *  @param args the command line arguments must hold one file name
	 */
	public static void main( String[] args ) {
		// verify that the argument exists.
		if (args.length < 1) {
			Errors.fatal( "Missing file name on command line" );

		} else if (args.length > 1) {
			Errors.fatal( "Unexpected command line args" );

		} else try {
			initializeTernary( new Scanner( new File( args[0] ) ) );
			checkNetwork();
			if (Errors.count() > 0) {
				writeTernary();
			} else {
				Simulation.run();
			}

		} catch (FileNotFoundException e) {
			Errors.fatal( "Could not read '" + args[0] + "'" );
		}
	}
}
