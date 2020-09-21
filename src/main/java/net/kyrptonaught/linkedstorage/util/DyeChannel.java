package net.kyrptonaught.linkedstorage.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.DyeColor;

import java.util.UUID;

public class DyeChannel {
    public byte[] dyeChannel;
    protected int type = 0;

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

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DyeChannel)) return false;
        DyeChannel otherChannel = (DyeChannel) other;
        if (type != otherChannel.type)
            return false;
        if (otherChannel instanceof PlayerDyeChannel)
            if (!((PlayerDyeChannel) otherChannel).playerID.equals(((PlayerDyeChannel) this).playerID))
                return false;

        return getChannelName().equals(otherChannel.getChannelName());
    }

    public PlayerDyeChannel toPlayerDyeChannel(UUID playerid) {
        return new PlayerDyeChannel(playerid, dyeChannel.clone());
    }

    public void toBuf(PacketByteBuf buffer) {
        buffer.writeCompoundTag(toTag(new CompoundTag()));
    }

    public static DyeChannel fromBuf(PacketByteBuf buffer) {
        return fromTag(buffer.readCompoundTag());
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putInt("type", type);
        tag.putByteArray("dyechannel", dyeChannel);
        return tag;
    }

    public static DyeChannel fromTag(CompoundTag tag) {
        DyeChannel dyeChannel = defaultChannel();
        if (tag.contains("dyechannel", 11)) {
            int[] oldChannel = tag.getIntArray("dyechannel");
            dyeChannel = new DyeChannel(new byte[]{(byte) oldChannel[0], (byte) oldChannel[1], (byte) oldChannel[2]});
        }
        if (tag.contains("dyechannel", 7))
            dyeChannel = new DyeChannel(tag.getByteArray("dyechannel"));
        int type = tag.getInt("type");
        if (type == 1)
            return dyeChannel.toPlayerDyeChannel(tag.getUuid("playerid"));
        return dyeChannel;
    }
}