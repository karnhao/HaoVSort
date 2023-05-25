package com.hao.haovsort.commands;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SortPlayer;
import com.hao.haovsort.utils.PlayerSelector;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;

public class SortDelay implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /sortdelay <new_delay> [<player>]

        try {
            if (args.length > 2 || args.length < 1)
                throw new InvalidArgsException("Expected one or two arguments");
            List<Player> targets = args.length == 2 ? PlayerSelector.getPlayers(sender, args[1])
                    : Arrays.asList((Player) sender);
            if (targets == null || targets.isEmpty())
                throw new InvalidArgsException("Player not found");
            Long newDelay = Long.parseLong(args[0]);
            if (newDelay < 1)
                throw new InvalidArgsException("Delay cannot lower than 1");
            targets.forEach((t) -> {
                SortPlayer player = AlgorithmsManager.getPlayer(t);
                if (player == null)
                    return;
                Stream.of(player.getAlgorithmCommandCollectors()).forEach((cc) -> {
                    cc.getCommandList().forEach((c) -> {
                        c.setDelay(newDelay);
                    });
                });
                player.getAlgorithms().setDelay(newDelay);
                player.getAlgorithms().wake();
                player.interrupt();
            });
        } catch (InvalidArgsException e) {
            sender.sendMessage(ChatColor.RED + "Invalid arguments: " + e.getMessage());
        } catch (ClassCastException e) {
            sender.sendMessage(ChatColor.RED + "Target is not a Player");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Cannot parse new delay: " + e.getMessage());
        }
        return true;
    }

}
