package com.hao.haovsort.sorting.utils;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.tabcompleter.SortTab;

import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * เครื่องแสดง algorithms
 */
final public class SortPlayer extends Thread {

    private Algorithms<?> algorithm;

    private List<Player> players;
    private Integer[] array;
    private AlgorithmCommandCollector[] commands;
    private Player owner;

    public void setCommands(AlgorithmCommandCollector... acc) {
        this.commands = acc;
    }

    public AlgorithmCommandCollector[] getAlgorithmCommandCollectors() {
        return this.commands;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public Player getOwner() {
        return this.owner;
    }

    public void setOwner(Player player) {
        this.owner = player;
    }

    @Override
    public void run() {
        try {
            Arrays.asList(getAlgorithmCommandCollectors()).forEach((acc) -> {
                if (!this.isInterrupted()) {
                    this.array = acc.getArray();
                    acc.getCommandList().forEach((command) -> {
                        if (!this.isInterrupted()) {
                            try {
                                runAlgorithm(command, command.getArgs());
                            } catch (NoSuchAlgorithmException e) {
                                this.alert("Sort command not found");
                                throw new StopSortException();
                            } catch (StopSortException stop) {
                                this.alert("Stop visualize");
                                throw new StopSortException();
                            } catch (InvalidArgsException arg) {
                                this.alert(arg.getMessage());
                                throw new StopSortException();
                            } catch (Exception ex) {
                                this.alert(ex.getMessage());
                                ex.printStackTrace();
                                throw new StopSortException();
                            }
                        }
                    });
                }
            });
        } catch (RuntimeException stop) {
        }
        this.players.forEach(AlgorithmsManager::cleanPlayer);
    }

    public void stopPlayer() {
        try {
            if (this.algorithm != null) {
                this.algorithm.interrupt();
                this.algorithm.join();
            }
            this.interrupt();
            this.join();
        } catch (InterruptedException e) {
        }
    }

    public void runAlgorithm(AlgorithmCommand command, String... args)
            throws NoSuchAlgorithmException, InvalidArgsException, InstantiationException, IllegalAccessException,
            StopSortException {
        Algorithms<?> algorithm = AlgorithmsManager.getAlgorithm(command.getType());
        if (algorithm == null)
            throw new NoSuchAlgorithmException(command.getType());
        this.algorithm = algorithm;
        algorithm.setArray(this.array);
        algorithm.setDelay(command.getDelay());
        algorithm.setPlayers(command.getPlayers());
        algorithm.setName(Arrays.toString(command.getPlayers().toArray()));
        algorithm.setArgs(args);
        algorithm.run();
    }

    @SuppressWarnings("unchecked")
    private static TabCompleter getFinalTabCompleter() {
        // /sort <player> <type> <delay> <length> args...
        return (sender, command, alias, args) -> {
            if (args.length <= 4)
                return new SortTab().onTabComplete(sender, command, alias, args);
            else {
                for (Algorithms<?> algorithm : AlgorithmsManager.getAlgorithms()) {
                    String name = Algorithms.getAlgorithmName((Class<? extends Algorithms<?>>) algorithm.getClass());
                    try {
                        if (name.equalsIgnoreCase(args[1]))
                            return algorithm.getTabCompleter().onTabComplete(sender, command, alias, args);
                    } catch (IllegalArgumentException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
    }

    public static void setTabCompleterToCommand(PluginCommand command) {
        command.setTabCompleter(SortPlayer.getFinalTabCompleter());
    }

    private void alert(String message, ChatColor color) {
        this.getPlayers().forEach((p) -> {
            p.spigot().sendMessage(
                    ChatMessageType.ACTION_BAR,
                    new ComponentBuilder(message)
                            .color(color)
                            .bold(true)
                            .create());
        });
        return;
    }

    private void alert(String message) {
        alert(message, ChatColor.RED);
    }
}
