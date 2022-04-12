package com.hao.haovsort.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PlayerSelector {
    private static final List<String> INIT = Arrays.asList("@a", "@p", "@s", "@r");

    public static List<String> getSuggestion(String s) {
        List<String> r = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        return Stream.concat(r.stream(), INIT.stream()).filter((t) -> t.toLowerCase().startsWith(s.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<Player> getPlayers(@Nonnull CommandSender sender, @Nonnull String target) {
        switch (target.toLowerCase()) {
            case "@a":
                return Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
            case "@p":
                Player selectedPlayer = null;
                List<Player> players;
                Location location;
                if (sender instanceof BlockCommandSender) {
                    location = ((BlockCommandSender) sender).getBlock().getLocation();
                    players = ((BlockCommandSender) sender).getBlock().getWorld().getPlayers();
                } else if (sender instanceof Player) {
                    location = ((Player) sender).getLocation();
                    players = ((Player) sender).getWorld().getPlayers();
                } else
                    return null;
                double temp = Double.MAX_VALUE;
                for (Player p : players) {
                    Double distance = location.distanceSquared(p.getLocation());
                    if (distance < temp) {
                        temp = distance;
                        selectedPlayer = p;
                    }
                }
                return Arrays.asList(selectedPlayer);
            case "@s":
                if (sender instanceof Player)
                    return Arrays.asList(((Player) sender));
                break;
            case "@r":
                if (Bukkit.getOnlinePlayers().isEmpty()) return null;
                List<Player> a = Bukkit.getOnlinePlayers().stream().collect(Collectors.toList());
                return Arrays.asList(a.get((int) (Math.random() * a.size())));
            default:
                Player p = Bukkit.getPlayer(target);
                if (p != null)
                    return Arrays.asList(p);
        }
        return null;
    }
}
