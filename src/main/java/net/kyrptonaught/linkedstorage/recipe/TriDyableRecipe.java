package net.kyrptonaught.linkedstorage.recipe;

import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.block.StorageBlock;
import net.kyrptonaught.linkedstorage.item.StorageItem;
import net.kyrptonaught.linkedstorage.util.DyeChannel;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class TriDyableRecipe extends SpecialCraftingRecipe {
    public TriDyableRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        Item center = inv.getStack(4).getItem();
        return (inv.getStack(0).getItem() instanceof DyeItem ||
                inv.getStack(1).getItem() instanceof DyeItem ||
                inv.getStack(2).getItem() instanceof DyeItem) &&
                (center instanceof StorageItem || (center instanceof BlockItem && ((BlockItem) center).getBlock() instanceof StorageBlock));
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack newStack = inv.getStack(4).copy();
        DyeChannel dyeChannel = LinkedInventoryHelper.getItemChannel(newStack).clone();
        for (int i = 0; i < 3; i++)
            if (inv.getStack(i).getItem() instanceof DyeItem)
                dyeChannel.setSlot(i, (byte) ((DyeItem) inv.getStack(i).getItem()).getColor().getId());
        LinkedInventoryHelper.setItemChannel(dyeChannel, newStack);
        return newStack;
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return LinkedStorageMod.triDyeRecipe;
    }
}
