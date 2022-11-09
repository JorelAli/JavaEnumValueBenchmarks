public class ValueOf {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		try {
			BigEnum.valueOf(input);
			System.out.println("Found");
		} catch (IllegalArgumentException e) {
			// We don't do anything here
		}

		System.out.println(System.nanoTime() - old);
	}

}
