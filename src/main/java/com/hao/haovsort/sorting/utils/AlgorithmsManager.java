package com.hao.haovsort.sorting.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.stream.Collectors;

import com.hao.haovsort.tabcompleters.SortTab;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class AlgorithmsManager {

    private static HashMap<String, Algorithms> map = new HashMap<>();
    private static ConcurrentHashMap<Player, SortPlayer> players = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public static void init() throws Exception {
        AlgorithmsLoader loader = new AlgorithmsLoader();
        List<Class<?>> classes = loader.getAllAlgorithmsClasses();
        classes.forEach((t) -> {
            try {
                putAlgorithms((Class<? extends Algorithms>) t);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException | NoSuchMethodException | SecurityException e) {
                e.printStackTrace();
            }
        });
        AlgorithmsManager.stopAll();
    }

    /**
     * เมื่อไม่เจอจะส่งกลับ null
     * 
     * @param name
     * @return Algorithms
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static Algorithms getAlgorithm(String name)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        Algorithms algorithm = map.get(name);
        return algorithm == null ? null : algorithm.newAlgorithm();
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
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * 
     * @throws InstantiationException
     * @throws SecurityException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * 
     */
    private static void putAlgorithms(Class<? extends Algorithms> algorithm)
            throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
            NoSuchMethodException, SecurityException {
        String name = Algorithms.getAlgorithmName(algorithm).toLowerCase();
        Bukkit.getLogger().log(Level.INFO, "Algorithm loaded : {0}", name);
        map.put(name, algorithm.getDeclaredConstructor().newInstance());
    }

    public static List<? extends Algorithms> getAlgorithms() {
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

    public static void stopPlayer(Player owner) throws NullPointerException {
        if (!players.containsKey(owner)) {
            throw new NullPointerException("No sorting player was found");
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

    static TabCompleter getFinalTabCompleter() {
        // /sort <player> <type> <delay> <length> args...
        return (sender, command, alias, args) -> {
            List<String> r = new SortTab().onTabComplete(sender, command, alias, args);
            if (r != null && !r.isEmpty())
                return r;
            if (args.length > 4) {
                for (Algorithms algorithm : map.values()) {
                    String name = Algorithms.getAlgorithmName((Class<? extends Algorithms>) algorithm.getClass());
                    try {
                        if (name.equalsIgnoreCase(args[1])) {
                            // recycle r variable
                            r = algorithm.getTabCompleter().onTabComplete(sender, command, alias,
                                    Arrays.copyOfRange(args, 4, args.length));
                            if (r != null)
                                return r;
                        }
                    } catch (IllegalArgumentException | SecurityException e) {
                        e.printStackTrace();
                    }
                }
            }
            return Collections.emptyList();
        };
    }

    public static void setTabCompleterToCommand(PluginCommand command) {
        command.setTabCompleter(getFinalTabCompleter());
    }

    public static List<String> getAlgorithmNames(String startWith) {
        return map.values().stream().map(t -> {
            try {
                Class<? extends Algorithms> clazz = (Class<? extends Algorithms>) t.getClass();
                return Algorithms.getAlgorithmName(clazz).toLowerCase();
            } catch (IllegalArgumentException | SecurityException e) {
                return null;
            }
        })
                .filter((t) -> t.startsWith(startWith.toLowerCase()))
                .collect(Collectors.toList());
    }

    public static List<String> getAlgorithmsName() {
        return getAlgorithmNames("");
    }
}
