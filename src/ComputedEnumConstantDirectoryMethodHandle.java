import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;

public class ComputedEnumConstantDirectoryMethodHandle {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		Map<String, ?> enumConstantDirectory = null;
		try {
			enumConstantDirectory = (Map<String, ?>) MethodHandles.privateLookupIn(Class.class, MethodHandles.lookup())
				.findVirtual(Class.class, "enumConstantDirectory", MethodType.methodType(Map.class)).invoke(BigEnum.class);
		} catch (Throwable e) {
			// Do nothing
		}

		if (enumConstantDirectory.containsKey(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
