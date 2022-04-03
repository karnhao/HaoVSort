package resources.algorithms;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Random extends Algorithms<Random> {

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        this.setIndexColor(ChatColor.RED);
        show();
        int i = a.length - 1;
        boolean loop = true;

        while (loop) {
            java.util.Random r = new java.util.Random();
            if (i > 0) {
                if (Math.random() >= Float.parseFloat(this.getArgs()[0])) {
                    setPitchs(this.pitchCal(i - 1));
                    setIndexes(i);
                    show();
                    i--;
                    continue;
                }
                int j = r.nextInt(i);
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                setPitchs(pitchCal(i - 1), pitchCal(j - 1));
                i--;
                setIndexes(i);
                show();
            } else {
                loop = false;
            }
        }
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return (args.length == 1) ? Arrays.asList("[<percentage>[0,1]]") : null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        Float f;
        try {
            f = Float.parseFloat(args[0]);
        } catch (NumberFormatException e) {
            throw new InvalidArgsException("Cannot format number.");
        } catch (IndexOutOfBoundsException ex){
            throw new InvalidArgsException("Args not found");
        }
        if (f < 0 || f > 1)
            throw new InvalidArgsException("Value out of range.");
    }
}
