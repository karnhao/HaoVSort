package resources.algorithms;

import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.SortingAlgorithm;

@SortingAlgorithm(name = "insert_merge")
public class InsertionMerge extends Algorithms<InsertionMerge> {

    private void inplaceMergeSort(Integer arr[], int l, int r) {
        if (l < r) {
            int m = (l + r) / 2;
            inplaceMergeSort(arr, l, m);
            inplaceMergeSort(arr, m + 1, r);
            inplaceMerge(arr, l, m, r);
            setPitchs();
            setIndexes();
        }
    }

    private void inplaceMerge(Integer arr[], int start, int mid, int end) {
        int start2 = mid + 1;

        // If the direct merge is already sorted
        if (arr[mid] <= arr[start2]) {
            return;
        }

        // Two pointers to maintain start
        // of both arrays to merge
        while (start <= mid && start2 <= end) {
            // If element 1 is in right place
            if (arr[start] <= arr[start2]) {
                setPitchs(pitchCal(arr[start], arr[start2]));
                setIndexes(start, start2);
                show();
                start++;
            } else {
                int value = arr[start2];
                int iindex = start2;

                // Shift all the elements between element 1
                // element 2, right by 1.
                while (iindex != start) {
                    setPitchs(pitchCal(arr[iindex]));
                    setIndexes(start, start2, iindex);
                    show();
                    arr[iindex] = arr[iindex - 1];
                    iindex--;
                }
                arr[start] = value;

                // Update all the pointers
                start++;
                mid++;
                start2++;
            }
        }
    }

    @Override
    public void sort(Integer[] a) {
        inplaceMergeSort(a, 0, a.length - 1);
    }
}
