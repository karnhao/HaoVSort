package com.hao.haovsort.tabcompleters;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.hao.haovsort.utils.PlayerSelector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SortDelayTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && args[0].length() == 0) return Arrays.asList("<new_delay>");
        if (args.length == 2) return PlayerSelector.getInstance().getSuggestion(args[1]);
        return Collections.<String>emptyList();
    }
    
}
