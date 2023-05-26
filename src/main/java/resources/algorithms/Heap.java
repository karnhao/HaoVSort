package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Heap extends Algorithms {

    @Override
    public void sort(Integer[] a) {
        int n = a.length;

        // Build heap (rearrange array)
        for (int i = n / 2 - 1; i >= 0; i--) {
            setPitchs(pitchCal(i, a[i]));
            setIndexes(i, n);
            show();

            heapify(a, n, i);
        }

        // One by one extract an element from heap
        for (int i = n - 1; i > 0; i--) {

            setPitchs(pitchCal(i, a[i]));
            setIndexes(i, n);
            show();

            // Move current root to end
            int temp = a[0];
            a[0] = a[i];
            a[i] = temp;

            // call max heapify on the reduced heap
            heapify(a, i, 0);
        }
    }

    // To heapify a subtree rooted with node i which is
    // an index in arr[]. n is size of heap
    private void heapify(Integer arr[], int n, int i) {

        int largest = i; // Initialize largest as root
        int l = 2 * i + 1; // left = 2*i + 1
        int r = 2 * i + 2; // right = 2*i + 2

        // If left child is larger than root
        if (l < n && arr[l] > arr[largest]) {
            largest = l;
        }

        // If right child is larger than largest so far
        if (r < n && arr[r] > arr[largest]) {
            largest = r;
        }

        // If largest is not root
        if (largest != i) {
            int swap = arr[i];
            arr[i] = arr[largest];
            arr[largest] = swap;

            // Recursively heapify the affected sub-tree
            heapify(arr, n, largest);
        }
        setPitchs(pitchCal(i, arr[i]));
        setIndexes(i, largest, n);
        show();
    }
}
