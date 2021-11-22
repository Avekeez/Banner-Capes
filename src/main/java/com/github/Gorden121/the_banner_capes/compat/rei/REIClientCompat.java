package com.github.Gorden121.the_banner_capes.compat.rei;

import com.github.Gorden121.the_banner_capes.compat.rei.client.categories.BannerCapeCraftingCategory;
import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeShapedDisplay;
import com.github.Gorden121.the_banner_capes.recipe.BannerCapeShapedRecipe;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.category.CategoryRegistry;
import me.shedaniel.rei.api.client.registry.display.DisplayRegistry;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandlerRegistry;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.minecraft.client.gui.screen.ingame.CraftingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.item.Items;
import net.minecraft.recipe.RecipeType;

public class REIClientCompat implements REIClientPlugin, BannerCapePlugin {

    @Override
    public void registerCategories(CategoryRegistry registry) {
        registry.add(new BannerCapeCraftingCategory());
        registry.addWorkstations(BANNER_CAPE_CRAFTING, EntryStacks.of(Items.CRAFTING_TABLE));
    }

    @Override
    public void registerDisplays(DisplayRegistry registry) {

        //registry.registerFiller(BannerCapeShapedRecipe.class, RecipeType.CRAFTING, BannerCapeShapedRecip);
        registry.registerRecipeFiller(BannerCapeShapedRecipe.class, RecipeType.CRAFTING, BannerCapeShapedDisplay::new);

        registry.registerFiller(BannerCapeShapedRecipe.class, BannerCapeShapedDisplay::new);
    }

    @Override
    public void registerScreens(ScreenRegistry registry) {

        registry.registerContainerClickArea(new Rectangle(88, 32, 28, 23), CraftingScreen.class, BANNER_CAPE_CRAFTING);
        registry.registerContainerClickArea(new Rectangle(137, 29, 10, 13), InventoryScreen.class, BANNER_CAPE_CRAFTING);
    }

    @Override
    public void registerTransferHandlers(TransferHandlerRegistry registry) {
        registry.register(new BannerCapeRecipeBookHandler());
    }

    @Override
    public double getPriority() {
        return 100;
    }
}