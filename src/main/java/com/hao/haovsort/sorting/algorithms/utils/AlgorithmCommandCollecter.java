package com.hao.haovsort.sorting.algorithms.utils;

import java.util.ArrayList;
import java.util.Arrays;

public class AlgorithmCommandCollecter {
    private ArrayList<AlgorithmCommand> commandList = new ArrayList<>();

    private Integer[] array;

    public AlgorithmCommandCollecter(Integer[] array, AlgorithmCommand... acs) {
        this.array = array;
        this.commandList = (ArrayList<AlgorithmCommand>) Arrays.asList(acs);
    }

    public void setCommandList(ArrayList<AlgorithmCommand> commandList) {
        this.commandList = commandList;
    }

    public Integer[] getArray() {
        return array;
    }

    public void setArray(Integer[] array) {
        this.array = array;
    }

    public ArrayList<AlgorithmCommand> getCommandList() {
        return commandList;
    }
}
