package j5im.bannercapes.client.render;

import com.mojang.datafixers.util.Pair;

import dev.emi.trinkets.api.*;

import j5im.bannercapes.BannerCapes;
import j5im.bannercapes.Nubbin;
import j5im.bannercapes.item.BannerCapeItem;

import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

import java.util.List;

public class BannerCapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    private final ModelPart collar;
    private final ModelPart cape;
    private final ModelPart nubbins;
    public BannerCapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
        // will use the default banner_base texture, with the wood
        this.collar = new ModelPart(64, 64, 0, 42);
        this.collar.addCuboid(0.0F, 0.0F, 0.0F, 8.0F, 1.0F, 1.0F, 0.0F);
        this.cape = new ModelPart(64, 64, 0, 0);
        this.cape.addCuboid(0.0F, 0.0F, 0.0F, 20.0F, 40.0F, 1.0F, 0.0F);
        this.nubbins = new ModelPart(16, 16, 8, 8);
        this.nubbins.addCuboid(-1.1f, 0, 0, 1.1f, 1f, 1, 0);
        this.nubbins.addCuboid(8f, 0, 0, 1.1f, 1, 1, 0);
    }

    @Override
    public void render(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        TrinketComponent comp = TrinketsApi.getTrinketComponent(player);
        ItemStack capeStack = comp.getStack(SlotGroups.CHEST, Slots.CAPE);
        if (!capeStack.isEmpty() && capeStack.getItem() == BannerCapes.BANNER_CAPE) {
            SpriteIdentifier bb_si = ModelLoader.BANNER_BASE;
            VertexConsumer bb_vc = bb_si.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            if (!player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
                matrix.translate(0, 0, 0.0625F);
            }
            matrix.push();
            ITrinket.translateToChest(matrix, this.getContextModel(), player, headYaw, headPitch);
            matrix.push();
            // shh magic numbers
            // multiples of 1/16 (pixel size), offset by 1/32 (originally had a pixel centered)
            // places it so one of the edges of the collar is at the top of the back
            matrix.translate(-0.25F, -0.4375F, 0.28125F);
            this.collar.render(matrix, bb_vc, light, OverlayTexture.DEFAULT_UV);
            renderNubbins(capeStack, matrix, vertexConsumerProvider, light);
            matrix.pop();
            if (capeStack.getSubTag("BlockEntityTag") != null) {
                matrix.push();
                // Same deal as above, but this time the offset is just to make it clip less
                // places the cape so the top is half a pixel into the collar
                matrix.translate(0.0F, -0.40625F, 0.325F);
                renderRotation(matrix, player, tickDelta);
                matrix.translate(-0.3125F, 0, 0);
                // scales it down to 10 by 20 pixel widths - cape is twice the resolution of
                // the player
                matrix.scale(0.5F, 0.5F, 1F);
                this.cape.render(matrix, bb_vc, light, OverlayTexture.DEFAULT_UV);
                renderPatterns(capeStack, matrix, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, bb_si);
                matrix.pop();
            }
            matrix.pop();
        }
    }

    // Renders patterns
    private void renderPatterns(ItemStack stack, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, int overlay, SpriteIdentifier si) {
        // translates nbt to patterns
        List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.method_24280(BannerCapeItem.getDyeColor(stack), BannerBlockEntity.method_24281(stack));
        // uses the built in banner renderer for patterns
        BannerBlockEntityRenderer.method_23802(matrix, vertexConsumerProvider, light, overlay, this.cape, si, true, list);
    }

    // Renders the nubbins, the lil things on the ends
    private void renderNubbins(ItemStack capeStack, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light) {
        Identifier tex = Nubbin.nubbinTextureFromIndex(capeStack.getOrCreateTag().getInt("Nubbins"));
        if (tex == null) tex = Nubbin.nubbinTextureFromIndex(0);

        VertexConsumer nub_vc = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(tex));
        this.nubbins.render(matrix, nub_vc, light, OverlayTexture.DEFAULT_UV);
    }

    // Renders the rotation of the cape
    // More or less from the CapeRendererFeature
    private void renderRotation(MatrixStack matrixStack, AbstractClientPlayerEntity player, float dt) {
        // represents a desired direction for the cape to point in global space
        // generally will point in the opposite direction of player movement
        double desiredX = MathHelper.lerp(dt, player.field_7524, player.field_7500) - MathHelper.lerp(dt, player.prevX, player.getX());
        double desiredY = MathHelper.lerp(dt, player.field_7502, player.field_7521) - MathHelper.lerp(dt, player.prevY, player.getY());
        double desiredZ = MathHelper.lerp(dt, player.field_7522, player.field_7499) - MathHelper.lerp(dt, player.prevZ, player.getZ());

        // vector pointing towards the back of the player's body
        float bodyYaw = player.bodyYaw;
        double backDirX = MathHelper.sin(bodyYaw * 0.017453292F);
        double backDirZ = -MathHelper.cos(bodyYaw * 0.017453292F);

        float pitch = MathHelper.clamp((float)desiredY * 10.0F, -6.0F, 32.0F);

        // add additional pitch based on how aligned the body is to its movement
        float speedPitch = (float)(desiredX * backDirX + desiredZ * backDirZ) * 100.0F;
        speedPitch = MathHelper.clamp(speedPitch, 0.0F, 150.0F);

        // make the cape wibblewobble up and down when the player is walking
        float speed = MathHelper.lerp(dt, player.field_7505, player.field_7483);
        pitch += MathHelper.sin(MathHelper.lerp(dt, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * speed;

        // adjust the yaw if the player movement is not aligned with the back
        float yaw = (float)(desiredX * backDirZ - desiredZ * backDirX) * 100.0F;
        yaw = MathHelper.clamp(yaw, -20.0F, 20.0F);

        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(6.0F + speedPitch / 2.0F + pitch));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(yaw / 2.0F));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw / 2.0F));
    }
}
