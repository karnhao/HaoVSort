package com.hao.haovsort.sorting.algorithms.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.algorithms.Random;
import com.hao.haovsort.sorting.algorithms.Selection;

public class AlgorithmsManager {

    private static Map<String, Class<? extends Algorithms<?>>> map = new HashMap<>();

    public static void init() throws Exception {
        putAlgorithms(Selection.class);
        putAlgorithms(Random.class);
    }

    public static Algorithms<?> getAlgorithm(String name) {
        try {
            return map.get(name).newInstance().login();
        } catch (InstantiationException | IllegalAccessException e) {
            return null;
        }
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
     */
    private static void putAlgorithms(Class<? extends Algorithms<?>> algorithm)
            throws IllegalAccessException, IllegalArgumentException,
            InvocationTargetException, NoSuchMethodException, SecurityException {
        map.put((String) algorithm.getDeclaredMethod("getAlgorithmName").invoke(null, new Object[] { null }),
                algorithm);
    }

    public static List<Algorithms<?>> getAlgorithms() {
        return map.values().stream().map((t) -> {
            try {
                return t.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                return null;
            }
        }).collect(Collectors.toList());
    }
}
