package net.kyrptonaught.linkedstorage.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.block.entity.BlockEntity;

@Environment(EnvType.CLIENT)
public class DummyStorageBlockEntity extends BlockEntity {
    public DummyStorageBlockEntity() {
        super(LinkedStorageModClient.dummy);
    }

    private DyeChannel dyeChannel;

    public void setChannel(DyeChannel channel) {
        this.dyeChannel = channel;
    }

    DyeChannel getChannel() {
        return dyeChannel;
    }
}
