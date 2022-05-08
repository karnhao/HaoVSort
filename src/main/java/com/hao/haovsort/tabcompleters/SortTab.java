package com.hao.haovsort.tabcompleters;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.utils.PlayerSelector;

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
            case 1:
                return PlayerSelector.getSuggestion(args[0]);
            case 2:
                return AlgorithmsManager.getAlgorithmNames(args[1]);
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
