package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.SortingAlgorithm;

@SortingAlgorithm(name = "gravity")
public class Bead extends Algorithms<Bead> {

    /**
     * Modified code from https://github.com/w0rthy/ArrayVisualizer/blob/master/src/array/visualizer/sort/GravitySort.java
     * Origin author S630690
     * @param a integer array
     * @throws InterruptedException
     */
    private void gravitySort(Integer[] a) {
        int max = Integer.MIN_VALUE;
        for (int i = 1; i < array.length; i++) {
            if (max < array[i]) {
                max = array[i];
            }
        }
        int[][] abacus = new int[a.length][max];
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[i]; j++)
                abacus[i][abacus[0].length - j - 1] = 1;
        }
        // apply gravity
        for (int i = 0; i < abacus[0].length; i++) {
            for (int j = 0; j < abacus.length; j++) {
                if (abacus[j][i] == 1) {
                    // Drop it
                    int droppos = j;
                    while (droppos + 1 < abacus.length && abacus[droppos][i] == 1)
                        droppos++;
                    if (abacus[droppos][i] == 0) {
                        abacus[j][i] = 0;
                        abacus[droppos][i] = 1;
                    }
                }
            }

            int count = 0;
            for (int x = 0; x < abacus.length; x++) {
                count = 0;
                for (int y = 0; y < abacus[0].length; y++)
                    count += abacus[x][y];
                a[x] = count;
                setPitchs(0f);
                show();
            }
        }
    }

    @Override
    public void sort(Integer[] a) {
        gravitySort(a);
    }
}
