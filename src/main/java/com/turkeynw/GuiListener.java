package com.turkeynw.tantiguidupe.listeners;

import com.turkeynw.tantiguidupe.TAntiGuiDupe;
import com.turkeynw.tantiguidupe.managers.ConfigManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class GuiListener implements Listener {

    private final TAntiGuiDupe plugin;
    private final ConfigManager configManager;

    public GuiListener(TAntiGuiDupe plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
    }

    private boolean isProtectedMenu(String title) {
        if (title == null) return false;
        for (String menu : configManager.getProtectedMenus()) {
            if (title.contains(menu)) {
                return true;
            }
        }
        return false;
    }

    //made by fuseheisen
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player player) {
            if (isProtectedMenu(event.getView().getTitle())) {
                plugin.getPacketManager().setMenuOpen(player.getUniqueId(), true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        String title = event.getView().getTitle();
        if (!isProtectedMenu(title)) return;

        ClickType click = event.getClick();
        if (click == ClickType.DROP || click == ClickType.CONTROL_DROP ||
                click == ClickType.NUMBER_KEY || click == ClickType.SWAP_OFFHAND ||
                click == ClickType.DOUBLE_CLICK || click == ClickType.SHIFT_LEFT ||
                click == ClickType.SHIFT_RIGHT) {

            event.setCancelled(true);
            syncInventory(player);
            return;
        }

        if (event.getRawSlot() < event.getView().getTopInventory().getSize()) {
            event.setCancelled(true);
            syncInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryDrag(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player player)) return;

        if (isProtectedMenu(event.getView().getTitle())) {
            for (int slot : event.getRawSlots()) {
                if (slot < event.getView().getTopInventory().getSize()) {
                    event.setCancelled(true);
                    syncInventory(player);
                    return;
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;


        plugin.getPacketManager().setMenuOpen(player.getUniqueId(), false);

        if (isProtectedMenu(event.getView().getTitle())) {
            ItemStack cursorItem = player.getItemOnCursor();
            if (cursorItem.getType() != Material.AIR) {
                player.setItemOnCursor(new ItemStack(Material.AIR));
            }
            syncInventory(player);
        }
    }
    //plugin made by fuseheisen
//fuseheisen
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (isProtectedMenu(player.getOpenInventory().getTitle())) {
            event.setCancelled(true);
            player.closeInventory();
            syncInventory(player);
        }
    }
    //plugin made by fuseheisen
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (isProtectedMenu(player.getOpenInventory().getTitle())) {
            event.setCancelled(true);
            player.closeInventory();
            syncInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        if (isProtectedMenu(player.getOpenInventory().getTitle())) {
            event.setCancelled(true);
            player.closeInventory();
            syncInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerSwapHandItems(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (isProtectedMenu(player.getOpenInventory().getTitle())) {
            event.setCancelled(true);
            player.closeInventory();
            syncInventory(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (isProtectedMenu(player.getOpenInventory().getTitle())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.getPacketManager().removePlayer(event.getPlayer().getUniqueId());
    }

    private void syncInventory(Player player) {
        plugin.getServer().getScheduler().runTaskLater(plugin, player::updateInventory, 1L);
    }
}