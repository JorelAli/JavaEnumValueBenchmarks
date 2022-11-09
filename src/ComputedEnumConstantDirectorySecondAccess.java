import java.lang.reflect.Method;
import java.util.Map;

public class ComputedEnumConstantDirectorySecondAccess {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		final String input = args[0];

		// First invocation
		try {
			BigEnum.valueOf(input);
		} catch (IllegalArgumentException e) {
			// Do nothing
		}

		long old = System.nanoTime();

		// Second invocation, using reflection
		Map<String, ?> enumConstantDirectory = null;
		try {
			Method m = Class.class.getDeclaredMethod("enumConstantDirectory");
			m.setAccessible(true);
			enumConstantDirectory = (Map<String, ?>) m.invoke(BigEnum.class);
		} catch (Exception e) {
			// Do nothing
		}

		if (enumConstantDirectory.containsKey(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
