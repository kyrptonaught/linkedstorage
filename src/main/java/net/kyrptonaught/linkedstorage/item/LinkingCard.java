package net.kyrptonaught.linkedstorage.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.client.LinkingCardScreen;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryProvider;
import net.minecraft.client.MinecraftClient;
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

public class LinkingCard extends Item implements LinkedInventoryProvider {
    public LinkingCard(Settings item$Settings_1) {
        super(item$Settings_1);
        Registry.register(Registry.ITEM, new Identifier(LinkedStorageMod.MOD_ID, "linkingcard"), this);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if (!context.getWorld().isClient && context.isPlayerSneaking() && LinkedInventoryHelper.itemHasChannel(context.getStack())) {
            String channel = LinkedInventoryHelper.getItemChannel(context.getStack());
            LinkedInventoryHelper.setBlockChannel(channel, context.getWorld(), context.getBlockPos());
            context.getPlayer().addChatMessage(new TranslatableText("text.linkedstorage.set", channel), false);
        }
        return ActionResult.SUCCESS;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity playerEntity, Hand hand) {
        if (world.isClient) {
            MinecraftClient.getInstance().openScreen(new LinkingCardScreen(new TranslatableText("item.linkedstorage.linkingcard"), playerEntity.getMainHandStack()));
        }
        return new TypedActionResult<>(ActionResult.SUCCESS, playerEntity.getMainHandStack());
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        list_1.add(new TranslatableText("text.linkingcard.1").formatted(Formatting.GRAY));
        list_1.add(new TranslatableText("text.linkingcard.2").formatted(Formatting.GRAY));
        list_1.add(new TranslatableText("text.linkingcard.3").formatted(Formatting.GRAY));
        list_1.add(new TranslatableText("text.linkingcard.4").formatted(Formatting.GRAY));
        if (LinkedInventoryHelper.itemHasChannel(itemStack_1))
            list_1.add(new TranslatableText("text.linkingcard.channel", LinkedInventoryHelper.getItemChannel(itemStack_1)).formatted(Formatting.GRAY));
    }
}