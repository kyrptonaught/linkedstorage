package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LinkedInventoryHelper {
    public static void setBlockChannel(DyeChannel channel, World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        sbe.setChannel(channel.clone());
    }

    public static void setBlockDye(int slot, int dye, World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        sbe.setDye(slot, dye);
    }

    public static void setItemChannel(DyeChannel channel, ItemStack stack) {
        channel.clone().toTag(stack.getOrCreateNbt());
    }

    public static DyeChannel getBlockChannel(World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        return sbe.getChannel();
    }

    public static DyeChannel getItemChannel(ItemStack stack) {
        return DyeChannel.fromTag(stack.getOrCreateNbt());
    }
}