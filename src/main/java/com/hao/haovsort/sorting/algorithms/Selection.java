package com.hao.haovsort.sorting.algorithms;

import java.util.Arrays;

import com.hao.haovsort.sorting.args.ArgsFilter;
import com.hao.haovsort.sorting.args.ArgsManager;
import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.utils.Util;

public class Selection extends Algorithms {

    public static final String NAME = "selection";

    @Override
    public void sort(Integer[] a) {
        int n = a.length;
        for (int i = 0; i < n - 1; i++) {
            int min_idx = i;
            for (int j = i + 1; j < n; j++) {
                if (j < n) {
                    // setPitch(soundPCal(0.5f, (float) a.length, a[j]));
                    setIndex(i, j, min_idx);
                    show();
                    if (a[j] < a[min_idx]) {
                        min_idx = j;
                    }
                }
            }
            int temp = a[min_idx];
            a[min_idx] = a[i];
            a[i] = temp;
        }
        show();
    }

    @Override
    protected void commandArgs(ArgsManager c) {
        c.setArgs(0, "length", new ArgsFilter() {
            @Override
            public String filter(String input) throws InvalidArgsException {
                if (!Util.isInteger(input))
                    throw new InvalidArgsException("Args isn't an integer.");
                int i = Integer.parseInt(input);
                if (i <= 0)
                    throw new InvalidArgsException("Length must not less than 1.");
                return input;
            }
        });
    }

    @Override
    public void init(AlgorithmsInitialize init) {
        init.setName(NAME);
    }
}
