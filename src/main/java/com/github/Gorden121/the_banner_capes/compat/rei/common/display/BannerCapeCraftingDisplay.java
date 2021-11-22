package com.github.Gorden121.the_banner_capes.compat.rei.common.display;

import com.github.Gorden121.the_banner_capes.compat.rei.BannerCapePlugin;
import com.github.Gorden121.the_banner_capes.recipe.BannerCapeShapedRecipe;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BannerCapeCraftingDisplay<C extends BannerCapeShapedRecipe>  extends BasicDisplay implements SimpleGridMenuDisplay {
    protected Optional<C> recipe;

    public BannerCapeCraftingDisplay(List<EntryIngredient> inputs, List<EntryIngredient> outputs, Optional<C> recipe) {
        super(inputs, outputs, Optional.empty());
        this.recipe = recipe;
    }

    @Override
    public CategoryIdentifier<?> getCategoryIdentifier() {
        return BannerCapePlugin.BANNER_CAPE_CRAFTING;
    }

    public Optional<C> getOptionalRecipe() {
        return recipe;
    }

    @Override
    public Optional<Identifier> getDisplayLocation() {
        return getOptionalRecipe().map(Recipe::getId);
    }

    public <T extends ScreenHandler> List<List<ItemStack>> getOrganisedInputEntries(SimpleGridMenuInfo<T, BannerCapeCraftingDisplay<?>> menuInfo, T container) {
        return CollectionUtils.map(getOrganisedInputEntries(menuInfo.getCraftingWidth(container), menuInfo.getCraftingHeight(container)), ingredient ->
                CollectionUtils.<EntryStack<?>, ItemStack>filterAndMap(ingredient, stack -> stack.getType() == VanillaEntryTypes.ITEM,
                        EntryStack::castValue));
    }

    public <T extends ScreenHandler> List<EntryIngredient> getOrganisedInputEntries(int menuWidth, int menuHeight) {
        List<EntryIngredient> list = new ArrayList<>(menuWidth * menuHeight);
        for (int i = 0; i < menuWidth * menuHeight; i++) {
            list.add(EntryIngredient.empty());
        }
        for (int i = 0; i < getInputEntries().size(); i++) {
            list.set(getSlotWithSize(this, i, menuWidth), getInputEntries().get(i));
        }
        return list;
    }

    public static int getSlotWithSize(BannerCapeCraftingDisplay<?> display, int index, int craftingGridWidth) {
        return getSlotWithSize(display.getWidth(), index, craftingGridWidth);
    }

    public static int getSlotWithSize(int recipeWidth, int index, int craftingGridWidth) {
        int x = index % recipeWidth;
        int y = (index - x) / recipeWidth;
        return craftingGridWidth * y + x;
    }

    public static BasicDisplay.Serializer<BannerCapeCraftingDisplay<?>> serializer() {
        return BasicDisplay.Serializer.<BannerCapeCraftingDisplay<?>>ofSimple(BannerCapeCustomDisplay::simple)
                .inputProvider(display -> display.getOrganisedInputEntries(3, 3));
    }

    @Override
    public List<EntryIngredient> getInputEntries(MenuSerializationContext<?, ?, ?> context, MenuInfo<?, ?> info, boolean fill) {
        List<EntryIngredient> list = new ArrayList<>(3 * 3);
        for (int i = 0; i < 3 * 3; i++) {
            list.add(EntryIngredient.empty());
        }
        List<EntryIngredient> inputEntries = getInputEntries();
        for (int i = 0; i < inputEntries.size(); i++) {
            list.set(getSlotWithSize(this, i, 3), inputEntries.get(i));
        }
        return list;
    }
}
