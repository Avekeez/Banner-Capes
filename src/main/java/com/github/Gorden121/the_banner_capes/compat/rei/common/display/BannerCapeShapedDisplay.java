package com.github.Gorden121.the_banner_capes.compat.rei.common.display;

import com.github.Gorden121.the_banner_capes.recipe.BannerCapeShapedRecipe;
import me.shedaniel.rei.api.common.util.EntryIngredients;

import java.util.Collections;
import java.util.Optional;

public class BannerCapeShapedDisplay extends BannerCapeCraftingDisplay<BannerCapeShapedRecipe> {
    public BannerCapeShapedDisplay(BannerCapeShapedRecipe recipe) {
        super(
                EntryIngredients.ofIngredients(recipe.getIngredients()),
                Collections.singletonList(EntryIngredients.of(recipe.getOutput())),
                Optional.of(recipe)
        );
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public int getWidth() {
        return recipe.get().getWidth();
    }

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    @Override
    public int getHeight() {
        return recipe.get().getHeight();
    }
}
