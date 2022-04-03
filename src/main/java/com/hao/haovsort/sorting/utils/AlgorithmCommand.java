package com.hao.haovsort.sorting.utils;

import java.util.List;

import org.bukkit.entity.Player;

public class AlgorithmCommand {
    private String type = null;
    private Long delay = 1l;
    private List<Player> players = null;
    private String[] args = null;

    public AlgorithmCommand(String type, Long delay, List<Player> players, String... args) {
        this.type = type;
        this.delay = delay;
        this.players = players;
        this.setArgs(args);
    }

    public AlgorithmCommand(String type, Long delay, List<Player> players) {
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

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
