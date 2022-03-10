package com.hao.haovsort.sorting.algorithms.utils;

public class NoSuchAlgorithmException extends Exception {
    private static String text_null = "Cannot find algorithm from the given name.";
    private static String text = "Cannot find %s algorithm.";

    public NoSuchAlgorithmException(String unknownAlgorithmString) {
        super((unknownAlgorithmString == null) ? text_null : String.format(text, unknownAlgorithmString));
    }

    public NoSuchAlgorithmException() {
        this(null);
    }
}
