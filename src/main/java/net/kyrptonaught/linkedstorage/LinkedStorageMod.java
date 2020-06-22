package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.network.OpenStoragePacket;
import net.kyrptonaught.linkedstorage.network.SetDyePacket;
import net.kyrptonaught.linkedstorage.recipe.CopyDyeRecipe;
import net.kyrptonaught.linkedstorage.recipe.TriDyableRecipe;
import net.kyrptonaught.linkedstorage.register.ModBlocks;
import net.kyrptonaught.linkedstorage.register.ModItems;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.kyrptonaught.linkedstorage.util.Migrator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.WorldSavePath;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";
    public static final ItemGroup GROUP = FabricItemGroupBuilder.build(new Identifier(MOD_ID, "linkedstorage"), () -> new ItemStack(ModBlocks.storageBlock));
    public static SpecialRecipeSerializer<TriDyableRecipe> triDyeRecipe;
    public static RecipeSerializer<CopyDyeRecipe> copyDyeRecipe;
    private static ChannelManager CMAN; //lol

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        ContainerProviderRegistry.INSTANCE.registerFactory(new Identifier(MOD_ID, "linkedstorage"), (syncId, id, player, buf) -> getContainer(syncId, player, DyeChannel.fromBuf(buf)));
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
        ChannelViewers.registerChannelWatcher();
        triDyeRecipe = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "tri_dyable_recipe"), new SpecialRecipeSerializer<>(TriDyableRecipe::new));
        copyDyeRecipe = Registry.register(Registry.RECIPE_SERIALIZER, new Identifier(MOD_ID, "copy_dye_recipe"), new CopyDyeRecipe.Serializer());

        ServerStartCallback.EVENT.register(server -> {
            CMAN = server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(() -> new ChannelManager(MOD_ID), MOD_ID);
            Migrator.Migrate(server.getSavePath(WorldSavePath.ROOT).toFile(), CMAN);//this took forever to figure out
        });
    }

    static LinkedContainer getContainer(int id, PlayerEntity player, DyeChannel channel) {
        ChannelViewers.addViewerFor(channel.getChannelName(), player);
        return new LinkedContainer(id, player.inventory, getInventory(channel));
    }

    public static LinkedInventory getInventory(DyeChannel dyeChannel) {
        return CMAN.getInv(dyeChannel);
    }
}