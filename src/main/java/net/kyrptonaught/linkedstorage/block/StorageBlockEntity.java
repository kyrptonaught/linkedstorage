package net.kyrptonaught.linkedstorage.block;

import com.google.common.base.Preconditions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class StorageBlockEntity extends OpenableBlockEntity {
    private DyeChannel dyeChannel = DyeChannel.defaultChannel();
    private LinkedInventory linkedInventory;

    StorageBlockEntity(BlockPos pos, BlockState state) {
        super(StorageBlock.blockEntity, pos, state);
    }

    StorageBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void readNbt(NbtCompound compoundTag_1) {
        super.readNbt(compoundTag_1);
        dyeChannel = DyeChannel.fromTag(compoundTag_1);
        this.markDirty();
    }

    @Override
    public void writeNbt(NbtCompound compoundTag_1) {
        super.writeNbt(compoundTag_1);
        dyeChannel.toTag(compoundTag_1);
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
    public final BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public final NbtCompound toInitialChunkDataNbt() {
        NbtCompound nbt = super.toInitialChunkDataNbt();
        writeNbt(nbt);
        return nbt;
    }

    // Thank you Fabric API
    public void sync() {
        Preconditions.checkNotNull(world); // Maintain distinct failure case from below
        if (!(world instanceof ServerWorld))
            throw new IllegalStateException("Cannot call sync() on the logical client! Did you check world.isClient first?");

        ((ServerWorld) world).getChunkManager().markForUpdate(getPos());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public int countViewers() {
        return ChannelViewers.getViewersFor(dyeChannel.getChannelName()) ? 1 : 0;
    }
}