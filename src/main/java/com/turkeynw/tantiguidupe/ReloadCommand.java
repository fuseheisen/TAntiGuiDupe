package com.turkeynw.tantiguidupe;

import com.turkeynw.tantiguidupe.managers.ConfigManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements CommandExecutor {

    private final TAntiGuiDupe plugin;
    private final ConfigManager configManager;

    public ReloadCommand(TAntiGuiDupe plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }
    //plugin made by fuseheisen
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("tantiguidupe.admin")) {
            sender.sendMessage(configManager.getNoPermissionMessage());
            return true;
        }
        //discord: fuseteas.
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            configManager.loadConfig();
            sender.sendMessage(configManager.getReloadedMessage());
            return true;
        }
        //plugin made by fuseheisen
        return false;
    }
}