package net.kyrptonaught.linkedstorage.register;

import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.MapColor;

public class ModBlocks {
    public static Block storageBlock;

    public static void register() {
        storageBlock = new StorageBlock(AbstractBlock.Settings.create().mapColor(MapColor.EMERALD_GREEN).requiresTool().strength(2.5f, 2.5f));
    }
}
