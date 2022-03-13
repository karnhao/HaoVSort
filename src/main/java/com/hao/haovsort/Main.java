package com.hao.haovsort;

import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.sorting.SortPlayer;
import java.util.logging.Level;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static String prefix;

    @Override
    public void onEnable() {
        prefix = String.format("[%s]", getName());

        PluginCommand sort = getCommand("sort");
        if (sort == null) {
            getLogger().log(Level.WARNING, "{0} NOT FOUND SORT COMMAND!", prefix);
            getLogger().log(Level.WARNING, "{0} Plugin is disable.", prefix);
            return;
        }
        sort.setExecutor(new Sort());
        SortPlayer.setTabCompleterToCommand(sort);

        getLogger().log(Level.INFO, "{0} plugin is enable.", prefix);
    }

    @Override
    public void onDisable() {
        getLogger().log(Level.INFO, "{0} Bye bye~", prefix);
    }
}
