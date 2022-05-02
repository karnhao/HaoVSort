package com.hao.haovsort.sorting.utils;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.FileUtil;

public class DefaultSong {
    private static final String FOLDERPATH = "resources/default_songs";

    public static final void save(File toPath) throws IOException, URISyntaxException {
        listFilesForFolder(new File(FOLDERPATH)).stream().filter((t) -> t.getName().endsWith(".nbs")).forEach((u) -> {
            FileUtil.copy(u, Paths.get(toPath.getPath(), u.getName()).toFile());
        });
    }

    public static final List<File> listFilesForFolder(File folder) throws URISyntaxException {
        List<File> files = new ArrayList<>();
        for (final File fileEntry : new File(DefaultSong.class.getResource("default_songs").toURI()).listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                files.add(fileEntry);
            }
        }
        return files;
    }
}
