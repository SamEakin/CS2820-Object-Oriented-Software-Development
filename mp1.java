/**
 * mp1.java, Machine Problem 1 for CS:2820, Spring 2017
 * @author Sam Eakin
 * @version 2017-01-20
 */
class mp1
{
	/**
	 * main program called from the command line
	 * @param args Command line arguments, with each word in a new string.
	 */
	public static void main(String args[])
	{
		if (args.length > 0) {
			for (String s: args) {
				System.out.print( s );
				System.out.print( " " );
			}
			System.out.println();
		} else {
			System.out.println( "Sam Eakin" );
		}
	}
}
