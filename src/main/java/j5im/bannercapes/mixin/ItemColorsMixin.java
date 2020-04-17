package j5im.bannercapes.mixin;

import j5im.bannercapes.BannerCapes;
import j5im.bannercapes.item.BannerCapeItem;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColors;
import org.apache.commons.logging.impl.Log4JLogger;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(ItemColors.class)
public class ItemColorsMixin {
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        BannerCapes.log(Level.DEBUG, "ItemColors mix'd-in!");
    }

    @Inject(
            method = "create(Lnet/minecraft/client/color/block/BlockColors;)Lnet/minecraft/client/color/item/ItemColors;",
            at = @At("RETURN"),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private static void create(BlockColors blockColors, CallbackInfoReturnable<ItemColors> cir) {
        BannerCapes.log(Level.DEBUG, "ItemColors mix'd-in!");
        ItemColors ic = cir.getReturnValue();
        ic.register(
                (stack, tintIndex) -> tintIndex != 1 ? -1 : BannerCapeItem.getBaseColor(stack),
                BannerCapes.BANNER_CAPE
        );
    }
}
