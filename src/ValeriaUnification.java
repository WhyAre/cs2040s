import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ValeriaUnification {

    static void debugPrint(Pair[] arr, int pivotInd, int start, int end) {
        System.out.printf("Pivot: %d, Start: %d, Stop: %d\n", pivotInd, start, end);

        var arrStr = Arrays.stream(arr)
                .map(Pair::toString)
                .collect(Collectors.joining(", "));
        System.out.println(arrStr);
    }

    /**
     * Finds all the partitions from [start, end) and returns the excess weights after inserting all the partition points.
     *
     * @return excess weight
     */
    static int findPartitions(Pair[] arr, int start, int end, int partitionSize,
                              int additionalWeight, ArrayList<Integer> output) {
        if (start >= end) {
            // Case where there are zero items
            return additionalWeight;
        }

        int pivotInd = Utils.partition(arr, start, end);

        debugPrint(arr, pivotInd, start, end);

        var leftSum = additionalWeight + IntStream.range(start, pivotInd)
                .map(ind -> arr[ind].weight())
                .sum();

        // Go left if there could be partitions there
        var leftExcess = (partitionSize <= leftSum)
                ? findPartitions(arr, start, pivotInd, partitionSize, additionalWeight, output)
                : leftSum;

        var pivotWeight = arr[pivotInd].weight();

        var excessWeight = leftExcess + pivotWeight;
        if (excessWeight >= partitionSize) {
            System.out.printf("Added pivot: %d\n", pivotInd);
            output.add(arr[pivotInd].id());
            excessWeight -= partitionSize;
        }

        return findPartitions(arr, pivotInd + 1, end, partitionSize, excessWeight, output);
    }

    static List<Integer> solve(Pair[] arr, int numPartitions) {
        var sumOfWeights = Arrays.stream(arr).mapToInt(Pair::weight).sum();
        var partitionSize = sumOfWeights / numPartitions;

        var output = new ArrayList<Integer>();

        findPartitions(arr, 0, arr.length, partitionSize, 0, output);

        return output;
    }

    public static void main(String[] args) {
        var tc = new Pair[]{
                new Pair(3, 1000),
                new Pair(1, 2000),
                new Pair(2, 1000),
                new Pair(4, 1500),
                new Pair(5, 500)
        };

        var tc1 = new Pair[]{
                new Pair(3, 150_000),
                new Pair(4, 42_000),
                new Pair(1, 1_000),
                new Pair(8, 151_000),
                new Pair(7, 109_000)
        };

        System.out.println(solve(tc, 3));
        System.out.println(solve(tc1, 3));
    }

    record Pair(int id, int weight) {
    }

}

