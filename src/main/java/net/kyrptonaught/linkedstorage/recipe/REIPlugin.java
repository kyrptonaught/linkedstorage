package net.kyrptonaught.linkedstorage.recipe;

import me.shedaniel.rei.api.RecipeHelper;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.minecraft.util.Identifier;

public class REIPlugin implements REIPluginV0 {
    @Override
    public Identifier getPluginIdentifier() {
        return new Identifier(LinkedStorageMod.MOD_ID, "rei_plugin");
    }
    @Override
    public void registerOthers(RecipeHelper recipeHelper) {
    }
}
