package com.hao.haovsort.listeners;

import com.hao.haovsort.sorting.utils.AlgorithmsManager;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerDisconnectListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onDisconnect(PlayerQuitEvent event) {
        AlgorithmsManager.cleanPlayer(event.getPlayer());
    }
}
