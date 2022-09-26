package net.kyrptonaught.linkedstorage.compat;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedContainer;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.kyrptonaught.quickshulker.api.QuickOpenableRegistry;
import net.kyrptonaught.quickshulker.api.RegisterQuickShulker;

public class Quickshulker implements RegisterQuickShulker {
    @Override
    public void registerProviders() {
        new QuickOpenableRegistry.Builder()
                .setItem(StorageItem.class)
                .supportsBundleing(true)
                .getBundleInv((player, stack) -> LinkedStorageMod.getInventory(LinkedInventoryHelper.getItemChannel(stack)))
                .setOpenAction((player, stack) -> {
                    player.openHandledScreen(LinkedContainer.createScreenHandlerFactory(LinkedInventoryHelper.getItemChannel(stack)));
                })
                .canOpenInHand(false)
                .register();
    }
}