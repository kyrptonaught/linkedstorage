package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.BannerBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;

import java.util.function.Supplier;

public class StorageBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int[] dyeChannel = new int[]{DyeColor.WHITE.getId(), DyeColor.WHITE.getId(), DyeColor.WHITE.getId()};
    private LinkedInventory linkedInventory;

    private StorageBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    StorageBlockEntity() {
        this(StorageBlock.blockEntity);
    }

    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        if (compoundTag_1.contains("dyechannel"))
            this.dyeChannel = compoundTag_1.getIntArray("dyechannel");
        this.markDirty();
    }

    LinkedInventory getLinkedInventory() {
        if (linkedInventory == null) updateInventory();
        return linkedInventory;
    }

    private void updateInventory() {
        if (!world.isClient) {
            linkedInventory = ChannelManager.getManager(world.getLevelProperties()).getInv(dyeChannel);
        }
    }

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        compoundTag_1.putIntArray("dyechannel", dyeChannel);
        return compoundTag_1;
    }

    public void setDye(int slot, int dye) {
        this.dyeChannel[slot] = dye;
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public void setChannel(int[] channel) {
        this.dyeChannel = channel;
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public int[] getChannel() {
        return dyeChannel;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        fromTag(tag);
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }
}