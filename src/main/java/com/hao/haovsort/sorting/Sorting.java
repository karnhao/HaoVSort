package com.hao.haovsort.sorting;

import com.hao.haovsort.commands.ArgsManager;
import java.util.ArrayList;
import java.util.List;

public abstract class Sorting {

    protected List<Integer> list = new ArrayList<>();

    protected List<Integer> getArray() {
        return this.list;
    }

    protected abstract void commandArgs(ArgsManager c);

    public void setArray(List<Integer> a) {
        this.list = new ArrayList<>(a);
    }

    public abstract void sort(Integer[] a);
}
