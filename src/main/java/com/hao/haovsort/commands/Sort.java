
package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.stream.IntStream;

import com.hao.haovsort.sorting.SortPlayer;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

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

    private static Plugin plugin = null;

    public Sort(Plugin plugin) {
        Sort.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        // args[0] = <player>
        // args[1] = <algorithm>
        // args[2] = <delay>
        // args[3] = <length>
        // args[4]... = args...
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    Player target = Bukkit.getPlayer(args[0]);
                    SortPlayer player = new SortPlayer();
                    if (target == null)
                        throw new Exception("Player not found.");
                    AlgorithmsManager.cleanPlayer(target);
                    Player[] targets = Arrays.asList(target).toArray(new Player[1]);
                    player.setPlayers(Arrays.asList(targets));
                    player.setOwner(target);
                    player.setCommands(new AlgorithmCommandCollector(Sort.createArray(Sort.getLength(args[3])),
                            new AlgorithmCommand("random", 10l, targets, "1"),
                            new AlgorithmCommand(args[1], Long.parseLong(args[2]), targets,
                                    Arrays.copyOfRange(args, 4, args.length)),
                            new AlgorithmCommand("finish", 10l, targets)));
                    player.start();
                } catch (Exception e) {
                    cs.sendMessage(ChatColor.RED + e.toString());
                    e.printStackTrace();
                }
            }
        }.runTaskLater(plugin, 1l);
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
}
