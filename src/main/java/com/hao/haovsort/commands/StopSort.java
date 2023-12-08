package com.hao.haovsort.commands;

import java.util.List;

import com.hao.haovsort.HaoVSort;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.utils.PlayerSelector;

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
                    if (!(cs instanceof Player))
                        // sender is not a player.
                        throw new InvalidArgsException("Cannot stop.");
                    HaoVSort.getInstance().getAlgorithmManager().stopPlayer((Player) cs);
                    break;
                case 1:
                    List<Player> targets = PlayerSelector.getInstance().getPlayers(cs, args[0]);
                    if (targets == null || targets.isEmpty()) {
                        throw new NullPointerException("Player not found.");
                    }
                    targets.forEach(HaoVSort.getInstance().getAlgorithmManager()::cleanPlayer);
                    break;
                default:
                    throw new InvalidArgsException("Syntax error : /stopsort <player>");
            }
        } catch (NullPointerException | InvalidArgsException e) {
            cs.sendMessage(ChatColor.RED + e.getMessage());
        }
        return true;
    }
}
