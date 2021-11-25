package com.github.Gorden121.the_banner_capes.compat;

import com.github.clevernucleus.dataattributes.api.API;
import net.fabricmc.loader.api.FabricLoader;

public interface GeneralModCompat {

    static boolean doesClassExist(String name) {
        try {
            Class.forName(name);
            return true;
        } catch (ClassNotFoundException ignored) {}
        return false;
    }

    static boolean isDataAttributesPresent = FabricLoader.getInstance().getModContainer(API.MODID).isPresent();

}
