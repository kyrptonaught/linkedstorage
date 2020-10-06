package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class StorageBlockEntity extends OpenableBlockEntity implements BlockEntityClientSerializable {
    private DyeChannel dyeChannel = DyeChannel.defaultChannel();
    private LinkedInventory linkedInventory;

    private StorageBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    public StorageBlockEntity() {
        this(StorageBlock.blockEntity);
    }

    @Override
    public void fromTag(BlockState state, CompoundTag compoundTag_1) {
        super.fromTag(state, compoundTag_1);
        dyeChannel = DyeChannel.fromTag(compoundTag_1);
        this.markDirty();
    }

    LinkedInventory getLinkedInventory() {
        if (linkedInventory == null) updateInventory();
        return linkedInventory;
    }

    private void updateInventory() {
        if (!world.isClient) {
            linkedInventory = LinkedStorageMod.getInventory(dyeChannel);
        }
    }

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        dyeChannel.toTag(compoundTag_1);
        return compoundTag_1;
    }

    public void setDye(int slot, int dye) {
        dyeChannel.setSlot(slot, (byte) dye);
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public void setChannel(DyeChannel channel) {
        this.dyeChannel = channel;
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public DyeChannel getChannel() {
        return dyeChannel;
    }

    @Override
    public void fromClientTag(CompoundTag tag) {
        dyeChannel = DyeChannel.fromTag(tag);
        this.markDirty();
    }

    @Override
    public CompoundTag toClientTag(CompoundTag tag) {
        return toTag(tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int countViewers() {
        return ChannelViewers.getViewersFor(dyeChannel.getChannelName()) ? 1 : 0;
    }
}