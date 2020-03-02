package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screen.ScreenProviderRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.client.StorageBlockRenderer;
import net.kyrptonaught.linkedstorage.client.StorageContainerScreen;
import net.kyrptonaught.linkedstorage.network.UpdateViewerList;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class LinkedStorageModClient implements ClientModInitializer {
    public static final Identifier TEXTURE = new Identifier(LinkedStorageMod.MOD_ID, "block/linkedstorage");

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(StorageBlock.blockEntity, StorageBlockRenderer::new);
        ScreenProviderRegistry.INSTANCE.registerFactory(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), (syncId, identifier, player, buf) -> {
            byte[] channel = buf.readByteArray();
            return new StorageContainerScreen(LinkedStorageMod.getContainer(syncId, player, channel), player.inventory, channel);
        });

        ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
            if (layer == 0)
                return DyeColor.WHITE.getMaterialColor().color;
            byte[] colors = LinkedInventoryHelper.getItemChannel(stack).dyeChannel;
            return DyeColor.byId(colors[layer - 1]).getMaterialColor().color;
        }, ModItems.storageItem, ModBlocks.storageBlock);

        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((atlasTexture, registry) -> {
            registry.register(TEXTURE);
        });
        UpdateViewerList.registerReceivePacket();
    }
}