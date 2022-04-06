package com.hao.haovsort.commands;

import java.io.File;
import java.util.logging.Level;

import com.hao.haovsort.Main;
import com.hao.haovsort.sorting.utils.AlgorithmsManager;
import com.hao.haovsort.utils.Configuration;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;

import net.md_5.bungee.api.ChatColor;

public class Reload implements CommandExecutor {

    private Plugin plugin;

    @Override
    public boolean onCommand(CommandSender cs, Command cmnd, String alias, String[] args) {
        AlgorithmsManager.stopAll();
        Bukkit.getLogger().log(Level.INFO, "{0} Reloading config...", Main.getPrefix());
        if (!new File(String.format("plugin/%s/config.yml", plugin.getName())).exists()) {
            plugin.saveDefaultConfig();
        }
        plugin.reloadConfig();
        Configuration.setFinal(plugin);
        Bukkit.getLogger().log(Level.INFO, "{0} Finish", Main.getPrefix());
        cs.sendMessage(ChatColor.GREEN + "Reload finish");
        return true;
    }

    public Reload(Plugin plugin) {
        this.plugin = plugin;
    }
}
