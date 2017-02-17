
r Douglas Jones
 *  @version 2017-02-10
 *   */

 import java.util.LinkedList;
 import java.io.File;
 import java.io.FileNotFoundException;
import java.util.Scanner;

 /** Utility package for error handling
  *  */
class Errors {
	private Errors(){}; // you may never instantiate this class

	/** Call this to warn of non fatal errors
	 * 	 */
	public static void warn( String message ) {
		System.err.println( "Warning: " + message );
	}

	/** Call this to report fatal errors
	 * 	 */
	public static void fatal( String message ) {
		System.err.println( "Fatal error: " + message );
				System.exit( -1 );
	}
}

/** Roads link intersections
 *  *  @see Intersection
 *   *  @see Errors
 *    *  @see RoadNetwork#findRoad(String)
 *     */
class Road {
	private final float travelTime;		// how long to travel down road
	private final Intersection destination;	// where road goes, or null
	private final Intersection source;	// source of road, or null
	// Road name is the source-destination names
	/** initializer scans and processes one road definition
	 */
	public Road( Scanner sc, LinkedList <Intersection> inters ) {
		String srcName = sc.next();
		String dstName = sc.next();

		source = RoadNetwork.findIntersection( srcName );
		if (source == null) {
			Errors.warn(
						"Road '" + srcName +
						"' '" + dstName +
						"' source undefined."
					);
		}
		destination = RoadNetwork.findIntersection( srcName );
		if (destination == null) {
			Errors.warn(
				"Road '" + srcName +
				"' '" + dstName +
				"' destination undefined.");
			}

			// Bug: What if there is no next float?
			travelTime = sc.nextFloat();
			sc.nextLine();
	}

	/** output this road in a format like that used for input
	 */
	public String toString() {
		String srcName;
		String dstName;

		if (source == null) {
			srcName = "???";
		} else {
			srcName = source.name;
		}

		if (destination == null) {
			dstName = "???";
		} else {
			dstName = destination.name;
		}
		
		return(
			"road " +
			srcName + " " +
			dstName + " " +
			travelTime
		);
	}
}
