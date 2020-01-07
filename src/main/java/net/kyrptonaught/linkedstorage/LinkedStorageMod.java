package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.network.OpenStoragePacket;
import net.kyrptonaught.linkedstorage.network.SetDyePacket;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "linkedstorage"), () -> new ItemStack(ModBlocks.storageBlock));

    @Override
    public void onInitialize() {
        ChannelManager.init();
        ModBlocks.register();
        ModItems.register();
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, buf.readByteArray()));
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
        ChannelViewers.registerChannelWatcher();
    }

    static LinkedContainer getContainer(int id, PlayerEntity player, byte[] channel) {
        DyeChannel dyeChannel = new DyeChannel(channel);
        ChannelViewers.addViewerFor(dyeChannel.getChannelName(), player);
        return new LinkedContainer(id, player.inventory, ChannelManager.getManager(player.getEntityWorld().getLevelProperties()).getInv(dyeChannel), channel);
    }
}