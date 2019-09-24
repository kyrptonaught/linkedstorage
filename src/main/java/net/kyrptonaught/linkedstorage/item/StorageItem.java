package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

public class StorageItem extends Item {
    public StorageItem(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageitem"), this);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        if (!world.isClient && !playerEntity.isSneaking()) {
            String channel = getChannel(stack, world);
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), playerEntity, (buf) -> buf.writeString(channel));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    public static String getChannel(ItemStack stack, World world) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.containsKey("channel"))
            tag.putString("channel", ChannelManager.getRandomKey(world));
        return tag.getString("channel");
    }
}
