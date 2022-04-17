package com.hao.haovsort.sorting.utils;

public class StopSortException extends RuntimeException {
    public StopSortException() {
        super("Stop sort");
    }

    public StopSortException(String message) {
        super("Stop sort : " + message);
    }
}
