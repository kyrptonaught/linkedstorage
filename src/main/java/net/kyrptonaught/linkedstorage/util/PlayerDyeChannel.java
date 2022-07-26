package net.kyrptonaught.linkedstorage.util;

import com.mojang.authlib.GameProfile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlayerDyeChannel extends DyeChannel {
    public UUID playerID;
    private Text playerName;

    public PlayerDyeChannel(UUID playerID, byte[] dyeChannel) {
        super(dyeChannel);
        this.playerID = playerID;
        super.type = 1;
    }

    @Override
    public String getChannelName() {
        return playerID + ":" + super.getChannelName();
    }

    @Override
    public String getSaveName() {
        return super.getChannelName();
    }

    @Override
    public DyeChannel clone() {
        return new PlayerDyeChannel(playerID, dyeChannel.clone());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public List<Text> getCleanName() {
        if (playerName == null) {
            IntegratedServer server = MinecraftClient.getInstance().getServer();
            Optional<GameProfile> player = Optional.empty();
            if (server != null)
                player = server.getUserCache().getByUuid(playerID);
            playerName = player.isPresent() ? Text.literal(player.get().getName()) : Text.translatable("text.linkeditem.unknownplayerdyechannel");
        }
        ArrayList<Text> output = new ArrayList<>(super.getCleanName());
        output.add(0, Text.translatable("text.linkeditem.playerdyechannel", playerName));
        return output;
    }

    @Override
    public NbtCompound toTag(NbtCompound tag) {
        tag.putUuid("playerid", playerID);
        return super.toTag(tag);
    }
}
