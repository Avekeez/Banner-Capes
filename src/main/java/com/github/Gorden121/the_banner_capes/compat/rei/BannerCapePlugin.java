package com.github.Gorden121.the_banner_capes.compat.rei;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeCraftingDisplay;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;

public interface BannerCapePlugin {
    CategoryIdentifier<BannerCapeCraftingDisplay<?>> BANNER_CAPE_CRAFTING = CategoryIdentifier.of(BannerCapes.MOD_ID, "crafting_bannercape_shaped");
}
