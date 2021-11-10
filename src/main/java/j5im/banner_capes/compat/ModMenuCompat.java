package j5im.banner_capes.compat;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

import j5im.banner_capes.config.BannerCapesConfig;
import me.shedaniel.autoconfig.AutoConfig;

public final class ModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(BannerCapesConfig.class, parent).get();
    }
}