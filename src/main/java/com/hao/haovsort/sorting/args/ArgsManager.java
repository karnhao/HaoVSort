package com.hao.haovsort.sorting.args;

import java.util.HashMap;
import java.util.Map;

public class ArgsManager {
    private Map<Integer, ArgsObject> args = new HashMap<>();

    /**
     * ตั้งค่า Args ที่ index ต่างๆ
     */
    public void setArgs(Integer index, String name, ArgsFilter filter) {
        args.put(index, new ArgsObject(index, name, filter));
    }

    public ArgsObject getArgs(Integer index) {
        return args.get(index);
    }
}
