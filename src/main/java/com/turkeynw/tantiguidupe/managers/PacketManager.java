package com.turkeynw.tantiguidupe.managers;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerAbstract;
import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.protocol.packettype.PacketType;
import com.turkeynw.tantiguidupe.TAntiGuiDupe;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PacketManager extends PacketListenerAbstract {
    //made by fuseheisen
    private final TAntiGuiDupe plugin;
    private final ConfigManager configManager;
    private final Map<UUID, Long> lastClick = new ConcurrentHashMap<>();
    private final Set<UUID> openMenus = ConcurrentHashMap.newKeySet();

    private final Map<UUID, Long> lastAnyPacket = new ConcurrentHashMap<>();
    private final Map<UUID, Long> freezePenalty = new ConcurrentHashMap<>();

    private final Map<UUID, Long> burstStartTime = new ConcurrentHashMap<>();
    private final Map<UUID, Integer> packetCount = new ConcurrentHashMap<>();

    public PacketManager(TAntiGuiDupe plugin, ConfigManager configManager) {
        this.plugin = plugin;
        this.configManager = configManager;
        PacketEvents.getAPI().getEventManager().registerListener(this);
    }

    public void setMenuOpen(UUID uuid, boolean open) {
        if (open) {
            openMenus.add(uuid);
        } else {
            openMenus.remove(uuid);
        }
    }

    public Set<UUID> getOpenMenus() {
        return openMenus;
    }

    @Override
    public void onPacketReceive(PacketReceiveEvent event) {
        if (!(event.getPlayer() instanceof Player player)) return;
        UUID uuid = player.getUniqueId();
        long now = System.currentTimeMillis();

        long burstStart = burstStartTime.getOrDefault(uuid, now);
        int count = packetCount.getOrDefault(uuid, 0);

        if (now - burstStart < 100) {
            count++;
            packetCount.put(uuid, count);

            if (count > 25 && openMenus.contains(uuid)) {
                freezePenalty.put(uuid, now + 1000);
                event.setCancelled(true);

                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.isOnline()) {
                        player.closeInventory();
                        plugin.getDesyncManager().forceSync(uuid);
                    }
                });
                return;
            }
        } else {
            burstStartTime.put(uuid, now);
            packetCount.put(uuid, 1);
        }

        long lastPacketTime = lastAnyPacket.getOrDefault(uuid, now);
        long timeSinceLastPacket = now - lastPacketTime;

        if (timeSinceLastPacket > 2500) {
            freezePenalty.put(uuid, now + 1000);

            if (openMenus.contains(uuid)) {
                Bukkit.getScheduler().runTask(plugin, () -> {
                    if (player.isOnline()) {
                        player.closeInventory();
                        plugin.getDesyncManager().forceSync(uuid);
                    }
                });
            }
        }

        lastAnyPacket.put(uuid, now);

        if (freezePenalty.containsKey(uuid) && now < freezePenalty.get(uuid)) {
            if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW ||
                    event.getPacketType() == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION ||
                    event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {

                event.setCancelled(true);
                plugin.getDesyncManager().forceSync(uuid);
                return;
            }
        }

        if (event.getPacketType() == PacketType.Play.Client.CLICK_WINDOW ||
                event.getPacketType() == PacketType.Play.Client.CREATIVE_INVENTORY_ACTION) {

            if (isSpamming(uuid, lastClick, now, configManager.getClickDelayMs())) {
                event.setCancelled(true);
                plugin.getDesyncManager().forceSync(uuid);
            }
        }
        else if (event.getPacketType() == PacketType.Play.Client.PLAYER_DIGGING) {
            if (openMenus.contains(uuid)) {
                event.setCancelled(true);
                plugin.getDesyncManager().forceSync(uuid);
            }
        }
    }
    //made by fuseheisen
    private boolean isSpamming(UUID uuid, Map<UUID, Long> map, long now, long delay) {
        if (map.containsKey(uuid)) {
            if (now - map.get(uuid) < delay) {
                return true;
            }
        }
        map.put(uuid, now);
        return false;
    }

    public void removePlayer(UUID uuid) {
        lastClick.remove(uuid);
        openMenus.remove(uuid);
        lastAnyPacket.remove(uuid);
        freezePenalty.remove(uuid);
        burstStartTime.remove(uuid);
        packetCount.remove(uuid);
    }
}