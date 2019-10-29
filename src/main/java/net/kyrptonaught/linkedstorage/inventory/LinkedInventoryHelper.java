package net.kyrptonaught.linkedstorage.inventory;

import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LinkedInventoryHelper {

    public static void setBlockChannel(String channel, World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        sbe.setChannel(channel);
    }

    public static void setItemChannel(String channel, ItemStack stack) {
        stack.getOrCreateTag().putString("channel", channel);
    }

    public static String getBlockChannel(World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        return sbe.getChannel();
    }

    public static String getItemChannel(ItemStack stack) {
        if (!itemHasChannel(stack)) setItemChannel("", stack);
        return stack.getOrCreateTag().getString("channel");
    }

    public static Boolean itemHasChannel(ItemStack stack) {
        return stack.getOrCreateTag().containsKey("channel");
    }
}