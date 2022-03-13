package com.hao.haovsort.sorting.args;

public class ArgsObject {

    private int index;
    private String name;
    private String value;

    public ArgsObject(int index, String name, String value) {
        this.index = index;
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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
