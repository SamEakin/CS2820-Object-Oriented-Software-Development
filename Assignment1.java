/** Assignment1.java -- Recursive Example Problem
 * @author Sam Eakin
 * @version 2017-01-24
 */
public class Assignment1 {

	public static int recurse(int i) {
		if (i<3) {
			return i;
		}
		return recurse(i-1) + recurse(i-3);
	}

	public static int otherRecurse(int i) {
		return (i < 3) ? i : recurse(i-1) + recurse(i-3);
	}

	public static void main(String[] args) {
		for (int i = 0; i < 10; i++){
			System.out.println("f("+i+")="+otherRecurse(i));
		}
	}
}
