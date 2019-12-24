package net.kyrptonaught.linkedstorage.network;

import io.netty.buffer.Unpooled;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.server.network.packet.CustomPayloadC2SPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class SetDyePacket {
    private static final Identifier DYE_SET_PACKET = new Identifier(LinkedStorageMod.MOD_ID, "dyesetpacket");

    public static void registerReceivePacket() {
        ServerSidePacketRegistry.INSTANCE.register(DYE_SET_PACKET, (packetContext, packetByteBuf) -> {
            int slot = packetByteBuf.readInt();
            BlockPos pos = packetByteBuf.readBlockPos();
            packetContext.getTaskQueue().execute(() -> {
                PlayerEntity player = packetContext.getPlayer();
                int dye = ((DyeItem) player.getMainHandStack().getItem()).getColor().getId();
                LinkedInventoryHelper.setBlockDye(slot, dye, packetContext.getPlayer().getEntityWorld(), pos);
                if (!player.isCreative())
                    player.getMainHandStack().decrement(1);
            });
        });
    }

    @Environment(EnvType.CLIENT)
    public static void sendPacket(int slot, BlockPos pos) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(slot);
        buf.writeBlockPos(pos);
        MinecraftClient.getInstance().getNetworkHandler().getConnection().send(new CustomPayloadC2SPacket(DYE_SET_PACKET, new PacketByteBuf(buf)));
    }
}