package net.kyrptonaught.linkedstorage.block;


import com.sun.org.apache.xpath.internal.operations.Bool;
import net.fabricmc.fabric.api.block.entity.BlockEntityClientSerializable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Nameable;

public class StorageBlockEntity extends BlockEntity implements BlockEntityClientSerializable, Nameable {
    private String channel = "";

    private StorageBlockEntity(BlockEntityType<?> blockEntityType_1) {
        super(blockEntityType_1);
    }

    StorageBlockEntity() {
        this(StorageBlock.blockEntity);
    }

    public void fromTag(CompoundTag compoundTag_1) {
        super.fromTag(compoundTag_1);
        if (compoundTag_1.containsKey("channel"))
            this.channel = compoundTag_1.getString("channel");
    }

    public CompoundTag toTag(CompoundTag compoundTag_1) {
        super.toTag(compoundTag_1);
        compoundTag_1.putString("channel", channel);
        return compoundTag_1;
    }

    public void setChannel(String channel) {
        this.channel = channel;
        this.markDirty();
    }

    public String getChannel() {
        return channel;
    }

    public void resetChannel() {
        this.channel = "";
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
    public Text getName() {
        return new LiteralText(getChannel());
    }

    @Override
    public boolean hasCustomName() {
        return !getChannel().equals("");
    }
}
