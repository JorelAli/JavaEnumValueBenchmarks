import java.util.Arrays;

public class ComputedArrayBinarySearchForLoop {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		final BigEnum[] values = BigEnum.values();
		final int length = values.length;
		String[] computed = new String[length];
		for (int i = 0; i < length; i++) {
			computed[i] = values[i].name();
		}

		if (Arrays.binarySearch(computed, input) >= 0) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
