package com.hao.haovsort.sorting.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
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

/**
 * Thread ที่จะแสดง Algorithm
 */
public abstract class Algorithms<T extends Algorithms<T>> extends Thread {

    protected static String name;
    protected Integer[] array;
    protected List<Player> players;
    private String[] args = {};
    private ChatColor indexColor = ChatColor.BLACK;
    private List<Integer> selectedIndexes = new LinkedList<>();
    private List<Float> pitchs = new LinkedList<>();
    private Long delay;

    public abstract void sort(Integer[] a) throws InterruptedException;

    public Long getDelay() {
        return delay;
    }

    public void setDelay(Long delay) {
        this.delay = delay;
    }

    protected void setIndexColor(ChatColor color) {
        this.indexColor = color;
    }

    protected ChatColor getIndexColor() {
        return this.indexColor;
    }

    protected Integer[] getArray() {
        return this.array;
    }

    protected String[] getArgs() {
        return this.args;
    }

    public void setArgs(String... args) throws InvalidArgsException {
        if (args == null)
            return;
        this.args = args;
    }

    /**
     * ใช้ใน {@link #sort(Integer[])} ไว้แสดงผล
     *
     * @throws InterruptedException
     */
    protected void show() throws InterruptedException {
        if (this.isInterrupted())
            throw new InterruptedException();
        Color color;
        ComponentBuilder cb = new ComponentBuilder();
        for (int i = 0; i < this.getArray().length; i++) {
            if (this.getIndexes().contains(i)) {
                cb.append("|").color(indexColor).bold(true);
            } else {
                color = Color.getHSBColor(this.colorCal(this.getArray()[i]), 1.0f, 1.0f);
                cb.append("|").color(ChatColor.of(color)).bold(true);
            }
        }
        if (this.getArray().length >= 300) {
            players.forEach((t) -> {
                t.sendMessage("");

                t.spigot().sendMessage(ChatMessageType.CHAT, cb.create());
            });
        } else {
            players.forEach((t) -> {
                t.spigot().sendMessage(ChatMessageType.ACTION_BAR, cb.create());
            });
        }
        playSortingSound();
        sleep(this.delay);
    }

    public static String getAlgorithmName(Class<? extends Algorithms<?>> clazz) {
        try {
            return (String) clazz.getField("NAME").get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return clazz.getSimpleName();
        }
    }

    public List<Player> getPlayers() {
        return this.players;
    }

    public void setArray(Integer[] a) {
        this.array = a;
    }

    public void setPlayers(List<Player> player) {
        this.players = player;
    }

    /**
     * Method นี้จะถูกเรียกใช้งานหลังจาก {@code argsFilter()} และก่อนที่จะเริ่มการจัดเรียง {@code sort()}
     */
    public void init() {
    };

    private void playSortingSound() {
        if (this.pitchs.isEmpty()) {
            this.pitchs = this.selectedIndexes.stream().map((t) -> this.pitchCal(t)).collect(Collectors.toList());
        }
        for (Float u : pitchs) {
            if (u == 0)
                continue;
            players.forEach((t) -> {
                t.playSound(t.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, SoundCategory.MASTER, 0.2f, u);
            });
        }
    }

    private float colorCal(int value) {
        return new Integer(value).floatValue() / new Integer(this.getArray().length).floatValue();
    }

    protected float pitchCal(Float min, Float max, int n) {
        return min + (((max - min) / this.getArray().length) * n);
    }

    protected float pitchCal(int n) {
        return pitchCal(0.5f, 2.0f, n);
    }

    protected List<Float> pitchCal(int... n) {
        return Arrays.stream(n).boxed().map(this::pitchCal).collect(Collectors.toList());
    }

    protected void setIndexes(Integer... indexes) {
        this.selectedIndexes = (indexes == null) ? new LinkedList<Integer>()
                : new LinkedList<Integer>(Arrays.asList(indexes));
    }

    protected void setIndexes(List<Integer> indexes) {
        this.selectedIndexes = indexes;
    }

    protected List<Integer> getIndexes() {
        return this.selectedIndexes;
    }

    protected void setPitchs(Float... pitchs) {
        this.pitchs = (pitchs == null) ? new LinkedList<Float>() : new LinkedList<Float>(Arrays.asList(pitchs));
    }

    protected void setPitchs(List<Float> pitchs) {
        this.pitchs = pitchs;
    }

    /**
     * Scale ความถี่ของคลื่นเสียงที่จะเล่นเมื่อใช้
     * 
     * <pre>
     * this.show()
     * </pre>
     * @return List<Float> pitchs
     */
    protected List<Float> getPitchs() {
        return this.pitchs;
    }

    @Override
    public void run() {
        try {
            this.argsFilter(args);
            this.init();
            this.sort(this.array);
        } catch (InterruptedException e) {
            throw new StopSortException();
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
     * @param args   args[0] ในที่นี้ จะเริ่มที่ args[4] ของ command หลัก
     * @return List ข้อความที่จะถูกแนะนำมาตอนพิมพ์คำสั่ง
     */
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    };

    /**
     * ให้ throw InvalidArgsException เมื่อ Args ไม่ถูกต้อง
     * ข้อความที่แนบมาด้วยจะแจ้งเป็น error กับผู้เล่นที่ใช้คำสั่งนี้
     */
    protected void argsFilter(String[] args) throws InvalidArgsException {
    };

    /**
     * TabCompleter ของ Algorithm นั้นๆ
     */
    public TabCompleter getTabCompleter() {
        return (CommandSender sender, Command command, String alias, String[] args1) -> Algorithms.this.onTabComplete(
                sender,
                new ArrayList<>(Arrays.asList(args1)).subList(4, args1.length).toArray(new String[args1.length - 4]));
    }

    /**
     * สร้าง Thread ของ Algorithm ที่พร้อมใช้งาน
     * @return Algorithm Thread object
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    protected Algorithms<T> newAlgorithm() throws InstantiationException, IllegalAccessException {
        return this.getClass().newInstance();
    }

}
