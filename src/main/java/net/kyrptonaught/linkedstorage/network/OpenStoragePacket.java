package net.kyrptonaught.linkedstorage.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class OpenStoragePacket {
    private static final Identifier OPEN_STORAGE_PACKET = new Identifier(LinkedStorageMod.MOD_ID, "openpacket");

    public static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(OPEN_STORAGE_PACKET, (packetContext, packetByteBuf) -> {
            BlockPos pos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                World world = player.getEntityWorld();
                ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), packetContext.getPlayer(), (buf) -> buf.writeByteArray(LinkedInventoryHelper.getBlockChannel(world, pos)));
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeBlockPos(pos);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(OPEN_STORAGE_PACKET, new PacketByteBuf(buf)));
    }
}