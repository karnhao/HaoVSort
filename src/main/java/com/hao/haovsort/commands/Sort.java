
package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import com.hao.haovsort.sorting.SortPlayer;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommandCollecter;

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
            SortPlayer player = new SortPlayer();
            if (target == null)
                throw new Exception("Player not found.");
            List<Player> targetList = Arrays.asList(target);
            player.setPlayers(targetList);
            player.setCommands(new AlgorithmCommandCollecter(Sort.createArray(Sort.getLength(args[3])),
                    new AlgorithmCommand(args[1],
                            Long.parseLong(args[2]),
                            (Player[]) Arrays.asList(target).toArray()), new AlgorithmCommand("random", 10l, (Player[]) targetList.toArray())));

        } catch (Exception e) {
            cs.sendMessage(ChatColor.RED + e.toString());
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
}
