package com.hao.haovsort.sorting.args;

/**
 * This class extend from {@link RuntimeException}
 */
public class InvalidArgsException extends RuntimeException {
    public InvalidArgsException(String reason) {
        super(reason);
    }
}
