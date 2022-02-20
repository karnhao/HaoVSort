package com.hao.haovsort;

import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.tabcompleter.SortTab;
import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    
    @Override
    public void onEnable() {
        PluginCommand sort = getCommand("sort");
        if (sort != null) {
            sort.setExecutor(new Sort());
            sort.setTabCompleter(new SortTab());
        }
        getLogger().log(Level.INFO, "[HaoVSort] plugin is enable.");
    }
}
