package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Selection extends Algorithms<Selection> {

    public static final String NAME = "selection";

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (j < n) {
                    setPitchs(this.pitchCal(a[j]));
                    setIndexes(i, j, min_idx);
                    show();
                    if (a[j] < a[min_idx]) {
                        min_idx = j;
                    }
                }
            }
            int temp = a[min_idx];
            a[min_idx] = a[i];
            a[i] = temp;
        }
        show();
    }
}
