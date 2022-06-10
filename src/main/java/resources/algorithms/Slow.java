package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Slow extends Algorithms<Slow> {

    @Override
    public void sort(Integer[] a) {
        slowSort(a, 0, a.length - 1);
    }

    private void slowSort(Integer a[], int i, int j) {
        if (i >= j) {
            return;
        }
        int m = i + (j - i) / 2; // midpoint, implemented this way to avoid overflow
        int temp;
        slowSort(a, i, m);
        slowSort(a, m + 1, j);
        if (a[j] < a[m]) {
            temp = a[j]; // swapping a[j] & a[m]
            a[j] = a[m];
            a[m] = temp;
        }
        setPitchs(pitchCal(a[i], a[j]));
        setIndexes(i, j);
        show();

        slowSort(a, i, j - 1);
    }
}
