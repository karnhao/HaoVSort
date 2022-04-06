package com.hao.haovsort.utils;

import org.bukkit.plugin.Plugin;

public class Configuration {
    private static Boolean debug;

    private static String sound_name;
    private static Float sound_value;

    private static Boolean limit_length;
    private static Integer maxActionBarArrayLength;

    public static void setFinal(Plugin plugin) {
        setDebug(plugin.getConfig().getBoolean("Debug"));
        setSortingSoundName(plugin.getConfig().getString("SortingSound.Name"));
        setSoundValue((float) plugin.getConfig().getDouble("SortingSound.Value"));
        setLimitLength(plugin.getConfig().getBoolean("LimitLength"));
        setMaxActionBarArrayLength(plugin.getConfig().getInt("MaxActionBarArrayLength"));
    }

    public static boolean getDebug() {
        return debug;
    }

    public static String getSortingSoundName() {
        return sound_name;
    }

    public static Float getSoundValue() {
        return sound_value;
    }

    public static boolean getLimitLength() {
        return limit_length;
    }

    public static Integer getMaxActionBarArrayLength() {
        return maxActionBarArrayLength;
    }

    private static void setDebug(Boolean debug) {
        debug = debug == null ? false : debug;
    }

    private static void setSortingSoundName(String sound_name) {
        sound_name = sound_name == null ? "minecraft:block.note_block.pling" : sound_name;
    }

    private static void setSoundValue(Float soundValue) {
        sound_value = soundValue == null ? 0.1f : sound_value;
    }

    private static void setLimitLength(Boolean limitLength) {
        limit_length = limitLength == null ? false : limit_length;
    }

    private static void setMaxActionBarArrayLength(Integer maxActionBarArrayLength) {
        maxActionBarArrayLength = maxActionBarArrayLength == null ? 332 : maxActionBarArrayLength;
    }
}
