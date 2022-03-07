package com.github.Gorden121.the_banner_capes.client.render.model;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class BannerCapeEntityModel {
    /**
     * The key of the cape model part, whose value is {@value}.
     */
    private static final String CAPE = "cape";
    /**
     * The key of the collar model part, whose value is {@value}.
     */
    private static final String COLLAR = "collar";
    /**
     * The key of the left nubbin model part, whose value is {@value}.
     */
    private static final String NUBBIN_LEFT = "nubbin_left";
    /**
     * The key of the right nubbin model part, whose value is {@value}.
     */
    private static final String NUBBIN_RIGHT = "nubbin_right";

    private final ModelPart cape;
    private final ModelPart collar;
    private final ModelPart nubbinLeft;
    private final ModelPart nubbinRight;

    public BannerCapeEntityModel() {
        List<ModelPart.Cuboid> collarParts = new ArrayList<>();
        collarParts.add(new ModelPart.Cuboid(0, 0, -4.5F, 0.0F, 0.0F, 9.0F, 1.5F, 1.5F, 0.0F, 0.0F, 0.0F, false, 9, 9));
        collar = new ModelPart(collarParts, Map.of());
        List<ModelPart.Cuboid> capeParts = new ArrayList<>();
        capeParts.add(new ModelPart.Cuboid(0, 0, -10.0F, 2.0F, -0.25F, 20.0F, 40.0F, 0.5F, 0.0F, 0.0F, 0.0F, false, 64, 64));
        cape = new ModelPart(capeParts, Map.of());
        List<ModelPart.Cuboid> nubbinLeftParts = new ArrayList<>();
        nubbinLeftParts.add(new ModelPart.Cuboid(0, 0, 4.5F, 0.0F, 0.0F, 2.5F, 1.5F, 1.5F, 0.0F, 0.0F, 0.0F, false, 9, 9));
        nubbinLeft = new ModelPart(nubbinLeftParts, Map.of());
        List<ModelPart.Cuboid> nubbinRightParts = new ArrayList<>();
        nubbinRightParts.add(new ModelPart.Cuboid(0, 0, -7F, 0.0F, 0.0F, 2.5F, 1.5F, 1.5F, 0.0F, 0.0F, 0.0F, false, 9, 9));
        nubbinRight = new ModelPart(nubbinRightParts, Map.of());
    }

    public ModelPart getCape() {
        return cape;
    }

    public ModelPart getCollar() {
        return collar;
    }

    public ModelPart getNubbinLeft() {
        return nubbinLeft;
    }

    public ModelPart getNubbinRight() {
        return nubbinRight;
    }
}
