package com.github.Gorden121.the_banner_capes.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.model.json.JsonUnbakedModel;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Environment(EnvType.CLIENT)
@Mixin(JsonUnbakedModel.class)
public interface JsonUnbakedModelAccessor {

    @Accessor("parentId")
    Identifier parentId();

    @Accessor("ambientOcclusion")
    boolean ambientOcclusion();

    @Invoker("isTextureReference")
    static boolean isTextureReference(String reference) {
        throw new AssertionError();
    }

}
