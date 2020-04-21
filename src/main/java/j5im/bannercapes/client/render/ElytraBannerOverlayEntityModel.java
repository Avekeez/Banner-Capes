package j5im.bannercapes.client.render;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import j5im.bannercapes.ElytraEntityModelAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DyeColor;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ElytraBannerOverlayEntityModel<T extends LivingEntity> extends AnimalModel<T> {
    private final ElytraEntityModel base;
    private final ModelPart wing_l = new ModelPart(this, 1 - 22, 1 - 4);
    private final ModelPart wing_r = new ModelPart(this, 1 - 22, 1 - 4);

    public ElytraBannerOverlayEntityModel(ElytraEntityModel elytra) {
        base = elytra;

        this.wing_l.addCuboid(-10.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);

        this.wing_r.mirror = true;
        this.wing_r.addCuboid(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 2.0F, 1.0F);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(wing_l, wing_r);
    }

    @Override
    public void setAngles(T entity, float limbAngle, float limbDistance, float customAngle, float headYaw, float headPitch) {
        wing_l.copyPositionAndRotation(((ElytraEntityModelAccess)base).getWingL());
        wing_r.copyPositionAndRotation(((ElytraEntityModelAccess)base).getWingR());

        wing_l.pivotX = 20;
        wing_r.pivotX = -20;
    }

    public void renderBannerPatterns(MatrixStack matrices, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, SpriteIdentifier si, List<Pair<BannerPattern, DyeColor>> list) {
        BannerBlockEntityRenderer.method_23802(matrices, vertexConsumerProvider, light, overlay, wing_l, si, false, list);
        BannerBlockEntityRenderer.method_23802(matrices, vertexConsumerProvider, light, overlay, wing_r, si, false, list);
    }
}
