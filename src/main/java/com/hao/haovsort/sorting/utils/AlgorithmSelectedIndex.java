package com.hao.haovsort.sorting.utils;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AlgorithmSelectedIndex {

    private int lifeTime = 0;
    private int lifeTimeCount = 0;
    private Color color = Color.BLACK;
    private int value;

    public AlgorithmSelectedIndex(int value) {
        this.value = value;
    }

    public AlgorithmSelectedIndex(int value, int lifeTime) {
        this.value = value;
        this.lifeTime = lifeTime;
    }

    public AlgorithmSelectedIndex(int value, Color color) {
        this.value = value;
        this.color = color;
    }

    public Color getIndexColor() {
        return this.color;
    }

    public void setIndexColor(Color indexColor) {
        this.color = indexColor;
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

    public void addLifeTimeCount(int count) {
        this.lifeTimeCount += count;
    }

    /**
     * add lifeTimeCount up 1 value
     */
    public void addLifeTimeCount() {
        this.lifeTimeCount++;
    }

    public int getLifeTimeCount() {
        return this.lifeTimeCount;
    }

    public float getWeight() {
        if (lifeTime < 1) {
            return 1;
        }
        return 1 - ((float) this.lifeTimeCount / this.lifeTime);
    }

    public static List<AlgorithmSelectedIndex> asList(Integer... a) {
        return Arrays.asList(a).stream().map(AlgorithmSelectedIndex::new).collect(Collectors.toList());
    }
}
