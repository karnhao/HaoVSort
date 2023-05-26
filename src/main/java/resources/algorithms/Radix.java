package resources.algorithms;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;

import org.bukkit.command.CommandSender;

public class Radix extends Algorithms {

    private Integer radix = null;

    @Override
    public void sort(Integer[] a) {
        int n = a.length;

        // Find the maximum number to know number of digits
        int m = getMax(a, n);
        for (int exp = 1; m / exp > 0; exp *= radix) {
            countSort(a, n, exp);
        }
    }

    // A utility function to get maximum value in arr[]
    private int getMax(Integer arr[], int n) {
        int mx = arr[0];
        for (int i = 1; i < n; i++) {
            if (arr[i] > mx) {
                mx = arr[i];
            }
        }
        return mx;
    }

    // A function to do counting sort of arr[] according to
    // the digit represented by exp.
    private void countSort(Integer arr[], int n, int exp) {
        Integer output[] = new Integer[n]; // output array
        Integer count[] = new Integer[radix];
        Arrays.fill(count, 0);

        // Store count of occurrences in count[]
        for (int i = 0; i < n; i++) {
            count[(arr[i] / exp) % radix]++;
            setPitchs(pitchCal(arr[i]));
            setIndexes(i, n);
            show();
        }
        for (int i = 1; i < radix; i++) {
            count[i] += count[i - 1];
        }
        for (int i = n - 1; i >= 0; i--) {
            output[count[(arr[i] / exp) % radix] - 1] = arr[i];
            count[(arr[i] / exp) % radix]--;
        }
        int q[] = new int[radix];
        for (int i = 0; i < radix; i++) {
            q[i] = i;
        }
        Integer aa[] = new Integer[radix * 2];
        for (int i = 0; i < Math.ceil(n / radix)
                + (((double) n / (double) radix != (int) ((double) n / (double) radix)) ? 1 : 0); i++) {
            for (int ja : q) {
                arr[(int) Math.ceil(ja * n / radix) + i] = output[(int) Math.ceil(ja * n / radix) + i];
                aa[ja] = (int) Math.ceil(ja * n / radix) + i;
                aa[radix + ja] = (int) Math.ceil(ja * n / radix);
                setPitchs(pitchCal(arr[(int) Math.ceil(ja * n / radix) + i]));
                setIndexes(aa);
                show();
            }
        }
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return (args.length == 1 && args[0].length() == 0) ? Arrays.asList("[<radix>]") : null;
    }

    @Override
    public void init() {
        radix = this.getArgs().length == 0 ? 4 : Integer.parseInt(this.getArgs()[0]);
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (this.getArgs().length == 0)
            return;
        try {
            int t = Integer.parseInt(args[0]);
            if (t < 2)
                throw new InvalidArgsException("Radix must not less than 2.");
        } catch (NumberFormatException e) {
            throw new InvalidArgsException("Cannot format " + args[0] + " to Integer.");
        } catch (InvalidArgsException i) {
            throw i;
        }
    }
}
