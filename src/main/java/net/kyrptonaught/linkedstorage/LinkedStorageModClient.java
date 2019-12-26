package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.block.StorageBlockRenderer;
import net.kyrptonaught.linkedstorage.client.StorageContainerScreen;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class LinkedStorageModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(StorageBlock.blockEntity, StorageBlockRenderer::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), (syncId, identifier, player, buf) ->
        {
            byte[] channel = buf.readByteArray();
            return new StorageContainerScreen(LinkedStorageMod.getContainer(syncId, player, channel), player.inventory, LinkedInventoryHelper.getChannelName(channel));
        });
        ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
            if (layer == 0) return DyeColor.WHITE.getMaterialColor().color;
            byte[] colors = LinkedInventoryHelper.getItemChannel(stack);
            return DyeColor.byId(colors[layer - 1]).getMaterialColor().color;
        }, ModItems.storageItem, ModBlocks.storageBlockItem, ModBlocks.storageBlock);
    }
}