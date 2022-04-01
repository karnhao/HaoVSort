package com.hao.haovsort.tabcompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.algorithms.utils.Algorithms;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /sort command
 */
public class SortTab implements TabCompleter {

    @SuppressWarnings("unchecked")
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        // /sort <player> <type> <delay> <length> args...
        switch (args.length) {
            case 1:
                // return Bukkit.getOnlinePlayers().stream().map((p) ->
                // p.getName()).collect(Collectors.toList());
                return null;
            case 2:
                return AlgorithmsManager.getAlgorithms().stream().map(t -> {
                    try {
                        Class<? extends Algorithms<?>> clazz = (Class<? extends Algorithms<?>>) t.getClass();
                        return Algorithms.getAlgorithmName(clazz);
                    } catch (IllegalArgumentException | SecurityException e) {
                        return null;
                    }
                })
                        .filter((t) -> t.startsWith(args[1].toLowerCase()))
                        .collect(Collectors.toList());
            case 3:
                if (args[2].isEmpty())
                    return Arrays.asList("<delay>");
                break;
            case 4:
                if (args[3].isEmpty())
                    return Arrays.asList("<length>");
                break;
            default:
                return null;
        }
        return null;
    }
}
