package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class StorageItem extends Item {
    public StorageItem(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "storageitem"), this);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient && context.getPlayer().isSneaking()) {
            byte[] channel = LinkedInventoryHelper.getBlockChannel(context.getWorld(), context.getBlockPos());
            LinkedInventoryHelper.setItemChannel(channel, context.getStack());
            context.getPlayer().addChatMessage(new TranslatableText("text.linkedstorage.set", LinkedInventoryHelper.getChannelName(channel)), false);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        if (!world.isClient) {
            byte[] channel = LinkedInventoryHelper.getItemChannel(stack);
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), playerEntity, (buf) -> buf.writeByteArray(channel));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        byte[] channel = LinkedInventoryHelper.getItemChannel(itemStack_1);
        String name = DyeColor.byId(channel[0]).getName() + ", " + DyeColor.byId(channel[1]).getName() + ", " + DyeColor.byId(channel[2]).getName();
        list_1.add(new TranslatableText("text.linkeditem.channel", name).formatted(Formatting.GRAY));
    }
}