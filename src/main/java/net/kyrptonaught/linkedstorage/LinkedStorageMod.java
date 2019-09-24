package net.kyrptonaught.linkedstorage;

import nerdhub.cardinal.components.api.ComponentRegistry;
import nerdhub.cardinal.components.api.ComponentType;
import nerdhub.cardinal.components.api.event.LevelComponentCallback;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.item.LinkingCard;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.kyrptonaught.linkedstorage.util.StorageManagerComponent;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.container.Container;
import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;

public class LinkedStorageMod implements ModInitializer, ClientModInitializer {
    public static final String MOD_ID = "linkedstorage";

    public static final ComponentType<StorageManagerComponent> CMAN = ComponentRegistry.INSTANCE.registerIfAbsent(new Identifier(LinkedStorageMod.MOD_ID, "sman"), StorageManagerComponent.class);

    @Override
    public void onInitialize() {
        LevelComponentCallback.EVENT.register((levelProperties, components) -> components.put(CMAN, new ChannelManager()));
        new StorageBlock(Block.Settings.of(Material.METAL).strength(2.5f, 2.5f));
        new StorageItem(new Item.Settings().group(ItemGroup.REDSTONE));
        new LinkingCard(new Item.Settings().group(ItemGroup.REDSTONE));
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, buf.readString()));
    }

    @Override
    public void onInitializeClient() {
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, identifier, player, buf) ->
        {
            String channel = buf.readString();
            return new StorageContainerScreen(getContainer(syncId, player, channel), player.inventory, channel);
        });
    }

    private Container getContainer(int id, PlayerEntity player, String channel) {
        return GenericContainer.createGeneric9x3(id, player.inventory, CMAN.get(player.getEntityWorld().getLevelProperties()).getValue().getInv(channel));
    }
}