package net.kyrptonaught.linkedstorage.network;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChannelViewers {
    private static final ConcurrentHashMap<String, Set<UUID>> viewers = new ConcurrentHashMap<>();

    public static Boolean getViewersFor(String channel) {
        if (!viewers.containsKey(channel)) return false;
        return viewers.get(channel).size() > 0;
    }

    static void addViewerFor(String channel, UUID uuid) {
        if (!viewers.containsKey(channel)) viewers.put(channel, ConcurrentHashMap.newKeySet());
        viewers.get(channel).add(uuid);
    }

    public static void addViewerFor(String channel, PlayerEntity player) {
        addViewerFor(channel, player.getUuid());
        if (!player.world.isClient)
            UpdateViewerList.sendPacket(player.getServer(), channel, player.getUuid(), true);
    }

    static void removeViewerFor(String channel, UUID player) {
        viewers.getOrDefault(channel, ConcurrentHashMap.newKeySet()).remove(player);
    }

    private static void removeViewerForServer(String channel, UUID player, MinecraftServer server) {
        removeViewerFor(channel, player);
        UpdateViewerList.sendPacket(server, channel, player, false);
    }

    public static void registerChannelWatcher() {
        ServerTickCallback.EVENT.register(server -> {
            for (String channel : ChannelViewers.viewers.keySet())
                for (UUID uuid : ChannelViewers.viewers.get(channel)) {
                    PlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                    if (player == null || !(player.container instanceof LinkedContainer)) {
                        removeViewerForServer(channel, uuid, server);
                    }
                }
        });
    }
}