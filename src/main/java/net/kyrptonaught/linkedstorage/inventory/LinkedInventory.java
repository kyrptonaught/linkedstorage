package net.kyrptonaught.linkedstorage.inventory;

import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;

public class LinkedInventory extends SimpleInventory implements SidedInventory {

    public LinkedInventory() {
        super(27);
    }

    @Override
    public int[] getAvailableSlots(Direction var1) {
        int[] result = new int[size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = i;
        }
        return result;

    }

    @Override
    public boolean canInsert(int var1, ItemStack var2, Direction var3) {
        return true;
    }

    @Override
    public boolean canExtract(int var1, ItemStack var2, Direction var3) {
        return true;
    }
}