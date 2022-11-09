import java.util.Arrays;

public class PrecomputedArrayBinarySearchHashcodes {

	public static void main(String[] args) {
		final String input = args[0];

		int[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).mapToInt(String::hashCode).sorted().toArray();

		long old = System.nanoTime();

		// I explicitly want to call .hashCode() in this test
		if (Arrays.binarySearch(computed, input.hashCode()) >= 0) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
