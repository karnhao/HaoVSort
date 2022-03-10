
package com.hao.haovsort.commands;

import com.hao.haovsort.sorting.SortPlayer;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

/**
 * /sort command
 * <p>
 * syntax : {@code /sort <player> <type> <delay> <length> ...(แล้วแต่ละ Algorithms จะกำหนด)[algorithm's args]}
 * <p>
 * example : {@code /sort karnhao selection 10 100 args...}
 */
public class Sort implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String string, String[] args) {
        try {
            Player target = Bukkit.getPlayer(args[0]);
            SortPlayer player = new SortPlayer();
            if (target == null) throw new Exception("Player not found.");
            player.setPlayers(target);
        } catch (Exception e) {
            cs.sendMessage(ChatColor.RED + e.toString());
        }
        return true;
    }
}
