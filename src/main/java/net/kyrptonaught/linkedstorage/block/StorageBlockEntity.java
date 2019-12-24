package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;


public class StorageBlockEntity extends BlockEntity implements BlockEntityClientSerializable {
    private int[] dyeChannel = new int[]{DyeColor.WHITE.getId(), DyeColor.WHITE.getId(), DyeColor.WHITE.getId()};

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

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        compoundTag_1.putIntArray("dyechannel", dyeChannel);
        return compoundTag_1;
    }

    public void setDye(int slot, int dye) {
        this.dyeChannel[slot] = dye;
        this.markDirty();
        if (!world.isClient) sync();
    }

    public void setChannel(int[] channel) {
        this.dyeChannel = channel;
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
