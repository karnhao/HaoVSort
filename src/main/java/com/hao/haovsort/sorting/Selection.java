package com.hao.haovsort.sorting;

import com.hao.haovsort.commands.ArgsManager;

public class Selection extends Sorting {


    @Override
    public void sort(Integer[] a) {
        
    }

    @Override
    protected void commandArgs(ArgsManager c) {
        c.setArgs(0, Integer.class, "length");
        
    }
}
