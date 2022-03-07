package com.github.Gorden121.the_banner_capes.data;

import com.google.gson.*;
import net.minecraft.client.render.model.json.ModelVariant;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

import java.lang.reflect.Type;

public class DecorationMaterialDataItem {

    public String textureIdentifier;
    public String itemIdentifier;
    public boolean isNubbin, isCollar;
    public AttributeModifierDataItem[] attributeModifiers;
    public boolean ignore;

    public DecorationMaterialDataItem(String textureIdentifier, String itemIdentifier, boolean isNubbin, boolean isCollar, AttributeModifierDataItem[] attributeModifiers, boolean ignore) {
        this.textureIdentifier = textureIdentifier;
        this.itemIdentifier = itemIdentifier;
        this.isNubbin = isNubbin;
        this.isCollar = isCollar;
        this.attributeModifiers = attributeModifiers;
        this.ignore = ignore;
    }
}

