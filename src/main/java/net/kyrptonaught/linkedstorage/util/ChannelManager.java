package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.PersistentState;

import java.util.HashMap;
import java.util.UUID;

public class ChannelManager extends PersistentState {

    private final InventoryStorage globalInventories = new InventoryStorage(null);
    private final HashMap<UUID, InventoryStorage> personalInventories = new HashMap<>();
    public boolean migrated = false;
    private final String saveVersion = "1.0";

    public ChannelManager(String key) {
        super(key);
    }

    /*
      public CompoundTag readTag(String id, int dataVersion) {
            try {
                CompoundTag data = NbtIo.readCompressed(new FileInputStream(new File(saveLocation, "linkedinventories.dat")));
                fromTag(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    /*
        public void save(File saveLocation) {
            try {
                CompoundTag data = new CompoundTag();
                data = toTag(data);
                NbtIo.writeCompressed(data, new FileOutputStream(new File(saveLocation, "linkedinventories.dat")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    */
    public void fromTag(CompoundTag tag) {
        globalInventories.fromTag(tag);
        personalInventories.clear();
        CompoundTag personalInvs = tag.getCompound("personalInvs");
        personalInvs.getKeys().forEach(uuid -> {
            InventoryStorage personalInv = new InventoryStorage(uuid);
            personalInv.fromTag(personalInvs.getCompound(uuid));
            personalInventories.put(UUID.fromString(uuid), personalInv);
        });
        migrated = tag.getBoolean("migrated");
        String savedVersion = tag.getString("saveVersion");
        if (!savedVersion.equals(saveVersion)) System.out.println("LinkedStorage savefile outdated");
    }

    public CompoundTag toTag(CompoundTag tag) {
        globalInventories.toTag(tag);
        CompoundTag personalInvs = new CompoundTag();
        personalInventories.values().forEach(inventoryStorage -> personalInvs.put(inventoryStorage.name, inventoryStorage.toTag(new CompoundTag())));
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