package com.hao.haovsort;

import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.commands.SortCustom;
import com.hao.haovsort.commands.SortDebug;
import com.hao.haovsort.commands.StopSort;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.tabcompleter.CustomSortTab;

import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static String prefix;

    @Override
    public void onEnable() {
        prefix = String.format("[%s]", getName());
        try {
            AlgorithmsManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().log(Level.WARNING, e.toString());
            getLogger().log(Level.WARNING, "{0} AlgorithmsManager cannot initialize!", prefix);
            getLogger().log(Level.WARNING, "{0} Plugin is disable.", prefix);
            return;
        }
        PluginCommand sort = getCommand("sort");
        PluginCommand stopSort = getCommand("sortstop");
        PluginCommand sortdebug = getCommand("sortdebug");
        PluginCommand sortcustom = getCommand("sortcustom");
        if (sort == null || stopSort == null || sortdebug == null || sortcustom == null) {
            getLogger().log(Level.WARNING, "{0} NOT FOUND SOME HAOVSORT COMMAND!", prefix);
        }
        sort.setExecutor(new Sort());
        stopSort.setExecutor(new StopSort());
        sortdebug.setExecutor(new SortDebug());
        sortcustom.setExecutor(new SortCustom());

        AlgorithmsManager.setTabCompleterToCommand(sort);
        sortcustom.setTabCompleter(new CustomSortTab());

        getLogger().log(Level.INFO, "{0} plugin is enable.", prefix);
    }

    @Override
    public void onDisable() {
        AlgorithmsManager.stopAll();
        getLogger().log(Level.INFO, "{0} Bye bye~", prefix);
    }

    public static String getPrefix() {
        return Main.prefix;
    }
}
