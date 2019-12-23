package net.kyrptonaught.linkedstorage.register;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.LinkedStorageModClient;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.minecraft.item.Item;

public class ModItems {
    public static void register() {
        new StorageItem(new Item.Settings().group(LinkedStorageMod.GROUP));
    }
}
