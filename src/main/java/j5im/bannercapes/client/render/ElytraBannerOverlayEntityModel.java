package j5im.bannercapes.client.render;

import com.google.common.collect.ImmutableList;
import j5im.bannercapes.ElytraEntityModelAccess;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;

@Environment(EnvType.CLIENT)
public class ElytraBannerOverlayEntityModel<T extends LivingEntity> extends AnimalModel<T> {
    private final ElytraEntityModel base;
    private final ModelPart wing_l = new ModelPart(this, 22, 0);
    private final ModelPart wing_r = new ModelPart(this, 22, 0);

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
    }
}
