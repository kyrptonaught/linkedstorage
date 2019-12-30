package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.client.DummyStorageBlockEntity;
import net.kyrptonaught.linkedstorage.client.DummyStorageBlockEntityRenderer;
import net.kyrptonaught.linkedstorage.client.StorageBlockRenderer;
import net.kyrptonaught.linkedstorage.client.StorageContainerScreen;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.network.UpdateViewerList;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class LinkedStorageModClient implements ClientModInitializer {
    public static final Identifier TEXTURE = new Identifier(LinkedStorageMod.MOD_ID, "block/linkedstorage");
    public static BlockEntityType<DummyStorageBlockEntity> dummy = BlockEntityType.Builder.create(DummyStorageBlockEntity::new, ModBlocks.storageBlock).build(null);

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(dummy, DummyStorageBlockEntityRenderer::new);
        BlockEntityRendererRegistry.INSTANCE.register(StorageBlock.blockEntity, StorageBlockRenderer::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), (syncId, identifier, player, buf) -> {
            byte[] channel = buf.readByteArray();
            return new StorageContainerScreen(LinkedStorageMod.getContainer(syncId, player, channel), player.inventory, channel);
        });

        ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
            if (layer == 0 || !LinkedInventoryHelper.itemHasChannel(stack))
                return DyeColor.WHITE.getMaterialColor().color;
            byte[] colors = LinkedInventoryHelper.getItemChannel(stack);
            return DyeColor.byId(colors[layer - 1]).getMaterialColor().color;
        }, ModItems.storageItem);
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(TEXTURE);
        });
        UpdateViewerList.registerReceivePacket();
    }


}