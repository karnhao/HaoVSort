package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;

public class Bogo extends Algorithms<Bogo> {

    @Override
    public void sort(Integer[] a) {
        while (isSorted(a) == false) {
            shuffle(a);
        }
    }

    // To generate permuatation of the array
    private void shuffle(Integer[] a) {
        // Math.random() returns a double positive
        // value, greater than or equal to 0.0 and
        // less than 1.0.
        for (int i = 0; i < a.length; i++) {
            int random = (int) (Math.random() * i);
            setPitchs(pitchCal(a[i], a[random]));
            setIndexes(i, random);
            show();
            swap(a, i, random);
        }
    }

    // Swapping 2 elements
    private void swap(Integer[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }

    // To check if array is sorted or not
    private boolean isSorted(Integer[] a) {
        for (int i = 1; i < a.length; i++) {
            setPitchs(pitchCal(i, a[i - 1]));
            setIndexes(i);
            show();
            if (a[i] < a[i - 1]) {
                return false;
            }
        }
        return true;
    }
}
