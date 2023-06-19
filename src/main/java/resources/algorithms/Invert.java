package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Invert extends Algorithms {

    @Override
    public void sort(Integer[] a) {
        Integer[] arr_copy = new Integer[a.length];
        for (int i = 0; i <= arr_copy.length - 1; i++) {
            arr_copy[i] = a[i];
            setIndexes(i);
            setPitchs(pitchCal(a[i]));
            show();
        }
        for (int i = a.length - 1; i > -1; i--) {
            a[i] = arr_copy[arr_copy.length - 1 - i];
            setIndexes(i);
            setPitchs(pitchCal(a[i]));
            show();
        }
    }
}
