package com.hao.haovsort.sorting.utils;

import java.awt.Color;
import java.util.Collection;
import java.util.LinkedList;

public class AlgorithmSelectedIndexCollector {

    public LinkedList<AlgorithmSelectedIndex> indexes = new LinkedList<>();
    private Color defaultColor = Color.BLACK;

    public void addInt(int value) {
        this.addIndex(value, defaultColor, 0);
    }

    public void addIndex(int value, Color color, int fadedTime) {
        AlgorithmSelectedIndex index;
        if (!this.has(value)) {
            index = new AlgorithmSelectedIndex(value);
        } else
            index = this.indexes.get(value);
        index.setIndexColor(color);
        index.setFadedTime(fadedTime);
        this.indexes.add(index);
    }

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
    }

    public Color getDefaultColor() {
        return this.defaultColor;
    }

    public boolean has(int value) {
        for (AlgorithmSelectedIndex i : this.indexes) {
            if (i.getValue() == value) {
                return true;
            }
        }
        return false;
    }

    /**
     * @return AlgorithmSelectedIndex or null.
     */
    public AlgorithmSelectedIndex getFromValue(int value) {
        for (AlgorithmSelectedIndex i : this.indexes) {
            if (i.getValue() == value) {
                return i;
            }
        }
        return null;
    }

    public void removeAllExpired() {
        for (short i = 0; i < this.indexes.size(); i++) {
            AlgorithmSelectedIndex si = this.indexes.get(i);
            if (si.getLifeTimeCount() >= si.getLifeTime()) {
                this.indexes.remove(i);
                i--;
            }
        }
    }

    public void addAll(Collection<? extends AlgorithmSelectedIndex> c) {
        c.forEach((t) -> {
            t.setIndexColor(defaultColor);
            for (short i = 0; i < this.indexes.size(); i++) {
                if (t.getValue() == this.indexes.get(i).getValue()) {
                    this.indexes.remove(i);
                }
            }
        });

        this.indexes.addAll(c);

    }

    public LinkedList<AlgorithmSelectedIndex> getIndexes() {
        return this.indexes;
    }

    public void clear() {
        this.indexes.clear();
    }

}
