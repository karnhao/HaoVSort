package com.hao.haovsort.sorting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hao.haovsort.sorting.algorithms.Selection;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmCommandCollecter;
import com.hao.haovsort.sorting.algorithms.utils.Algorithms;
import com.hao.haovsort.sorting.algorithms.utils.NoSuchAlgorithmException;

import org.bukkit.entity.Player;

/**
 * เครื่องแสดง algorithms
 */
final public class SortPlayer {
    private Player players[];
    private List<Integer> array = new ArrayList<>();
    private static Map<String, Algorithms<?>> map = new HashMap<>();
    private AlgorithmCommandCollecter[] commands;

    public static void init() throws Exception {
        SortPlayer.putAlgorithms(new Selection());
    }

    public void setCommands(AlgorithmCommandCollecter... acc) {
        this.commands = acc;
    }

    public AlgorithmCommandCollecter[] getAlgorithmCommandCollecter() {
        return this.commands;
    }

    /**
     * <p>
     * เพิ่ม algorithm ใน SortPlayer โดยจะสร้าง object จาก class
     * ที่ใส่เข้ามาแล้วเรียกใช้งาน putAlgorithms พร้อมให้ object จาก class นี้เป็น
     * parameter
     * </p>
     * 
     * <p>
     * จะแตกต่างจาก putAlgorithms คือเป็นการนำเข้า class แทนที่จะเป็น object
     * </p>
     */
    public static void register(Class<? extends Algorithms<?>> algorithm)
            throws InstantiationException, IllegalAccessException {
        SortPlayer.putAlgorithms(algorithm.newInstance());
    }

    /**
     * <p>
     * เพิ่ม algorithm ใน SortPlayer
     * <hr>
     * <p>
     * ตัวอย่าง:
     * 
     * <pre>
     * SortPlayer.putAlgorithms(new Selection());
     * </pre>
     * 
     * ตัวอย่าง: {@code SortPlayer.putAlgorithms(new Selection());}
     */
    public static void putAlgorithms(Algorithms<?> algorithm) {
        SortPlayer.map.put(algorithm.getName(), algorithm.login());
    }

    public void setPlayers(Player... players) {
        this.players = players;
    }

    public Player[] getPlayers() {
        return this.players;
    }

    public void start() {
        getAlgorithm("selection").interrupt();
    }

    public void runAlgorithm(String name, String... args) throws NoSuchAlgorithmException {
        Algorithms<?> algorithm = SortPlayer.getAlgorithm(name);
        if (algorithm == null)
            throw new NoSuchAlgorithmException(name);
        algorithm.setArray(this.array);
        algorithm.run();
    }

    private static Algorithms<?> getAlgorithm(String name) {
        return SortPlayer.map.get(name);
    }
}
