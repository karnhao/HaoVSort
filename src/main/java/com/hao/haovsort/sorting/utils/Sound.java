package com.hao.haovsort.sorting.utils;

import org.bukkit.SoundCategory;

public class Sound {
    String name;
    SoundCategory soundCategory;
    float pitch;
    float volume;

    public Sound(String name, SoundCategory soundCategory, float pitch, float volume) {
        this.name = name;
        this.soundCategory = soundCategory;
        this.pitch = pitch;
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SoundCategory getSoundCategory() {
        return soundCategory;
    }

    public void setSoundCategory(SoundCategory soundCategory) {
        this.soundCategory = soundCategory;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }
}
