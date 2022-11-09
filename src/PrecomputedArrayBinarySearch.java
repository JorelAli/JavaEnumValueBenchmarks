import java.util.Arrays;

public class PrecomputedArrayBinarySearch {

	public static void main(String[] args) {
		final String input = args[0];

		String[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).toArray(String[]::new);

		long old = System.nanoTime();

		if (Arrays.binarySearch(computed, input) >= 0) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
