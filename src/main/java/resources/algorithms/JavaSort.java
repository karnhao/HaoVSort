package resources.algorithms;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.hao.haovsort.sorting.utils.Algorithms;

import org.bukkit.command.CommandSender;

public class JavaSort extends Algorithms<JavaSort> {

    public static String NAME = "Java";
    private static List<String> SUGGESTION = Arrays.asList("True", "False");

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        javasort(a, 0, a.length - 1, true);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return SUGGESTION;
        return null;
    }

    private void javasort(Integer[] a, int left, int right, boolean leftmost) throws InterruptedException {
        int length = right - left + 1;

        // Use insertion sort on tiny arrays
        if (length < 47) {
            if (leftmost) {
                /*
                 * Traditional (without sentinel) insertion sort,
                 * optimized for server VM, is used in case of
                 * the leftmost part.
                 */
                for (int i = left, j = i; i < right; j = ++i) {

                    Integer ai = a[i + 1];
                    while (ai < a[j]) {
                        setPitchs(pitchCal(a[j]));
                        setIndexes(i, j);
                        show();

                        a[j + 1] = a[j];
                        if (j-- == left) {
                            break;
                        }
                    }
                    a[j + 1] = ai;
                }
            } else {
                /*
                 * Skip the longest ascending sequence.
                 */
                do {
                    if (left >= right) {
                        return;
                    }
                } while (a[++left] >= a[left - 1]);

                /*
                 * Every element from adjoining part plays the role
                 * of sentinel, therefore this allows us to avoid the
                 * left range check on each iteration. Moreover, we use
                 * the more optimized algorithm, so called pair insertion
                 * sort, which is faster (in the context of Quicksort)
                 * than traditional implementation of insertion sort.
                 */
                for (int k = left; ++left <= right; k = ++left) {

                    Integer a1 = a[k], a2 = a[left];

                    if (a1 < a2) {
                        a2 = a1;
                        a1 = a[left];
                    }
                    while (a1 < a[--k]) {

                        setPitchs(pitchCal(a[k]));
                        setIndexes(k);
                        show();

                        a[k + 2] = a[k];
                    }
                    a[++k + 1] = a1;

                    while (a2 < a[--k]) {

                        setPitchs(pitchCal(a[k]));
                        setIndexes(k);
                        show();

                        a[k + 1] = a[k];
                    }
                    a[k + 1] = a2;
                }
                Integer last = a[right];

                while (last < a[--right]) {

                    setPitchs(pitchCal(a[right]));
                    setIndexes(last, right, left);
                    show();

                    a[right + 1] = a[right];
                }
                a[right + 1] = last;
            }
            return;
        }

        // Inexpensive approximation of length / 7
        int seventh = (length >> 3) + (length >> 6) + 1;

        /*
         * Sort five evenly spaced elements around (and including) the
         * center element in the range. These elements will be used for
         * pivot selection as described below. The choice for spacing
         * these elements was empirically determined to work well on
         * a wide variety of inputs.
         */
        int e3 = (left + right) >>> 1; // The midpoint
        int e2 = e3 - seventh;
        int e1 = e2 - seventh;
        int e4 = e3 + seventh;
        int e5 = e4 + seventh;

        // Sort these elements using insertion sort
        if (a[e2] < a[e1]) {
            Integer t = a[e2];
            a[e2] = a[e1];
            a[e1] = t;
        }

        if (a[e3] < a[e2]) {
            Integer t = a[e3];
            a[e3] = a[e2];
            a[e2] = t;
            if (t < a[e1]) {
                a[e2] = a[e1];
                a[e1] = t;
            }
        }
        if (a[e4] < a[e3]) {
            Integer t = a[e4];
            a[e4] = a[e3];
            a[e3] = t;
            if (t < a[e2]) {
                a[e3] = a[e2];
                a[e2] = t;
                if (t < a[e1]) {
                    a[e2] = a[e1];
                    a[e1] = t;
                }
            }
        }
        if (a[e5] < a[e4]) {
            Integer t = a[e5];
            a[e5] = a[e4];
            a[e4] = t;
            if (t < a[e3]) {
                a[e4] = a[e3];
                a[e3] = t;
                if (t < a[e2]) {
                    a[e3] = a[e2];
                    a[e2] = t;
                    if (t < a[e1]) {
                        a[e2] = a[e1];
                        a[e1] = t;
                    }
                }
            }
        }

        // Pointers
        int less = left; // The index of the first element of center part
        int great = right; // The index before the first element of right part

        if (!Objects.equals(a[e1], a[e2]) && !Objects.equals(a[e2], a[e3]) && !Objects.equals(a[e3], a[e4])
                && !Objects.equals(a[e4], a[e5])) {
            /*
             * Use the second and fourth of the five sorted elements as pivots.
             * These values are inexpensive approximations of the first and
             * second terciles of the array. Note that pivot1 <= pivot2.
             */
            Integer pivot1 = a[e2];
            Integer pivot2 = a[e4];

            /*
             * The first and the last elements to be sorted are moved to the
             * locations formerly occupied by the pivots. When partitioning
             * is complete, the pivots are swapped back into their final
             * positions, and excluded from subsequent sorting.
             */
            a[e2] = a[left];
            a[e4] = a[right];

            /*
             * Skip elements, which are less or greater than pivot values.
             */
            // while (a[++less] < pivot1);
            // while (a[--great] > pivot2);

            /*
             * Partitioning:
             *
             * left part center part right part
             * +--------------------------------------------------------------+
             * | < pivot1 | pivot1 <= && <= pivot2 | ? | > pivot2 |
             * +--------------------------------------------------------------+
             * ^ ^ ^
             * | | |
             * less k great
             *
             * Invariants:
             *
             * all in (left, less) < pivot1
             * pivot1 <= all in [less, k) <= pivot2
             * all in (great, right) > pivot2
             *
             * Pointer k is the first index of ?-part.
             */
            outer: for (int k = less - 1; ++k <= great;) {

                Integer ak = a[k];
                if (ak < pivot1) { // Move a[k] to left part
                    setPitchs(pitchCal(a[k], a[less], a[great]));
                    setIndexes(k, left, right, less, great);
                    show();

                    a[k] = a[less];
                    /*
                     * Here and below we use "a[i] = b; i++;" instead
                     * of "a[i++] = b;" due to performance issue.
                     */
                    a[less] = ak;
                    ++less;
                } else if (ak > pivot2) { // Move a[k] to right part
                    while (a[great] > pivot2) {
                        setPitchs(pitchCal(a[k], a[less], a[great]));
                        setIndexes(k, left, right, less, great);
                        show();

                        if (great-- == k) {
                            break outer;
                        }
                    }
                    if (a[great] < pivot1) { // a[great] <= pivot2
                        a[k] = a[less];
                        a[less] = a[great];
                        ++less;
                    } else { // pivot1 <= a[great] <= pivot2
                        a[k] = a[great];
                    }
                    /*
                     * Here and below we use "a[i] = b; i--;" instead
                     * of "a[i--] = b;" due to performance issue.
                     */
                    a[great] = ak;
                    --great;
                }
            }

            // Swap pivots into their final positions
            a[left] = a[less - 1];
            a[less - 1] = pivot1;
            a[right] = a[great + 1];
            a[great + 1] = pivot2;

            // Sort left and right parts recursively, excluding known pivots
            javasort(a, left, less - 2, leftmost);
            javasort(a, great + 2, right, false);

            /*
             * If center part is too large (comprises > 4/7 of the array),
             * swap internal pivot values to ends.
             */
            if (less < e1 && e5 < great) {
                /*
                 * Skip elements, which are equal to pivot values.
                 */
                while (Objects.equals(a[less], pivot1)) {
                    ++less;
                }

                while (Objects.equals(a[great], pivot2)) {
                    --great;
                }

                /*
                 * Partitioning:
                 *
                 * left part center part right part
                 * +----------------------------------------------------------+
                 * | == pivot1 | pivot1 < && < pivot2 | ? | == pivot2 |
                 * +----------------------------------------------------------+
                 * ^ ^ ^
                 * | | |
                 * less k great
                 *
                 * Invariants:
                 *
                 * all in (*, less) == pivot1
                 * pivot1 < all in [less, k) < pivot2
                 * all in (great, *) == pivot2
                 *
                 * Pointer k is the first index of ?-part.
                 */
                outer: for (int k = less - 1; ++k <= great;) {

                    Integer ak = a[k];
                    if (Objects.equals(ak, pivot1)) { // Move a[k] to left part
                        setPitchs(pitchCal(a[k], a[great], a[less]));
                        setIndexes(k, great, less, left, right);
                        show();

                        a[k] = a[less];
                        a[less] = ak;
                        ++less;
                    } else if (Objects.equals(ak, pivot2)) { // Move a[k] to right part
                        while (Objects.equals(a[great], pivot2)) {
                            setPitchs(pitchCal(a[k], a[great], a[less]));
                            setIndexes(k, great, less, left, right);

                            show();

                            if (great-- == k) {
                                break outer;
                            }
                        }
                        if (Objects.equals(a[great], pivot1)) { // a[great] < pivot2
                            a[k] = a[less];
                            /*
                             * Even though a[great] equals to pivot1, the
                             * assignment a[less] = pivot1 may be incorrect,
                             * if a[great] and pivot1 are floating-point zeros
                             * of different signs. Therefore in float and
                             * double sorting methods we have to use more
                             * accurate assignment a[less] = a[great].
                             */
                            a[less] = a[great];
                            ++less;
                        } else { // pivot1 < a[great] < pivot2
                            a[k] = a[great];
                            setPitchs(pitchCal(a[k], a[great], a[less]));
                            setIndexes(k, great, less, left, right);
                            show();
                        }
                        a[great] = ak;
                        --great;
                    }
                }
            }

            // Sort center part recursively
            javasort(a, less, great, false);

        } else { // Partitioning with one pivot
            /*
             * Use the third of the five sorted elements as pivot.
             * This value is inexpensive approximation of the median.
             */
            double pivot = a[e3];

            /*
             * Partitioning degenerates to the traditional 3-way
             * (or "Dutch National Flag") schema:
             *
             * left part center part right part
             * +-------------------------------------------------+
             * | < pivot | == pivot | ? | > pivot |
             * +-------------------------------------------------+
             * ^ ^ ^
             * | | |
             * less k great
             *
             * Invariants:
             *
             * all in (left, less) < pivot
             * all in [less, k) == pivot
             * all in (great, right) > pivot
             *
             * Pointer k is the first index of ?-part.
             */
            for (int k = less; k <= great; ++k) {

                if (a[k] == pivot) {
                    continue;
                }
                Integer ak = a[k];
                if (ak < pivot) { // Move a[k] to left part
                    setPitchs(pitchCal(a[k], a[great], a[less]));
                    setIndexes(k, great, less, left, right);
                    show();

                    a[k] = a[less];
                    a[less] = ak;
                    ++less;
                } else { // a[k] > pivot - Move a[k] to right part
                    while (a[great] > pivot) {
                        setPitchs(pitchCal(a[k], a[great], a[less]));
                        setIndexes(k, great, less, left, right);
                        show();

                        --great;
                    }
                    if (a[great] < pivot) { // a[great] <= pivot
                        a[k] = a[less];
                        a[less] = a[great];
                        ++less;
                    } else { // a[great] == pivot
                        /*
                         * Even though a[great] equals to pivot, the
                         * assignment a[k] = pivot may be incorrect,
                         * if a[great] and pivot are floating-point
                         * zeros of different signs. Therefore in float
                         * and double sorting methods we have to use
                         * more accurate assignment a[k] = a[great].
                         */
                        setPitchs(pitchCal(a[k]));
                        setIndexes(k);
                        show();

                        a[k] = a[great];
                    }
                    a[great] = ak;
                    --great;
                }
            }

            /*
             * Sort left and right parts recursively.
             * All elements from center part are equal
             * and, therefore, already sorted.
             */
            javasort(a, left, less - 1, leftmost);
            javasort(a, great + 1, right, false);
        }
    }
}
