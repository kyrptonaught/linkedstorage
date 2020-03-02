package net.kyrptonaught.linkedstorage.recipe;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.kyrptonaught.linkedstorage.LinkedStorageMod;
import net.kyrptonaught.linkedstorage.util.LinkedInventoryHelper;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.DefaultedList;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.PacketByteBuf;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class CopyDyeRecipe extends ShapedRecipe {
    String group;
    public CopyDyeRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> ingredients, ItemStack output) {
        super(id, group, width, height, ingredients, output);
        this.group = group;
    }

    public ItemStack craft(CraftingInventory craftingInventory) {
        ItemStack output = this.getOutput().copy();
        LinkedInventoryHelper.setItemChannel(LinkedInventoryHelper.getItemChannel(craftingInventory.getInvStack(4)), output);
        return output;
    }

    public static Map<String, Ingredient> getComponents(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();
        Iterator var2 = json.entrySet().iterator();

        while (var2.hasNext()) {
            Map.Entry<String, JsonElement> entry = (Map.Entry) var2.next();
            if (((String) entry.getKey()).length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + (String) entry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(entry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(entry.getKey(), Ingredient.fromJson((JsonElement) entry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    private static String[] getPattern(JsonArray json) {
        String[] strings = new String[json.size()];
        if (strings.length > 3) {
            throw new JsonSyntaxException("Invalid pattern: too many rows, 3 is maximum");
        } else if (strings.length == 0) {
            throw new JsonSyntaxException("Invalid pattern: empty pattern not allowed");
        } else {
            for (int i = 0; i < strings.length; ++i) {
                String string = JsonHelper.asString(json.get(i), "pattern[" + i + "]");
                if (string.length() > 3) {
                    throw new JsonSyntaxException("Invalid pattern: too many columns, 3 is maximum");
                }

                if (i > 0 && strings[0].length() != string.length()) {
                    throw new JsonSyntaxException("Invalid pattern: each row must be the same width");
                }

                strings[i] = string;
            }

            return strings;
        }
    }

    @VisibleForTesting
    static String[] combinePattern(String... lines) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int m = 0; m < lines.length; ++m) {
            String string = lines[m];
            i = Math.min(i, findNextIngredient(string));
            int n = findNextIngredientReverse(string);
            j = Math.max(j, n);
            if (n < 0) {
                if (k == m) {
                    ++k;
                }

                ++l;
            } else {
                l = 0;
            }
        }

        if (lines.length == l) {
            return new String[0];
        } else {
            String[] strings = new String[lines.length - l - k];

            for (int o = 0; o < strings.length; ++o) {
                strings[o] = lines[o + k].substring(i, j + 1);
            }

            return strings;
        }
    }

    private static int findNextIngredient(String pattern) {
        int i;
        for (i = 0; i < pattern.length() && pattern.charAt(i) == ' '; ++i) {
        }

        return i;
    }

    private static int findNextIngredientReverse(String pattern) {
        int i;
        for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
        }

        return i;
    }

    private static DefaultedList<Ingredient> getIngredients(String[] pattern, Map<String, Ingredient> key, int width, int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet(key.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = (Ingredient) key.get(string);
                if (ingredient == null) {
                    throw new JsonSyntaxException("Pattern references symbol '" + string + "' but it's not defined in the key");
                }

                set.remove(string);
                defaultedList.set(j + width * i, ingredient);
            }
        }

        if (!set.isEmpty()) {
            throw new JsonSyntaxException("Key defines symbols that aren't used in pattern: " + set);
        } else {
            return defaultedList;
        }
    }

    public RecipeSerializer<?> getSerializer() {
        return LinkedStorageMod.copyDyeRecipe;
    }

    public static class Serializer implements RecipeSerializer<CopyDyeRecipe> {
        public CopyDyeRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Map<String, Ingredient> map = CopyDyeRecipe.getComponents(JsonHelper.getObject(jsonObject, "key"));
            String[] strings = CopyDyeRecipe.combinePattern(CopyDyeRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = CopyDyeRecipe.getIngredients(strings, map, i, j);
            ItemStack itemStack = CopyDyeRecipe.getItemStack(JsonHelper.getObject(jsonObject, "result"));
            return new CopyDyeRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public CopyDyeRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString(32767);
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); ++k) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new CopyDyeRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, CopyDyeRecipe shapedRecipe) {
            packetByteBuf.writeVarInt(shapedRecipe.getWidth());
            packetByteBuf.writeVarInt(shapedRecipe.getHeight());
            packetByteBuf.writeString(shapedRecipe.group);
            Iterator var3 = shapedRecipe.getPreviewInputs().iterator();

            while (var3.hasNext()) {
                Ingredient ingredient = (Ingredient) var3.next();
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(shapedRecipe.getOutput());
        }
    }
}