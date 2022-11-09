import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EnumValueOfBenchmark {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		final String input = "A3000";
		// final String input = "A1";
		
		{
			// Precomputed array of string values
			String[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).toArray(String[]::new);
			
			long old = System.nanoTime();
			
			if(Arrays.binarySearch(computed, input) >= 0) {
				System.out.println("found");
			}
			
			System.out.println("Precomputed array of strings:         " + (System.nanoTime() - old) + "ns");
		}
		
		{
			long old = System.nanoTime();
			String[] computed = Arrays.stream(BigEnum.values()).map(Enum::name).toArray(String[]::new);
			
			if(Arrays.binarySearch(computed, input) >= 0) {
				System.out.println("found");
			}
			
			System.out.println("Computed array of strings (stream):   " + (System.nanoTime() - old) + "ns");
		}
		
		{
			long old = System.nanoTime();
			final BigEnum[] values = BigEnum.values();
			final int length = values.length;
			String[] computed = new String[length];
			for(int i = 0; i < length; i++) {
				computed[i] = values[i].name();
			}
			
			if(Arrays.binarySearch(computed, input) >= 0) {
				System.out.println("found");
			}
			
			System.out.println("Computed array of strings (for loop): " + (System.nanoTime() - old) + "ns");
		}
		
		{
			Set<String> values = Arrays.stream(BigEnum.values()).map(Enum::name).collect(Collectors.toSet());
			long old = System.nanoTime();
			
			if(values.contains(input)) {
				System.out.println("found");
			}
			
			System.out.println("Precomputed set of strings:           " + (System.nanoTime() - old) + "ns");
		}
		
		{
			long old = System.nanoTime();
			Set<String> values = Arrays.stream(BigEnum.values()).map(Enum::name).collect(Collectors.toSet());
			
			if(values.contains(input)) {
				System.out.println("found");
			}
			
			System.out.println("Computed set of strings (stream):     " + (System.nanoTime() - old) + "ns");
		}
		
		{
			long old = System.nanoTime();

			final BigEnum[] values = BigEnum.values();
			final int length = values.length;
			Set<String> valuesSet = new HashSet<>(length);
			for(BigEnum value : BigEnum.values()) {
				valuesSet.add(value.name());
			}
			
			if(valuesSet.contains(input)) {
				System.out.println("found");
			}
			
			System.out.println("Computed set of strings (for-each):   " + (System.nanoTime() - old) + "ns");
		}
		
		{
			Map<String, ?> enumConstantDirectory = null;
			try {
				// Requires --add-opens runtime flag: https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
				// --add-opens has the following syntax: {A}/{package}={B}
				// java --add-opens java.base/java.lang=ALL-UNNAMED
				
				Method m = Class.class.getDeclaredMethod("enumConstantDirectory");
				m.setAccessible(true);
				enumConstantDirectory = (Map<String, ?>) m.invoke(BigEnum.class);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			long old = System.nanoTime();
			
			if(enumConstantDirectory.containsKey(input)) {
				System.out.println("found");
			}
			
			System.out.println("Precomputed direct access class enumConstantDirectory: " + (System.nanoTime() - old) + "ns");
		}

		{
			long old = System.nanoTime();
			Map<String, ?> enumConstantDirectory = null;
			try {
				// Requires --add-opens runtime flag: https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
				// --add-opens has the following syntax: {A}/{package}={B}
				// java --add-opens java.base/java.lang=ALL-UNNAMED
				Method m = Class.class.getDeclaredMethod("enumConstantDirectory");
				m.setAccessible(true);
				enumConstantDirectory = (Map<String, ?>) m.invoke(BigEnum.class);
			} catch (ReflectiveOperationException e) {
				e.printStackTrace();
			}
			
			if(enumConstantDirectory.containsKey(input)) {
				System.out.println("found");
			}
			
			System.out.println("Computed direct access class enumConstantDirectory: " + (System.nanoTime() - old) + "ns");
		}
		
		{
			long old = System.nanoTime();
			Map<String, ?> enumConstantDirectory = null;
			try {
				// Requires --add-opens runtime flag: https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
				// --add-opens has the following syntax: {A}/{package}={B}
				// java --add-opens java.base/java.lang=ALL-UNNAMED
				enumConstantDirectory = (Map<String, ?>) MethodHandles.privateLookupIn(Class.class, MethodHandles.lookup()).findVirtual(Class.class, "enumConstantDirectory", MethodType.methodType(Map.class)).invoke(BigEnum.class);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
			if(enumConstantDirectory.containsKey(input)) {
				System.out.println("found");
			}
			
			System.out.println("Computed direct access (MethodHandle) class enumConstantDirectory: " + (System.nanoTime() - old) + "ns");
		}
		
		{
			MethodHandle mh = null;
			try {
				mh = MethodHandles.privateLookupIn(Class.class, MethodHandles.lookup()).findVirtual(Class.class, "enumConstantDirectory", MethodType.methodType(Map.class));
			} catch (NoSuchMethodException | IllegalAccessException e1) {
				e1.printStackTrace();
			}
			long old = System.nanoTime();
			Map<String, ?> enumConstantDirectory = null;
			try {
				// Requires --add-opens runtime flag: https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
				// --add-opens has the following syntax: {A}/{package}={B}
				// java --add-opens java.base/java.lang=ALL-UNNAMED
				enumConstantDirectory = (Map<String, ?>) mh.invoke(BigEnum.class);
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
			if(enumConstantDirectory.containsKey(input)) {
				System.out.println("found");
			}
			
			System.out.println("Precomputed direct access (MethodHandle) class enumConstantDirectory: " + (System.nanoTime() - old) + "ns");
		}

		
	}
	
}
