import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValeriaUnification {

    static void debugPrintArr(Pair[] arr, int pivotInd, int start, int end, int additionalPower) {
        System.out.printf("Pivot: %d, Start: %d, Stop: %d, Additional Power: %d\n", pivotInd,
                start, end, additionalPower);

        var arrStr = Arrays.stream(arr)
                .map(Pair::toString)
                .collect(Collectors.joining(", "));
        System.out.println(arrStr);
    }

    /**
     * Finds all the partitions from [start, end) and returns the excess power level.
     *
     * @param arr             original array
     * @param start           start index (inclusive) of the subarray
     * @param end             end index (exclusive of the subarray
     * @param partitionSize   size of each partition
     * @param additionalPower additional power
     * @param output          array list of partition points
     * @return excess power
     */
    static int findPartitions(Pair[] arr, int start, int end, int partitionSize,
                              int additionalPower, ArrayList<Integer> output) {
        if (start >= end) {
            // Case where there are zero items
            return additionalPower;
        }

        int pivotInd = Utils.partition(arr, start, end);

        debugPrintArr(arr, pivotInd, start, end, additionalPower);

        var leftSum = additionalPower + IntStream.range(start, pivotInd)
                .map(ind -> arr[ind].power())
                .sum();

        // Go left if there could be partitions there
        var leftExcess = (partitionSize <= leftSum)
                ? findPartitions(arr, start, pivotInd, partitionSize, additionalPower, output)
                : leftSum;

        var pivotPower = arr[pivotInd].power();

        var excessPower = leftExcess + pivotPower;

        if (excessPower == partitionSize) {
            // NOTE: If we were to change the assumption that there is no exact partitioning point,
            // you can probably change this to >=

            // I found a partition point
            // arr[pivotInd] is the last element (inclusive) in the partition
            System.out.printf("Found partition point at index %d\n", pivotInd);

            if (pivotInd + 1 < arr.length) {
                // Adds the next element after the pivotInd to represent the
                // first element (inclusive) of the next partition.
                output.add(arr[pivotInd + 1].id());
            }

            excessPower = 0;
        }

        return findPartitions(arr, pivotInd + 1, end, partitionSize, excessPower, output);
    }

    static void solve(Pair[] arr, int numPartitions) {
        var sumOfWeights = Arrays.stream(arr).mapToInt(Pair::power).sum();
        var partitionSize = sumOfWeights / numPartitions;
        System.out.printf("Partition Size: %d\n", partitionSize);

        var output = new ArrayList<Integer>();

        findPartitions(arr, 0, arr.length, partitionSize, 0, output);

        if (output.isEmpty()) {
            System.out.println("[0, infty)");
            return;
        }

        System.out.printf("[0, %d), ", output.getFirst());
        for (int i = 0; i + 1 < output.size(); i++) {
            System.out.printf("[%d, %d), ", output.get(i), output.get(i + 1));
        }
        System.out.printf("[%d, infty)\n", output.getLast());
    }

    public static void main(String[] args) {
//        var tc = new Pair[]{
//                new Pair(3, 1000),
//                new Pair(1, 2000),
//                new Pair(2, 1000),
//                new Pair(4, 1500),
//                new Pair(5, 500)
//        };
//
//        var tc1 = new Pair[]{
//                new Pair(3, 150_000),
//                new Pair(4, 42_000),
//                new Pair(1, 1_000),
//                new Pair(8, 151_000),
//                new Pair(7, 109_000)
//        };

        var tc = new Pair[]{
                new Pair(4, 750),
                new Pair(1, 2000),
                new Pair(3, 250),
                new Pair(2, 1000),
                new Pair(6, 1000),
                new Pair(5, 2000),
                new Pair(7, 2000)
        };
        solve(tc, 3);
    }

    record Pair(int id, int power) {
    }

}
