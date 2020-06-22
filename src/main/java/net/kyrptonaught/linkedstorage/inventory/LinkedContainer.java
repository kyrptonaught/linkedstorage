package net.kyrptonaught.linkedstorage.inventory;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.GenericContainerScreenHandler;

public class LinkedContainer extends GenericContainerScreenHandler {
    public LinkedContainer(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(null, syncId, playerInventory, inventory, 3);
    }
}
