package com.hao.haovsort.sorting.algorithms.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.args.InvalidArgsException;

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

    protected static String name;
    protected List<Integer> array;
    protected List<Player> player;
    private String[] args;
    private ChatColor indexColor = ChatColor.BLACK;
    private List<Integer> selectedIndex;
    private List<Float> pitch;
    private Long delay;

    @Override
    public abstract void sort(Integer[] a) throws InterruptedException;

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    protected void setIndexColor(ChatColor color){
        this.indexColor = color;
    }

    protected ChatColor getIndexColor() {
        return this.indexColor;
    }

    @SuppressWarnings("unchecked")
    public T login() {
        AlgorithmsInitialize init = new AlgorithmsInitialize();
        this.init(init);
        T.name = init.getName();
        return (T) this;
    }

    protected List<Integer> getArray() {
        return this.array;
    }

    protected String[] getArgs() {
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
        for (int i = 0; i < this.getArray().size(); i++) {
            if (this.getIndex().contains(i)) {
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

    public static <T extends Algorithms<T>> String getAlgorithmName() {
        return T.name;
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

    public abstract void init(AlgorithmsInitialize init);

    private void playSortingSound() {
        if (this.pitch == null) {
            this.pitch = this.selectedIndex.stream().map((t) -> this.pitchCal(t)).collect(Collectors.toList());
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

    protected float pitchCal(Float min, Float max, int n) {
        return min + (((max - min) / this.getArray().size()) * n);
    }

    protected float pitchCal(int n) {
        return pitchCal(0.5f, 2.0f, n);
    }

    protected void setIndex(Integer... index) {
        this.selectedIndex = (index == null) ? new ArrayList<Integer>() : Arrays.asList(index);
    }

    protected List<Integer> getIndex(){
        return this.selectedIndex;
    }

    protected void setPitch(Float... pitch) {
        this.pitch = Arrays.asList(pitch);
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
     * @return List ข้อความที่จะถูกแนะนำมาตอนพิมพ์คำสั่ง
     */
    protected abstract List<String> onTabComplete(CommandSender sender, String[] args);

    protected void argsFilter(String[] args) throws InvalidArgsException {
        // NO Filter.
    };

    public TabCompleter getTabCompleter() {
        return (CommandSender sender, Command command, String alias, String[] args1)
                -> Algorithms.this.onTabComplete(sender, (String[]) new ArrayList<>(Arrays.asList(args1)).subList(3, args1.length).toArray());
    }
}
