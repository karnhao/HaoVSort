package com.hao.haovsort.sorting.algorithms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.hao.haovsort.sorting.args.ArgsManager;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.awt.Color;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

public abstract class Algorithms<T extends Algorithms<T>> extends Thread implements AlgorithmsFace {

    protected ArgsManager args = new ArgsManager();
    protected String name;
    protected List<Integer> array;
    protected List<Player> player;
    private ChatColor indexColor;
    private Integer[] selectedIndex;
    private Float[] pitch;
    private Long delay;

    @Override
    public abstract void sort(Integer[] a) throws InterruptedException;

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    @SuppressWarnings("unchecked")
    public T login() {
        AlgorithmsInitialize init = new AlgorithmsInitialize();
        this.init(init);
        this.commandArgs(this.args);
        this.name = init.getName();
        return (T) this;
    }

    protected List<Integer> getArray() {
        return this.array;
    }

    protected ArgsManager getArgs() {
        return this.args;
    }

    /**
     * ใช้ใน {@link #sort(Integer[])} ไว้แสดงผล
     *
     * @throws InterruptedException
     */
    protected void show() throws InterruptedException {
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

        sleep(this.delay);
    }

    public String getAlgorithmName() {
        return this.name;
    }

    public List<Player> getPlayers() {
        return this.player;
    }

    @Override
    public void setArray(List<Integer> a) {
        this.array = new ArrayList<>(a);
    }

    public void setPlayer(List<Player> player) {
        this.player = player;
    }

    protected abstract void commandArgs(ArgsManager c);

    public abstract void init(AlgorithmsInitialize init);

    private void playSortingSound() {
        if (this.pitch == null) {
            this.pitch = (Float[]) Arrays.asList(this.selectedIndex).stream().map(Integer::floatValue)
                    .map((t) -> this.pitchCal(t)).toArray();
        }
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

    @Override
    public void run() {
        try {
            this.sort((Integer[]) this.array.toArray());
        } catch (InterruptedException e) {
            this.interrupt();
        }
    }

    @Override
    public void interrupt() {
        super.interrupt();
    }

    /**
     * รูปแบบจะเป็นแบบนี้ :
     * <p>
     * {@code /sort  &lt;player&gt; &lt;type&gt; &lt;delay&gt; &lt;length&gt; args[0] args[1] args[2] ...}
     *
     * @param sender ผู้ที่ใช้คำสั่งนี้
     * @param args args[0] ในที่นี้ จะเริ่มที่ args[3] ของ command หลัก
     * @return ข้อความที่จะถูกแนะนำมาตอนพิมพ์คำสั่ง
     */
    protected abstract List<String> onTabComplete(CommandSender sender, String[] args);

    public TabCompleter getTabCompleter() {
        return (CommandSender sender, Command command, String alias, String[] args1)
                -> Algorithms.this.onTabComplete(sender, (String[]) new ArrayList<>(Arrays.asList(args1)).subList(3, args1.length).toArray());
    }
}
