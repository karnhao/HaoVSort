package com.hao.haovsort.utils;

import java.io.IOException;
import java.net.URL;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.hao.haovsort.sorting.utils.AlgorithmsLoader;

import org.bukkit.command.CommandSender;

import net.md_5.bungee.api.ChatColor;

public class Util {
    public static boolean isInteger(String s) {
        return isInteger(s, 10);
    }

    public static boolean isInteger(String s, int radix) {
        if (s.isEmpty())
            return false;
        for (int i = 0; i < s.length(); i++) {
            if (i == 0 && s.charAt(i) == '-') {
                if (s.length() == 1)
                    return false;
                continue;
            }
            if (Character.digit(s.charAt(i), radix) < 0)
                return false;
        }
        return true;
    }

    public static String argsToString(String[] s) {
        StringBuilder builder = new StringBuilder();
        Arrays.asList(s).forEach((t) -> builder.append(" " + t));
        if (s[s.length - 1].equalsIgnoreCase(""))
            builder.append(" ");
        return builder.toString();
    }

    public static void alert(CommandSender cs, String e) {
        cs.sendMessage(ChatColor.RED + e);
    }

    public static int getLength(String s) {
        if (s == null || s == "")
            return 100;
        return Integer.parseInt(s);
    }

    public static Integer[] createArray(int length) {
        return IntStream.range(1, length + 1).boxed().toArray(Integer[]::new);
    }

    public static final List<String> listFilesForFolder(String path) throws IOException {
        CodeSource src = AlgorithmsLoader.class.getProtectionDomain().getCodeSource();
        List<String> paths = new ArrayList<>();
        if (src != null) {
            URL jar = src.getLocation();
            ZipInputStream zip = new ZipInputStream(jar.openStream());
            while (true) {
                ZipEntry e = zip.getNextEntry();
                if (e == null)
                    break;
                String name = e.getName();
                if (name.startsWith(path))
                    paths.add(name);
            }
            return paths.stream().map((t) -> t.substring(path.length() + 1))
                    .collect(Collectors.toList());
        }
        throw new IOException();
    }
}
