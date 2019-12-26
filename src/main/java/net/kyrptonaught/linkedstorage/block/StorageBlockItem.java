package net.kyrptonaught.linkedstorage.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.kyrptonaught.linkedstorage.inventory.LinkedInventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public class StorageBlockItem extends BlockItem {
    public StorageBlockItem(Block block, Settings settings) {
        super(block, settings);
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendTooltip(ItemStack itemStack_1, World world_1, List<Text> list_1, TooltipContext tooltipContext_1) {
        byte[] channel = LinkedInventoryHelper.getItemChannel(itemStack_1);
        String name = DyeColor.byId(channel[0]).getName() + ", " + DyeColor.byId(channel[1]).getName() + ", " + DyeColor.byId(channel[2]).getName();
        list_1.add(new TranslatableText("text.linkeditem.channel", name).formatted(Formatting.GRAY));
    }
}
