package com.hao.haovsort;

import com.hao.haovsort.commands.Reload;
import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.commands.SortCustom;
import com.hao.haovsort.commands.SortDebug;
import com.hao.haovsort.commands.StopSort;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.tabcompleter.CustomSortTab;
import com.hao.haovsort.utils.Configuration;

import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static String prefix;
    private static final String BREAKER = ";";

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Configuration.setFinal(this);
        prefix = String.format("[%s]", getName());

        try {
            AlgorithmsManager.init();
        } catch (Exception e) {
            e.printStackTrace();
            getLogger().log(Level.WARNING, e.toString());
            getLogger().log(Level.WARNING, "AlgorithmsManager cannot initialize!", prefix);
            getLogger().log(Level.WARNING, "Plugin is disable.", prefix);
            return;
        }

        PluginCommand sort = getCommand("sort");
        PluginCommand stopSort = getCommand("sortstop");
        PluginCommand sortdebug = Configuration.getDebug() ? getCommand("sortdebug") : null;
        PluginCommand sortcustom = getCommand("sortcustom");
        PluginCommand sortreload = getCommand("sortreload");

        sort.setExecutor(new Sort());
        stopSort.setExecutor(new StopSort());
        if (Configuration.getDebug())
            sortdebug.setExecutor(new SortDebug());
        sortcustom.setExecutor(new SortCustom(BREAKER));
        sortreload.setExecutor(new Reload(this));

        AlgorithmsManager.setTabCompleterToCommand(sort);
        sortcustom.setTabCompleter(new CustomSortTab(BREAKER));

        getLogger().log(Level.INFO, "plugin is enable.");
    }

    @Override
    public void onDisable() {
        AlgorithmsManager.stopAll();
        getLogger().log(Level.INFO, "Bye bye~", prefix);
    }

    public static String getPrefix() {
        return Main.prefix;
    }
}
