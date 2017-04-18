/** Gate.java -- class that describe a ternary logic gate
 *  @author Sam Eakin
 *  @author Douglas Jones
 *  @version mp5, 2017-04-13
 */

import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.util.PriorityQueue;

/** Gates are linked by wires
 *  @see Wire
 *  @see MinGate
 *  @see MaxGate
 *  @see NotGate
 *  @see IsTGate
 *  @see IsFGate
 *  @see IsUGate
 *  @see TernaryLogic#findGate(String)
 *
 *  The logic used to simulate a gate is largely lifted from the
 *  posted solution to Homework 8.
 */
public abstract class Gate {
	private final LinkedList <Wire> outgoing = new LinkedList <Wire> ();
	/** setter method to add outgoing wires to this gate
	 *  @param w the wire that connects from this gate
	 */
	public void addOutgoing( Wire w ) {
		outgoing.add( w );
	}

	private int incount = 0;	// how many inputs are connected?
	/** setter method to add incoming wires to this gate
	 *  @param w the wire that connects to this gate
	 */
	public void addIncoming( Wire w ) {
		// actually, we don't need w, but the public doesn't know that
		incount = incount + 1;
	}

	public final String name;	// the name of the gate

	// Gripe:  We'd like to declare the following as final, but can't
	// because they're set by the subclass constructor
	public       int    inputs;	// the type of the gate
	public       float  delay;	// the type of the gate

	/** constructor needed by subclasses to set the final fields of gate
	 */
	protected Gate( String n ) {
		name = n;
	}

	/** factory method scans and processes one gate definition
	 */
	public static Gate newGate( Scanner sc ) {
		String myName = ScanSupport.nextName( sc );
		if ("".equals( myName )) {
			Errors.warn(
				"gate has no name"
			);
			sc.nextLine();
			return null;
		}

		if (TernaryLogic.findGate( myName ) != null) {
			Errors.warn(
				"Gate '" + myName +
				"' redefined."
			);
			sc.nextLine();
			return null;
		}

		String myType = ScanSupport.nextName( sc );
		if ("min".equals( myType )) {
			return new MinGate( sc, myName );

		} else if ("max".equals( myType )) {
			return new MaxGate( sc, myName );

		} else if ("neg".equals( myType )) {
			return new NegGate( sc, myName );

		} else if ("isfalse".equals( myType )) {
			return new IsFGate( sc, myName );

		} else if ("istrue".equals( myType )) {
			return new IsTGate( sc, myName );

		} else if ("isunknown".equals( myType )) {
			return new IsUGate( sc, myName );

		} else {
			Errors.warn(
				"Gate '" + myName +
				"' '" + myType +
				"' has an illegal type."
			);
			sc.nextLine();
			return null;
		}
	}

	/** Scan gate's delay and line end to finish initialization;
	 *  this is always called at the end of the subclass constructor
	 */
	protected final void finishGate( Scanner sc ) {
		delay = sc.nextFloat();
		if (delay != delay) { // really asks if delay == NaN
			Errors.warn(
				this.myString() + " -- has no delay"
			);
		} else if (delay < 0.0f) {
			Errors.warn(
				this.myString() + " -- has negative delay."
			);
		}
		ScanSupport.lineEnd( sc, () -> this.myString() );
	}

	/** output this Gate in a format like that used for input
	 *  it would have been nice to use toString here, but can't protect it
	 */
	protected String myString() {
		return(
			"gate " + name
		);
	}

	// ***** Logic Simulation *****

	// for logic values, inputCount[v] shows how many inputs have that value
	int inputCounts[] = new int[3];
	// this gate's most recently computed output value
	int output;

	/** Sanity check for gates */
	public void check() {
		if (incount < inputs) {
			Errors.warn(
				this.myString() + " -- has missing inputs."
			);
		} else if (incount > inputs) {
			Errors.warn(
				this.myString() + " -- has too many inputs."
			);
		}

		// initially, all the inputs are unknown
		inputCounts[0] = 0;
		inputCounts[1] = inputs;
		inputCounts[2] = 0;

		// and initially, the output is unknown
		output = 1;

		// some subclasses will add to this behavior
	}
	
	/** Every subclass must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected abstract int logicValue();

	/** Event service routine called when the input to a gate changes
	 *  @param time the time at which the input changes
	 *  @param old the previous logic value carried over this input
	 *  @param new the new logic value carried over this input
	 */
	public void inputChangeEvent( float time, int oldv, int newv ) {
		inputCounts[oldv]--;
		inputCounts[newv]++;
		final int newOut = logicValue();
		if (output != newOut) {
			final int old = output;
			Simulation.schedule(
				time + delay,
				(float t) -> outputChangeEvent( t, old, newOut )
			);
			output = newOut;
		}
	};

	/** Event service routine called when the output of a gate changes
	 *  @param time the time at which the output changes
	 *  @param old the previous logic value of this gate's output
	 *  @param new the new logic value of this gate's output
	 */
	public void outputChangeEvent( float time, int oldv, int newv ) {

		// update output value to print
		printValue(oldv, newv);
		// print each gate value
		printGates(time);
		
		// send the new value out to all the outgoing wires
		for ( Wire w: outgoing ) {
			// this is optimized, we could have scheduled an event
			w.inputChangeEvent( time, oldv, newv );
		}
	};

	public String currentOutputValue;
	
	/** Event service routine called when the output of a gate changes
	 *  @param time the time at which the output changes
	 *  @param old the previous logic value of this gate's output
	 *  @param new the new logic value of this gate's output
	 *  @see currentOutputValue
	 */
	public void printValue(int oldv, int newv) {
		// was FALSE
		if (oldv == 0){
			if (newv == 0){
				currentOutputValue = "|    ";
			}
			else if (newv == 1){
				currentOutputValue = "|_   ";
			}
			else if (newv == 2){
				currentOutputValue = "|___ ";
			}
		}
		// was UNKNOWN
		else if (oldv == 1){
			if (newv == 0){
				currentOutputValue = " _|  ";
			}
			else if (newv == 1){
				currentOutputValue = "  |  ";
			}
			else if (newv == 2){
				currentOutputValue = "  |_ ";
			}
		}
		// was TRUE
		else if (oldv == 2){
			if (newv == 0){
				currentOutputValue = " ___|";
			}
			else if (newv == 1){
				currentOutputValue = "   _|";
			}
			else if (newv == 2){
				currentOutputValue = "    |";
			}
		}
		else
			System.out.println("Error with output transition.");
	}

	private static float printInterval;

	/** Routine called at beginning of simulation
	 *  @param int i interval of checking gate output values. 
	 */
	public static void initPrint(float i) {
		printInterval = i;
		Simulation.schedule(0.0f, (float t) -> printGates(t) );

		// prints the name of each gate once
		for( Gate g: TernaryLogic.gates ) {
			System.out.print( " " + g.name );
		}
		System.out.println();
	}

	/** Prints gate values and schedules next interval.
	 * @see printInterval
	 */
	private static void printGates(float time) {
		for( Gate g: TernaryLogic.gates ) {
			System.out.print( " " + g.currentOutputValue );
		}

		System.out.println();
		Simulation.schedule( time + printInterval, (float t) -> printGates(t) );
	}
}

/** MinGate a kind of Gate
 *  @see Gate
 */
class MinGate extends Gate {
	/** initializer scans and processes one min gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	MinGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		// get inputs
		if (sc.hasNextInt()) {
			inputs = sc.nextInt();
		} else {
			Errors.warn(
				this.myString() +
				" min -- has no input count"
			);
		}

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " min " + inputs + " " + delay
		);
	}

	// ***** Logic Simulation for MinGate *****

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		// find the minimum of all the inputs
		int newOutput = 0;
		while (inputCounts[newOutput] == 0) newOutput++;
		return newOutput;
	}
}

/** MaxGate a kind of Gate
 *  @see Gate
 */
class MaxGate extends Gate {
	/** initializer scans and processes one max gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	MaxGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		// get inputs
		if (sc.hasNextInt()) {
			inputs = sc.nextInt();
		} else {
			Errors.warn(
				this.myString() + " max -- has no input count"
			);
		}

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " max " + inputs + " " + delay
		);
	}

	// ***** Logic Simulation for MaxGate *****

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		// find the maximum of all the inputs
		int newOutput = 2;
		while (inputCounts[newOutput] == 0) newOutput--;
		return newOutput;
	}
}

/** NegGate a kind of Gate
 *  @see Gate
 */
class NegGate extends Gate {
	/** initializer scans and processes one neg gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	NegGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		inputs = 1; // it is a one-input gate

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " neg " + " " + delay
		);
	}

	// ***** Logic Simulation for NegGate *****

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		// Warning this is mildly tricky code
		int newOutput = 2;
		while (inputCounts[2 - newOutput] == 0) newOutput--;
		return newOutput;
	}
}

/** IsTGate a kind of Gate
 *  @see Gate
 */
class IsTGate extends Gate {
	/** initializer scans and processes one neg gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	IsTGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		inputs = 1; // it is a one-input gate

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " istrue " + delay
		);
	}

	// ***** Logic Simulation for IsTGate *****

	/** Sanity check for IsTGate */
	public void check() {
		super.check();

		// now change the output from unknown to false
		Simulation.schedule(
			delay,
			(float t) -> this.outputChangeEvent( t, 1, 0 )
		);
		output = 0;
	}

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		int newOutput = 0;
		if (inputCounts[2] != 0) newOutput = 2;
		return newOutput;
	}
}

/** IsFGate a kind of Gate
 *  @see Gate
 */
class IsFGate extends Gate {
	/** initializer scans and processes one neg gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	IsFGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		inputs = 1; // it is a one-input gate

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " isfalse " + delay
		);
	}

	// ***** Logic Simulation for IsFGate *****

	/** Sanity check for IsFGate */
	public void check() {
		super.check();

		// now change the output from unknown to false
		Simulation.schedule(
			delay,
			(float t) -> this.outputChangeEvent( t, 1, 0 )
		);
		output = 0;
	}

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		int newOutput = 0;
		if (inputCounts[0] != 0) newOutput = 2;
		return newOutput;
	}
}

/** IsUGate a kind of Gate
 *  @see Gate
 */
class IsUGate extends Gate {
	/** initializer scans and processes one neg gate
	 *  @parame sc Scanner from which gate description is read
	 *  @param myName the value to be put in the name field
	 */
	IsUGate( Scanner sc, String myName ) {
		// the text "gate myName min" has already been scanned
		super( myName );

		inputs = 1; // it is a one-input gate

		this.finishGate( sc );
	}

	/** output this Gate in a format like that used for input
	 */
	public String toString() {
		return(
			this.myString() + " isundefined " + delay
		);
	}

	// ***** Logic Simulation for IsUGate *****

	/** Sanity check for IsUGate */
	public void check() {
		super.check();

		// now change the output from unknown to true
		Simulation.schedule(
			delay,
			(float t) -> this.outputChangeEvent( t, 1, 2 )
		);
		output = 2;
	}

	/** Every subclass of Gate must define this function;
	 *  @return the new logic value, a function of <TT>inputCounts</TT>;
	 */
	protected int logicValue() {
		int newOutput = 0;
		if (inputCounts[1] != 0) newOutput = 2;
		return newOutput;
	}
}

