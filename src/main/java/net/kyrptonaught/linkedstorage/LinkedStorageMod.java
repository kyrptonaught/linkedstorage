package net.kyrptonaught.linkedstorage;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.LevelComponentCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.kyrptonaught.linkedstorage.util.StorageManager;
import net.kyrptonaught.linkedstorage.util.StorageManagerComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class LinkedStorageMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "linkedstorage";

    public static final ComponentType<StorageManagerComponent> SMAN = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(LinkedStorageMod.MOD_ID, "sman"), StorageManagerComponent.class);

    @Override
    public void onInitialize() {
        LevelComponentCallback.EVENT.register((levelProperties, components) -> components.put(SMAN, new StorageManager()));
        new StorageBlock(Block.Settings.copy(Blocks.ENDER_CHEST));
        new StorageItem(new Item.Settings().group(ItemGroup.REDSTONE));
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> createContainer(syncId, player, buf.readInt()));
    }

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, identifier, player, buf) -> new StorageContainerScreen(createContainer(syncId, player,buf.readInt()), player.inventory));
    }

    public Container createContainer(int id, PlayerEntity player, int channel) {
        return GenericContainer.createGeneric9x3(id, player.inventory, SMAN.get(player.getEntityWorld().getLevelProperties()).getValue().getInv(channel));
    }
}