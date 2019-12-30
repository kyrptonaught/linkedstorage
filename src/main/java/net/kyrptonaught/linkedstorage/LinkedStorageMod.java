package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.kyrptonaught.linkedstorage.network.OpenStoragePacket;
import net.kyrptonaught.linkedstorage.network.SetDyePacket;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "linkedstorage"), () -> new ItemStack(ModBlocks.storageBlock));

    @Override
    public void onInitialize() {
        ChannelManager.init();
        ModBlocks.register();
        ModItems.register();
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, buf.readByteArray(), buf.readBlockPos()));
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
    }

    static LinkedContainer getContainer(int id, PlayerEntity player, byte[] channel, BlockPos pos) {
        return new LinkedContainer(id, player.inventory, ChannelManager.getManager(player.getEntityWorld().getLevelProperties()).getInv(channel), pos);
    }
}