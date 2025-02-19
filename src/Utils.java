public class Utils {
    // Helper method to swap 2 items in an array
    static void swap(ValeriaUnification.Pair[] arr, int i, int j) {
        var tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }

    // Partitions from [start, end)
    static int partition(ValeriaUnification.Pair[] arr, int start, int end) {
        var pivot = arr[start];

        int i = start + 1;
        for (int j = i; j < end; j++) {
            if (arr[j].id() < pivot.id()) {
                swap(arr, j, i++);
            }
        }

        swap(arr, i - 1, start);
        return i - 1;
    }

}
