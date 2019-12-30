package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.util.ChannelManager;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;

public class StorageBlockEntity extends OpenableBlockEntity implements BlockEntityClientSerializable {
    private byte[] dyeChannel = LinkedInventoryHelper.getDefaultChannel();
    private LinkedInventory linkedInventory;

    private StorageBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    public StorageBlockEntity() {
        this(StorageBlock.blockEntity);
    }

    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        if (compoundTag_1.contains("dyechannel", 11)) {
            int[] oldChannel = compoundTag_1.getIntArray("dyechannel");
            this.dyeChannel = new byte[]{(byte) oldChannel[0], (byte) oldChannel[1], (byte) oldChannel[2]};
        }
        if (compoundTag_1.contains("dyechannel", 7)) {
            this.dyeChannel = compoundTag_1.getByteArray("dyechannel");
        }
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
        compoundTag_1.putByteArray("dyechannel", dyeChannel);
        return compoundTag_1;
    }

    public void setDye(int slot, int dye) {
        this.dyeChannel[slot] = (byte) dye;
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public void setChannel(byte[] channel) {
        this.dyeChannel = channel;
        updateInventory();
        this.markDirty();
        if (!world.isClient) sync();
    }

    public byte[] getChannel() {
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

    @Override
    @Environment(EnvType.CLIENT)
    public int countViewers() {
        return ChannelViewers.getViewersFor(LinkedInventoryHelper.getChannelName(dyeChannel)) ? 1 : 0;
    }
}