package com.hao.haovsort.sorting.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.ArgsManager;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.awt.Color;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public abstract class Algorithms implements AlgorithmsFace {
    protected ArgsManager args = new ArgsManager();
    protected String name;
    protected List<Integer> list = new ArrayList<>();
    protected List<Player> player;
    private ChatColor indexColor;
    private Integer[] selectedIndex;
    private Float[] pitch;

    public abstract void sort(Integer[] a);

    public static <T extends Algorithms> T login(Class<T> clazz) throws InstantiationException, IllegalAccessException {
        T algorithm = clazz.newInstance();
        AlgorithmsInitialize init = new AlgorithmsInitialize();
        algorithm.init(init);
        algorithm.commandArgs(algorithm.args);
        algorithm.name = init.getName();
        return algorithm;
    }

    protected List<Integer> getArray() {
        return this.list;
    }

    protected ArgsManager getArgs() {
        return this.args;
    }

    /**
     * ใช้ใน {@link #sort(Integer[])}
     * ไว้แสดงผล
     */
    protected void show() {
        Color color;
        ComponentBuilder cb = new ComponentBuilder();
        ArrayList<Integer> sI = new ArrayList<>();
        for (int j = 0; j <= selectedIndex.length - 1; j++) {
            sI.add(selectedIndex[j]);
        }
        for (int i = 0; i < this.getArray().size(); i++) {
            if (sI.contains(i)) {
                cb.append("|").color(indexColor).bold(true);
            } else {
                color = Color.getHSBColor(this.colorCal(this.getArray().get(i)), 1.0f, 1.0f);
                cb.append("|").color(ChatColor.of(color)).bold(true);
            }
        }
        if (this.getArray().size() >= 300) {
            player.forEach((t) -> {
                t.sendMessage("");

                t.spigot().sendMessage(ChatMessageType.CHAT, cb.create());
            });
        } else {
            player.forEach((t) -> {
                t.spigot().sendMessage(ChatMessageType.ACTION_BAR, cb.create());
            });
        }
        playSortingSound();
    }

    public String getName() {
        return this.name;
    }

    public List<Player> getPlayers() {
        return this.player;
    }

    public void setArray(List<Integer> a) {
        this.list = new ArrayList<>(a);
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }

    protected abstract void commandArgs(ArgsManager c);

    public abstract void init(AlgorithmsInitialize init);

    private void playSortingSound() {
        if(this.pitch == null) this.pitch = (Float[]) Arrays.asList(this.selectedIndex).stream().map(Integer::floatValue)
                .map((t) -> this.pitchCal(t)).toArray();
        for (Float u : pitch) {
            if (u == 0)
                continue;
            player.forEach((t) -> {
                t.playSound(t.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 100, u);
            });
        }
    }

    private float colorCal(int value) {
        return new Integer(value).floatValue() / new Integer(this.getArray().size()).floatValue();
    }

    private float pitchCal(Float min, Float max, Float n) {
        return min + (((max - min) / this.getArray().size()) * n);
    }

    private float pitchCal(Float n) {
        return pitchCal(0.5f, 2.0f, n);
    }

    protected void setIndex(Integer... index) {
        this.selectedIndex = index;
    }

    protected void setPitch(Float... pitch) {
        this.pitch = pitch;
    }
}
