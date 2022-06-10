package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Stooge extends Algorithms<Stooge> {

    @Override
    public void sort(Integer[] a) {
        stoogesort(a, 0, a.length - 1);
    }

    private void stoogesort(Integer arr[], int l, int h) {

        // Base Case
        if (l >= h) {
            return;
        }

        // If first element is smaller
        // than last element, swap them
        if (arr[l] > arr[h]) {
            int temp = arr[l];
            arr[l] = arr[h];
            arr[h] = temp;
        }

        // If there are more than
        // 2 elements in the array
        if (h - l + 1 > 2) {

            int t = (h - l + 1) / 3;

            // Recursively sort the
            // first 2/3 elements
            stoogesort(arr, l, h - t);

            // Recursively sort the
            // last 2/3 elements
            stoogesort(arr, l + t, h);

            // Recursively sort the
            // first 2/3 elements again
            stoogesort(arr, l, h - t);
            setIndexes(l, h);
            setPitchs(pitchCal(arr[l], arr[h]));
            show();
        }
    }
}
