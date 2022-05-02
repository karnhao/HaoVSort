package com.hao.haovsort.sorting.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.xxmicloxx.NoteBlockAPI.model.Song;
import com.xxmicloxx.NoteBlockAPI.utils.NBSDecoder;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

final public class SongCollector {

    private static HashMap<String, Song> map = new HashMap<String, Song>();

    public static void init(Plugin plugin) throws IOException, URISyntaxException {
        map.clear();
        String path = String.format("plugins/%s/songs", plugin.getName());
        File songs = new File(path);
        if (!songs.exists())
            songs.mkdirs();
            DefaultSong.save(songs);
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            paths
                    .filter(Files::isRegularFile)
                    .map((t) -> {
                        try {
                            Bukkit.getLogger().info("Loading " + t.toFile().getName());
                            return NBSDecoder.parse(t.toFile());
                        } catch (Exception e) {
                            e.printStackTrace();
                            Bukkit.getLogger().log(Level.WARNING, "Cannot load {0}", t.toFile().getName());
                            return null;
                        }
                    }).filter(Objects::nonNull).forEach(SongCollector::putSong);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void putSong(Song song) {
        String name = song.getTitle().toLowerCase().replaceAll("\\s+", "_");
        if (name == null || name.length() == 0)
            name = song.getPath().getName().toLowerCase().replaceAll("\\s+", "_");
        map.put(name, song);
    }

    public static List<String> getAllSongsName() {
        return getAllSongsName("");
    }

    public static List<String> getAllSongsName(String startWith) {
        return map.keySet().stream().filter((t) -> t.startsWith(startWith.toLowerCase())).collect(Collectors.toList());
    }

    public static Song getSong(String name) {
        return map.get(name);
    }
}
