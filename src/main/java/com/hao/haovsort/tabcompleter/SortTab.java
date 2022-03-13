package com.hao.haovsort.tabcompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsManager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /sort command
 */
public class SortTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        // /sort <player> <type> <delay> <length> args...
        switch (args.length) {
            case 0:
                // return Bukkit.getOnlinePlayers().stream().map((p) ->
                // p.getName()).collect(Collectors.toList());
                return null;
            case 1:
                return AlgorithmsManager.getAlgorithms().stream().map((algorithm) -> algorithm.getName().toLowerCase())
                        .filter((t) -> t.startsWith(args[1]))
                        .collect(Collectors.toList());
            case 2:
                if (args[2].isEmpty())
                    return Arrays.asList("<delay>");
            case 3:
                if (args[3].isEmpty())
                    return Arrays.asList("<length>");
            default:
                return null;
        }
    }
}
