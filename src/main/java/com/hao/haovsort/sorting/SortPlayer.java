package com.hao.haovsort.sorting;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.sorting.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.utils.AlgorithmCommandCollector;
import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.NoSuchAlgorithmException;
import com.hao.haovsort.sorting.utils.StopSortException;
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
        this.players.forEach(AlgorithmsManager::cleanPlayer);
        this.alert("Preparing...", ChatColor.AQUA);
        // try {
        //     sleep(100l);
        // } catch (InterruptedException e1) {
        //     throw new StopSortException();
        // }
        AlgorithmsManager.addPlayer(this.getOwner(), this);
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
                                throw new RuntimeException();
                            } catch (StopSortException stop) {
                                this.alert("Stop visualize");
                                throw new RuntimeException();
                            } catch (Exception ex) {
                                this.alert(ex.getMessage());
                                ex.printStackTrace();
                                throw new RuntimeException();
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
        this.algorithm.interrupt();
        this.interrupt();
        try {
            this.algorithm.join();
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
        algorithm.setPlayer(Arrays.asList(command.getPlayers()));
        algorithm.setName(Arrays.toString(command.getPlayers()));
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
