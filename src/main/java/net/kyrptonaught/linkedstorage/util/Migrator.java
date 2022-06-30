package net.kyrptonaught.linkedstorage.util;

import net.kyrptonaught.linkedstorage.inventory.LinkedInventory;

import java.util.HashMap;
import java.util.UUID;

public class Migrator {
    public static void Migrate(ChannelManager CMAN, String oldSavedVersion) {
        if (oldSavedVersion.equals("1.0")) { //stupid me saved the bytearray as a string without a separator. What is 11111? 11 1 11, 1 11 11, or 11 11 1?
            HashMap<String, LinkedInventory> inventoryHashMap = CMAN.getGlobalInventories().getInventories();
            for (String key : inventoryHashMap.keySet()) {
                if (key.contains(":")) continue;
                fixKey(inventoryHashMap, inventoryHashMap.remove(key), key);
            }
            HashMap<UUID, InventoryStorage> personalInventories = CMAN.getPersonalInventories();

            for (UUID uuid : personalInventories.keySet()) {
                inventoryHashMap = personalInventories.get(uuid).getInventories();
                for (String key : inventoryHashMap.keySet()) {
                    if (key.contains(":")) continue;
                    fixKey(inventoryHashMap, inventoryHashMap.remove(key), key.replace(uuid.toString(), ""));
                }
            }
        }
    }

    public static void fixKey(HashMap<String, LinkedInventory> inventoryHashMap, LinkedInventory oldInv, String key) {
        if (key.length() == 3) {
            inventoryHashMap.put(key.charAt(0) + ":" + key.charAt(1) + ":" + key.charAt(2), oldInv);
            return;
        } else if (key.length() == 6) {
            inventoryHashMap.put(key.substring(0, 2) + ":" + key.substring(2, 4) + ":" + key.substring(4, 6), oldInv);
            return;
        }

        if (key.length() == 4) {
            String temp = key.charAt(0) + ":" + key.charAt(1) + ":" + key.substring(2, 4);
            String temp2 = key.charAt(0) + ":" + key.substring(1, 3) + ":" + key.charAt(3);
            String temp3 = key.substring(0, 2) + ":" + key.charAt(2) + ":" + key.charAt(3);

            if (checkCode(temp)) inventoryHashMap.put(temp, oldInv.copy());
            if (checkCode(temp2)) inventoryHashMap.put(temp2, oldInv.copy());
            if (checkCode(temp3)) inventoryHashMap.put(temp3, oldInv.copy());

        } else if (key.length() == 5) {
            String temp = key.charAt(0) + ":" + key.substring(1, 3) + ":" + key.substring(3, 5);
            String temp2 = key.substring(0, 2) + ":" + key.charAt(2) + ":" + key.substring(3, 5);
            String temp3 = key.substring(0, 2) + ":" + key.substring(2, 4) + ":" + key.charAt(4);
            if (checkCode(temp)) inventoryHashMap.put(temp, oldInv.copy());
            if (checkCode(temp2)) inventoryHashMap.put(temp2, oldInv.copy());
            if (checkCode(temp3)) inventoryHashMap.put(temp3, oldInv.copy());
        }
    }

    public static boolean checkCode(String key) {
        String[] split = key.split(":");
        return Integer.parseInt(split[0]) < 16 && Integer.parseInt(split[1]) < 16 && Integer.parseInt(split[2]) < 16;
    }
}
