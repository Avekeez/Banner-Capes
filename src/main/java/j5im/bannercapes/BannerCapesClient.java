package j5im.bannercapes;

import j5im.bannercapes.item.BannerCapeItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;

public class BannerCapesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 2) return BannerCapeItem.getBaseColor(stack);
                    if (tintIndex == 1) return BannerCapeItem.getNubbinColor(stack);
                    return -1;
                },
                BannerCapes.BANNER_CAPE);
    }
}
