package resources.algorithms;

import java.util.Arrays;
import java.util.LinkedList;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Bead extends Algorithms<Bead> {
    private void beadsort(Integer[] a, boolean reverse) throws InterruptedException {
        if (a.length == 0)
            return;
        int[] array = new int[a.length];
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i];
        }
        int max = array[0];
        int min = array[0];
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
            if (min > array[i]) {
                min = array[i];
            }
        }

        for (int i = 0; i < a.length; i++) {
            a[i] = min;
        }
        LinkedList<Float> pitchs = new LinkedList<>(Arrays.asList(0.0f));
        int[] counter = new int[max - min];
        for (int value : array) {
            for (int i = 0; value > min; value--, i++) {
                int index = counter[i]++;
                if (!reverse) {
                    index = a.length - index - 1;
                }
                a[index]++;
                setIndexes(index);
                setPitchs(pitchs);
                show();
            }
        }
    }

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        beadsort(a, false);
    }
}
