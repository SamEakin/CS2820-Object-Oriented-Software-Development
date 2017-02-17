import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

class Read {

	class Person {
		String first;
		String last;

		Person(String firstName, String lastName) {
			this.first = firstName;
			this.last = lastName;
		}

		public static void void createPerson(Scanner sc) {
			while (sc.hasNext()) {
				String word = sc.next();
				if ("person".equals(word)){
					
				}
			}

		}
	}

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("ERROR: incorrect input");
		}
		else {
			createPerson(new Scanner(new File(args[0]) ) );
		}

	}

}
