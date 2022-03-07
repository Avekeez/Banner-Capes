package com.github.Gorden121.the_banner_capes.recipe;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.Collar;
import com.github.Gorden121.the_banner_capes.Nubbin;
import com.github.Gorden121.the_banner_capes.data.DecorationMaterialDataItem;
import com.github.Gorden121.the_banner_capes.item.BannerCapeItem;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.*;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.Pair;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.*;

public class BannerCapeShapedRecipe implements CraftingRecipe {

    final int width;
    final int height;
    final DefaultedList<Ingredient> input;
    final ItemStack output;
    final String group;
    private final Identifier id;
    public BannerCapeShapedRecipe(Identifier id, String group, int width, int height, DefaultedList<Ingredient> input, ItemStack output) {
        this.id = id;
        this.group = group;
        this.width = width;
        this.height = height;
        this.input = input;
        this.output = output;
    }

    /**
     * Compiles a pattern and series of symbols into a list of ingredients (the matrix) suitable for matching
     * against a crafting grid.
     */
    static DefaultedList<Ingredient> createPatternMatrix(String[] pattern, Map<String, Ingredient> symbols, int width, int height) {
        DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(width * height, Ingredient.EMPTY);
        Set<String> set = Sets.newHashSet((Iterable) symbols.keySet());
        set.remove(" ");

        for (int i = 0; i < pattern.length; ++i) {
            for (int j = 0; j < pattern[i].length(); ++j) {
                String string = pattern[i].substring(j, j + 1);
                Ingredient ingredient = symbols.get(string);
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

    /**
     * Removes empty space from around the recipe pattern.
     *
     * <p>Turns patterns such as:</p>
     * <pre>
     * {@code
     * "   o"
     * "   a"
     * "    "
     * }
     * </pre>
     * Into:
     * <pre>
     * {@code
     * "o"
     * "a"
     * }
     * </pre>
     *
     * @return a new recipe pattern with all leading and trailing empty rows/columns removed
     */
    @VisibleForTesting
    static String[] removePadding(String... pattern) {
        int i = Integer.MAX_VALUE;
        int j = 0;
        int k = 0;
        int l = 0;

        for (int m = 0; m < pattern.length; ++m) {
            String string = pattern[m];
            i = Math.min(i, findFirstSymbol(string));
            int n = findLastSymbol(string);
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

        if (pattern.length == l) {
            return new String[0];
        } else {
            String[] strings = new String[pattern.length - l - k];

            for (int o = 0; o < strings.length; ++o) {
                strings[o] = pattern[o + k].substring(i, j + 1);
            }

            return strings;
        }
    }

    private static int findFirstSymbol(String line) {
        int i;
        for (i = 0; i < line.length() && line.charAt(i) == ' '; ++i) {

        }

        return i;
    }

    private static int findLastSymbol(String pattern) {
        int i;
        for (i = pattern.length() - 1; i >= 0 && pattern.charAt(i) == ' '; --i) {
        }

        return i;
    }

    static String[] getPattern(JsonArray json) {
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

    /**
     * Reads the pattern symbols.
     *
     * @return a mapping from a symbol to the ingredient it represents
     */
    static Map<String, Ingredient> readSymbols(JsonObject json) {
        Map<String, Ingredient> map = Maps.newHashMap();

        for (Map.Entry<String, JsonElement> stringJsonElementEntry : json.entrySet()) {
            if (stringJsonElementEntry.getKey().length() != 1) {
                throw new JsonSyntaxException("Invalid key entry: '" + stringJsonElementEntry.getKey() + "' is an invalid symbol (must be 1 character only).");
            }

            if (" ".equals(stringJsonElementEntry.getKey())) {
                throw new JsonSyntaxException("Invalid key entry: ' ' is a reserved symbol.");
            }

            map.put(stringJsonElementEntry.getKey(), Ingredient.fromJson(stringJsonElementEntry.getValue()));
        }

        map.put(" ", Ingredient.EMPTY);
        return map;
    }

    public static ItemStack outputFromJson(JsonObject json) {
        Item item = getItem(json);
        if (json.has("data")) {
            throw new JsonParseException("Disallowed data tag found");
        } else {
            int i = JsonHelper.getInt(json, "count", 1);
            if (i < 1) {
                throw new JsonSyntaxException("Invalid output count: " + i);
            } else {
                return new ItemStack(item, i);
            }
        }
    }

    public static Item getItem(JsonObject json) {
        String string = JsonHelper.getString(json, "item");
        Item item = Registry.ITEM.getOrEmpty(new Identifier(string)).orElseThrow(() -> {
            return new JsonSyntaxException("Unknown item '" + string + "'");
        });
        if (item == Items.AIR) {
            throw new JsonSyntaxException("Invalid item: " + string);
        } else {
            return item;
        }
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BannerCapes.BANNER_CAPE_SHAPED_SERIALIZER;
    }

    int nubbinIndex = Nubbin.getFirstNubbinable();
    int collarIndex = Collar.getFirstCollarable();
    int outputTimer = 0;
    int outputTimerMax = 60;
    @Override
    public ItemStack getOutput() {
        ItemStack stack = new ItemStack(BannerCapes.BANNER_CAPE, 1);
        NbtCompound nbtCompound = stack.getOrCreateNbt();

        DecorationMaterialDataItem[] materialArray = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray;
        if(outputTimer >= outputTimerMax) {
            nubbinIndex = Nubbin.getNextNubbinable(nubbinIndex);
            collarIndex = Collar.getNextCollarable(collarIndex);
            outputTimer = 0;
        }
        else
            outputTimer++;

        String nubbin_left = materialArray[nubbinIndex].itemIdentifier;
        String nubbin_right = materialArray[nubbinIndex].itemIdentifier;
        String collar = materialArray[collarIndex].itemIdentifier;

        if (nubbin_left != null && !nubbin_left.isEmpty()) {
            nbtCompound.putString("NubbinLeft", nubbin_left);
        }
        if (nubbin_right != null && !nubbin_right.isEmpty()) {
            nbtCompound.putString("NubbinRight", nubbin_right);
        }
        if (collar != null && !collar.isEmpty()) {
            nbtCompound.putString("Collar", collar);
        }

        return stack;
    }

    @Override
    public DefaultedList<Ingredient> getIngredients() {
        return this.input;
    }

    @Override
    public boolean fits(int width, int height) {
        return width >= this.width && height >= this.height;
    }

    @Override
    public boolean matches(CraftingInventory craftingInventory, World world) {
        for (int i = 0; i <= craftingInventory.getWidth() - this.width; ++i) {
            for (int j = 0; j <= craftingInventory.getHeight() - this.height; ++j) {
                if (this.matchesPattern(craftingInventory, i, j, true)) {
                    return true;
                }

                if (this.matchesPattern(craftingInventory, i, j, false)) {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean matchesPattern(CraftingInventory inv, int offsetX, int offsetY, boolean flipped) {
        for (int i = 0; i < inv.getWidth(); ++i) {
            for (int j = 0; j < inv.getHeight(); ++j) {
                int k = i - offsetX;
                int l = j - offsetY;
                Ingredient ingredient = Ingredient.EMPTY;
                if (k >= 0 && l >= 0 && k < this.width && l < this.height) {
                    if (flipped) {
                        ingredient = (Ingredient) this.input.get(this.width - k - 1 + l * this.width);
                    } else {
                        ingredient = (Ingredient) this.input.get(k + l * this.width);
                    }
                }

                if (!ingredient.test(inv.getStack(i + j * inv.getWidth()))) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {

        ItemStack Slot0Stack = inv.getStack(0);
        ItemStack Slot1Stack = inv.getStack(1);
        ItemStack Slot2Stack = inv.getStack(2);
        ItemStack Slot4Stack = inv.getStack(4);
        ItemStack Slot7Stack = inv.getStack(7);

        if (Slot4Stack.isEmpty()) {
            return Slot4Stack;
        } else if (Slot4Stack.getItem() instanceof BannerItem

        ) {
            ItemStack capeStack = new ItemStack(BannerCapes.BANNER_CAPE);

            if (!Slot0Stack.isEmpty() && Nubbin.isNubbinable(Slot0Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinLeft", Nubbin.identifierFromItem(Slot0Stack));
            }
            if (!Slot2Stack.isEmpty() && Nubbin.isNubbinable(Slot2Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinRight", Nubbin.identifierFromItem(Slot2Stack));
            }
            if (!Slot1Stack.isEmpty() && Collar.isCollarable(Slot1Stack)) {
                capeStack.getOrCreateNbt().putString("Collar", Collar.identifierFromItem(Slot1Stack));
            }
            if (!Slot4Stack.isEmpty() && Slot4Stack.getItem() instanceof BannerItem) {
                NbtCompound tag = Slot4Stack.getSubNbt("BlockEntityTag");
                tag = (tag == null) ? new NbtCompound() : tag.copy();
                tag.putInt("Base", ((BannerItem) Slot4Stack.getItem()).getColor().getId());
                capeStack.setSubNbt("BlockEntityTag", tag);
            }

            return capeStack;
        } else if (Slot4Stack.getItem() instanceof BannerCapeItem) {
            ItemStack capeStack = Slot4Stack.copy();
            if (!Slot0Stack.isEmpty() && Nubbin.isNubbinable(Slot0Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinLeft", Nubbin.identifierFromItem(Slot0Stack));
            }
            if (!Slot2Stack.isEmpty() && Nubbin.isNubbinable(Slot2Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinRight", Nubbin.identifierFromItem(Slot2Stack));
            }
            if (!Slot1Stack.isEmpty() && Collar.isCollarable(Slot1Stack)) {
                capeStack.getOrCreateNbt().putString("Collar", Collar.identifierFromItem(Slot1Stack));
            }
            if (!Slot7Stack.isEmpty() && Slot7Stack.getItem() instanceof BannerItem) {
                NbtCompound tag = Slot7Stack.getSubNbt("BlockEntityTag");
                tag = (tag == null) ? new NbtCompound() : tag.copy();
                tag.putInt("Base", ((BannerItem) Slot7Stack.getItem()).getColor().getId());
                capeStack.setSubNbt("BlockEntityTag", tag);
            }
            return capeStack;
        }

        return Slot4Stack;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public boolean isEmpty() {
        DefaultedList<Ingredient> defaultedList = this.getIngredients();
        return defaultedList.isEmpty() || defaultedList.stream().filter((ingredient) -> {
            return !ingredient.isEmpty();
        }).anyMatch((ingredient) -> {
            return ingredient.getMatchingStacks().length == 0;
        });
    }

    public static class Serializer implements RecipeSerializer<BannerCapeShapedRecipe> {
        public BannerCapeShapedRecipe read(Identifier identifier, JsonObject jsonObject) {
            String string = JsonHelper.getString(jsonObject, "group", "");
            Map<String, Ingredient> map = BannerCapeShapedRecipe.readSymbols(JsonHelper.getObject(jsonObject, "key"));
            String[] strings = BannerCapeShapedRecipe.removePadding(BannerCapeShapedRecipe.getPattern(JsonHelper.getArray(jsonObject, "pattern")));
            int i = strings[0].length();
            int j = strings.length;
            DefaultedList<Ingredient> defaultedList = BannerCapeShapedRecipe.createPatternMatrix(strings, map, i, j);
            ItemStack itemStack = BannerCapeShapedRecipe.outputFromJson(JsonHelper.getObject(jsonObject, "result"));
            return new BannerCapeShapedRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public BannerCapeShapedRecipe read(Identifier identifier, PacketByteBuf packetByteBuf) {
            int i = packetByteBuf.readVarInt();
            int j = packetByteBuf.readVarInt();
            String string = packetByteBuf.readString();
            DefaultedList<Ingredient> defaultedList = DefaultedList.ofSize(i * j, Ingredient.EMPTY);

            for (int k = 0; k < defaultedList.size(); ++k) {
                defaultedList.set(k, Ingredient.fromPacket(packetByteBuf));
            }

            ItemStack itemStack = packetByteBuf.readItemStack();
            return new BannerCapeShapedRecipe(identifier, string, i, j, defaultedList, itemStack);
        }

        public void write(PacketByteBuf packetByteBuf, BannerCapeShapedRecipe shapedRecipe) {
            packetByteBuf.writeVarInt(shapedRecipe.width);
            packetByteBuf.writeVarInt(shapedRecipe.height);
            packetByteBuf.writeString(shapedRecipe.group);

            for (Ingredient ingredient : shapedRecipe.input) {
                ingredient.write(packetByteBuf);
            }

            packetByteBuf.writeItemStack(shapedRecipe.output);
        }
    }
}
