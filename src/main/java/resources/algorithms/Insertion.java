package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Insertion extends Algorithms<Insertion> {

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        int n = a.length;
        for (int i = 1; i < n; ++i) {
            int key = a[i];
            int j = i - 1;

            /*
             * Move elements of arr[0..i-1], that are
             * greater than key, to one position ahead
             * of their current position
             */
            while (j >= 0 && a[j] > key) {
                setPitchs(pitchCal(a[j]));
                setIndexes(i, j);
                show();
                a[j + 1] = a[j];
                j = j - 1;
            }
            a[j + 1] = key;
        }
    }
}
