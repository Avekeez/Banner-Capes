package j5im.banner_capes.client;

import j5im.banner_capes.BannerCapes;
import j5im.banner_capes.item.BannerCapeItem;
import j5im.banner_capes.utils.ColorUtils;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.object.builder.v1.client.model.FabricModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class BannerCapesClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ColorProviderRegistry.ITEM.register((stack, tintIndex) -> {
                    if (tintIndex == 2) return BannerCapeItem.getBaseColorStatic(stack);
                    if (tintIndex == 1) return BannerCapeItem.getNubbinColorStatic(stack);
                    if(tintIndex == 0) return  BannerCapeItem.getCollarColorStatic(stack);
                    return ColorUtils.RGBtoDecimalColor(255,20,147);
                },
                BannerCapes.BANNER_CAPE);

        FabricModelPredicateProviderRegistry.register(
                BannerCapes.BANNER_CAPE,
                new Identifier(BannerCapes.MOD_ID, "has_banner"),
                (itemStack, clientWorld, livingEntity, i) -> itemStack.getSubNbt("BlockEntityTag") != null ? 1 : 0);
    }
}
