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

    private static void setDebug(Boolean bool) {
        debug = bool == null ? false : bool;
    }

    private static void setSortingSoundName(String soundName) {
        sound_name = soundName == null ? "minecraft:block.note_block.pling" : soundName;
    }

    private static void setSoundValue(Float soundValue) {
        sound_value = soundValue == null ? 0.1f : soundValue;
    }

    private static void setLimitLength(Boolean limitLength) {
        limit_length = limitLength == null ? false : limitLength;
    }

    private static void setMaxActionBarArrayLength(Integer length) {
        maxActionBarArrayLength = length == null ? 332 : length;
    }
}
