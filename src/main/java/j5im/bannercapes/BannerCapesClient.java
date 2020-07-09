package j5im.bannercapes;

import j5im.bannercapes.item.BannerCapeItem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class BannerCapesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 2) return BannerCapeItem.getBaseColor(stack);
                    if (tintIndex == 1) return BannerCapeItem.getNubbinColor(stack);
                    return -1;
                },
                BannerCapes.BANNER_CAPE);

        FabricModelPredicateProviderRegistry.register(
                BannerCapes.BANNER_CAPE,
                new Identifier(BannerCapes.MOD_ID, "has_banner"),
                (itemStack, clientWorld, livingEntity) ->
                        itemStack.getSubTag("BlockEntityTag") != null ? 1 : 0);
    }
}
