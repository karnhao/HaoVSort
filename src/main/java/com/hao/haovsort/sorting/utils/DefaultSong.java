package com.hao.haovsort.sorting.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;

import com.hao.haovsort.utils.Util;

import org.bukkit.Bukkit;

public class DefaultSong {
    private static final String FOLDERPATH = "resources/songs";

    public static final void save(File toPath) throws IOException {
        Util.listFilesForFolder(FOLDERPATH).stream().filter((t) -> t.endsWith(".nbs")).forEach((u) -> {
            Path target = Paths.get(toPath.getPath(), u);
            String path = FOLDERPATH + "/" + u;
            Bukkit.getLogger().log(Level.INFO, "Adding {0} to {1}", new Object[] { path, target });
            InputStream in = DefaultSong.class.getClassLoader().getResourceAsStream(path);
            if (in != null)
                try {
                    Files.copy(in, target);
                } catch (IOException e) {
                    e.printStackTrace();
                }
        });
    }
}
