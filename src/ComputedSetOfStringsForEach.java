import java.util.HashSet;
import java.util.Set;

public class ComputedSetOfStringsForEach {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		final BigEnum[] values = BigEnum.values();
		Set<String> valuesSet = new HashSet<>(values.length);
		for (BigEnum value : values) {
			valuesSet.add(value.name());
		}

		if (valuesSet.contains(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
