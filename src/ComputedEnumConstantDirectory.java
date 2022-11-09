import java.lang.reflect.Method;
import java.util.Map;

public class ComputedEnumConstantDirectory {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

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
