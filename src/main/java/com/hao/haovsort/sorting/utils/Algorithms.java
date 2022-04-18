package com.hao.haovsort.sorting.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.args.InvalidArgsException;
import com.hao.haovsort.utils.Configuration;

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

    protected Integer[] array;
    private List<Player> players;
    private String[] args = {};
    private ChatColor indexColor = ChatColor.BLACK;
    private LinkedList<Integer> selectedIndexes = new LinkedList<>();
    private LinkedList<Float> pitchs = new LinkedList<>();
    private Long delay;

    public abstract void sort(Integer[] a) throws InterruptedException;

    final public Long getDelay() {
        return delay;
    }

    final public void setDelay(Long delay) {
        this.delay = delay;
    }

    final protected void setIndexColor(ChatColor color) {
        this.indexColor = color;
    }

    final protected ChatColor getIndexColor() {
        return this.indexColor;
    }

    final protected Integer[] getArray() {
        return this.array;
    }

    final protected String[] getArgs() {
        return this.args;
    }

    final public void setArgs(String... args) throws InvalidArgsException {
        if (args == null)
            return;
        this.args = args;
    }

    /**
     * ใช้ใน {@link #sort(Integer[])} ไว้แสดงผล
     *
     * @throws InterruptedException
     */
    final protected void show() throws InterruptedException {
        if (this.isInterrupted())
            throw new InterruptedException();
        Color color;
        ComponentBuilder cb = new ComponentBuilder();
        for (int i = 0; i < this.getArray().length; i++) {
            if (this.getIndexes().contains(i)) {
                cb.append("|").color(indexColor).bold(true);
            } else {
                color = this.getArray()[i] != -1 ? Color.getHSBColor(this.colorCal(this.getArray()[i]), 1.0f, 1.0f)
                        : Color.WHITE;
                cb.append("|").color(ChatColor.of(color)).bold(true);
            }
        }
        if (this.getArray().length > Configuration.getMaxActionBarArrayLength()) {
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

    final public static String getAlgorithmName(Class<? extends Algorithms<?>> clazz) {
        try {
            return (String) clazz.getField("NAME").get(null);
        } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
            return clazz.getSimpleName();
        }
    }

    final public List<Player> getPlayers() {
        return this.players;
    }

    final public void setArray(Integer[] a) {
        this.array = a;
    }

    final public void setPlayers(List<Player> player) {
        this.players = player;
    }

    /**
     * Method นี้จะถูกเรียกใช้งานหลังจาก {@code argsFilter()}
     * และก่อนที่จะเริ่มการจัดเรียง {@code sort()}
     */
    public void init() {
    };

    final private void playSortingSound() {
        if (this.pitchs.isEmpty()) {
            this.pitchs = new LinkedList<>(
                    this.selectedIndexes.stream().map(this::pitchCal).collect(Collectors.toList()));
        }
        pitchs.stream().filter((t) -> t != 0).forEach((p) -> {
            players.forEach(player -> player.playSound(player.getLocation(), Configuration.getSortingSoundName(),
                    SoundCategory.MASTER, Configuration.getSoundValue(), p));
        });
    }

    final private float colorCal(int value) {
        return new Integer(value).floatValue() / new Integer(this.getArray().length).floatValue();
    }

    final protected float pitchCal(Float min, Float max, int n) {
        return min + (((max - min) / this.getArray().length) * n);
    }

    final protected float pitchCal(int n) {
        return pitchCal(0.5f, 2.0f, n);
    }

    final protected LinkedList<Float> pitchCal(int... n) {
        return new LinkedList<>(Arrays.stream(n).boxed().map(this::pitchCal).collect(Collectors.toList()));
    }

    final protected void setIndexes(Integer... indexes) {
        this.selectedIndexes = (indexes == null) ? new LinkedList<Integer>()
                : new LinkedList<Integer>(Arrays.asList(indexes));
    }

    final protected void setIndexes(LinkedList<Integer> indexes) {
        this.selectedIndexes = indexes;
    }

    final protected List<Integer> getIndexes() {
        return this.selectedIndexes;
    }

    final protected void setPitchs(Float... pitchs) {
        this.pitchs = (pitchs == null) ? new LinkedList<Float>() : new LinkedList<Float>(Arrays.asList(pitchs));
    }

    final protected void setPitchs(LinkedList<Float> pitchs) {
        this.pitchs = pitchs;
    }

    /**
     * Scale ความถี่ของคลื่นเสียงที่จะเล่นเมื่อใช้
     * 
     * <pre>
     * this.show()
     * </pre>
     * 
     * @return List<Float> pitchs
     */
    final protected List<Float> getPitchs() {
        return this.pitchs;
    }

    @Override
    final public void run() {
        try {
            this.argsFilter(args);
            this.init();
            this.sort(this.array);
        } catch (InterruptedException e) {
            throw new StopSortException();
        }
    }

    @Override
    final public void interrupt() {
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
        return (CommandSender sender, Command command, String alias, String[] args) -> Algorithms.this.onTabComplete(
                sender, args);
    }

    /**
     * สร้าง Thread ของ Algorithm อันใหม่ที่พร้อมใช้งาน
     * 
     * @return Algorithm Thread object
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    @SuppressWarnings("unchecked")
    final protected Algorithms<T> newAlgorithm() throws InstantiationException, IllegalAccessException {
        return this.getClass().newInstance();
    }

}
