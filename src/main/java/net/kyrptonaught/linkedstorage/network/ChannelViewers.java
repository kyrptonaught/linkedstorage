package net.kyrptonaught.linkedstorage.network;

import net.fabricmc.fabric.api.event.server.ServerTickCallback;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;

public class ChannelViewers {
    private static HashMap<String, HashSet<String>> viewers = new HashMap<>();

    public static Boolean getViewersFor(String channel) {
        if (!viewers.containsKey(channel)) return false;
        return viewers.get(channel).size() > 0;
    }

    static void addViewerFor(String channel, String uuid) {
        if (!viewers.containsKey(channel)) viewers.put(channel, new HashSet<>());
        viewers.get(channel).add(uuid);
    }

    public static void addViewerFor(String channel, PlayerEntity player) {
        addViewerFor(channel, player.getUuidAsString());
        if (!player.world.isClient)
            UpdateViewerList.sendPacket(player.getServer(), channel, player.getUuid(), true);
    }

    static void removeViewerFor(String channel, String player) {
        viewers.getOrDefault(channel, new HashSet<>()).remove(player);
    }

    private static void removeViewerForServer(String channel, String player, MinecraftServer server) {
        removeViewerFor(channel, player);
        UpdateViewerList.sendPacket(server, channel, UUID.fromString(player), false);
    }

    public static void registerChannelWatcher() {
        ServerTickCallback.EVENT.register(server -> {
            for (String channel : ChannelViewers.viewers.keySet())
                for (String uuid : ChannelViewers.viewers.get(channel)) {
                    PlayerEntity player = server.getPlayerManager().getPlayer(UUID.fromString(uuid));
                    if (player == null || !(player.container instanceof LinkedContainer)) {
                        removeViewerForServer(channel, uuid, server);
                    }
                }
        });
    }
}