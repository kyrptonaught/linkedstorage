package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.List;

public class LinkingCard extends Item {
    public LinkingCard(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "linkingcard"), this);
    }

    public void useOnStorageBlock(ItemStack stack, StorageBlockEntity sbe, PlayerEntity player) {
        if (hasChannel(stack.getOrCreateTag())) {
            String channel = getChannel(stack.getOrCreateTag());
            sbe.setChannel(channel);
            player.addChatMessage(new TranslatableText("text.linkedstorage.set", channel), false);
        } else {
            player.addChatMessage(new TranslatableText("text.linkedstorage.nochannel"), false);
        }
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        ItemStack stack = context.getPlayer().inventory.getMainHandStack();
        if (context.getWorld().isClient || !(context.getWorld().getBlockState(context.getBlockPos()).getBlock() instanceof StorageBlock))
            return ActionResult.PASS;
        if (context.getPlayer().isSneaking()) {
            String channel = StorageBlock.getChannelForBE(context.getWorld(), context.getBlockPos());
            setChannel(stack, channel);
            context.getPlayer().addChatMessage(new TranslatableText("text.linkedstorage.copied", channel), false);
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack stack = playerEntity_1.getMainHandStack();
        ItemStack offhand = playerEntity_1.getOffHandStack();
        if (!world_1.isClient && !offhand.isEmpty() && offhand.getItem() instanceof StorageItem) {
            if (!playerEntity_1.isSneaking()) {
                CompoundTag tag = stack.getOrCreateTag();
                if (hasChannel(tag)) {
                    String channel = getChannel(tag);
                    setChannel(offhand, channel);
                    playerEntity_1.addChatMessage(new TranslatableText("text.linkedstorage.set", channel), false);
                } else {
                    playerEntity_1.addChatMessage(new TranslatableText("text.linkedstorage.nochannel"), false);
                }

            } else {
                String channel = StorageItem.getChannel(offhand, world_1);
                setChannel(stack, channel);
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

     String getChannel(CompoundTag stack) {
        return stack.getString("channel");
    }

    private Boolean hasChannel(CompoundTag stack) {
        return stack.containsKey("channel");
    }

    private void setChannel(ItemStack stack, String channel) {
        stack.getOrCreateTag().putString("channel", channel);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
            list_1.add(new TranslatableText("text.linkingcard.1").formatted(Formatting.GRAY));
            list_1.add(new TranslatableText("text.linkingcard.2").formatted(Formatting.GRAY));
            list_1.add(new TranslatableText("text.linkingcard.3").formatted(Formatting.GRAY));
            list_1.add(new TranslatableText("text.linkingcard.4").formatted(Formatting.GRAY));
        if (hasChannel(itemStack_1.getOrCreateTag()))
            list_1.add(new TranslatableText("text.linkingcard.channel",getChannel(itemStack_1.getOrCreateTag())).formatted(Formatting.GRAY));
    }
}
