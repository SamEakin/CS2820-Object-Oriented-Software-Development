public class FindLargest {

	static int largestNumber(int[] numbers) {
		int largest = numbers[0];
		
		for(int number: numbers) {
			if(number > largest) {
				largest = number;
			}
		}
		return largest;
	}

	static int largestNumber(int x, int y, int z) {
		int largest = x;
		if (y>x) largest = y;
		else if (z>x) largest = z;
		return largest;
	}

	public static void main(String[] args) {

		int x = 20, y = 10, z = 15;
		int[] nums = {20,10,15};

		int largest = largestNumber(nums);
		System.out.println("Largest = " + largest);

		largest = largestNumber(x,y,z);
		System.out.println("largest = " + largest);



	}
}
