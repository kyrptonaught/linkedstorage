package net.kyrptonaught.linkedstorage;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
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
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class LinkedStorageMod implements ModInitializer {
    public static final String MOD_ID = "linkedstorage";

    private static final RegistryKey<ItemGroup> ITEM_GROUP = RegistryKey.of(RegistryKeys.ITEM_GROUP, new Identifier(MOD_ID, "linkedstorage"));
    public static SpecialRecipeSerializer<TriDyableRecipe> triDyeRecipe;
    public static RecipeSerializer<CopyDyeRecipe> copyDyeRecipe;
    private static ChannelManager CMAN; //lol

    public static final ScreenHandlerType<LinkedContainer> LINKED_SCREEN_HANDLER_TYPE = ScreenHandlerRegistry.registerExtended(new Identifier(MOD_ID, "linkedstorage"), LinkedContainer::new);

    @Override
    public void onInitialize() {
        ModBlocks.register();
        ModItems.register();
        Registry.register(Registries.ITEM_GROUP, ITEM_GROUP,
                FabricItemGroup.builder()
                        .displayName(Text.translatable("itemGroup.linkedstorage.linkedstorage"))
                        .icon(() -> new ItemStack(ModBlocks.storageBlock))
                        .entries((context, entries) -> {
                            entries.add(ModBlocks.storageBlock);
                            entries.add(ModItems.storageItem);
                        })
                        .build());
        SetDyePacket.registerReceivePacket();
        OpenStoragePacket.registerReceivePacket();
        ChannelViewers.registerChannelWatcher();
        triDyeRecipe = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "tri_dyable_recipe"), new SpecialRecipeSerializer<>(TriDyableRecipe::new));
        copyDyeRecipe = Registry.register(Registries.RECIPE_SERIALIZER, new Identifier(MOD_ID, "copy_dye_recipe"), new CopyDyeRecipe.Serializer());
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            CMAN = (ChannelManager) server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(ChannelManager::fromNbt, ChannelManager::new, MOD_ID);
        });
    }

    public static LinkedInventory getInventory(DyeChannel dyeChannel) {
        if (CMAN == null) return new LinkedInventory();
        return CMAN.getInv(dyeChannel);
    }
}
