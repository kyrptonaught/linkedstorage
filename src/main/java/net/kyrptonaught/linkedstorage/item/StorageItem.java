package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
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
        if (!context.getWorld().isClient) {
            PlayerEntity playerEntity = context.getPlayer();
            if (playerEntity.isSneaking() && context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof StorageBlock) {
                DyeChannel channel = LinkedInventoryHelper.getBlockChannel(context.getWorld(), context.getBlockPos());
                LinkedInventoryHelper.setItemChannel(channel, context.getStack());
            } else use(context.getWorld(), context.getPlayer(), context.getHand());
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        ItemStack stack = playerEntity.getStackInHand(hand);
        if (!world.isClient) {
            DyeChannel channel = LinkedInventoryHelper.getItemChannel(stack);
            ContainerProviderRegistry.INSTANCE.openContainer(new Identifier(LinkedStorageMod.MOD_ID, "linkedstorage"), playerEntity, channel::toBuf);
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        DyeChannel channel = LinkedInventoryHelper.getItemChannel(itemStack_1);
        list_1.add(new TranslatableText("text.linkeditem.channel", channel.getCleanName()).formatted(Formatting.GRAY));
    }
}