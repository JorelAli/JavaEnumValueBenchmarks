import java.util.Arrays;

public class PrecomputedArrayBinarySearchHashcodes2 {

	public static void main(String[] args) {
		final String input = args[0];
		final int hashCode = input.hashCode();

		int[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).mapToInt(String::hashCode).sorted().toArray();

		long old = System.nanoTime();

		// I explicitly don't want to use .hashCode() in this time
		if (Arrays.binarySearch(computed, hashCode) >= 0) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
