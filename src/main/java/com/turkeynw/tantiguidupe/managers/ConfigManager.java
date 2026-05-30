package com.turkeynw.tantiguidupe.managers;

import com.turkeynw.tantiguidupe.TAntiGuiDupe;
import java.util.List;

public class ConfigManager {
    //made by fuseheisen
    private final TAntiGuiDupe plugin;
    private long clickDelayMs;
    private long dropDelayMs;
    private long recipeDelayMs;
    private long actionDelayMs;
    private List<String> protectedMenus;

    //
    private long syncTimerTicks;
    private long burstTimeWindowMs;
    private int burstPacketLimit;
    private long freezeTimeLimitMs;
    private long freezePenaltyMs;

    private String reloadedMessage;
    private String noPermissionMessage;

    public ConfigManager(TAntiGuiDupe plugin) {
        this.plugin = plugin;
        loadConfig();
    }

    public void loadConfig() {
        plugin.reloadConfig();
        this.clickDelayMs = plugin.getConfig().getLong("settings.click-delay-ms", 50);
        this.dropDelayMs = plugin.getConfig().getLong("settings.drop-delay-ms", 100);
        this.recipeDelayMs = plugin.getConfig().getLong("settings.recipe-delay-ms", 100);
        this.actionDelayMs = plugin.getConfig().getLong("settings.action-delay-ms", 50);
        this.protectedMenus = plugin.getConfig().getStringList("settings.protected-menus");

        //
        this.syncTimerTicks = plugin.getConfig().getLong("settings.advanced.sync-timer-ticks", 10L);
        this.burstTimeWindowMs = plugin.getConfig().getLong("settings.advanced.burst-time-window-ms", 100L);
        this.burstPacketLimit = plugin.getConfig().getInt("settings.advanced.burst-packet-limit", 25);
        this.freezeTimeLimitMs = plugin.getConfig().getLong("settings.advanced.freeze-time-limit-ms", 2500L);
        this.freezePenaltyMs = plugin.getConfig().getLong("settings.advanced.freeze-penalty-ms", 1000L);

        this.reloadedMessage = plugin.getConfig().getString("messages.reloaded", "§aYenilendi.");
        this.noPermissionMessage = plugin.getConfig().getString("messages.no-permission", "§cYetki yok.");
    }
    //fuseheisen
    public long getClickDelayMs() { return clickDelayMs; }
    public long getDropDelayMs() { return dropDelayMs; }
    public long getRecipeDelayMs() { return recipeDelayMs; }
    public long getActionDelayMs() { return actionDelayMs; }
    public List<String> getProtectedMenus() { return protectedMenus; }

    public long getSyncTimerTicks() { return syncTimerTicks; }
    public long getBurstTimeWindowMs() { return burstTimeWindowMs; }
    public int getBurstPacketLimit() { return burstPacketLimit; }
    public long getFreezeTimeLimitMs() { return freezeTimeLimitMs; }
    public long getFreezePenaltyMs() { return freezePenaltyMs; }

    public String getReloadedMessage() { return reloadedMessage; }
    public String getNoPermissionMessage() { return noPermissionMessage; }
}