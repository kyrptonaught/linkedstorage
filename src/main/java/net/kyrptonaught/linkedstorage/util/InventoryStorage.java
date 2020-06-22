package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.collection.DefaultedList;

import java.util.HashMap;

public class InventoryStorage {
    private final HashMap<String, LinkedInventory> inventories = new HashMap<>();
    public String name;

    public InventoryStorage(String name) {
        this.name = name;
    }

    public void fromTag(CompoundTag tag) {
        inventories.clear();
        CompoundTag invs = tag.getCompound("invs");
        for (String key : invs.getKeys()) {
            inventories.put(key, fromList(invs.getCompound(key)));
        }
    }

    public CompoundTag toTag(CompoundTag tag) {
        CompoundTag invs = new CompoundTag();
        for (String key : inventories.keySet()) {
            if (!inventories.get(key).isEmpty())
                invs.put(key, Inventories.toTag(new CompoundTag(), toList(inventories.get(key))));
        }
        tag.put("invs", invs);
        return tag;
    }

    public LinkedInventory getInv(DyeChannel dyeChannel) {
        String channel = dyeChannel.getChannelName();
        if (!inventories.containsKey(channel))
            inventories.put(channel, new LinkedInventory());
        return inventories.get(channel);
    }

    private DefaultedList<ItemStack> toList(Inventory inv) {
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inv.size(), ItemStack.EMPTY);
        for (int i = 0; i < inv.size(); i++)
            stacks.set(i, inv.getStack(i));
        return stacks;
    }

    private LinkedInventory fromList(CompoundTag tag) {
        LinkedInventory inventory = new LinkedInventory();
        DefaultedList<ItemStack> stacks = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);
        Inventories.fromTag(tag, stacks);
        for (int i = 0; i < stacks.size(); i++)
            inventory.setStack(i, stacks.get(i));
        return inventory;
    }
}
