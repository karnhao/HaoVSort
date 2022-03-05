package com.hao.haovsort.sorting;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.hao.haovsort.sorting.algorithms.Algorithms;

import org.bukkit.entity.Player;

public class SortPlayer {
    private Player player[];
    private static Map<String, Algorithms> map = new HashMap<>();

    public static void putAlgorithms(Class<? extends Algorithms> clazz) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        SortPlayer.map.put((String) clazz.getMethod("getName").invoke(null),
                (Algorithms) clazz.getMethod("login").invoke(null, clazz));
    }

    public void setPlayer(Player... player) {
        this.player = player;
    }

    public Player[] getPlayer() {
        return this.player;
    }

    public void sort(String name, String[] args) {

    }
}
