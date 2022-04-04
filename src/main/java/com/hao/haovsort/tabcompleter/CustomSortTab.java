package com.hao.haovsort.tabcompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.hao.haovsort.sorting.utils.Algorithms;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.utils.Util;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class CustomSortTab implements TabCompleter {

    @SuppressWarnings("unchecked")
    @Override
    public List<String> onTabComplete(CommandSender cs, Command cmnd, String string, String[] args) {
        // /sortcustom <player> <delay> <length> random 1 ; radix 4 ; finish #00AA00
        // /sort karnhao 10 100 random 1 ; radix 4 ; finish #00AA00
        // /sort karnhao 10 100 random 1;radix 4;finish #00AA00

        // /sort karnhao 10 100 r
        // .....................random
        // .....................radix
        // /sort karnhao 10 100 random
        // ............................<[0,1]>
        // /sort karnhao 10 100 random 1
        // ..............................;
        // /sort karnhao 10 100 random 1 ;
        // ................................bitonic
        // ................................bogo
        // ................................[...]

        // /sort karnhao 10 100 r
        // .....................random
        // .....................radix
        // /sort karnhao 10 100 random
        // ............................<[0,1]>
        // /sort karnhao 10 100 random 1
        // .............................;
        // /sort karnhao 10 100 random 1;
        // ...............................bitonic
        // ...............................bogo
        // ...............................[...]
        // /sort karnhao 10 100 random 1;bitonic
        // .....................................;
        switch (args.length) {
            case 1:
                return null;
            case 2:
                if (args[1].isEmpty())
                    return Arrays.asList("<delay>");
                break;
            case 3:
                if (args[2].isEmpty())
                    return Arrays.asList("<length>");
                break;
            default:
                if (args.length > 3) {
                    String name = getCurrentAlgorithmName(format(Arrays.copyOfRange(args, 3, args.length)));
                    String[] algorithm_args = getCurrentAlgorithmArgs(format(Arrays.copyOfRange(args, 3, args.length)));
                    try {
                        List<String> r = getAlgorithmTabCompleter(name).onTabComplete(cs, cmnd, string, algorithm_args);
                        if (r == null || r.isEmpty()) {
                            return Arrays.asList(";");
                        }
                        return r;
                    } catch (NullPointerException e) {
                        return AlgorithmsManager.getAlgorithms().stream().map(t -> {
                            try {
                                Class<? extends Algorithms<?>> clazz = (Class<? extends Algorithms<?>>) t.getClass();
                                return Algorithms.getAlgorithmName(clazz).toLowerCase();
                            } catch (IllegalArgumentException | SecurityException ex) {
                                ex.printStackTrace();
                                return null;
                            }
                        })
                                .filter((t) -> t.startsWith(name.toLowerCase()))
                                .collect(Collectors.toList());
                    } catch (InstantiationException | IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
        }
        return null;
    }

    private static TabCompleter getAlgorithmTabCompleter(String algorithm_name)
            throws InstantiationException, IllegalAccessException {
        return AlgorithmsManager.getAlgorithm(algorithm_name).getTabCompleter();
    }

    private static String[] split(String str) {
        if (str == null || str.length() == 0)
            return new String[] { "" };
        String temp = str.replaceAll("[;]", " ; ");
        String[] s = temp.split(" ");
        List<String> r = new ArrayList<>();
        for (int i = 0; i < s.length; i++) {
            if (s[i].equalsIgnoreCase(""))
                continue;
            r.add(s[i]);
        }
        if (str.charAt(str.length() - 1) == ' ')
            r.add("");
        return r.toArray(new String[r.size()]);
    }

    private static String getCurrentAlgorithmName(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i].equalsIgnoreCase(";")) {
                return i == args.length - 1 ? "" : args[i + 1];
            }
        }
        return args[0];
    }

    private static String[] getCurrentAlgorithmArgs(String[] args) {
        for (int i = args.length - 1; i >= 0; i--) {
            if (args[i].equalsIgnoreCase(";")) {
                String[] r = i == args.length - 1 ? new String[] { "" } : Arrays.copyOfRange(args, i + 2, args.length);
                return r.length == 0 ? new String[] { "" } : r;
            }
        }
        return Arrays.copyOfRange(args, 1, args.length);
    }

    private static String[] format(String[] args) {
        return split(Util.argsToString(args));
    }
}
