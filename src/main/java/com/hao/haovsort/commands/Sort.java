
package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SortPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

/**
 * /sort command
 * <p>
 * syntax :
 * {@code /sort <player> <type> <delay> <length> ...(แล้วแต่ละ Algorithms จะกำหนด)[algorithm's args]}
 * <p>
 * example : {@code /sort karnhao selection 10 100 args...}
 */
public class Sort implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        try {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) {
                throw new Exception("Player not found.");
            }
            AlgorithmsManager.cleanPlayer(target);
            SortPlayer player = new SortPlayer();
            List<Player> targets = Arrays.asList(target);
            Long delay = Long.parseLong(args[2]);
            if (delay < 1)
                throw new Exception("Delay cannot lower than 1");
            player.setPlayers(targets);
            player.setOwner(target);
            player.setCommands(new AlgorithmCommandCollector(Sort.createArray(Sort.getLength(args[3])),
                    new AlgorithmCommand("random", 10l, targets, "1"),
                    new AlgorithmCommand(args[1], delay, targets,
                            Arrays.copyOfRange(args, 4, args.length)),
                    new AlgorithmCommand("finish", 10l, targets)));
            player.start();
            AlgorithmsManager.addPlayer(target, player);
        } catch (Exception e) {
            alert(cs, e.toString());
        }
        return true;
    }

    private static int getLength(String s) {
        if (s == null || s == "")
            return 100;
        return Integer.parseInt(s);
    }

    private static Integer[] createArray(int length) {
        return IntStream.range(1, length + 1).boxed().toArray(Integer[]::new);
    }

    private static void alert(CommandSender cs, String e) {
        cs.sendMessage(ChatColor.RED + e);
    }
}
