/** Wires link gates
 *  @see Gate
 *  @see Errors
 *  @see TernaryLogic#findGate(String)
 */
public class Wire {
	private final float delay;	// time delay of this wire
	private final Gate destination;	// where wire goes, or null
	private final Gate source;	// source of wire, or null
	// Wire name is the source-destination names

	/** initializer scans and processes one wire definition
	 */
	public Wire( Scanner sc ) {
		// textual names of source and dest
		String srcName = ScanSupport.nextName( sc );
		String dstName = ScanSupport.nextName( sc );
		// if there are no next names on this line, these are ""
		// therefore, the findGate calls below will fail

		// lookup names of source and dest
		source = TernaryLogic.findGate( srcName );
		if (source == null) {
			Errors.warn(
				"Wire '" + srcName +
				"' '" + dstName +
				"' source undefined."
			);
		}
		destination = TernaryLogic.findGate( dstName );
		if (destination == null) {
			Errors.warn(
				"Wire '" + srcName +
				"' '" + dstName +
				"' destination undefined."
			);
		}

		delay = ScanSupport.nextFloat( sc );
		if (delay != delay) { // really asks if delay == NaN
			Errors.warn(
				"Wire '" + srcName +
				"' '" + dstName +
				"' has no delay."
			);
		} else if (delay < 0.0f) {
			Errors.warn(
				"Wire '" + srcName +
				"' '" + dstName +
				"' '" + delay +
				"' has negative delay."
			);
		}
		ScanSupport.lineEnd( sc, () -> this.toString() );

		// Now, tell the gates that they've been wired together
		if (destination != null) destination.addIncoming( this );
		if (source != null) source.addOutgoing( this );
	}

	/** output this wire in a format like that used for input
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
			"wire " +
			srcName + " " +
			dstName + " " +
			delay
		);
	}

	// ***** Logic Simulation *****

	/** Event service routine called when the input to a wire changes
	 *  @param time the time at which the input changes
	 *  @param old the previous logic value carried over this wire
	 *  @param new the new logic value carried over this wire
	 */
	public void inputChangeEvent( float time, int oldv, int newv ) {
		Simulation.schedule(
			time + delay,
			(float t) -> outputChangeEvent( t, oldv, newv )
		);
	};

	/** Event service routine called when the output of a wire changes
	 *  @param time the time at which the output changes
	 *  @param old the previous logic value carried over this wire
	 *  @param new the new logic value carried over this wire
	 */
	public void outputChangeEvent( float time, int oldv, int newv ) {
		// this version is optimized, we could have scheduled an event
		destination.inputChangeEvent( time, oldv, newv );
	};
}

