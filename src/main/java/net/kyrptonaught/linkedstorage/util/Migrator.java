package net.kyrptonaught.linkedstorage.util;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtIo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Migrator {
    public static void Migrate(File server, ChannelManager CMAN) {
        if (!CMAN.migrated) {
            try {
                File levelDat = new File(server, "level.dat");
                if (!levelDat.exists() || !levelDat.canRead()) return;
                CompoundTag data = NbtIo.readCompressed(new FileInputStream(levelDat)).getCompound("Data");
                if (data.contains("cardinal_components", NbtType.LIST)) {
                    ListTag componentList = data.getList("cardinal_components", NbtType.COMPOUND);
                    componentList.stream().map(CompoundTag.class::cast).forEach(nbt -> {
                        if (nbt.getString("componentId").equals("linkedstorage:sman")) {
                            CMAN.fromTag(nbt);
                            CMAN.migrated = true;
                        }
                    });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
