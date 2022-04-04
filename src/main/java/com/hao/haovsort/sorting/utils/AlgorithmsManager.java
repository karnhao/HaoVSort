package com.hao.haovsort.sorting.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.hao.haovsort.Main;
import com.hao.haovsort.tabcompleter.SortTab;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AlgorithmsManager {

    private static HashMap<String, Algorithms<?>> map = new HashMap<>();
    private static ConcurrentHashMap<Player, SortPlayer> players = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static void init() throws Exception {
        AlgorithmsLoader loader = new AlgorithmsLoader();
        List<Class<?>> classes = loader.getAllAlgorithmsClasses();
        classes.forEach((t) -> {
            try {
                putAlgorithms((Class<? extends Algorithms<?>>) t);
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        AlgorithmsManager.stopAll();
    }

    public static Algorithms<? extends Algorithms<?>> getAlgorithm(String name)
            throws InstantiationException, IllegalAccessException {
        return map.get(name).newAlgorithm();
    }

    /**
     * <p>
     * เพิ่ม algorithm ใน SortPlayer
     * <hr>
     * <p>
     * ตัวอย่าง:
     * 
     * <pre>
     * SortPlayer.AlgorithmsManager(Selection.class);
     * </pre>
     * 
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * 
     */
    private static void putAlgorithms(Class<? extends Algorithms<?>> algorithm)
            throws InstantiationException, IllegalAccessException {
        String name = Algorithms.getAlgorithmName(algorithm).toLowerCase();
        Bukkit.getLogger().log(Level.INFO, "{0} Loaded {1}", new Object[] { Main.getPrefix(), name });
        map.put(name, algorithm.newInstance());
    }

    public static List<? extends Algorithms<?>> getAlgorithms() {
        return map.values().stream().collect(Collectors.toList());
    }

    public static void addPlayer(Player owner, SortPlayer player) {
        players.put(owner, player);
    }

    public static Map<Player, SortPlayer> getPlayers() {
        return players;
    }

    public static SortPlayer getPlayer(Player owner) {
        return players.get(owner);
    }

    public static void stopPlayer(Player owner) throws Exception {
        if (!players.containsKey(owner)) {
            throw new Exception("No sorting player was found");
        }
        players.get(owner).stopPlayer();
        players.remove(owner);
    }

    public static void cleanPlayer(Player owner) {
        try {
            stopPlayer(owner);
        } catch (Exception e) {
        }
    }

    public static void stopAll() {
        AlgorithmsManager.players.values().forEach((t) -> {
            t.stopPlayer();
        });
    }

    @SuppressWarnings("unchecked")
    static TabCompleter getFinalTabCompleter() {
        // /sort <player> <type> <delay> <length> args...
        return (sender, command, alias, args) -> {
            if (args.length <= 4)
                return new SortTab().onTabComplete(sender, command, alias, args);
            else {
                for (Algorithms<?> algorithm : getAlgorithms()) {
                    String name = Algorithms.getAlgorithmName((Class<? extends Algorithms<?>>) algorithm.getClass());
                    try {
                        if (name.equalsIgnoreCase(args[1])) {
                            return algorithm.getTabCompleter().onTabComplete(sender, command, alias,
                                    Arrays.copyOfRange(args, 4, args.length));
                        }
                    } catch (IllegalArgumentException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            }
        };
    }

    public static void setTabCompleterToCommand(PluginCommand command) {
        command.setTabCompleter(getFinalTabCompleter());
    }
}
