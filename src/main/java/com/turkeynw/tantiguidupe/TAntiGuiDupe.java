package com.turkeynw.tantiguidupe;

import com.turkeynw.tantiguidupe.managers.ConfigManager;
import com.turkeynw.tantiguidupe.managers.DesyncManager;
import com.turkeynw.tantiguidupe.managers.PacketManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TAntiGuiDupe extends JavaPlugin {

    private ConfigManager configManager;
    private PacketManager packetManager;
    private DesyncManager desyncManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.packetManager = new PacketManager(this, configManager);
        this.desyncManager = new DesyncManager(this);

        getServer().getPluginManager().registerEvents(new GuiListener(this, configManager), this);
        getCommand("tagd").setExecutor(new ReloadCommand(this, configManager));

        getServer().getConsoleSender().sendMessage("§6[TAntiGuiDupe] §fTAntiGuiDupe made by §efuseheisen §f: §aBaşarıyla Aktif Edildi.");
    }

    @Override
    public void onDisable() {
        if (desyncManager != null) {
            desyncManager.stop();
        }

        getServer().getConsoleSender().sendMessage("§6[TAntiGuiDupe] §cEklenti devre dışı bırakıldı.");
    }

    //fuseheisen
    public ConfigManager getConfigManager() {
        return configManager;
    }

    public PacketManager getPacketManager() {
        return packetManager;
    }

    public DesyncManager getDesyncManager() {
        return desyncManager;
    }
}