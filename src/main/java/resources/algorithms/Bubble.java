package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Bubble extends Algorithms<Bubble> {

    @Override
    public void sort(Integer[] a) {
        int n = a.length;
        int temp;
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                setPitchs(pitchCal(a[j]));
                if (j != 0) {
                    this.getPitchs().add(pitchCal(a[j - 1]));
                }
                setIndexes(n - 1 - i, j - 1);
                show();
                if (a[j] > a[j + 1]) {
                    // swap arr[j+1] and arr[j]
                    temp = a[j];
                    a[j] = a[j + 1];
                    a[j + 1] = temp;
                }
            }
        }
    }
}
