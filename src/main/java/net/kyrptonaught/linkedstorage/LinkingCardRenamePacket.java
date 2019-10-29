package net.kyrptonaught.linkedstorage;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;

import java.nio.charset.StandardCharsets;

public class LinkingCardRenamePacket {
    private static final Identifier LINK_CARD_CHANNEL = new Identifier(LinkedStorageMod.MOD_ID, "linkingcardchannel");

    static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(LINK_CARD_CHANNEL, (packetContext, packetByteBuf) -> {
            String newChannel = new String(packetByteBuf.readByteArray());
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                LinkedInventoryHelper.setItemChannel(newChannel, player.getMainHandStack());
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(String channelName) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeByteArray(channelName.getBytes(StandardCharsets.UTF_8));
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(LINK_CARD_CHANNEL, new PacketByteBuf(buf)));
    }
}