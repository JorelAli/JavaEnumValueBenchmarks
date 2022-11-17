# JavaEnumValueBenchmarks

A test suite to compare methods of checking if a value is valid in an enum.

> **tl;dr:**
>
> Using `Enum.valueOf(String)` caches a map after running it the first time. Subsequent calls are faster. For a sneaky teeny bit of performance boost, you may want to consider accessing enums in a static initializer to squeeze out the most overall runtime performance of code that may be calls multiple times.
>
> Using a `Set<String>` of enum value names is consistently the fastest method to check if an enum value exists in an enum. The operation of constructing such a set is similar in performance to the initial call to `Enum.valueOf(String)`.

## Setup

```sh
pip install -r requirements.txt
```

-----

## Running the tests

```sh
python run.py
```

-----

## Test Structure

Each test has the following structure:

```java
public class XXX {

    public static void main(String[] args) {
        final String input = args[0];

        // Pre computation goes here, where it isn't tracked by our timer

        long old = System.nanoTime();

        // Test goes here

        System.out.println(System.nanoTime() - old);
    }

}
```

We declare a variable `old` which stores the current system time in nanoseconds, we run our test and then we print the difference between the new current system time and the old system time. This method of keeping track of time is as simple as it gets and is used for every test to maintain consistency.

The primary goals is to check if the input (specified by `input`, defaulting to `A3000`) is present in the enum `BigEnum`. `BigEnum` is an enum with 2700 entries, named `A0001`, `A0002`, ..., `A2700`. This number of enum entries is close to the maximum that Java can support (Java's static initializers have a limit of 65535 bytes and enums shove everything into a static initializer block).

If the value provided is located, `"Found"` is printed to stdout.

-----

## Test descriptions

In general, we have the following tests:

- Using the standard `valueOf(String)` method in an enum
- Using binary search on an array
- Using a set
- Direct enum constant directory access

### Control

The control test does nothing. This returns a generic number in nanoseconds which is subtracted from each test in the test suite.

### ForEachIterator

This test uses a `for (X x : X.values())` for loop over the elements of the enum, and compares if the provided input is equal to the name of the enum entry being iterated over. This test is expected to perform slowly as this is the "standard" way to check if an element exists in an array of (unsorted) values.

### Enum valueOf tests

These tests use Java's enum's `valueOf(String)` method to check if an enum value exists.

- ValueOf

  This test uses the Enum `valueOf(String)` method that enums provide to determine if a value is present in the enum. If the value is not present, an `IllegalArgumentException` is thrown (we ignore this). This is the typical method used to check if a value is present in an enum.

- ValueOfSecondAccess

  This test is similar to the `ValueOf` test, but makes use of an optimization present in the implementation of the enum `valueOf(String)` method. In OpenJDK, the `valueOf(String)` method accesses the class's enum constant directory (a map of the enum names to their respective enum values) using:

  ```java
  enumClass.enumConstantDirectory()
  ```

  The `enumConstantDirectory` method's Javadocs states:

  > Returns a map from simple name to enum constant. This package-private method is used internally by Enum to implement `public static <T extends Enum<T>> T valueOf(Class<T>, String)` efficiently. Note that the map is returned by this method is created lazily on first use. Typically it won't ever get created.

  This means that the first time `enumConstantDirectory` (and by transitivity, `valueOf(String)`) is called, the enum constant directory has to be generated. Subsequent calls should offer better performance. This test calls the `valueOf(String)` method before running the test to demonstrate the performance of subsequent (second) accesses.

### Array binary search tests

These tests use the `Arrays.binarySearch()` to check if a value exists.

- PrecomputedArrayBinarySearch

  This test uses a pre-computed `String[]` of all of the enum names and performs a binary search using `Arrays.binarySearch()` to check if the input string is present. This assumes that the array is sorted (which conveniently for us, is the case with `BigEnum`).

- ComputedArrayBinarySearchStream

  This test is similar to PrecomputedArrayBinarySearch, but instead of pre-computing the array of enum names, it generates them during the test's execution time. This test uses Java's Stream API to generate the `String[]` of values.

- ComputedArrayBinarySearchForLoop

  This test is similar to ComputedArrayBinarySearchStream, but uses a for loop to generate the `String[]` of values.

- PrecomputedArrayBinarySearchHashcodes

  This test uses an `int[]` instead of a `String[]`, making use of Java's `Object.hashCode()` method. This test uses a pre-computed `int[]`, but computes `input.hashCode()` during the search check (as is most likely to be encountered in production). This is expected to give better performance over the binary search over a `String[]` due to a faster comparison between integers compared to string objects.

- PrecomputedArrayBinarySearchHashcodes2

  This test is identical to PrecomputedArrayBinarySearchHashcodes, except it also pre-computes the hash code of the input instead of computing it during the search check. This is expected to perform faster than PrecomputedArrayBinarySearchHashcodes as it has one less operation to perform during its check.

### Set contains tests

These tests use `Set.contains()` to check if a value exists.

- PrecomputedSetOfStrings

  This pre-computes a `Set<String>` containing all of the enum names and uses `.contains(String)` to check if the value is present in the enum.

- PrecomputedSetOfHashCodes

  Similar to PrecomputedSetOfStrings, this instead uses a `Set<Integer>` containing all of the hash codes of all of the names of enum values. This builds upon the implication that using hash codes may yield better performing results as in PrecomputedArrayBinarySearchHashcodes compared to PrecomputedArrayBinarySearch.

- ComputedSetOfStringsStream

  The computed equivalent of PrecomputedSetOfStrings, using streams similar to ComputedArrayBinarySearchStream.

- ComputedSetOfStringsForEach

  The same as ComputedSetOfStringsStream, using a for loop like ComputedArrayBinarySearchForLoop.

### Direct enum constant directory access

As mentioned in the ValueOfSecondAccess test, we found that the enum constant directory is accessed when finding the value of an enum name.

Since the enum constant directory is package-private, we have to make use of reflection/method handles. This cannot be done in the `java.lang` packages due to Java 9's module system, but [this can be circumvented using a runtime flag](https://stackoverflow.com/a/41265267/4779071):

```sh
java --add-opens java.base/java.lang=ALL-UNNAMED ...
```

- PrecomputedEnumConstantDirectory

  In this test we make use of reflection to access the enum constant directory method. We then call this method which generates the enum constant directory for the first time. This should yield similar results to ValueOfSecondAccess, with a slight performance increase as we're directly accessing the map without various safety checks (e.g. if the value is null etc.)

- ComputedEnumConstantDirectory

  The same as PrecomputedEnumConstantDirectory, except we're taking into account all of the performance of all of that reflection. This uses the first access, similar to the ValueOf test.

- ComputedEnumConstantDirectorySecondAccess

  The same as ComputedEnumConstantDirectory, except we pre-initialize the enum constant directory. We still test the performance impact of using reflection to directly access the enum constant directory.

- ComputedEnumConstantDirectoryMethodHandle

  The same ComputedEnumConstantDirectorySecondAccess, except we use method handles instead.

- ComputedEnumConstantDirectoryMethodHandle2

  The same as ComputedEnumConstantDirectoryMethodHandle, except we pre-compute the method handle.

-----

## Output

```txt
openjdk version "17.0.1" 2021-10-19
OpenJDK Runtime Environment (build 17.0.1+12-39)
OpenJDK 64-Bit Server VM (build 17.0.1+12-39, mixed mode, sharing)

Running on AMD Ryzen 9 5900X 12-Core Processor (X86_64) @ 3.7010 GHz

Compiling... Done!
Running 100 iterations of test Control ... Done!
Running 100 iterations of test ForEachIterator ... Done!
Running 100 iterations of test ValueOf ... Done!
Running 100 iterations of test ValueOfSecondAccess ... Done!
Running 100 iterations of test PrecomputedArrayBinarySearch ... Done!
Running 100 iterations of test ComputedArrayBinarySearchStream ... Done!
Running 100 iterations of test ComputedArrayBinarySearchForLoop ... Done!
Running 100 iterations of test PrecomputedArrayBinarySearchHashcodes ... Done!
Running 100 iterations of test PrecomputedArrayBinarySearchHashcodes2 ... Done!
Running 100 iterations of test PrecomputedSetOfStrings ... Done!
Running 100 iterations of test PrecomputedSetOfHashCodes ... Done!
Running 100 iterations of test ComputedSetOfStringsStream ... Done!
Running 100 iterations of test ComputedSetOfStringsForEach ... Done!
Running 100 iterations of test PrecomputedEnumConstantDirectory ... Done!
Running 100 iterations of test ComputedEnumConstantDirectory ... Done!
Running 100 iterations of test ComputedEnumConstantDirectoryMethodHandle ... Done!
Running 100 iterations of test ComputedEnumConstantDirectoryMethodHandle2 ... Done!

Printing lovely test results report...

╭────────────────────────────────────────────┬─────────────────────────────────────────────────────╮
│ Test                                       │   Average time over 100 iterations (in nanoseconds) │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ Control                                    │                                            -1,371.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ForEachIterator                            │                                        28,357,777.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ValueOf                                    │                                        29,193,459.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ValueOfSecondAccess                        │                                             6,668.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedArrayBinarySearch               │                                            12,327.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedArrayBinarySearchStream            │                                        30,484,903.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedArrayBinarySearchForLoop           │                                        28,341,247.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedArrayBinarySearchHashcodes      │                                             5,228.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedArrayBinarySearchHashcodes2     │                                             4,262.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedSetOfStrings                    │                                             2,266.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedSetOfHashCodes                  │                                             3,483.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedSetOfStringsStream                 │                                        30,679,362.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedSetOfStringsForEach                │                                        28,362,472.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ PrecomputedEnumConstantDirectory           │                                             3,237.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedEnumConstantDirectory              │                                        29,226,389.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedEnumConstantDirectorySecondAccess  │                                           711,524.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedEnumConstantDirectoryMethodHandle  │                                           613,065.0 │
├────────────────────────────────────────────┼─────────────────────────────────────────────────────┤
│ ComputedEnumConstantDirectoryMethodHandle2 │                                           466,513.0 │
╰────────────────────────────────────────────┴─────────────────────────────────────────────────────╯
```

-----

## Summary

Main takeaways:

- The Java enum `valueOf()` method uses caching! This makes initial calls slow, but subsequent calls much faster.

- Precomputed methods are significantly faster (to be expected) than methods that have not been pre-computed.

- Performing an array binary search over an `int[]` is faster than doing so over a `String[]`. This does not apply to `Set<Integer>` compared to `Set<String>` however.

- Precomputing a `Set<String>` of all of the names present in an enum is consistently the faster option 2-3 times faster than the general `Enum.valueOf(String)` method.

- Using reflection and method handles does not improve performance in this case.

-----

## License

WTFPL. Do whatever you want with this information.
