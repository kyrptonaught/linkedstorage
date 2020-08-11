package net.kyrptonaught.linkedstorage.inventory;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.kyrptonaught.linkedstorage.network.ChannelViewers;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;

public class LinkedContainer extends GenericContainerScreenHandler {
    public LinkedContainer(int syncId, PlayerInventory playerInventory, Inventory inventory) {
        super(LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE, syncId, playerInventory, inventory, 3);
    }

    public LinkedContainer(int syncId, PlayerInventory playerInventory, PacketByteBuf buf) {
        this(syncId, playerInventory, new LinkedInventory());

    }

    @Override
    public ItemStack onSlotClick(int slotId, int clickData, SlotActionType actionType, PlayerEntity player) {
        if (slotId != -999 && player.inventory.getMainHandStack().getItem() instanceof StorageItem && this.getSlot(slotId).getStack().getItem() instanceof StorageItem)
            return ItemStack.EMPTY;

        return super.onSlotClick(slotId, clickData, actionType, player);
    }

    public static ExtendedScreenHandlerFactory createScreenHandlerFactory(DyeChannel channel) {
        return new ExtendedScreenHandlerFactory() {
            @Override
            public ScreenHandler createMenu(int syncId, PlayerInventory inventory, PlayerEntity player) {
                ChannelViewers.addViewerFor(channel.getChannelName(), player);
                return new LinkedContainer(syncId, inventory, LinkedStorageMod.getInventory(channel));
            }

            @Override
            public Text getDisplayName() {
                return new TranslatableText("container.linkedstorage");
            }

            @Override
            public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) { channel.toBuf(buf); }
        };
    }

    @Override
    public ScreenHandlerType<?> getType() {
        return LinkedStorageMod.LINKED_SCREEN_HANDLER_TYPE;
    }
}
