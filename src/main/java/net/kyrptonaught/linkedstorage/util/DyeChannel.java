package net.kyrptonaught.linkedstorage.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;

public class DyeChannel {
    public byte[] dyeChannel;

    public DyeChannel(byte[] dyeChannel) {
        this.dyeChannel = dyeChannel;
    }

    public void setSlot(int slot, byte dye) {
        dyeChannel[slot] = dye;
    }

    public String getChannelName() {
        return dyeChannel[0] + "" + dyeChannel[1] + "" + dyeChannel[2];
    }

    public String getCleanName() {
        return DyeColor.byId(dyeChannel[0]).getName() + ", " + DyeColor.byId(dyeChannel[1]).getName() + ", " + DyeColor.byId(dyeChannel[2]).getName();
    }

    public DyeChannel clone() {
        return new DyeChannel(dyeChannel.clone());
    }

    public static DyeChannel defaultChannel() {
        return new DyeChannel(new byte[]{(byte) DyeColor.WHITE.getId(), (byte) DyeColor.WHITE.getId(), (byte) DyeColor.WHITE.getId()});
    }

    public void toTag(CompoundTag tag) {
        tag.putByteArray("dyechannel", dyeChannel);
    }

    public static DyeChannel fromTag(CompoundTag tag) {
        if (tag.contains("dyechannel", 11)) {
            int[] oldChannel = tag.getIntArray("dyechannel");
            return new DyeChannel(new byte[]{(byte) oldChannel[0], (byte) oldChannel[1], (byte) oldChannel[2]});
        }
        if (tag.contains("dyechannel", 7))
            return new DyeChannel(tag.getByteArray("dyechannel"));
        return defaultChannel();
    }
}