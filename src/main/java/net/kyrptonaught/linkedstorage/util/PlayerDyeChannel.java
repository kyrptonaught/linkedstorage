package net.kyrptonaught.linkedstorage.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DyeColor;

import java.util.UUID;

public class PlayerDyeChannel extends DyeChannel {
    public UUID playerID;

    public PlayerDyeChannel(UUID playerID, byte[] dyeChannel) {
        super(dyeChannel);
        this.playerID = playerID;
        super.type = 1;
    }

    public String getCleanName() {
        return "Player: " + playerID + ", " + DyeColor.byId(super.dyeChannel[0]).getName() + ", " + DyeColor.byId(super.dyeChannel[1]).getName() + ", " + DyeColor.byId(super.dyeChannel[2]).getName();
    }

    public CompoundTag toTag(CompoundTag tag) {
        tag.putUuid("playerid", playerID);
        return super.toTag(tag);
    }
}
