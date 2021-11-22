package com.github.Gorden121.the_banner_capes.compat.rei;

import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeCraftingDisplay;
import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import me.shedaniel.rei.api.common.transfer.info.MenuInfoRegistry;
import me.shedaniel.rei.api.common.transfer.info.simple.RecipeBookGridMenuInfo;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;

public class REIServerCompat implements REIServerPlugin, BannerCapePlugin {

    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(BANNER_CAPE_CRAFTING, BannerCapeCraftingDisplay.serializer());
    }
}
