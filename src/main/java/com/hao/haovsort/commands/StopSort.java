package com.hao.haovsort.commands;

import com.hao.haovsort.sorting.utils.AlgorithmsManager;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class StopSort implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String str, String[] args) {
        try {
            switch (args.length) {
                case 0:
                    if (cs instanceof Player)
                        AlgorithmsManager.stopPlayer((Player) cs);
                    else
                        throw new Exception("Cannot stop.");
                    break;
                case 1:
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null)
                        throw new NullPointerException();
                    AlgorithmsManager.stopPlayer(target);
                    break;
                default:
                    throw new Exception("Syntax error : /stopsort <player>");
            }
        } catch (Exception e) {
            cs.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
