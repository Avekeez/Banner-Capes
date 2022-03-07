package com.github.Gorden121.the_banner_capes.client.render;

import com.google.common.collect.Maps;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Either;
import com.github.Gorden121.the_banner_capes.interfaces.BannerCapeable;
import com.github.Gorden121.the_banner_capes.item.BannerCapeItem;
import com.github.Gorden121.the_banner_capes.mixin.JsonUnbakedModelAccessor;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.render.model.ModelRotation;
import net.minecraft.client.render.model.SpriteAtlasManager;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class BannerCapeItemRenderer implements BuiltinItemRendererRegistry.DynamicItemRenderer {

    boolean isSetup;
    ModelLoader modelLoader;
    SpriteAtlasManager atlasManager;
    ResourceManager resourceManager;
    TextureManager textureManager;
    Map<CapeColorComponents, BakedModel> bakedModels;

    public BannerCapeItemRenderer() {
    }

    private static Either<SpriteIdentifier, String> resolveReference(Identifier id, String name) {
        if (JsonUnbakedModelAccessor.isTextureReference(name)) {
            return Either.right(name.substring(1));
        } else {
            Identifier identifier = Identifier.tryParse(name);
            if (identifier == null) {
                throw new JsonParseException(name + " is not valid resource location");
            } else {
                return Either.left(new SpriteIdentifier(id, identifier));
            }
        }
    }

    public void Setup() {
        resourceManager = MinecraftClient.getInstance().getResourceManager();
        textureManager = MinecraftClient.getInstance().getTextureManager();
        modelLoader = new ModelLoader(resourceManager, MinecraftClient.getInstance().getBlockColors(), MinecraftClient.getInstance().getProfiler(), 0);
        atlasManager = modelLoader.upload(textureManager, MinecraftClient.getInstance().getProfiler());
        bakedModels = new HashMap<>();

        isSetup = true;
    }

    @Override
    public void render(ItemStack stack, ModelTransformation.Mode mode, MatrixStack matrix, VertexConsumerProvider vertexConsumerProvider, int light, int overlay) {

        matrix.translate(0.45D, 0.45D, 0.45D);

        if (!isSetup)
            Setup();

        if (stack.getItem() instanceof BannerCapeItem) {

            BakedModel bakedModel = null;

            CapeColorComponents colorKey = new CapeColorComponents(stack);
            if (colorKey.atLeastOneNotNull()) {
                if (bakedModels.containsKey(colorKey)) {
                    bakedModel = bakedModels.get(colorKey);
                } else {
                    JsonUnbakedModel unbakedModel = null;
                    try {
                        unbakedModel = loadModelFromJson(getModel(stack));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (unbakedModel == null)
                        return;

                    Map<String, String> map = new HashMap<>();
                    if(colorKey.capeColor != null)
                    map.put("layer0", getTextureFromDyeColor(colorKey.capeColor));
                    if(colorKey.leftNubbinTexture != null)
                    map.put("layer1", colorKey.leftNubbinTexture.toString());
                    if(colorKey.rightNubbinTexture != null)
                    map.put("layer2", colorKey.rightNubbinTexture.toString());
                    if(colorKey.collarTexture != null)
                    map.put("layer3", colorKey.collarTexture.toString());

                    unbakedModel = changeTextures(unbakedModel, map);
                    bakedModel = unbakedModel.bake(modelLoader, atlasManager::getSprite, ModelRotation.X0_Y0, new Identifier("the_banner_capes:banner_cape_generated"));
                }

                if (bakedModel == null)
                    return;

                PlayerEntity player = MinecraftClient.getInstance().player;
                boolean leftHanded;
                if (player != null)
                    leftHanded = MinecraftClient.getInstance().player.getStackInHand(Hand.OFF_HAND).equals(stack);
                else
                    leftHanded = false;

                if (mode == ModelTransformation.Mode.GUI) {

                    DiffuseLighting.disableGuiDepthLighting();
                    VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
                    MinecraftClient.getInstance().getItemRenderer().renderItem(stack, mode, leftHanded, matrix, immediate, light, overlay, bakedModel);
                    immediate.draw();
                    DiffuseLighting.enableGuiDepthLighting();
                } else {
                    MinecraftClient.getInstance().getItemRenderer().renderItem(stack, mode, leftHanded, matrix, vertexConsumerProvider, light, overlay, bakedModel);
                }
            }
        }
    }

    private JsonUnbakedModel loadModelFromJson(Identifier id) throws IOException {
        Reader reader = null;
        Resource resource = null;

        JsonUnbakedModel jsonUnbakedModel;
        try {

            resource = this.resourceManager.getResource(new Identifier(id.getNamespace(), "models/" + id.getPath() + ".json"));
            reader = new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8);

            jsonUnbakedModel = JsonUnbakedModel.deserialize(reader);

        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(resource);
        }

        return jsonUnbakedModel;
    }

    private Map<String, Either<SpriteIdentifier, String>> getTextureMap(Map<String, String> inputMap) {
        Identifier identifier = SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
        Map<String, Either<SpriteIdentifier, String>> map = Maps.newHashMap();

        for (Map.Entry<String, String> stringStringEntry : inputMap.entrySet()) {
            map.put(stringStringEntry.getKey(), resolveReference(identifier, stringStringEntry.getValue()));
        }

        return map;
    }

    public JsonUnbakedModel changeTextures(JsonUnbakedModel currentModel, Map<String, String> inputMap) {
        return new JsonUnbakedModel(((JsonUnbakedModelAccessor) currentModel).parentId(), currentModel.getElements(), getTextureMap(inputMap), ((JsonUnbakedModelAccessor) currentModel).ambientOcclusion(), currentModel.getGuiLight(), currentModel.getTransformations(), currentModel.getOverrides());
    }

    public String getTextureFromDyeColor(DyeColor dyeColor) {
        switch (dyeColor) {
            case PURPLE:
                return "minecraft:block/purple_concrete";
            case RED:
                return "minecraft:block/red_concrete";
            case BLUE:
                return "minecraft:block/blue_concrete";
            case CYAN:
                return "minecraft:block/cyan_concrete";
            case GRAY:
                return "minecraft:block/gray_concrete";
            case LIME:
                return "minecraft:block/lime_concrete";
            case PINK:
                return "minecraft:block/pink_concrete";
            case BLACK:
                return "minecraft:block/black_concrete";
            case BROWN:
                return "minecraft:block/brown_concrete";
            case GREEN:
                return "minecraft:block/green_concrete";
            case ORANGE:
                return "minecraft:block/orange_concrete";
            case YELLOW:
                return "minecraft:block/yellow_concrete";
            case MAGENTA:
                return "minecraft:block/magenta_concrete";
            case LIGHT_BLUE:
                return "minecraft:block/light_blue_concrete";
            case LIGHT_GRAY:
                return "minecraft:block/light_gray_concrete";
            case WHITE:
            default:
                return "minecraft:block/white_concrete";
        }
    }

    public Identifier getModel(ItemStack stack) {

        if(stack.getItem() instanceof BannerCapeItem) {

            boolean cape;
            cape = BannerCapeable.hasBanner(stack);
            boolean collar;
            collar = BannerCapeable.hasCollar(stack);
            boolean nubbin_left;
            nubbin_left = BannerCapeable.hasLeftNubbin(stack);
            boolean nubbin_right;
            nubbin_right = BannerCapeable.hasRightNubbin(stack);

            //All True
            if(cape && collar && nubbin_left && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_full_model");
            }
            //Three out of four
            else if(collar && nubbin_left && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_co_ln_rn_model");
            }
            else if(cape && nubbin_left && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_ln_rn_model");
            }
            else if(cape && collar && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_co_rn_model");
            }
            else if(cape && collar && nubbin_left) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_co_ln_model");
            }
            //Two out of four
            else if(nubbin_left && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_ln_rn_model");
            }
            else if(cape && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_rn_model");
            }
            else if(cape && nubbin_left) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_ln_model");
            }
            else if(collar && nubbin_left) {
                return new Identifier("the_banner_capes:item/banner_cape_co_ln_model");
            }
            else if(collar && nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_co_rn_model");
            }
            else if(cape && collar) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_co_model");
            }
            //One out of four
            else if(nubbin_left) {
                return new Identifier("the_banner_capes:item/banner_cape_ln_model");
            }
            else if(nubbin_right) {
                return new Identifier("the_banner_capes:item/banner_cape_rn_model");
            }
            else if(collar) {
                return new Identifier("the_banner_capes:item/banner_cape_co_model");
            }
            else if(cape) {
                return new Identifier("the_banner_capes:item/banner_cape_ca_model");
            }
        }

        return null;
    }
}

class CapeColorComponents {

    final DyeColor capeColor;
    final Identifier collarTexture;
    final Identifier leftNubbinTexture;
    final Identifier rightNubbinTexture;

    boolean hasCapeColor() {return capeColor != null;}
    boolean hasCollarTexture() {return collarTexture != null;}
    boolean hasLeftNubbinTexture() {return leftNubbinTexture != null;}
    boolean hasRightNubbinTexture() {return rightNubbinTexture != null;}


    public CapeColorComponents(ItemStack stack) {

        if ((stack.getItem() instanceof BannerCapeItem)) {
            this.capeColor = BannerCapeItem.getDyeColorStatic(stack);
            this.collarTexture = BannerCapeItem.getCollarTextureStatic(stack);
            this.leftNubbinTexture = BannerCapeItem.getNubbinTextureStatic(stack, true);
            this.rightNubbinTexture = BannerCapeItem.getNubbinTextureStatic(stack, false);
        } else {
            this.capeColor = null;
            this.collarTexture = null;
            this.leftNubbinTexture = null;
            this.rightNubbinTexture = null;
        }
    }

    public boolean atLeastOneNotNull() {
        return capeColor != null || collarTexture != null || leftNubbinTexture != null || rightNubbinTexture != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CapeColorComponents that = (CapeColorComponents) o;

        if (capeColor != that.capeColor) return false;
        if (!Objects.equals(collarTexture, that.collarTexture))
            return false;
        if (!Objects.equals(leftNubbinTexture, that.leftNubbinTexture))
            return false;
        return Objects.equals(rightNubbinTexture, that.rightNubbinTexture);
    }

    @Override
    public int hashCode() {
        int result = capeColor != null ? capeColor.hashCode() : 0;
        result = 31 * result + (collarTexture != null ? collarTexture.hashCode() : 0);
        result = 31 * result + (leftNubbinTexture != null ? leftNubbinTexture.hashCode() : 0);
        result = 31 * result + (rightNubbinTexture != null ? rightNubbinTexture.hashCode() : 0);
        return result;
    }
}