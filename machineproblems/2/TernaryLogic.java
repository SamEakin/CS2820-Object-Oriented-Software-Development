/** TernaryLogic.java -- Machine Problem 2
 *  @author Samuel Eakin
 *  @version 2017-02-20
 */
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/** Utility package for error handling
 *  @see TernaryLogic
 *  @see #main
 */
class Errors {
	private Errors(){}; // you may never instantiate this class

	// Call this to warn of non fatal errors
	public static void warn( String message ) {
		System.err.println( "Warning: " + message );
	}

	// Call this to report fatal errors
	public static void fatal( String message ) {
		System.err.println( "Fatal error: " + message );
		System.exit(-1);
	}
}

/** Gates have wire inputs and time delay
 *  @see Wire
 *  @see TernaryLogic
 *  @see Errors
 */
class Gate {
	public String name;
	private String type;
	private int inputs;
	private float delay;

	// constructor
	// INPUT FORMAT: gate name type inputs delay
	// @TODO this needs to be called from within a factory method!
	public Gate(Scanner sc, LinkedList <Gate> gates) {
		String inputName = sc.next();
		String inputType = sc.next();
		int inputInputs = sc.nextInt();
		float inputDelay = sc.nextFloat();

		if (TernaryLogic.findGate(inputName) != null) {
			Errors.warn("Gate "+inputName+" redefined.");
		}

		sc.nextLine();
		name = inputName;
		type = inputType;
		inputs = inputInputs;
		delay = inputDelay;
	}

	public String toString() {
		return ("Gate "+name+" "+type+" "+inputs+" "+delay);
	}
}

/** Wire -- connects to gates with one another
 *  @see Gate
 *  @see TernaryLogic
 *  @see Errors
 */
class Wire {
	public String source;
	public String destination;
	private float delay;
	
	// constructor
	// INPUT FORMAT: wire source destination delay
	// @TODO this needs to be called from within a factory method!
	public Wire(String inputSrc, String inputDst, float inputDelay) {
		source = inputSrc;
		destination = inputDst;
		delay = inputDelay;
	}


	public String toString() {
		return ("Wire "+source+" "+destination+" "+delay);
	}
}

/** TernaryLogic -- main program that reads and writes logic gate network 
 *  @see Gate
 *  @see Wire
 *  @see Errors
 *  @see #main
 */
class TernaryLogic {

	static LinkedList <Gate> gates = new LinkedList <Gate> ();
	static LinkedList <Wire> wires = new LinkedList <Wire> ();

	// @TODO: FACTORY METHOD
	public static void wireFactory(Scanner sc, LinkedList <Gate> gates) {
		String inputSrc = sc.next();
		String inputDst = sc.next();
		float inputDelay = sc.nextFloat();

		if (TernaryLogic.findGate(inputSrc) == null) {
			Errors.warn("Gate "+inputSrc+" undefined.");
		}
		if (TernaryLogic.findGate(inputDst) == null) {
			Errors.warn("Gate "+inputDst+" undefined.");
		}
		else {
			sc.nextLine();
			//@TODO: check for incorrect input data types
			wires.add(new Wire(inputSrc, inputDst, inputDelay));
		}
	}

	
	public static void initializeLogicGates(Scanner sc) {
		while (sc.hasNext()) {
			String command = sc.next();
			if ("gate".equals(command)) {
				gates.add(new Gate(sc, gates));
			}
			else if ("wire".equals(command)) {
				wireFactory(sc, gates);
			}
			else if ("--".equals(command)) { // comment
				sc.nextLine(); // skip the whole line
			}
			else {
				Errors.warn("Command: "+command+" is not gate or wire!");
				sc.nextLine(); // skip the whole line
			}
		}
	}

	// check if a Gate exists already
	public static Gate findGate(String gateName) {
		for (Gate gate: gates) {
			if (gate.name.equals(gateName))
				return gate;
		}
		return null;
	}

	public static void printGates() {
		for (Gate gate: gates) {
			System.out.println(gate.toString());
		}
		for (Wire wire: wires) {
			System.out.println(wire.toString());
		}
	}

	// main -- checks for valid input file, reads it.
	public static void main( String[] args ) {
		// verify that the argument exists.
		if (args.length < 1) {
			Errors.fatal( "Missing file name on command line" );

		} else if (args.length > 1) {
			Errors.fatal( "Unexpected command line args" );

		} else try {
			initializeLogicGates( new Scanner( new File( args[0] ) ) );
			printGates(); // display logic gates and wires

		} catch (FileNotFoundException e) {
			Errors.fatal( "Could not read '" + args[0] + "'" );
		}
	}
}
