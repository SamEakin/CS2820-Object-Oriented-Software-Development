/** Support methods for scanning
 *  @author Douglas Jones
 *  @version 2017-04-05
 *  This is ripped from RoadNetwork.java version 2017-03-31 with a change:
 *  nextFloat method is added based on the posted solution to Homework 7.
 *  @see Errors
 */

import java.util.Scanner;
import java.util.regex.Pattern;

public class ScanSupport {

	/** Pattern for identifers
	 */
	public static final Pattern name // letter followed by alphanumeric
		= Pattern.compile( "[a-zA-Z][a-zA-Z0-9_]*|" );

	/** Pattern for floating point numbers
	 */
	public static final Pattern numb // Digits.Digits or .Digits or nothing
		= Pattern.compile( "[0-9]+\\.?[0-9]*|\\.[0-9]+|" );

	/** Pattern for whitespace excluding things like newline
	 */
	public static final Pattern whitespace
		= Pattern.compile( "[ \t]*" );

	/** Get next name without skipping to next line (unlike sc.Next())
	 *  @param sc the scanner from which end of line is scanned
	 *  @return the name, if there was one, or an empty string
	 */
	public static String nextName( Scanner sc ) {
		sc.skip( whitespace );

		// the following is weird code, it skips the name
		// and then returns the string that matched what was skipped
		sc.skip( name );
		return sc.match().group();
	}

	/** Get next float without skipping lines (unlike sc.nextFloat())
	 *  @param sc the scanner from which end of line is scanned
	 *  @return the name, if there was one, or NaN if not
	 */
	public static Float nextFloat( Scanner sc ) {
		sc.skip( whitespace );

		// the following is weird code, it skips the name
		// and then returns the string that matched what was skipped
		sc.skip( numb );
		String f = sc.match().group();

		// now convert what we can or return NaN
		if (!"".equals( f )) {
			return Float.parseFloat( f );
		} else {
			return Float.NaN;
		}
	}

	/** Class used only for deferred parameter passing to lineEnd
	 */
	public interface EndMessage {
		public abstract String myString();
	}

	/** Advance to next line and complain if is junk at the line end;
	 *  call this when all useful content has been consumed from the line
	 *  then call this to skip optional line-end comments to the next line
	 *  @see Errors
	 *  @param sc the scanner from which end of line is scanned
	 *  @param message will be evaluated only when there is an error;
	 *	it is typically passed as a lambda expression, for example,
	 *      ScanSupport.lineEnd( sc, () -> "this " + x + " that" );
	 */
	public static void lineEnd( Scanner sc, EndMessage message ) {
		sc.skip( whitespace );
		String lineEnd = sc.nextLine();
		if ( (!lineEnd.equals( "" ))
		&&   (!lineEnd.startsWith( "--" )) ) {
			Errors.warn(
				"" + message.myString() +
				" followed unexpected by '" + lineEnd + "'"
			);
		}
	}
}

