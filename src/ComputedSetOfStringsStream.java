import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class ComputedSetOfStringsStream {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		Set<String> values = Arrays.stream(BigEnum.values()).map(Enum::name).collect(Collectors.toSet());

		if (values.contains(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
