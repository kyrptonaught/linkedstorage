package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.LinkedInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.DefaultedList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StorageManager implements StorageManagerComponent {
    private HashMap<Integer, LinkedInventory> inventories = new HashMap<>();

    @Override
    public StorageManager getValue() {
        return this;
    }

    public LinkedInventory getInv(int channel) {
        if (!inventories.containsKey(channel))
            inventories.put(channel, new LinkedInventory());
        return inventories.get(channel);
    }

    @Override
    public void fromTag(CompoundTag tag) {
        int[] channels = tag.getIntArray("channels");
        inventories.clear();
        CompoundTag invs = tag.getCompound("invs");
        for (int inv : channels) {
            inventories.put(inv, fromList(invs.getCompound("inv" + inv)));
        }
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        List<Integer> channels = new ArrayList<>(inventories.keySet());
        tag.putIntArray("channels", channels);
        CompoundTag invs = new CompoundTag();
        for (Integer inv : channels) {
            invs.put("inv" + inv, Inventories.toTag(new CompoundTag(), toList(inventories.get(inv))));
        }
        tag.put("invs", invs);
        return tag;
    }

    private DefaultedList<ItemStack> toList(Inventory inv) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inv.getInvSize(), ItemStack.EMPTY);
        for (int i = 0; i < inv.getInvSize(); i++)
            stacks.set(i, inv.getInvStack(i));
        return stacks;
    }

    public LinkedInventory fromList(CompoundTag tag) {
        LinkedInventory inventory = new LinkedInventory();
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.getInvSize(), ItemStack.EMPTY);
        Inventories.fromTag(tag, stacks);
        for (int i = 0; i < stacks.size(); i++)
            inventory.setInvStack(i, stacks.get(i));
        return inventory;
    }
}