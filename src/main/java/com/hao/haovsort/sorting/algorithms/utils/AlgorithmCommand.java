package com.hao.haovsort.sorting.algorithms.utils;

import org.bukkit.entity.Player;

public class AlgorithmCommand {
    private String type = null;
    private Long delay = 1l;
    private Player[] players = null;

    public AlgorithmCommand(String type, Long delay, Player[] players) {
        this.type = type;
        this.delay = delay;
        this.players = players;
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
