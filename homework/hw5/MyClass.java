import java.lang.IllegalArgumentException;
public class MyClass {
	private int f;
	
	// prevent public default
	private MyClass() {}

	MyClass(int p) {
		f = p;
	}

	// factory
static MyClass factory(int p) {
		MyClass m = new MyClass();
		// BUG: missing code to prevent p = null
		m.f = p;
		return m;
	}

	public static void main(String[] args) {

		// TESTING
		MyClass constructTest = new MyClass();
		MyClass constructTest2 = new MyClass(5);
		MyClass factoryTest2 = MyClass.factory(6);

	}
}
