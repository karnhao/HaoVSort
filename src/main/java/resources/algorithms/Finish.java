package resources.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.Algorithms;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Finish extends Algorithms<Finish> {
    private final static List<String> SUGGESTION_COLOR = Arrays.asList("#", "#00AA00");

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        setIndexColor(ChatColor.of(this.getArgs().length > 0 ? this.getArgs()[0] : "#00AA00"));
        List<Integer> intf = new ArrayList<>();
        for (int i = 0; i < a.length; i++) {
            setPitch(pitchCal(a[i]));
            intf.add(i);
            Integer[] b = intf.toArray(new Integer[intf.size()]);
            setIndex(b);
            show();
        }
        setIndex();
        setPitch();
        show();
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        if (args.length == 1)
            return SUGGESTION_COLOR.stream().filter((t) -> t.startsWith(args[0])).collect(Collectors.toList());
        return null;
    }

    @Override
    protected void argsFilter(String[] args) throws InvalidArgsException {
        if (args.length == 1)
            try {
                ChatColor.of(args[0]);
            } catch (Exception e) {
                throw new InvalidArgsException("Cannot format " + args[0] + " to Color");
            }
    }
}
