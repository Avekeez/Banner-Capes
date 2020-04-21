package j5im.bannercapes.mixin;

import com.mojang.datafixers.util.Pair;
import j5im.bannercapes.client.render.ElytraBannerOverlayEntityModel;
import j5im.bannercapes.item.IBannerDecoratable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.ElytraFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.texture.Sprite;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ElytraFeatureRenderer.class)
public abstract class ElytraFeatureRendererMixin<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M> {

    @Shadow
    private static Identifier SKIN;

    @Shadow
    private ElytraEntityModel<T> elytra;

    private ItemStack lastStackSeen = ItemStack.EMPTY;
    private ElytraBannerOverlayEntityModel<T> overlayModel;

    public ElytraFeatureRendererMixin(FeatureRendererContext<T, M> context) {
        super(context);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    public void init(FeatureRendererContext<T, M> context, CallbackInfo ci) {
        overlayModel = new ElytraBannerOverlayEntityModel<T>(elytra);
    }

    // Capture the stack equipped by the player in render and save it in a special place forever
    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE_ASSIGN",
                    target = "Lnet/minecraft/entity/LivingEntity;getEquippedStack(Lnet/minecraft/entity/EquipmentSlot;)Lnet/minecraft/item/ItemStack;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, ItemStack stack) {
        lastStackSeen = stack;
    }

    @ModifyVariable(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;push()V"
            ))
    private Identifier changeTexture(Identifier in) {
        if (lastStackSeen.getItem() == Items.ELYTRA) {
            if (lastStackSeen.getSubTag("BlockEntityTag") != null) {
                in = SKIN;
            }
        }
        return in;
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/render/item/ItemRenderer;getArmorVertexConsumer(Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/RenderLayer;ZZ)Lnet/minecraft/client/render/VertexConsumer;"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void setOverlayAngles(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, ItemStack stack) {
        //getContextModel().copyStateTo(this.overlayModel);
        overlayModel.setAngles(livingEntity, f, g, j, k, l);
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/util/math/MatrixStack;pop()V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void renderOverlay(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, ItemStack stack) {
        if (stack.getSubTag("BlockEntityTag") != null) {
            SpriteIdentifier si = ModelLoader.SHIELD_BASE_NO_PATTERN;
            List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(IBannerDecoratable.getDyeColor(stack), BannerBlockEntity.method_24281(stack));
            overlayModel.renderBannerPatterns(matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV, si, list);
        }

        //VertexConsumer vc = ItemRenderer.getArmorVertexConsumer(vertexConsumerProvider, this.overlayModel.getLayer(SKIN), false, stack.hasEnchantmentGlint());
        //VertexConsumer vc = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(new Identifier("minecraft", "textures/entity/creeper.png")));
        //overlayModel.render(matrixStack, vc, i, OverlayTexture.DEFAULT_UV, 1, 1, 1,1);
    }
}
