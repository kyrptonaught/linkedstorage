package net.kyrptonaught.linkedstorage.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;

import java.util.UUID;

public class UpdateViewerList {
    private static final Identifier UPDATE_VIEWERS = new Identifier(LinkedStorageMod.MOD_ID, "updateviewers");

    public static void registerReceivePacket() {
        ClientPlayNetworking.registerGlobalReceiver(UPDATE_VIEWERS, (client, handler, buf, responseSender) -> {
            String channel = buf.readString(32767);
            UUID uuid = buf.readUuid();
            boolean adding = buf.readBoolean();
            client.execute(() -> {
                if (adding)
                    ChannelViewers.addViewerFor(channel, uuid);
                else
                    ChannelViewers.removeViewerFor(channel, uuid);
            });
        });
    }

    static void sendPacket(MinecraftServer server, String channel, UUID uuid, Boolean add) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeString(channel);
        buf.writeUuid(uuid);
        buf.writeBoolean(add);
        server.getPlayerManager().sendToAll(new CustomPayloadS2CPacket(UPDATE_VIEWERS, new PacketByteBuf(buf)));
    }
}
