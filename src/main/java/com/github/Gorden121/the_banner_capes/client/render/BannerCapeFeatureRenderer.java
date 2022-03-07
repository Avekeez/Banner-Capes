package com.github.Gorden121.the_banner_capes.client.render;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.Collar;
import com.github.Gorden121.the_banner_capes.Nubbin;
import com.github.Gorden121.the_banner_capes.client.render.model.BannerCapeEntityModel;
import com.mojang.datafixers.util.Pair;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import dev.emi.trinkets.api.client.TrinketRenderer;
import com.github.Gorden121.the_banner_capes.interfaces.BannerCapeable;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
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
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;

import java.util.List;

public class BannerCapeFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    BannerCapeEntityModel capeModel;

    public BannerCapeFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);

        capeModel = new BannerCapeEntityModel();
    }

    @Override
    public void render(MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float customAngle, float headYaw, float headPitch) {
        if(TrinketsApi.getTrinketComponent(player).isPresent()) {
        TrinketComponent comp = TrinketsApi.getTrinketComponent(player).get();
        ItemStack capeStack = comp.getInventory().get("chest").get("cape").getStack(0);

        if (!capeStack.isEmpty() && capeStack.getItem() instanceof BannerCapeable capeItem) {
            SpriteIdentifier bb_si = ModelLoader.BANNER_BASE;
            VertexConsumer bb_vc = bb_si.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
            if (!player.getEquippedStack(EquipmentSlot.CHEST).isEmpty()) {
                matrix.translate(0, 0, 0.0625F);
            }
            matrix.push();
            TrinketRenderer.translateToChest(matrix, this.getContextModel(), player);

            matrix.push();
            // shh magic numbers
            // multiples of 1/16 (pixel size), offset by 1/32 (originally had a pixel centered)
            // places it so one of the edges of the collar is at the top of the back
            matrix.translate(0, -(1F / 16F * BannerCapes.config().bannerCapeDesign.getCollarOffset()), 0.28125F);
            if (BannerCapeable.hasCollar(capeStack))
                renderCollar(capeStack, matrix, vertexConsumerProvider, light);
            if (BannerCapeable.hasLeftNubbin(capeStack))
                renderNubbin(true, capeStack, matrix, vertexConsumerProvider, light);
            if (BannerCapeable.hasRightNubbin(capeStack))
                renderNubbin(false, capeStack, matrix, vertexConsumerProvider, light);
            matrix.pop();

            if (BannerCapeable.hasBanner(capeStack)) {
                matrix.push();
                // Same deal as above, but this time the offset is just to make it clip less
                // places the cape so the top is half a pixel into the collar
                matrix.translate(0.0F, -(1F / 16F * BannerCapes.config().bannerCapeDesign.getCapeOffset()), 0.325F);
                renderRotation(matrix, player, tickDelta);
                // scales it down to 10 by 20 pixel widths - cape is twice the resolution of
                // the player
                matrix.scale((1F / 16F * BannerCapes.config().bannerCapeDesign.getBannerSizeX()), (1F / 16F * BannerCapes.config().bannerCapeDesign.getBannerSizeY()), 1F);
                capeModel.getCape().render(matrix, bb_vc, light, OverlayTexture.DEFAULT_UV);
                renderPatterns(capeItem, capeStack, matrix, vertexConsumerProvider, light, bb_si);
                matrix.pop();
            }
            matrix.pop();
        }
        }
    }

    private void renderPatterns(BannerCapeable item, ItemStack stack, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, SpriteIdentifier si) {
        // translates nbt to patterns
        List<Pair<BannerPattern, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(item.getDyeColor(stack), BannerBlockEntity.getPatternListTag(stack));
        // uses the built in banner renderer for patterns
        BannerBlockEntityRenderer.renderCanvas(matrix, vertexConsumerProvider, light, OverlayTexture.DEFAULT_UV, capeModel.getCape(), si, true, list, false);
    }

    // Renders the collar, the top middle bar
    private void renderCollar(ItemStack capeStack, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light) {
        Identifier tex = Collar.textureFromString(capeStack.getOrCreateNbt().getString("Collar"));
        if (tex == null) tex = Collar.getFirstCollarableTexture();
        tex = new Identifier(tex.getNamespace(), "textures/" + tex.getPath() + ".png");

        VertexConsumer col_vc = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(tex));
        capeModel.getCollar().render(matrix, col_vc, light, OverlayTexture.DEFAULT_UV);
    }

    // Renders the nubbins, the lil things on the ends
    private void renderNubbin(boolean leftRight, ItemStack capeStack, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light) {
        if (leftRight) {
            Identifier texLeft = Nubbin.textureFromString(capeStack.getOrCreateNbt().getString("NubbinLeft"));
            if (texLeft == null) texLeft = Nubbin.getFirstNubbinableTexture();
            texLeft = new Identifier(texLeft.getNamespace(), "textures/" + texLeft.getPath() + ".png");
            VertexConsumer nubLeft_vc = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(texLeft));
            capeModel.getNubbinLeft().render(matrix, nubLeft_vc, light, OverlayTexture.DEFAULT_UV);
        } else {
            Identifier texRight = Nubbin.textureFromString(capeStack.getOrCreateNbt().getString("NubbinRight"));
            if (texRight == null) texRight = Nubbin.getFirstNubbinableTexture();
            texRight = new Identifier(texRight.getNamespace(), "textures/" + texRight.getPath() + ".png");
            VertexConsumer nubRight_vc = vertexConsumerProvider.getBuffer(RenderLayer.getEntitySolid(texRight));
            capeModel.getNubbinRight().render(matrix, nubRight_vc, light, OverlayTexture.DEFAULT_UV);
        }
    }

    // Renders the rotation of the cape
    // More or less from the CapeRendererFeature
    private void renderRotation(MatrixStack matrixStack, AbstractClientPlayerEntity player, float dt) {
        // represents a desired direction for the cape to point in global space
        // generally will point in the opposite direction of player movement
        double desiredX = MathHelper.lerp(dt, player.prevCapeX, player.capeX) - MathHelper.lerp(dt, player.prevX, player.getX());
        double desiredY = MathHelper.lerp(dt, player.prevCapeY, player.capeY) - MathHelper.lerp(dt, player.prevY, player.getY());
        double desiredZ = MathHelper.lerp(dt, player.prevCapeZ, player.capeZ) - MathHelper.lerp(dt, player.prevZ, player.getZ());

        // vector pointing towards the back of the player's body
        float bodyYaw = player.bodyYaw;
        double backDirX = MathHelper.sin(bodyYaw * 0.017453292F);
        double backDirZ = -MathHelper.cos(bodyYaw * 0.017453292F);

        float pitch = MathHelper.clamp((float) desiredY * 10.0F, -6.0F, 32.0F);

        // add additional pitch based on how aligned the body is to its movement
        float speedPitch = (float) (desiredX * backDirX + desiredZ * backDirZ) * 100.0F;
        speedPitch = MathHelper.clamp(speedPitch, 0.0F, 150.0F);

        // make the cape wibblewobble up and down when the player is walking
        float speed = MathHelper.lerp(dt, player.prevStrideDistance, player.strideDistance);
        pitch += MathHelper.sin(MathHelper.lerp(dt, player.prevHorizontalSpeed, player.horizontalSpeed) * 6.0F) * 32.0F * speed;

        // adjust the yaw if the player movement is not aligned with the back
        float yaw = (float) (desiredX * backDirZ - desiredZ * backDirX) * 100.0F;
        yaw = MathHelper.clamp(yaw, -20.0F, 20.0F);

        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + speedPitch / 2.0F + pitch));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(yaw / 2.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F - yaw / 2.0F));
    }
}
