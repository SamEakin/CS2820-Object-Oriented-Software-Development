import java.util.*;
public class Gate {
	String name;
	float delay; 

	//int output;
	//LinkedList <Wire> wires;

	/** Constructor
	 * input: scanner
	 * parse one line of text file for
	 */ 
	Gate(String name, float delay) {
		this.name = name;
		this.delay = delay;
	}

	static Gate initializeGate(Scanner sc) {
		String name = sc.next();
		float delay = sc.nextFloat();
		return new Gate(name, delay);
	}

	static String toString(Scanner sc) {
		String objectType = sc.next();
		String name = sc.next();
		float delay = sc.nextFloat();
		return objectType + " " + name + " " + delay;
	}
}

class Wire {
}
