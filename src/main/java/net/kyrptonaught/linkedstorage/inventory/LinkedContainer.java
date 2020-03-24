package net.kyrptonaught.linkedstorage.inventory;

import net.minecraft.container.GenericContainer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;

public class LinkedContainer extends GenericContainer {
    public LinkedContainer(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(null, syncId, playerInventory, inventory, 3);
    }
}
