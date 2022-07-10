package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Merge extends Algorithms<Merge> {
    private void merge(Integer arr[], int l, int m, int r) {
        // Find sizes of two subarrays to be merged
        int n1 = m - l + 1;
        int n2 = r - m;

        /* Create temp arrays */
        int L[] = new int[n1];
        int R[] = new int[n2];

        /* Copy data to temp arrays */
        for (int i = 0; i < n1; ++i) {
            L[i] = arr[l + i];
        }
        for (int j = 0; j < n2; ++j) {
            R[j] = arr[m + 1 + j];
        }

        /* Merge the temp arrays */
        // Initial indexes of first and second subarrays
        int i = 0;
        int j = 0;

        // Initial index of merged subarry array
        int k = l;
        while (i < n1 && j < n2) {
            if (L[i] <= R[j]) {
                arr[k] = L[i];
                i++;
                setPitchs(pitchCal(L[i - 1]));
                setIndexes(j + m + 1);
            } else {
                arr[k] = R[j];
                j++;
                setPitchs(pitchCal(R[j - 1]));
                setIndexes(j + m + 1);
            }
            k++;
            getIndexesIntegers().add(k);
            show();
        }

        /* Copy remaining elements of L[] if any */
        while (i < n1) {
            arr[k] = L[i];
            i++;
            k++;
            setPitchs(pitchCal(arr[k - 1]));
            setIndexes(k);
            show();
        }

        /* Copy remaining elements of R[] if any */
        while (j < n2) {
            arr[k] = R[j];
            j++;
            k++;
            setPitchs(pitchCal(arr[k - 1]));
            setIndexes(k);
            show();
        }
    }

    private void mergesort(Integer arr[], int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            mergesort(arr, l, m);
            mergesort(arr, m + 1, r);
            merge(arr, l, m, r);
            setPitchs();
            setIndexes();
        }
    }

    @Override
    public void sort(Integer[] a) {
        mergesort(a, 0, a.length - 1);
    }
}
