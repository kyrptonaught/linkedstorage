package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlockEntity;
import net.minecraft.block.entity.BlockEntity;
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
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "linkcard"), this);
    }

    public void useOnStorageBlock(ItemStack stack, StorageBlockEntity sbe, PlayerEntity player) {
        CompoundTag tag = stack.getOrCreateTag();
        String channel = tag.getString("channel");
        sbe.setChannel(channel);
        player.addChatMessage(new TranslatableText("text.linkedstorage.set", channel), false);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        System.out.println("wee");
        ItemStack stack = context.getPlayer().inventory.getMainHandStack();
        BlockEntity be = context.getWorld().getBlockEntity(context.getBlockPos());
        if (context.getWorld().isClient || !(be instanceof StorageBlockEntity))
            return ActionResult.PASS;
        if (context.getPlayer().isSneaking()) {
            String channel = ((StorageBlockEntity) be).getChannel();
            stack.getOrCreateTag().putString("channel", channel);
            context.getPlayer().addChatMessage(new TranslatableText("text.linkedstorage.copied", channel), false);
        }
        return ActionResult.PASS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world_1, PlayerEntity playerEntity_1, Hand hand_1) {
        ItemStack stack = playerEntity_1.getMainHandStack();
        if (!playerEntity_1.isSneaking() && !world_1.isClient) {
            ItemStack offhand = playerEntity_1.getOffHandStack();
            CompoundTag tag = stack.getOrCreateTag();
            if (!offhand.isEmpty() && tag.containsKey("channel")) {
                String channel = tag.getString("channel");
                offhand.getOrCreateTag().putString("channel", channel);
                playerEntity_1.addChatMessage(new TranslatableText("text.linkedstorage.set", channel), false);
            }
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, stack);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        CompoundTag tag = itemStack_1.getOrCreateTag();
        if (tag.containsKey("channel")) {
            list_1.add(new LiteralText(tag.getString("channel")).formatted(Formatting.GRAY));
        }
    }
}
