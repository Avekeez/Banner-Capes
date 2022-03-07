package com.github.Gorden121.the_banner_capes.client;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.client.render.BannerCapeItemRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;

@Environment(EnvType.CLIENT)
public class BannerCapesClient implements ClientModInitializer {

    public static final BannerCapeItemRenderer BANNER_CAPE_ITEM_RENDERER = new BannerCapeItemRenderer();

    @Override
    public void onInitializeClient() {

        BuiltinItemRendererRegistry.INSTANCE.register(BannerCapes.BANNER_CAPE, BANNER_CAPE_ITEM_RENDERER);

    }
}
