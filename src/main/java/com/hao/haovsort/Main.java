package com.hao.haovsort;

import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.sorting.SortPlayer;
import com.hao.haovsort.tabcompleter.SortTab;
import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static String prefix;

    @Override
    public void onEnable() {
        prefix = String.format("[%s]", getName());

        try {
            SortPlayer.init();
        } catch (Exception e) {
            getLogger().log(Level.WARNING, e.getMessage());
        }

        PluginCommand sort = getCommand("sort");
        if (sort != null) {
            sort.setExecutor(new Sort());
            sort.setTabCompleter(new SortTab());
        }

        getLogger().log(Level.INFO, "{0} plugin is enable.", prefix);
    }
}
