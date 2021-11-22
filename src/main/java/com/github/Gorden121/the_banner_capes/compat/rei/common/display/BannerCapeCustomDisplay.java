package com.github.Gorden121.the_banner_capes.compat.rei.common.display;


import com.github.Gorden121.the_banner_capes.recipe.BannerCapeShapedRecipe;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import me.shedaniel.rei.api.common.registry.RecipeManagerContext;
import me.shedaniel.rei.api.common.transfer.info.MenuInfo;
import me.shedaniel.rei.api.common.transfer.info.MenuSerializationContext;
import me.shedaniel.rei.api.common.transfer.info.simple.SimpleGridMenuInfo;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Optional;

public class BannerCapeCustomDisplay extends BannerCapeCraftingDisplay<BannerCapeShapedRecipe> {
    private int width;
    private int height;

    public BannerCapeCustomDisplay(@Nullable BannerCapeShapedRecipe possibleRecipe, List<EntryIngredient> input, List<EntryIngredient> output) {
        super(input, output, Optional.ofNullable(possibleRecipe));
        BitSet row = new BitSet(3);
        BitSet column = new BitSet(3);
        for (int i = 0; i < 9; i++)
            if (i < input.size()) {
                EntryIngredient stacks = input.get(i);
                if (stacks.stream().anyMatch(stack -> !stack.isEmpty())) {
                    row.set((i - (i % 3)) / 3);
                    column.set(i % 3);
                }
            }
        this.width = column.cardinality();
        this.height = row.cardinality();
    }

    public static BannerCapeCustomDisplay simple(List<EntryIngredient> input, List<EntryIngredient> output, Optional<Identifier> location) {
        BannerCapeShapedRecipe optionalRecipe = (BannerCapeShapedRecipe) location.flatMap(resourceLocation -> RecipeManagerContext.getInstance().getRecipeManager().get(resourceLocation))
                .orElse(null);
        return new BannerCapeCustomDisplay(optionalRecipe, input, output);
    }

    @Override
    public List<EntryIngredient> getInputEntries(MenuSerializationContext<?, ?, ?> context, MenuInfo<?, ?> info, boolean fill) {
        if (fill && info instanceof SimpleGridMenuInfo) {
            List<EntryIngredient> out = new ArrayList<>();
            int craftingWidth = ((SimpleGridMenuInfo<ScreenHandler, ?>) info).getCraftingWidth(context.getMenu());
            int craftingHeight = ((SimpleGridMenuInfo<ScreenHandler, ?>) info).getCraftingHeight(context.getMenu());
            for (int i = 0; i < 9; i++) {
                if (i < inputs.size()) {
                    int x = i % 3;
                    if (x < craftingWidth) {
                        out.add(inputs.get(i));
                        if (out.size() > craftingWidth * craftingHeight) break;
                    }
                }
            }
            while (out.size() < craftingWidth * craftingHeight) out.add(EntryIngredient.empty());
            return out;
        }

        return super.getInputEntries(context, info, fill);
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }
}
