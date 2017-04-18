/** Utility package for error handling
 */
class Errors {
	private Errors(){}; // you may never instantiate this class

        private static int count = 0; // warning count, really public read only
        /** Provide public read only access to the count of warnings. */
        public static int count() {
                return count;
        }       

        /** Call this to warn of non fatal errors
         */ 
        public static void warn( String message ) {
                System.err.println( "Warning: " + message );
                count = count + 1;
        }

	/** Call this to report fatal errors
	 */
	public static void fatal( String message ) {
		System.err.println( "Fatal error: " + message );
		System.exit( -1 );
	}
}

