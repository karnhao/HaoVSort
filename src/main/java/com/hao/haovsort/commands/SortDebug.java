package com.hao.haovsort.commands;

import java.util.Map;
import java.util.stream.Collectors;

import com.hao.haovsort.HaoVSort;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class SortDebug implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String str, String[] args) {
        cs.sendMessage(convertMapWithStream(HaoVSort.getInstance().getAlgorithmManager().getPlayers()));
        return true;
    }

    private String convertMapWithStream(Map<?, ?> map) {
        String mapAsString = map.keySet().stream()
                .map(key -> key + "=" + map.get(key))
                .collect(Collectors.joining(", ", "{", "}"));
        return mapAsString;
    }
}
