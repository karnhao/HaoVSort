package resources.algorithms;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;

public class Bitonic extends Algorithms<Bitonic> {

    @Override
    public void sort(Integer[] a) {
        bitonicSort(a, 0, a.length, 1);
    }

    private void compAndSwap(Integer a[], int i, int j, int dir) {

        if ((a[i] > a[j] && dir == 1)
                || (a[i] < a[j] && dir == 0)) {
            // Swapping elements
            int temp = a[i];
            a[i] = a[j];
            a[j] = temp;
            setPitchs(pitchCal(a[i]));
            setIndexes(i, j);
            show();
        }
    }

    /*
     * It recursively sorts a bitonic sequence in ascending
     * order, if dir = 1, and in descending order otherwise
     * (means dir=0). The sequence to be sorted starts at
     * index position low, the parameter cnt is the number
     * of elements to be sorted.
     */
    private void bitonicMerge(Integer a[], int low, int cnt, int dir) {

        if (cnt > 1) {
            int i;
            int k = cnt / 2;
            for (i = low; i < low + k; i++) {
                compAndSwap(a, i, i + k, dir);
            }
            bitonicMerge(a, low, k, dir);
            bitonicMerge(a, low + k, k, dir);
        }
    }

    /*
     * This funcion first produces a bitonic sequence by
     * recursively sorting its two halves in opposite sorting
     * orders, and then calls bitonicMerge to make them in
     * the same order
     */
    private void bitonicSort(Integer a[], int low, int cnt, int dir) {
        if (cnt > 1) {
            int k = cnt / 2;
            // sort in ascending order since dir here is 1
            bitonicSort(a, low, k, 1);

            // sort in descending order since dir here is 0
            bitonicSort(a, low + k, k, 0);

            // Will merge wole sequence in ascending order
            // since dir=1.
            bitonicMerge(a, low, cnt, dir);
        }
    }

    @Override
    public void init() {
        if (!isAPowerOF(getArray().length, 2))
            throw new InvalidArgsException("This algorithm works only when size of input is a power of 2.");
    }

    private static boolean isAPowerOF(int n, int base) {
        for (int i = 1; n >= Math.pow(base, i); i++) {
            if (Math.pow(base, i) == n) {
                return true;
            }
        }
        return false;
    }
}
