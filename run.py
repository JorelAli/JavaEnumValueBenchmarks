from tabulate import tabulate
import subprocess
from cpuinfo import get_cpu_info

# Print the Java version
subprocess.run(['java', '-version'], stdout=subprocess.PIPE)
print()
print('Running on', get_cpu_info()['brand_raw'], '(%s) @ %s' % (get_cpu_info()['arch'], get_cpu_info()['hz_actual_friendly']))
print()

# Compile everything...
print('Compiling... ', end='', flush=True)
subprocess.run(['javac', '-cp', 'src/', '-d', 'bin/', 'src/*.java'], stdout=subprocess.PIPE, shell=True)
print("Done!")

tests = [
    'ForEachIterator',
    'ValueOf',
    'ValueOfSecondAccess',
    'PrecomputedArrayBinarySearch',
    'ComputedArrayBinarySearchStream',
    'ComputedArrayBinarySearchForLoop',
    'PrecomputedSetOfStrings',
    'ComputedSetOfStringsStream',
    'ComputedSetOfStringsForEach',
    'PrecomputedEnumConstantDirectory',
    'ComputedEnumConstantDirectory',
    'ComputedEnumConstantDirectoryMethodHandle',
    'ComputedEnumConstantDirectoryMethodHandle2'
]

iterations = 5

results = []

# Requires --add-opens runtime flag:
# https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
# --add-opens has the following syntax: {A}/{package}={B}
# java --add-opens java.base/java.lang=ALL-UNNAMED

for test in tests:
    total = 0
    print('Running', iterations, 'iterations of test', test, '... ', end='', flush=True)
    for i in range(iterations):
        if test == 'ValueOfSecondAccess':
            # ValueOfSecondAccess has a second parameter...
            result = subprocess.run(['java', '--add-opens', 'java.base/java.lang=ALL-UNNAMED', '-cp', 'bin/', test, 'A3000', 'A3000'], stdout=subprocess.PIPE, shell=True)
        else:
            result = subprocess.run(['java', '--add-opens', 'java.base/java.lang=ALL-UNNAMED', '-cp', 'bin/', test, 'A3000'], stdout=subprocess.PIPE, shell=True)
        total += int(result.stdout.decode('utf-8'))
    print('Done!')
    results.append([test, total / iterations])

print()
print("Printing lovely test results report...")
print()
print(tabulate(results, floatfmt=",", headers=["Test", "Average time over " + str(iterations) + " in nanoseconds"], tablefmt="rounded_grid"))