package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SortPlayer;
import com.hao.haovsort.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SortCustom implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        // /sortcustom <player> <delay> <length> random 1 ; radix 4 ; finish #00AA00
        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null)
                throw new NullPointerException("Player not found.");
            AlgorithmsManager.cleanPlayer(target);
            SortPlayer player = new SortPlayer();
            List<Player> targets = Arrays.asList(target);
            Long delay = Long.parseLong(args[1]);
            if (delay < 1)
                throw new Exception("Delay cannot lower than 1");
            player.setPlayers(targets);
            player.setOwner(target);
            player.setCommands(new AlgorithmCommandCollector(Util.createArray(Util.getLength(args[2])),
                    getAlgorithmCommands(Arrays.copyOfRange(args, 3, args.length), targets, delay)));
            player.start();
            AlgorithmsManager.addPlayer(target, player);
        } catch (Exception e) {
            Util.alert(cs, e.toString());
        }
        return false;
    }

    private static AlgorithmCommand[] getAlgorithmCommands(String[] args, List<Player> players, Long delay) {
        String str = Util.argsToString(args);
        return Arrays.asList(str.split(";")).stream().map((t) -> t.trim())
                .map((t) -> new AlgorithmCommand(t.split(" ")[0], delay, players,
                        Arrays.copyOfRange(t.split(" "), 1, t.split(" ").length)))
                .collect(Collectors.toList())
                .toArray(new AlgorithmCommand[str.split(";").length]);
    }
}
