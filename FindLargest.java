public class FindLargest {

	// method override if given 3 ints
	static int largestNumber(int x, int y, int z) {
		int largest = x;
		if (y>x) largest = y;
		else if (z>x) largest = z;
		return largest;
	}
	
	// method if given array of any amount of numbers
	static int largestNumber(int[] numbers) {
		int largest = numbers[0];
		
		for(int number: numbers) {
			if(number > largest) {
				largest = number;
			}
		}
		return largest;
	}

	static void largestNumberNested(int x, int y, int z) {
		if(x > y)
			if (x > z) print(x);
		else if(y > z)
			if (y > x) print(y);
		else if(z > x)
			if (z > y) print(z);
	}

	static void print(int num) {
		System.out.println(num);
	}

	public static void main(String[] args) {

		int x = 20, y = 10, z = 15;
		int[] nums = {x,y,z};

		int largest = largestNumber(nums);
		System.out.println("Largest = " + largest);

		largest = largestNumber(x,y,z);
		System.out.println("largest = " + largest);

		largestNumberNested(x,y,z);
	}
}
