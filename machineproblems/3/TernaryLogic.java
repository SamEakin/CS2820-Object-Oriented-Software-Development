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
abstract class Gate {
	public String name;
	protected int inputs;
	protected float delay;

	public Gate(){
		name = "default";
		inputs = 0;
		delay = 0;
	}
}

class minGate extends Gate{
	// min/max gate constructor HAS INPUTS
	public minGate(String inputName, int inputInputs, float inputDelay) {
		name = inputName;
		inputs = inputInputs;
		delay = inputDelay;
	}
	public String toString() {
		return ("min Gate "+name+" "+inputs+" "+delay);
	}
}
class maxGate extends Gate{
	// min/max gate constructor HAS INPUTS
	public maxGate(String inputName, int inputInputs, float inputDelay) {
		name = inputName;
		inputs = inputInputs;
		delay = inputDelay;
	}
	public String toString() {
		return ("max Gate "+name+" "+inputs+" "+delay);
	}
}
class negGate extends Gate{
	// min/max gate constructor HAS INPUTS
	public negGate(String inputName, int inputInputs, float inputDelay) {
		name = inputName;
		inputs = inputInputs;
		delay = inputDelay;
	}
	public String toString() {
		return ("neg Gate "+name+" "+inputs+" "+delay);
	}
}
class isTrueGate extends Gate{
	public isTrueGate(String inputName, float inputDelay) {
		name = inputName;
		delay = inputDelay;
		inputs = 1;
	}
	public String toString() {
		return ("isTrue Gate "+name+" "+delay);
	}
}
class isFalseGate extends Gate{
	public isFalseGate(String inputName, float inputDelay) {
		name = inputName;
		delay = inputDelay;
		inputs = 1;
	}
	public String toString() {
		return ("isFalse Gate "+name+" "+delay);
	}
}
class isUnknownGate extends Gate{
	public isUnknownGate(String inputName, float inputDelay) {
		name = inputName;
		delay = inputDelay;
		inputs = 1;
	}
	public String toString() {
		return ("isUnknown Gate "+name+" "+delay);
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

	// Read each line of the file and determine whether it is a gate or a wire.
	// Throws an error if it is neither.
	public static void initializeLogicGates(Scanner sc) {
		while (sc.hasNext()) {
			String command = sc.next();
			if ("gate".equals(command)) {
				gateFactory(sc);
			}
			else if ("wire".equals(command)) {
				wireFactory(sc);
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

	// Factory method checks for correct inputs and calls a constructor if it passes.
	// Takes in a scanner and the list of gates to validate.
	public static void wireFactory(Scanner sc) {
		String inputSrc = sc.next();
		String inputDst = sc.next();

		if (!sc.hasNextFloat()) {
			Errors.warn("Wire "+inputSrc+" "+inputDst+" delay expected.");
			sc.nextLine();
			return;
		}

		float inputDelay = sc.nextFloat();

		if (findGate(inputSrc) == null) {
			Errors.warn("Gate "+inputSrc+" undefined.");
		}
		else if (findGate(inputDst) == null) {
			Errors.warn("Gate "+inputDst+" undefined.");
		}

		else {
			sc.nextLine();
			wires.add(new Wire(inputSrc, inputDst, inputDelay));
		}
	}

	// Factory method checks for correct inputs and calls a constructor if it passes
	// Takes in a scanner and the list of gates to validate.
	public static void gateFactory(Scanner sc) {
		String[] validTypes = {"min","max","neg","isTrue","isFalse","isUnknown"};
		String inputName = sc.next();
		String gateType = sc.next();

		if (!isValidType(gateType)) {
			Errors.warn("Gate "+inputName+" "+gateType+" is invalid gate type.");
			sc.nextLine();
		}

		if (findGate(inputName) != null) {
			Errors.warn("Gate "+inputName+" already defined.");
			sc.nextLine();
		}

		else {
			// check for int and float if gate is min/max/neg
			for(int i = 0; i < 3; i++){
				if (validTypes[i].equals(gateType)){
					//@TODO: check for int/float  
					readIntAndFloat(sc, inputName, gateType);
				}
			}
			for(int i = 3; i < 6; i++){
				if (validTypes[i].equals(gateType)){
					//@TODO: check for float  
					readFloat(sc, inputName, gateType);
				}
			}
		}
	}

	// Helper method to validate Gate type.
	public static boolean isValidType(String inputType) {
		String[] validTypes = {"min","max","neg","isTrue","isFalse","isUnknown"};
		for(String type: validTypes) {
			if (type.equals(inputType)) {
				return true;
			}
		}
		return false;
	}
	
	// check if a Gate exists already
	public static Gate findGate(String gateName) {
		for (Gate gate: gates) {
			if (gate.name.equals(gateName))
				return gate;
		}
		return null;
	}

	protected static void readIntAndFloat(Scanner sc, String inputName, String gateType){
		if (!isValidInputs(sc, inputName, gateType)){
			return;
		}
		int inputInputs = sc.nextInt();
		if (!isValidDelay(sc, inputName, gateType)){
			return;
		}
		float inputDelay = sc.nextFloat();

		if ("min".equals(gateType) ){
			gates.add(new minGate(inputName, inputInputs, inputDelay));
		}
		else if ("max".equals(gateType)){
			gates.add(new maxGate(inputName, inputInputs, inputDelay));
		}
		else if ("neg".equals(gateType)){
			gates.add(new negGate(inputName, inputInputs, inputDelay));
		}
		else{ // invalid gate type
			Errors.warn("Gate "+inputName+" "+gateType+" is invalid gate type.");
		}
	}
	
	protected static void readFloat(Scanner sc, String inputName, String gateType){
		if (!isValidDelay(sc, inputName, gateType)){
			return;
		}
		float inputDelay = sc.nextFloat();

		if ("isTrue".equals(gateType)){
			gates.add(new isTrueGate(inputName, inputDelay));
		}
		else if ("isFalse".equals(gateType)){
			gates.add(new isFalseGate(inputName, inputDelay));
		}
		else if ("isUnknown".equals(gateType)){
			gates.add(new isUnknownGate(inputName, inputDelay));
		}
		else{ // invalid gate type
			Errors.warn("Gate "+inputName+" "+gateType+" is invalid gate type.");
		}
	}

	protected static boolean isValidInputs(Scanner sc, String inputName, String gateType){
		if (!sc.hasNextInt()) {
			Errors.warn("Gate "+inputName+" "+gateType+" inputs expected.");
			sc.nextLine();
			return false;
		}
		else 
			return true;
	}

	protected static boolean isValidDelay(Scanner sc, String inputName, String gateType){
		if (!sc.hasNextFloat()) {
			Errors.warn("Gate "+inputName+" "+gateType+" delay expected.");
			sc.nextLine();
			return false;
		}
		else 
			return true;
	}

	public static void printGates() {
		System.out.println("Ternary Logic Network: ");
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
