package com.hao.haovsort.sorting.algorithms.utils;

import org.bukkit.entity.Player;

public class AlgorithmCommand {
    private String command = null;
    private Long delay = 1l;
    private Player player = null;

    public AlgorithmCommand(String command, Long delay, Player player) {
        this.command = command;
        this.delay = delay;
        this.player = player;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
}
