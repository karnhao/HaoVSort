package com.hao.haovsort.tabcompleter;

import java.util.List;

import com.hao.haovsort.utils.PlayerSelector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class StopSortTab implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1)
            return PlayerSelector.getSuggestion(args[0]);
        return null;
    }
}
