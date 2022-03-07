package com.github.Gorden121.the_banner_capes.mixin;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.interfaces.BannerCapeable;
import dev.emi.trinkets.api.TrinketsApi;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.CapeFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// if a player is lucky enough to have a real cape, replace it with the banner cape if it's
// currently being worn
@Environment(EnvType.CLIENT)
@Mixin(CapeFeatureRenderer.class)
public class CapeFeatureRendererMixin {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        BannerCapes.log(Level.DEBUG, "CapeFeatureRenderer mix'd-in!");
    }

    @Inject(method = "render*", at = @At("HEAD"), cancellable = true)
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AbstractClientPlayerEntity abstractClientPlayerEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci) {
        if(TrinketsApi.getTrinketComponent(abstractClientPlayerEntity).isPresent()){
        ItemStack capeStack = TrinketsApi.getTrinketComponent(abstractClientPlayerEntity).get().getInventory().get("chest").get("cape").getStack(0);
        if (!capeStack.isEmpty() && capeStack.getItem() instanceof BannerCapeable) {
            ci.cancel();
        }}
    }
}
