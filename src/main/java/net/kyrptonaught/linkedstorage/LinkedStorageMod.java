package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.network.OpenStoragePacket;
import net.kyrptonaught.linkedstorage.network.SetDyePacket;
import net.kyrptonaught.linkedstorage.recipe.CopyDyeRecipe;
import net.kyrptonaught.linkedstorage.recipe.TriDyableRecipe;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "linkedstorage"), () -> new ItemStack(ModBlocks.storageBlock));
    public static SpecialRecipeSerializer<TriDyableRecipe> triDyeRecipe;
    public static RecipeSerializer<CopyDyeRecipe> copyDyeRecipe;

    @Override
    public void onInitialize() {
        ChannelManager.init();
        ModBlocks.register();
        ModItems.register();
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, buf.readByteArray()));
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
        ChannelViewers.registerChannelWatcher();
        triDyeRecipe = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "tri_dyable_recipe"), new SpecialRecipeSerializer<>(TriDyableRecipe::new));
        copyDyeRecipe = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "copy_dye_recipe"), new CopyDyeRecipe.Serializer());
    }

    static LinkedContainer getContainer(int id, PlayerEntity player, byte[] channel) {
        DyeChannel dyeChannel = new DyeChannel(channel);
        ChannelViewers.addViewerFor(dyeChannel.getChannelName(), player);
        return new LinkedContainer(id, player.inventory, ChannelManager.getManager(player.getEntityWorld().getLevelProperties()).getInv(dyeChannel), channel);
    }
}