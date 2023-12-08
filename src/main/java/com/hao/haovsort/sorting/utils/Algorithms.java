package com.hao.haovsort.sorting.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.HaoVSort;
import com.hao.haovsort.sorting.args.InvalidArgsException;

import org.bukkit.SoundCategory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.awt.Color;
import java.lang.reflect.InvocationTargetException;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;

/**
 * Thread ที่จะแสดง Algorithm
 */
@SortingAlgorithm(name = "")
public abstract class Algorithms extends Thread {

    protected Integer[] array;
    private List<Player> players;
    private String[] args = {};
    private AlgorithmSelectedIndexCollector selectedIndexes = new AlgorithmSelectedIndexCollector();
    private LinkedList<Float> pitchs = new LinkedList<>();
    private Long delay;
    private boolean end = false;

    /**
     * Make your own Algorithms here.
     * 
     * Use {@code this.show()} to show the visualization to the screen. This method will make a delay based on the arguments.
     * 
     * 
     * @param a array of integers.
     */
    public abstract void sort(Integer[] a);

    final public Long getDelay() {
        return delay;
    }

    /**
     * Set the new delay of this algorithm.
     * @param delay
     */
    final public void setDelay(Long delay) {
        this.delay = delay;
    }

    final protected void setIndexColor(Color color) {
        this.selectedIndexes.setDefaultColor(color);
    }

    final protected Color getIndexColor() {
        return this.selectedIndexes.getDefaultColor();
    }

    final protected Integer[] getArray() {
        return this.array;
    }

    final protected String[] getArgs() {
        return this.args;
    }

    final public void setArgs(String... args) {
        if (args == null)
            return;
        this.args = args;
    }

    /**
     * use this method in {@link #sort(Integer[])} to show the visualization.
     *
     * @throws StopSortException
     */
    final protected void show() {
        if (this.isStopped())
            throw new StopSortException();
        Color color;
        boolean action_bar = !(this.getArray().length > HaoVSort.getInstance().getConfiguration().getMaxActionBarArrayLength());
        ComponentBuilder cb = new ComponentBuilder(action_bar ? "" : "\n");
        for (int i = 0; i < this.getArray().length; i++) {
            color = this.getArray()[i] != -1 ? Color.getHSBColor(this.colorCal(this.getArray()[i]), 1.0f, 1.0f)
                    : Color.WHITE;

            AlgorithmSelectedIndex selectIndex = this.selectedIndexes.getFromValue(i);

            if (selectIndex != null) {
                // index ที่ถูกเลือก แสดงสีพิเศษ

                float weight = selectIndex.getWeight();
                selectIndex.addLifeTimeCount();

                cb.append("|").color(ChatColor.of(blend(selectIndex.getIndexColor(), color, weight, 1 - weight)))
                        .bold(true);

            } else {
                // index สีปกติ
                cb.append("|")
                        .color(this.getIndexesIntegers().contains(i) ? ChatColor.of(getIndexColor())
                                : ChatColor.of(color))
                        .bold(true);

            }
        }
        this.selectedIndexes.removeAllExpired();
        players.forEach((t) -> {
            t.spigot().sendMessage(action_bar ? ChatMessageType.ACTION_BAR : ChatMessageType.CHAT, cb.create());
        });

        playSortingSound();
        try {
            sleep(this.delay);
        } catch (InterruptedException e) {
        }
    }

    final public static String getAlgorithmName(Class<? extends Algorithms> clazz) {
        String name = clazz.getAnnotation(SortingAlgorithm.class).name();
        return name.isEmpty() ? clazz.getSimpleName() : name;
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
     * You can do anything you like with this method.
     * 
     * This method will be call after {@code argsFilter()}
     * and before {@code sort()}
     */
    public void init() {
    };

    final private void playSortingSound() {
        if (this.pitchs.isEmpty()) {
            this.pitchs = new LinkedList<>(
                    this.selectedIndexes.indexes.stream().map((t) -> pitchCal(t.getValue()))
                            .collect(Collectors.toList()));
        }
        pitchs.stream().filter((t) -> t != 0).forEach((p) -> {
            players.forEach(player -> player.playSound(player.getLocation(), HaoVSort.getInstance().getConfiguration().getSortingSoundName(),
                    SoundCategory.MASTER, HaoVSort.getInstance().getConfiguration().getSoundValue(), p));
        });
    }

    final private float colorCal(int value) {
        return (float) value / this.getArray().length;
    }

    final protected float pitchCal(Float min, Float max, int n) {
        return min + (((max - min) / this.getArray().length) * n);
    }

    final protected float pitchCal(Integer n) {
        return pitchCal(0.5f, 2.0f, n);
    }

    final protected LinkedList<Float> pitchCal(int... n) {
        return new LinkedList<>(Arrays.stream(n).boxed().map(this::pitchCal).collect(Collectors.toList()));
    }

    /**
     * Clear selectedIndexes
     */
    final protected void clearIndexes() {
        this.selectedIndexes.clear();
    }

    final protected void setIndexes(Integer... indexes) {
        this.selectedIndexes.addAll(AlgorithmSelectedIndex.asList(indexes));
    }

    final protected void setIndexes(LinkedList<Integer> indexes) {
        this.selectedIndexes.addAll(indexes.stream().map(AlgorithmSelectedIndex::new)
                .collect(Collectors.toCollection(LinkedList::new)));
    }

    final protected void setIndexes(AlgorithmSelectedIndex... indexes) {
        this.selectedIndexes.addAll(new LinkedList<>(Arrays.asList(indexes)));
    }

    final protected LinkedList<Integer> getIndexesIntegers() {
        return this.selectedIndexes.indexes.stream().map((t) -> t.getValue())
                .collect(Collectors.toCollection(LinkedList::new));
    }

    final protected LinkedList<AlgorithmSelectedIndex> getAlgorithmSelectedIndexes() {
        return this.selectedIndexes.indexes;
    }

    final protected void setPitchs(Float... pitchs) {
        this.pitchs = (pitchs == null) ? new LinkedList<Float>() : new LinkedList<Float>(Arrays.asList(pitchs));
    }

    final protected void setPitchs(LinkedList<Float> pitchs) {
        this.pitchs = pitchs;
    }

    /**
     * List of pitch that will be play at the same time as the <code>this.show()</code> method is called.
     * @return List<Float> pitchs
     */
    final protected List<Float> getPitchs() {
        return this.pitchs;
    }

    @Override
    final public void run() {
        this.argsFilter(args);
        this.init();
        this.sort(this.array);
        if (this.isStopped())
            throw new StopSortException();
    }

    final public void wake() {
        this.interrupt();
    }

    final public void stopAlgorithm() {
        this.end = true;
        this.wake();
    }

    /**
     * Format :
     * <p>
     * {@code /sort  &lt;player&gt; &lt;type&gt; &lt;delay&gt; &lt;length&gt; args[0] args[1] args[2] ...}
     *
     * @param sender Command sender.
     * @param args   This args[0] will start at args[4] of the main onTabComplete.
     * @return List of string that will be suggest when typing the command.
     */
    protected List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    };

    /**
     * You can throw InvalidArgsException when there are invalid arguments.
     * You can also attach your string to an exception object and that will be show to the command sender.
     */
    protected void argsFilter(String[] args) throws InvalidArgsException {
    };

    /**
     * TabCompleter of Algorithm
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
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     */
    final protected Algorithms newAlgorithm() throws InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return this.getClass().getDeclaredConstructor().newInstance();
    }

    final public boolean isStopped() {
        return this.end;
    }

    /**
     * รวมสี 2 สีเข้าด้วยกันตามค่าน้ำหนัก
     * <code>weight0</code> และ <code>weight1</code> ควรจะต้องรวมกันได้ 1.0 พอดี
     * 
     * @param c0      สี
     * @param c1      สี
     * @param weight0 ค่าระหว่าง 0.0 ถึง 1.0
     * @param weight1 ค่าระหว่าง 0.0 ถึง 1.0
     */
    final private static Color blend(Color c0, Color c1, float weight0, float weight1) {

        float r = (weight0 * c0.getRed()) + (weight1 * c1.getRed());
        float g = (weight0 * c0.getGreen()) + (weight1 * c1.getGreen());
        float b = (weight0 * c0.getBlue()) + (weight1 * c1.getBlue());
        float a = Math.max(c0.getAlpha(), c1.getAlpha());

        return new Color(Math.min(255, (int) r), Math.min(255, (int) g), Math.min(255, (int) b), (int) a);

    }

}
