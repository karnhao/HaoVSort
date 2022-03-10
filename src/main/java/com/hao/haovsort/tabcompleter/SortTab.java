package com.hao.haovsort.tabcompleter;

import java.util.Arrays;
import java.util.List;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

/**
 * TabCompleter for /sort command
 */
public class SortTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        if (cs.getName().equalsIgnoreCase("sort")) {
            return Arrays.asList("NODATA");
        }
        return null;
    }
}
