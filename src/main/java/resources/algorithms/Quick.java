package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Quick extends Algorithms<Quick> {
    // public final static String NAME = "quick";

    /*
     * This function takes last element as pivot,
     * places the pivot element at its correct
     * position in sorted array, and places all
     * smaller (smaller than pivot) to left of
     * pivot and all greater elements to right
     * of pivot
     */
    private int partition(Integer arr[], int low, int high) throws InterruptedException {
        int pivot = arr[high];
        int i = (low - 1); // index of smaller element
        for (int j = low; j < high; j++) {
            setPitch(pitchCal(arr[j]), pitchCal(arr[i + 1]));
            setIndex(i, j);
            show();
            if (arr[j] < pivot) {
                i++;

                // swap arr[i] and arr[j]
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        setIndex(i);
        show();

        // swap arr[i+1] and arr[high] (or pivot)
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;

        return i + 1;
    }

    /*
     * The main function that implements QuickSort()
     * arr[] --> Array to be sorted,
     * low --> Starting index,
     * high --> Ending index
     */
    private void qsort(Integer arr[], int low, int high) throws InterruptedException {
        if (low < high) {
            /*
             * pi is partitioning index, arr[pi] is
             * now at right place
             */
            int pi = partition(arr, low, high);
            // Recursively sort elements before
            // partition and after partition
            qsort(arr, low, pi - 1);
            qsort(arr, pi + 1, high);
        }
    }

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        qsort(a, 0, a.length - 1);
    }
}
