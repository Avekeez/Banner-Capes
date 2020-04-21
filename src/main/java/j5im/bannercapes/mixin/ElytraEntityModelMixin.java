package j5im.bannercapes.mixin;

import j5im.bannercapes.ElytraEntityModelAccess;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.ElytraEntityModel;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ElytraEntityModel.class)
public abstract class ElytraEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ElytraEntityModelAccess {
    @Shadow
    private ModelPart field_3364;
    @Shadow
    private ModelPart field_3365;

    public ModelPart getWingL() {
        return field_3365;
    }

    public ModelPart getWingR() {
        return field_3364;
    }
}
