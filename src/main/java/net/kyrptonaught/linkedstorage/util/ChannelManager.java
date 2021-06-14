package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.UUID;

public class ChannelManager extends PersistentState {

    private final InventoryStorage globalInventories = new InventoryStorage(null);
    private final HashMap<UUID, InventoryStorage> personalInventories = new HashMap<>();
    public boolean migrated = false;
    private final String saveVersion = "1.0";

    public ChannelManager() {
        super();
    }
    public static PersistentState fromNbt(NbtCompound tag) {
        ChannelManager cman = new ChannelManager();
        cman.globalInventories.fromTag(tag);
        cman.personalInventories.clear();
        NbtCompound personalInvs = tag.getCompound("personalInvs");
        personalInvs.getKeys().forEach(uuid -> {
            InventoryStorage personalInv = new InventoryStorage(uuid);
            personalInv.fromTag(personalInvs.getCompound(uuid));
            cman.personalInventories.put(UUID.fromString(uuid), personalInv);
        });
        cman.migrated = tag.getBoolean("migrated");
        String savedVersion = tag.getString("saveVersion");
        if (!savedVersion.equals(cman.saveVersion)) System.out.println("LinkedStorage savefile outdated");
        return cman;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        globalInventories.toTag(tag);
        NbtCompound personalInvs = new NbtCompound();
        personalInventories.values().forEach(inventoryStorage -> personalInvs.put(inventoryStorage.name, inventoryStorage.toTag(new NbtCompound())));
        tag.put("personalInvs", personalInvs);
        tag.putBoolean("migrated", migrated);
        tag.putString("saveVersion", saveVersion);
        return tag;
    }

    public LinkedInventory getInv(DyeChannel dyeChannel) {
        if (dyeChannel instanceof PlayerDyeChannel)
            return getPersonalInv((PlayerDyeChannel) dyeChannel);
        return globalInventories.getInv(dyeChannel);
    }

    public LinkedInventory getPersonalInv(PlayerDyeChannel dyeChannel) {
        if (!personalInventories.containsKey(dyeChannel.playerID))
            personalInventories.put(dyeChannel.playerID, new InventoryStorage(dyeChannel.playerID.toString()));
        return personalInventories.get(dyeChannel.playerID).getInv(dyeChannel);
    }

    @Override
    public boolean isDirty() {
        return true;
    }
}