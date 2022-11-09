import java.lang.reflect.Method;
import java.util.Map;

public class PrecomputedEnumConstantDirectory {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		final String input = args[0];

		Map<String, ?> enumConstantDirectory = null;
		try {
			Method m = Class.class.getDeclaredMethod("enumConstantDirectory");
			m.setAccessible(true);
			enumConstantDirectory = (Map<String, ?>) m.invoke(BigEnum.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long old = System.nanoTime();

		if (enumConstantDirectory.containsKey(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
