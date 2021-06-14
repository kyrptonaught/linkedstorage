package net.kyrptonaught.linkedstorage.recipe;

import com.google.gson.JsonObject;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;

public class CopyDyeRecipe extends ShapedRecipe {

    public CopyDyeRecipe(ShapedRecipe shapedRecipe) {
        super(shapedRecipe.getId(), "linkedstorage", shapedRecipe.getWidth(), shapedRecipe.getHeight(), shapedRecipe.getIngredients(), shapedRecipe.getOutput());
    }

    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack output = this.getOutput().copy();
        LinkedInventoryHelper.setItemChannel(LinkedInventoryHelper.getItemChannel(craftingInventory.getStack(4)), output);
        return output;
    }

    public RecipeSerializer<?> getSerializer() {
        return LinkedStorageMod.copyDyeRecipe;
    }

    public static class Serializer implements RecipeSerializer<CopyDyeRecipe> {

        @Override
        public CopyDyeRecipe read(Identifier id, JsonObject json) {
            return new CopyDyeRecipe(ShapedRecipe.Serializer.SHAPED.read(id, json));
        }

        @Override
        public CopyDyeRecipe read(Identifier id, PacketByteBuf buf) {
            return new CopyDyeRecipe(ShapedRecipe.Serializer.SHAPED.read(id, buf));
        }

        @Override
        public void write(PacketByteBuf buf, CopyDyeRecipe recipe) {
            ShapedRecipe.Serializer.SHAPED.write(buf, recipe);
        }
    }
}