package net.kyrptonaught.linkedstorage.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;

import java.util.List;
import java.util.UUID;

public class DyeChannel implements Cloneable {
    public byte[] dyeChannel;
    protected int type = 0;

    public DyeChannel(byte[] dyeChannel) {
        this.dyeChannel = dyeChannel;
    }

    public void setSlot(int slot, byte dye) {
        dyeChannel[slot] = dye;
    }

    public String getChannelName() {
        return dyeChannel[0] + ":" + dyeChannel[1] + ":" + dyeChannel[2];
    }

    public List<Text> getCleanName() {
        Text dyechannel = Text.translatable("item.minecraft.firework_star." + DyeColor.byId(dyeChannel[0]).getName()).append(", ")
                .append(Text.translatable("item.minecraft.firework_star." + DyeColor.byId(dyeChannel[1]).getName())).append(", ")
                .append(Text.translatable("item.minecraft.firework_star." + DyeColor.byId(dyeChannel[2]).getName()));

        return List.of(Text.translatable("text.linkeditem.channel", dyechannel));
    }

    public String getSaveName() {
        return getChannelName();
    }

    public DyeChannel clone() {
        return new DyeChannel(dyeChannel.clone());
    }

    public static DyeChannel defaultChannel() {
        return new DyeChannel(new byte[]{(byte) DyeColor.WHITE.getId(), (byte) DyeColor.WHITE.getId(), (byte) DyeColor.WHITE.getId()});
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof DyeChannel otherChannel)) return false;
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
        buffer.writeNbt(toTag(new NbtCompound()));
    }

    public static DyeChannel fromBuf(PacketByteBuf buffer) {
        return fromTag(buffer.readNbt());
    }

    public NbtCompound toTag(NbtCompound tag) {
        tag.putInt("type", type);
        tag.putByteArray("dyechannel", dyeChannel);
        return tag;
    }

    public static DyeChannel fromTag(NbtCompound tag) {
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
