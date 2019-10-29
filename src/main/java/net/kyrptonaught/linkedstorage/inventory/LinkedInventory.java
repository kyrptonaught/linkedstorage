package net.kyrptonaught.linkedstorage.inventory;

import net.minecraft.inventory.BasicInventory;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class LinkedInventory extends BasicInventory implements SidedInventory {

    public LinkedInventory() {
        super(27);
    }

    @Override
    public int[] getInvAvailableSlots(Direction var1) {
        int[] result = new int[getInvSize()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;

    }

    @Override
    public boolean canInsertInvStack(int var1, ItemStack var2, Direction var3) {
        return true;
    }

    @Override
    public boolean canExtractInvStack(int var1, ItemStack var2, Direction var3) {
        return true;
    }
}