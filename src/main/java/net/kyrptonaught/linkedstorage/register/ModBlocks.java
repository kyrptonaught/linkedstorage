package net.kyrptonaught.linkedstorage.register;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.block.StorageBlockItem;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;

public class ModBlocks {
    public static Block storageBlock;
    public static Item storageBlockItem;

    public static void register() {
        storageBlock = new StorageBlock(Block.Settings.of(Material.METAL).strength(2.5f, 2.5f));
        storageBlockItem = Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageblock"), new StorageBlockItem(storageBlock, new Item.Settings().group(LinkedStorageMod.GROUP)));
    }

    public static VoxelShape rotate(Direction facing, double x, double y, double z, double x2, double y2, double z2) {
        switch (facing) {
            case SOUTH:
                return Block.createCuboidShape(x, y, 16 - z2, x2, y2, 16 - z);
            case WEST:
                return Block.createCuboidShape(z, y, x, z2, y2, x2);
            case EAST:
                return Block.createCuboidShape(16 - z2, y, x, 16 - z, y2, x2);
            case DOWN:
                return Block.createCuboidShape(x, z, y, x2, z2, y2);
            case UP:
                return Block.createCuboidShape(x, 16 - z2, y, x2, 16 - z, y2);
        }
        return Block.createCuboidShape(x, y, z, x2, y2, z2);//NORTH
    }
}
