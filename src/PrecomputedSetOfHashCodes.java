import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

public class PrecomputedSetOfHashCodes {

	public static void main(String[] args) {
		final String input = args[0];
		final int hashCode = input.hashCode();

		Set<Integer> values = Arrays.stream(BigEnum.values()).map(Enum::name).map(String::hashCode).collect(Collectors.toUnmodifiableSet());

		long old = System.nanoTime();

		if (values.contains(hashCode)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
