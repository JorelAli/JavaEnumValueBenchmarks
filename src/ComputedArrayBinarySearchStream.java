import java.util.Arrays;

public class ComputedArrayBinarySearchStream {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		String[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).toArray(String[]::new);

		if (Arrays.binarySearch(computed, input) >= 0) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
