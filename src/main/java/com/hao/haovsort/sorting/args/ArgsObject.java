package com.hao.haovsort.sorting.args;

public class ArgsObject {

    public enum ArgType {
        STRING, INTEGER, FLOAT, BOOLEAN
    }

    private int index;
    private String name;
    private ArgsFilter filter;

    public ArgsObject(int index, String name, ArgsFilter filter) {
        this.index = index;
        this.name = name;
        this.filter = filter;
    }

    public ArgsFilter getFilter() {
        return filter;
    }

    public void setFilter(ArgsFilter filter) {
        this.filter = filter;
    }

    public int getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }
}
