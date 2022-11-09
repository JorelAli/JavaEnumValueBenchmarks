import subprocess
from tabulate import tabulate

# Print the Java version
subprocess.run(['java', '-version'], stdout=subprocess.PIPE)
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
    'ComputedArrayBinarySearchForLoop'
]

iterations = 5

results = []

for test in tests:
    total = 0
    print('Running', iterations, 'iterations of test', test, '... ', end='', flush=True)
    for i in range(iterations):
        if test == 'ValueOfSecondAccess':
            # ValueOfSecondAccess has a second parameter...
            result = subprocess.run(['java', '-cp', 'bin/', test, 'A3000', 'A3000'], stdout=subprocess.PIPE, shell=True)
        else:
            result = subprocess.run(['java', '-cp', 'bin/', test, 'A3000'], stdout=subprocess.PIPE, shell=True)
        total += int(result.stdout.decode('utf-8'))
    print('Done!')
    results.append([test, total / iterations])

print(tabulate(results, floatfmt=",", headers=["Test", "Average time over " + str(iterations) + " in nanoseconds"], tablefmt="rounded_grid"))