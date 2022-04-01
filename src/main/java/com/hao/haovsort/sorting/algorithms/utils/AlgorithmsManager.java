package com.hao.haovsort.sorting.algorithms.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.SortPlayer;
import com.hao.haovsort.sorting.algorithms.Finish;
import com.hao.haovsort.sorting.algorithms.Random;
import com.hao.haovsort.sorting.algorithms.Selection;

import org.bukkit.entity.Player;

public class AlgorithmsManager {

    private static Map<String, Algorithms<?>> map = new HashMap<>();
    private static Map<String, Class<? extends Algorithms<?>>> mapClazz = new HashMap<>();
    private static Map<Player, SortPlayer> players = new HashMap<>();

    public static void init() throws Exception {
        putAlgorithms(Selection.class);
        putAlgorithms(Random.class);
        putAlgorithms(Finish.class);
    }

    public static Algorithms<?> getAlgorithm(String name) throws InstantiationException, IllegalAccessException {
        return mapClazz.get(name).newInstance().login();
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
        String name = Algorithms.getAlgorithmName(algorithm);
        map.put(name, algorithm.newInstance());
        mapClazz.put(name, algorithm);
    }

    public static List<? extends Algorithms<?>> getAlgorithms() {
        return map.values().stream().collect(Collectors.toList());
    }

    public static void addPlayer(Player owner, SortPlayer player) {
        players.put(owner, player);
    }

    public static SortPlayer getPlayer(Player owner) {
        return players.get(owner);
    }

    public static void stopPlayer(Player owner) throws Exception {
        if (!players.containsKey(owner))
            throw new Exception("No sorting player was found");
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
}
