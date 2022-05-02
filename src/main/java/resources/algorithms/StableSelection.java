package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class StableSelection extends Algorithms<StableSelection> {
    public static final String NAME = "stable_selection";

    private void stableSelectionSort(Integer[] a, int n) throws InterruptedException {
        // Iterate through array elements
        for (int i = 0; i < n - 1; i++) {

            // Loop invariant : Elements till
            // a[i - 1] are already sorted.

            // Find minimum element from
            // arr[i] to arr[n - 1].
            int min = i;
            for (int j = i + 1; j < n; j++)
                if (a[min] > a[j]) {
                    min = j;
                    setIndexes(i, j);
                    setPitchs(pitchCal(j));
                    show();
                }

            // Move minimum element at current i.
            int key = a[min];
            while (min > i) {
                a[min] = a[min - 1];
                min--;
                setIndexes(i, min);
                setPitchs(pitchCal(min));
                show();
            }

            a[i] = key;
        }
    }

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        stableSelectionSort(a, a.length);
    }
}
