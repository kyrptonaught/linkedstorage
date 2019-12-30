package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.minecraft.block.entity.BlockEntity;

@Environment(EnvType.CLIENT)
public class DummyStorageBlockEntity extends BlockEntity {
    public DummyStorageBlockEntity() {
        super(LinkedStorageModClient.dummy);
    }

    private byte[] dyeChannel;

    public void setChannel(byte[] channel) {
        this.dyeChannel = channel;
    }

    byte[] getChannel() {
        return dyeChannel;
    }
}
