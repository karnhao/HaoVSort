package resources.algorithms;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;

import org.bukkit.command.CommandSender;

public class Wait extends Algorithms<Wait> {

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        Long delay;
        try {
            delay = Long.parseLong(this.getArgs()[0]);
        } catch (NumberFormatException e) {
            throw new InvalidArgsException("Cannot format " + getArgs()[0] + " as long : " + e.getMessage());
        }
        sleep(delay);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1 && args[0].length() == 0)
            return Arrays.asList("<miliseconds>");
        return null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (args.length != 1)
            throw new InvalidArgsException("Expected 1 argument");
    }

}
