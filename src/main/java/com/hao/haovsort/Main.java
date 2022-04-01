package com.hao.haovsort;

import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.commands.StopSort;
import com.hao.haovsort.sorting.SortPlayer;
import com.hao.haovsort.sorting.algorithms.utils.AlgorithmsManager;

import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static String prefix;

    @Override
    public void onEnable() {
        prefix = String.format("[%s]", getName());

        PluginCommand sort = getCommand("sort");
        PluginCommand stopSort = getCommand("stopsort");
        if (sort == null || stopSort == null) {
            getLogger().log(Level.WARNING, "{0} NOT FOUND SORT COMMAND!", prefix);
            getLogger().log(Level.WARNING, "{0} Plugin is disable.", prefix);
            return;
        }
        try {
            AlgorithmsManager.init();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, e.toString());
            getLogger().log(Level.WARNING, "{0} AlgorithmsManager cannot initialize!", prefix);
            getLogger().log(Level.WARNING, "{0} Plugin is disable.", prefix);
            return;
        }

        sort.setExecutor(new Sort(this));
        SortPlayer.setTabCompleterToCommand(sort);

        stopSort.setExecutor(new StopSort());

        getLogger().log(Level.INFO, "{0} plugin is enable.", prefix);
    }

    @Override
    public void onDisable() {
        AlgorithmsManager.stopAll();
        getLogger().log(Level.INFO, "{0} Bye bye~", prefix);
    }
}
