package net.kyrptonaught.linkedstorage.inventory;

import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LinkedInventoryHelper {

    public static void setBlockChannel(int[] channel, World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        sbe.setChannel(channel);
    }

    public static void setBlockDye(int slot, int dye, World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        sbe.setDye(slot, dye);
    }

    public static void setItemChannel(int[] channel, ItemStack stack) {
        stack.getOrCreateTag().putIntArray("dyechannel", channel);
    }

    public static int[] getBlockChannel(World world, BlockPos pos) {
        StorageBlockEntity sbe = (StorageBlockEntity) world.getBlockEntity(pos);
        return sbe.getChannel();
    }

    public static int[] getItemChannel(ItemStack stack) {
        if (!itemHasChannel(stack)) setItemChannel(getDefaultChannel(), stack);
        return stack.getOrCreateTag().getIntArray("dyechannel");
    }

    public static int[] getDefaultChannel() {
        return new int[]{DyeColor.WHITE.getId(), DyeColor.WHITE.getId(), DyeColor.WHITE.getId()};
    }

    public static String getChannelName(int[] dyeChannel) {
        return dyeChannel[0] + "" + dyeChannel[1] + "" + dyeChannel[2];

    }

    public static Boolean itemHasChannel(ItemStack stack) {
        return stack.getOrCreateTag().contains("dyechannel");
    }
}