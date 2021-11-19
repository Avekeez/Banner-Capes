package com.github.Gorden121.the_banner_capes.compat;

import com.github.Gorden121.the_banner_capes.config.BannerCapesConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import me.shedaniel.autoconfig.AutoConfig;

public final class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(BannerCapesConfig.class, parent).get();
    }
}