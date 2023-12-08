package com.hao.haovsort.utils;

import org.bukkit.plugin.Plugin;

public class Configuration {
    private Boolean debug;

    private String sound_name;
    private Float sound_value;

    private Boolean limit_length;
    private Integer maxActionBarArrayLength;

    public Configuration(Plugin plugin) {
        this.setDebug(plugin.getConfig().getBoolean("Debug"));
        this.setSortingSoundName(plugin.getConfig().getString("SortingSound.Name"));
        this.setSoundValue((float) plugin.getConfig().getDouble("SortingSound.Value"));
        this.setLimitLength(plugin.getConfig().getBoolean("LimitLength"));
        this.setMaxActionBarArrayLength(plugin.getConfig().getInt("MaxActionBarArrayLength"));
    }

    public boolean getDebug() {
        return debug;
    }

    public String getSortingSoundName() {
        return sound_name;
    }

    public Float getSoundValue() {
        return sound_value;
    }

    public boolean getLimitLength() {
        return limit_length;
    }

    public Integer getMaxActionBarArrayLength() {
        return maxActionBarArrayLength;
    }

    private void setDebug(Boolean bool) {
        debug = bool == null ? false : bool;
    }

    private void setSortingSoundName(String soundName) {
        sound_name = soundName == null ? "minecraft:block.note_block.pling" : soundName;
    }

    private void setSoundValue(Float soundValue) {
        sound_value = soundValue == null ? 0.1f : soundValue;
    }

    private void setLimitLength(Boolean limitLength) {
        limit_length = limitLength == null ? false : limitLength;
    }

    private void setMaxActionBarArrayLength(Integer length) {
        maxActionBarArrayLength = length == null ? 332 : length;
    }
}
