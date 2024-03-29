package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Intro extends Algorithms {

    // the actual data that has to be sorted
    private Integer[] a;

    // the number of elements in the data
    private int n = 0;

    // The utility function to insert the data
    // private void dataAppend(int temp) {
    //     a[n] = temp;
    //     n++;
    // }

    // The utility function to swap two elements
    private void swap(int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
        setIndexes(i, j);
        setPitchs(pitchCal(a[j], a[i]));
        show();
    }

    // To maxHeap a subtree rooted with node i which is
    // an index in a[]. heapN is size of heap
    private void maxHeap(int i, int heapN, int begin) {
        int temp = a[begin + i - 1];
        int child;

        while (i <= heapN / 2) {
            child = 2 * i;

            if (child < heapN
                    && a[begin + child - 1] < a[begin + child])
                child++;

            if (temp >= a[begin + child - 1])
                break;

            a[begin + i - 1] = a[begin + child - 1];

            setIndexes(i, heapN, begin);
            setPitchs(pitchCal(a[begin + i - 1]));
            show();

            i = child;
        }
        a[begin + i - 1] = temp;
    }

    // Function to build the heap (rearranging the array)
    private void heapify(int begin, int end, int heapN) {
        for (int i = (heapN) / 2; i >= 1; i--)
            maxHeap(i, heapN, begin);
    }

    // main function to do heapsort
    private void heapSort(int begin, int end) {
        int heapN = end - begin;

        // Build heap (rearrange array)
        this.heapify(begin, end, heapN);

        // One by one extract an element from heap
        for (int i = heapN; i >= 1; i--) {

            // Move current root to end
            swap(begin, begin + i);

            // call maxHeap() on the reduced heap
            maxHeap(1, i, begin);
        }
    }

    // function that implements insertion sort
    private void insertionSort(int left, int right) {

        for (int i = left; i <= right; i++) {
            int key = a[i];
            int j = i;

            // Move elements of arr[0..i-1], that are
            // greater than the key, to one position ahead
            // of their current position
            while (j > left && a[j - 1] > key) {

                setIndexes(j);
                setPitchs(pitchCal(a[j - 1]));
                show();

                a[j] = a[j - 1];
                j--;
            }
            a[j] = key;
        }
    }

    // Function for finding the median of the three elements
    private int findPivot(int a1, int b1, int c1) {
        int max = Math.max(Math.max(a[a1], a[b1]), a[c1]);
        int min = Math.min(Math.min(a[a1], a[b1]), a[c1]);
        int median = max ^ min ^ a[a1] ^ a[b1] ^ a[c1];
        if (median == a[a1])
            return a1;
        if (median == a[b1])
            return b1;
        return c1;
    }

    // This function takes the last element as pivot, places
    // the pivot element at its correct position in sorted
    // array, and places all smaller (smaller than pivot)
    // to the left of the pivot
    // and greater elements to the right of the pivot
    private int partition(int low, int high) {

        // pivot
        int pivot = a[high];

        // Index of smaller element
        int i = (low - 1);
        for (int j = low; j <= high - 1; j++) {

            // If the current element is smaller
            // than or equal to the pivot
            if (a[j] <= pivot) {

                // increment index of smaller element
                i++;
                swap(i, j);
            }
        }
        swap(i + 1, high);
        return (i + 1);
    }

    // The main function that implements Introsort
    // low --> Starting index,
    // high --> Ending index,
    // depthLimit --> recursion level
    private void sortDataUtil(int begin, int end, int depthLimit) {
        if (end - begin > 16) {
            if (depthLimit == 0) {

                // if the recursion limit is
                // occurred call heap sort
                this.heapSort(begin, end);
                return;
            }

            depthLimit = depthLimit - 1;
            int pivot = findPivot(begin,
                    begin + ((end - begin) / 2) + 1,
                    end);
            swap(pivot, end);

            // p is partitioning index,
            // arr[p] is now at right place
            int p = partition(begin, end);

            // Separately sort elements before
            // partition and after partition
            sortDataUtil(begin, p - 1, depthLimit);
            sortDataUtil(p + 1, end, depthLimit);
        }

        else {
            // if the data set is small,
            // call insertion sort
            insertionSort(begin, end);
        }
    }

    // A utility function to begin the
    // Introsort module
    private void sortData() {

        // Initialise the depthLimit
        // as 2*log(length(data))
        int depthLimit = (int) (2 * Math.floor(Math.log(n) /
                Math.log(2)));

        this.sortDataUtil(0, n - 1, depthLimit);
    }

    @Override
    public void sort(Integer[] a) {
        this.a = a;
        // for (int i = 0; i < a.length; i++) {
        //     dataAppend(a[i]);

        //     setIndexes(i);
        //     setPitchs(pitchCal(a[i]));
        //     show();
        // }
        n = a.length;
        sortData();
    }
}
