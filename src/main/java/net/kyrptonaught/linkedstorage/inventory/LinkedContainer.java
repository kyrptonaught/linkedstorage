package net.kyrptonaught.linkedstorage.inventory;

import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.util.math.BlockPos;

public class LinkedContainer extends GenericContainer {
    public BlockPos linkedBlock;

    public LinkedContainer(int syncId, PlayerInventory playerInventory, Inventory inventory, BlockPos linkedBlock) {
        super(null, syncId, playerInventory, inventory, 3);
        this.linkedBlock = linkedBlock;
    }
}
