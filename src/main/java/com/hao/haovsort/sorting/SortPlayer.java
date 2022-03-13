package com.hao.haovsort.sorting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommand;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommandCollecter;
import com.hao.haovsort.sorting.algorithms.utils.Algorithms;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.algorithms.utils.NoSuchAlgorithmException;
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
    private String[] args;

    private List<Player> players;
    private List<Integer> array = new ArrayList<>();
    private AlgorithmCommandCollecter[] commands;

    public void setCommands(AlgorithmCommandCollecter... acc) {
        this.commands = acc;
    }

    public AlgorithmCommandCollecter[] getAlgorithmCommandCollecter() {
        return this.commands;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public void run() {
        Arrays.asList(getAlgorithmCommandCollecter()).forEach((acc) -> {
            this.array = Arrays.asList(acc.getArray());
            acc.getCommandList().forEach((command) -> {
                try {
                    this.algorithm = runAlgorithm(command, args);
                } catch (NoSuchAlgorithmException e) {
                    this.getPlayers().forEach((p) -> {
                        p.spigot().sendMessage(
                                ChatMessageType.ACTION_BAR,
                                new ComponentBuilder("Sort command not found")
                                        .color(ChatColor.RED)
                                        .bold(true)
                                        .create());
                    });
                }
            });
        });
    }

    @Override
    public void interrupt() {
        this.algorithm.interrupt();
    }

    public void setArgs(String[] args){
        this.args = args;
    }

    public String[] getArgs(){
        return this.args;
    }

    public Algorithms<?> runAlgorithm(AlgorithmCommand command, String... args) throws NoSuchAlgorithmException {
        Algorithms<?> algorithm = AlgorithmsManager.getAlgorithm(command.getType());
        if (algorithm == null)
            throw new NoSuchAlgorithmException(command.getType());
        algorithm.setArray(this.array);
        algorithm.setDelay(command.getDelay());
        algorithm.setPlayer(Arrays.asList(command.getPlayers()));
        algorithm.setName(Arrays.toString(command.getPlayers()));
        algorithm.run();
        return algorithm;
    }

    private static TabCompleter getFinalTabCompleter() {
        // /sort <player> <type> <delay> <length> args...
        return (sender, command, alias, args) -> {
            if (args.length <= 4)
                return new SortTab().onTabComplete(sender, command, alias, args);
            else {
                for (Algorithms<?> algorithm : AlgorithmsManager.getAlgorithms()) {
                    if (algorithm.getName().equalsIgnoreCase(args[1]))
                        return algorithm.getTabCompleter().onTabComplete(sender, command, alias, args);
                }
                return null;
            }
        };
    }

    public static void setTabCompleterToCommand(PluginCommand command) {
        command.setTabCompleter(SortPlayer.getFinalTabCompleter());
    }
}
