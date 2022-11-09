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
    'Control',
    'ForEachIterator',
    'ValueOf',
    'ValueOfSecondAccess',
    'PrecomputedArrayBinarySearch',
    'ComputedArrayBinarySearchStream',
    'ComputedArrayBinarySearchForLoop',
    'PrecomputedArrayBinarySearchHashcodes',
    'PrecomputedArrayBinarySearchHashcodes2',
    'PrecomputedSetOfStrings',
    'PrecomputedSetOfHashCodes',
    'ComputedSetOfStringsStream',
    'ComputedSetOfStringsForEach',
    'PrecomputedEnumConstantDirectory',
    'ComputedEnumConstantDirectory',
    'ComputedEnumConstantDirectorySecondAccess',
    'ComputedEnumConstantDirectoryMethodHandle',
    'ComputedEnumConstantDirectoryMethodHandle2'
]

iterations = 100

results = []

control_result = 0

# Requires --add-opens runtime flag:
# https://stackoverflow.com/questions/41265266/how-to-solve-inaccessibleobjectexception-unable-to-make-member-accessible-m
# --add-opens has the following syntax: {A}/{package}={B}
# java --add-opens java.base/java.lang=ALL-UNNAMED

for test in tests:
    total = 0
    print('Running', iterations, 'iterations of test', test, '... ', end='', flush=True)
    for i in range(iterations):
        result = subprocess.run(['java', '--add-opens', 'java.base/java.lang=ALL-UNNAMED', '-cp', 'bin/', test, 'A3000'], stdout=subprocess.PIPE, shell=True)
        total += int(result.stdout.decode('utf-8'))
    print('Done!')
    if test == "Control":
        control_result = total / iterations
        results.append([test, -(total / iterations)])
    else:
        results.append([test, (total / iterations) - control_result])

print()
print("Printing lovely test results report...")
print()
print(tabulate(results, floatfmt=",", headers=["Test", "Average time over " + str(iterations) + " iterations (in nanoseconds)"], tablefmt="rounded_grid"))