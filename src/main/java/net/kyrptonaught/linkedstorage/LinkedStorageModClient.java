package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.client.ClientSpriteRegistryCallback;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.impl.client.rendering.ColorProviderRegistryImpl;
import net.fabricmc.loader.api.FabricLoader;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.client.StorageBlockRenderer;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.network.UpdateViewerList;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.util.PlayerDyeChannel;
import net.minecraft.client.gui.screen.ingame.GenericContainerScreen;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;


@Environment(EnvType.CLIENT)
public class LinkedStorageModClient implements ClientModInitializer {
    public static final Identifier TEXTURE = new Identifier(LinkedStorageMod.MOD_ID, "block/linkedstorage");

    @Override
    public void onInitializeClient() {
        BlockEntityRendererRegistry.INSTANCE.register(StorageBlock.blockEntity, StorageBlockRenderer::new);
        FabricModelPredicateProviderRegistry.register(ModItems.storageItem, new Identifier("open"), (stack, world, entity) -> {
            String channel = LinkedInventoryHelper.getItemChannel(stack).getChannelName();
            return ChannelViewers.getViewersFor(channel) ? 1 : 0;
        });
        ScreenRegistry.register(LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE, GenericContainerScreen::new);
        ColorProviderRegistryImpl.ITEM.register((stack, layer) -> {
            DyeChannel dyeChannel = LinkedInventoryHelper.getItemChannel(stack);
            if (layer > 0 && layer < 4) {
                byte[] colors = dyeChannel.dyeChannel;
                return DyeColor.byId(colors[layer - 1]).getMaterialColor().color;
            }
            if (layer == 4 && dyeChannel instanceof PlayerDyeChannel)
                return DyeColor.LIGHT_BLUE.getMaterialColor().color;
            return DyeColor.WHITE.getMaterialColor().color;
        }, ModItems.storageItem, ModBlocks.storageBlock);
        ClientSpriteRegistryCallback.event(TexturedRenderLayers.CHEST_ATLAS_TEXTURE).register((atlasTexture, registry) -> registry.register(TEXTURE));
        UpdateViewerList.registerReceivePacket();
        FabricLoader.getInstance().getModContainer(LinkedStorageMod.MOD_ID).ifPresent(modContainer -> {
            ResourceManagerHelper.registerBuiltinResourcePack(new Identifier(LinkedStorageMod.MOD_ID, "enderstorage"), "resourcepacks/enderstorage", modContainer, false);
        });
    }

}