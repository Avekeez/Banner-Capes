package com.github.Gorden121.the_banner_capes.config;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;
import java.util.List;

@Config(name = "the_banner_capes")
public final class BannerCapesConfig implements ConfigData {

// --Commented out by Inspection START (10.11.2021 20:17):
//    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
//    @ConfigEntry.Gui.Tooltip
//    public int monstersDropRelicsChance = 15;
// --Commented out by Inspection STOP (10.11.2021 20:17)

    public String ConfigVersion = BannerCapes.ConfigVersionInternal;

    public boolean enableModSafetyCheck = true;

    public boolean enableAttributeModifiers = true;

    @ConfigEntry.Gui.EnumHandler(option = ConfigEntry.Gui.EnumHandler.EnumDisplayOption.BUTTON)
    public BannerCapeDesign bannerCapeDesign = BannerCapeDesign.BANNER_CAPES_DESIGN;

    @ConfigEntry.Gui.CollapsibleObject
    public CustomBannerCapeDesign customBannerCapeDesign = new CustomBannerCapeDesign(BannerCapeDesign.BANNER_CAPES_DESIGN);

    public List<String> knownMods = new ArrayList<>();

    @Override
    public void validatePostLoad() throws ValidationException {

        ConfigData.super.validatePostLoad();

        List<String> idList = new ArrayList<>();
        ModContainer[] containers = FabricLoader.getInstance().getAllMods().toArray(ModContainer[]::new);

        for (ModContainer container : containers) {
            idList.add(container.getMetadata().getId());
        }

        if (!knownMods.equals(idList)) {
            knownMods = idList;
        }

        if (!ConfigVersion.equals(BannerCapes.ConfigVersionInternal)) {
            ConfigVersion = BannerCapes.ConfigVersionInternal;
            enableModSafetyCheck = true;
        }
    }
}

