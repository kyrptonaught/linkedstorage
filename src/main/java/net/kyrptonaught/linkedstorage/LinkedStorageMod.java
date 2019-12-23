package net.kyrptonaught.linkedstorage;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.LevelComponentCallback;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.kyrptonaught.linkedstorage.util.StorageManagerComponent;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";
    public static final ComponentType<StorageManagerComponent> CMAN = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(LinkedStorageMod.MOD_ID, "sman"), StorageManagerComponent.class);
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "linkedstorage"), () -> new ItemStack(ModBlocks.storageBlock));
    @Override
    public void onInitialize() {
        LevelComponentCallback.EVENT.register((levelProperties, components) -> components.put(CMAN, new ChannelManager()));
        ModBlocks.register();
       ModItems.register();
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, buf.readIntArray()));
        SetDyePacket.registerReceivePacket();
    }

    static Container getContainer(int id, PlayerEntity player, int[] channel) {
        return GenericContainer.createGeneric9x3(id, player.inventory, CMAN.get(player.getEntityWorld().getLevelProperties()).getValue().getInv(channel));
    }
}