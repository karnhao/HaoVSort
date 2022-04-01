package com.hao.haovsort.sorting.algorithms.utils;

import org.bukkit.entity.Player;

public class AlgorithmCommand {
    private String type = null;
    private Long delay = 1l;
    private Player[] players = null;
    private String[] args = null;

    public AlgorithmCommand(String type, Long delay, Player[] players, String... args) {
        this.type = type;
        this.delay = delay;
        this.players = players;
        this.setArgs(args);
    }

    public AlgorithmCommand(String type, Long delay, Player[] players) {
        this(type, delay, players, (String[]) null);
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String... args) {
        if (args != null)
            this.args = args;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }
}
