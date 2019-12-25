package net.kyrptonaught.linkedstorage.register;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.minecraft.item.Item;

public class ModItems {
    public static Item storageItem;

    public static void register() {
        storageItem = new StorageItem(new Item.Settings().group(LinkedStorageMod.GROUP).maxCount(1));
    }
}
