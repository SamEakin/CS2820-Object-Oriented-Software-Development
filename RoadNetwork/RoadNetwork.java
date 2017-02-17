/** RoadNetwork.java -- classes that describe a road network [LECTURE NOTES]
 *  @author Douglas Jones
 *  @version 2017-01-25
 */

import java.util.LinkedList;
import java.io.File;
import java.util.Scanner;

/** Roads link intersections
 *  @see Intersection
 */
class Road {
	float travelTime; 				// how ong to travel down this road
	Intersection destination;		// where does the road go
	Intersection source;			// where does it come from
	// Road names are based on the intersections they connect. 

	public Road(Scanner sc, LinkedList <Intersection> inters) {
		// scan and process one road definition
		String srcName = sc.next(); // source intersection name
		String dstName = sc.next(); // destination intersection name
		// Bug: need to look up src and dest names
		// source = ??
		// destination = ??

		travelTime = sc.nextFLoat();

		sc.nextLine();
	}
}

/** Intersectons are linked by one-way roads
 *  @see Road
 */
class Intersection {
	LinkedList <Road> outgoing = new LinkedList <Road> ();
	String name;
}

class Vehicle {
	// Bug: Does this go here?
}

class Event {
	// Bug: Does this go here?
}

/** RoadNetwork, the main program to build a network of roads and intersections
 *  @see Road
 *  @see Intersection
 *  @see Test.txt for processing sample Road data inputs
 * 		intersection name
 * 		road startName endName timeEstimate
 *
 * 		TEXT PROCESSING --- JAVA STREAMS
 * 		ccccccccccccccccccccc    (characters)
 * 		|word|word|word|word|    (lexical analysis - dividing characters into words ---  AKA Scanner)
 * 		|     sentence      |
 * 		- text can be written in object File
 */
public class RoadNetwork{
	// Bug: Do it need a list of intersections?
	// Bug: Do I need a list of roads?
	
	static LinkedList <Road> roads = new LinkedList <Road> ();
	static LinkedList <Intersection> inters = new LinkedList <Intersection> ();
	
	static void initializeNetwork(Scanner sc){
		while (sc.hasNext()) {
			String command = sc.next();
			if ((command == "intersection") 
			||  (command == "i")          ) {
				inters.add( new Intersection(sc, inters);
			} else if ((command == "road")
			||		   (command == "r")  ) {
				roads.add( new Road(sc, inters);
			} else {
				// Bug: Unknown command.
			}
		}
	}
	
	public static void main(String[] args) {
		try {
			initializeNetwork(new Scanner( new File(args[0]) ));
		} catch {
			// Bug: should we check to see if there are any args?
			// Bug: what if the file doesn't exist?
		}
	}
}



























