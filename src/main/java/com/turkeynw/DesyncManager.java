package com.turkeynw.tantiguidupe.managers;

import com.turkeynw.tantiguidupe.TAntiGuiDupe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import java.util.UUID;

public class DesyncManager {

    private final TAntiGuiDupe plugin;
    private BukkitTask syncTask;

    public DesyncManager(TAntiGuiDupe plugin) {
        this.plugin = plugin;
        startPeriodicSync();
    }

    //discord
    //fuseteas.
    // made by fuseheisen
    public void forceSync(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if (player != null && player.isOnline()) {
            Bukkit.getScheduler().runTask(plugin, player::updateInventory);
        }
    }

    //plugin
    //made
    //by
    //fuseheisen
    private void startPeriodicSync() {
        syncTask = Bukkit.getScheduler().runTaskTimer(plugin, () -> {
            for (UUID uuid : plugin.getPacketManager().getOpenMenus()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player != null && player.isOnline()) {
                    player.updateInventory();
                }
            }
        }, 10L, 10L); // 10 Tick = 0.5 Saniye
    }

    public void stop() {
        if (syncTask != null) {
            syncTask.cancel();
        }
    }
}