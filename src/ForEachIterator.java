public class ForEachIterator {

	public static void main(String[] args) {
		final String input = args[0];

		long old = System.nanoTime();

		for (BigEnum value : BigEnum.values()) {
			if (value.name().equals(input)) {
				System.out.println("Found");
				break;
			}
		}

		System.out.println(System.nanoTime() - old);
	}

}
