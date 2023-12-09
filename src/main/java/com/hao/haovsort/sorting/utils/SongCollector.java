package com.hao.haovsort.sorting.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

final public class SongCollector {

    private HashMap<String, Song> map;

    public void init(Plugin plugin) throws IOException, URISyntaxException {
        map = new HashMap<String, Song>();
        map.clear();
        String path = String.format("plugins/%s/songs", plugin.getName());
        File songs = new File(path);
        if (!songs.exists()) {
            songs.mkdirs();
            DefaultSong.save(songs);
        }
        Bukkit.getLogger().info("Reading songs file asynchronously...");
        new BukkitRunnable() {
            @Override
            public void run() {
                for (File song : songs.listFiles()) {
                    String name = song.getName();
                    int dotIndex = name.lastIndexOf('.');
                    if (dotIndex == -1) continue;
                    if (name.substring(dotIndex).equalsIgnoreCase(".nbs")) continue;
                    putSong(NBSDecoder.parse(song));
                }
                Bukkit.getLogger().info("HaoVSort loading songs file finished");
            }
        }.runTaskAsynchronously(plugin);

    }

    private void putSong(Song song) {
        String name = song.getTitle().toLowerCase().replaceAll("\\s+", "_");
        if (name == null || name.length() == 0)
            name = song.getPath().getName().toLowerCase().replaceAll("\\s+", "_");
        map.put(name, song);
    }

    public List<String> getAllSongsName() {
        return getAllSongsName("");
    }

    public List<String> getAllSongsName(String startWith) {
        return map.keySet().stream().filter((t) -> t.startsWith(startWith.toLowerCase())).collect(Collectors.toList());
    }

    public Song getSong(String name) {
        return map.get(name);
    }
}
