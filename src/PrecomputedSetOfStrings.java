import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PrecomputedSetOfStrings {

	public static void main(String[] args) {
		final String input = args[0];

		Set<String> values = Arrays.stream(BigEnum.values()).map(Enum::name).collect(Collectors.toSet());

		long old = System.nanoTime();

		if (values.contains(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
