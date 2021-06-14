package net.kyrptonaught.linkedstorage.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.DyeColor;

import java.util.UUID;

public class PlayerDyeChannel extends DyeChannel {
    public UUID playerID;
    private String playerName;

    public PlayerDyeChannel(UUID playerID, byte[] dyeChannel) {
        super(dyeChannel);
        this.playerID = playerID;
        super.type = 1;
    }

    @Override
    public String getChannelName() {
        return playerID + super.getChannelName();
    }

    @Override
    public DyeChannel clone() {
        return new PlayerDyeChannel(playerID, dyeChannel.clone());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public String getCleanName() {
        // if (playerName == null)
        //  playerName = MinecraftClient.getInstance().getNetworkHandler().getPlayerListEntry(playerID).getDisplayName().asFormattedString();
        return "Playerbound, " + DyeColor.byId(super.dyeChannel[0]).getName() + ", " + DyeColor.byId(super.dyeChannel[1]).getName() + ", " + DyeColor.byId(super.dyeChannel[2]).getName();
    }


    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putUuid("playerid", playerID);
        return super.toTag(tag);
    }
}
