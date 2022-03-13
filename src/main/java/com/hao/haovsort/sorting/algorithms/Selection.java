package com.hao.haovsort.sorting.algorithms;

import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.algorithms.utils.Algorithms;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsInitialize;

import org.bukkit.command.CommandSender;

public class Selection extends Algorithms<Selection> {

    public static final String NAME = "selection";

    @Override
    public void sort(Integer[] a) throws InterruptedException {
        getPlayers().forEach((t) -> {
            t.sendMessage(this.getArgs()[0]);
        });
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
    public void init(AlgorithmsInitialize init) {
        init.setName(NAME);
    }

    @Override
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return Arrays.asList("Hello", "World");
    }
}
