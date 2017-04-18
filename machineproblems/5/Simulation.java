/** Simulation support framework
 *  This is ripped from RoadNetwork.java version 2017-03-31.
 */
public class Simulation {

	/** Interface allowing actions to be passed as lambda expressions
	 */
	public interface Action {
		void trigger( float time );
	}
	
	/** Events are queued for Simulation.run to find
	 */
	private static class Event {
		float time;
		Action act;

		// constructor
		Event( float t, Action a ) {
			time = t;
			act = a;
		};

		void trigger() {
			act.trigger( time );
		}
	}

	private static PriorityQueue <Event> eventSet =
		new PriorityQueue <Event> (
			(Event e1, Event e2)->Float.compare( e1.time, e2.time )
		);

	/** Users call schedule to schedule one action at some time.
	 *  @param time specifies when the event should occur.
	 *  @param act  specifies what to do at that time.
	 *  Typically, act is a lambda expression, so a call to schedule could
	 *  look like this:
	 *  <PRE>
	 *  Simulation.schedule( t, (float t)->object.method( params, t ) )
	 *  </PRE>
	 */
	public static void schedule( float time, Action act ) {
		eventSet.add( new Event( time, act ) );
	}

	/** the main program should build the model,
	 *  this inolves scheduling some initial events
	 *  and then, just once, it should call run.
	 */
	public static void run() {
		while (!eventSet.isEmpty()) {
			Event e = eventSet.remove();
			e.trigger();
		}
	}
}

