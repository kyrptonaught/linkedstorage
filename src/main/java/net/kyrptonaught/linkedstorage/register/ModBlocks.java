package net.kyrptonaught.linkedstorage.register;

import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;

public class ModBlocks {
    public static Block storageBlock;

    public static void register() {
        storageBlock = new StorageBlock(Block.Settings.of(Material.METAL).strength(2.5f, 2.5f));
    }
}
