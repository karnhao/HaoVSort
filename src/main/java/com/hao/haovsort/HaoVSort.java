package com.hao.haovsort;

import com.hao.haovsort.commands.Reload;
import com.hao.haovsort.commands.Sort;
import com.hao.haovsort.commands.SortCustom;
import com.hao.haovsort.commands.SortDebug;
import com.hao.haovsort.commands.SortDelay;
import com.hao.haovsort.commands.StopSort;
import com.hao.haovsort.listeners.PlayerDisconnectListener;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.sorting.utils.SongCollector;
import com.hao.haovsort.tabcompleters.CustomSortTab;
import com.hao.haovsort.tabcompleters.NothingTab;
import com.hao.haovsort.tabcompleters.SortDelayTab;
import com.hao.haovsort.tabcompleters.StopSortTab;
import com.hao.haovsort.utils.Configuration;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

public class HaoVSort extends JavaPlugin {
    private static final String BREAKER = ";";
    private static HaoVSort instance;
    private boolean noteBlockAPI = true;
    private Configuration configuration;
    private SongCollector songCollector;
    private AlgorithmsManager algorithmManager;

    private HaoVSort() {
    };

    @Override
    public void onEnable() {
        HaoVSort.instance = this;
        saveDefaultConfig();
        configuration = new Configuration(this);
        if (!Bukkit.getPluginManager().isPluginEnabled("NoteBlockAPI")) {
            getLogger().severe("*** NoteBlockAPI is not installed or not enabled. ***");
            noteBlockAPI = false;
        }

        try {
            if (noteBlockAPI) {
                SongCollector songCollector = new SongCollector();
                songCollector.init(this);
                this.songCollector = songCollector;
            }
            algorithmManager.init();
        } catch (Exception e) {
            // something wrong...
            e.printStackTrace();

            getLogger().log(Level.WARNING, "AlgorithmsManager cannot initialize!");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        PluginCommand sort = getCommand("sort");
        PluginCommand stopSort = getCommand("sortstop");
        PluginCommand sortdebug = configuration.getDebug() ? getCommand("sortdebug") : null;
        PluginCommand sortcustom = getCommand("sortcustom");
        PluginCommand sortreload = getCommand("sortreload");
        PluginCommand sortdelay = getCommand("sortdelay");

        sort.setExecutor(new Sort());
        stopSort.setExecutor(new StopSort());
        if (sortdebug != null)
            sortdebug.setExecutor(new SortDebug());
        sortcustom.setExecutor(new SortCustom(BREAKER));
        sortreload.setExecutor(new Reload(this));
        sortdelay.setExecutor(new SortDelay());

        algorithmManager.setTabCompleterToCommand(sort);
        sortcustom.setTabCompleter(new CustomSortTab(BREAKER));
        stopSort.setTabCompleter(new StopSortTab());
        if (sortdebug != null)
            sortdebug.setTabCompleter(new NothingTab());
        sortreload.setTabCompleter(new NothingTab());
        sortdelay.setTabCompleter(new SortDelayTab());

        Bukkit.getPluginManager().registerEvents(new PlayerDisconnectListener(), this);

        getLogger().log(Level.INFO, "Plugin is enable.");
    }

    @Override
    public void onDisable() {
        algorithmManager.stopAll();
        getLogger().log(Level.INFO, "Plugin is disable.");
    }

    public boolean getNoteBlockAPI() {
        return this.noteBlockAPI;
    }

    public SongCollector getSongCollector() {
        return this.songCollector;
    }

    public void setSongCollector(SongCollector songCollector) {
        this.songCollector = songCollector;
    }

    public AlgorithmsManager getAlgorithmManager() {
        return algorithmManager;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public static HaoVSort getInstance() {
        return HaoVSort.instance;
    }
}
