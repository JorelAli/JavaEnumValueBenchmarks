import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Map;

public class ComputedEnumConstantDirectoryMethodHandle2 {

	public static void main(String[] args) {
		final String input = args[0];

		try {
			BigEnum.valueOf(input);
		} catch (IllegalArgumentException e) {
			// Do nothing
		}

		MethodHandle mh = null;
		try {
			mh = MethodHandles.privateLookupIn(Class.class, MethodHandles.lookup()).findVirtual(Class.class, "enumConstantDirectory", MethodType.methodType(Map.class));
		} catch (ReflectiveOperationException e) {
			// Do nothing
		}

		long old = System.nanoTime();

		Map<String, ?> enumConstantDirectory = null;
		try {
			enumConstantDirectory = (Map<String, ?>) mh.invoke(BigEnum.class);
		} catch (Throwable e) {
			// Do nothing
		}

		if (enumConstantDirectory.containsKey(input)) {
			System.out.println("Found");
		}

		System.out.println(System.nanoTime() - old);
	}

}
