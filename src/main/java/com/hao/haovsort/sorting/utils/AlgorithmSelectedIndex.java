package com.hao.haovsort.sorting.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmSelectedIndex {
    private int lifeTime = 0;
    private Color color = Color.BLACK;
    private int value;

    AlgorithmSelectedIndex(int value) {
        this.value = value;
    };

    public Color getIndexColor() {
        return this.color;
    }

    public void setFadedTime(int tick) {
        this.lifeTime = tick;
    }

    public int getLifeTime() {
        return this.lifeTime;
    }

    public void pass() {
        if (lifeTime > 0)
            lifeTime -= 1;
    }

    public int getValue() {
        return this.value;
    }

    public static List<AlgorithmSelectedIndex> asList(Integer... a) {
        return Arrays.asList(a).stream().map(AlgorithmSelectedIndex::new).collect(Collectors.toList());
    }
}
