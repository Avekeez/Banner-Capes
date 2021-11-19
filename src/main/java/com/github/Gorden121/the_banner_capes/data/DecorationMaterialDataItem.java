package com.github.Gorden121.the_banner_capes.data;

import net.minecraft.util.Identifier;

public class DecorationMaterialDataItem {

    public final Identifier textureIdentifier;
    public final Identifier itemIdentifier;
    public final boolean isNubbin, isCollar;
    public final AttributeModifierDataItem[] attributeModifiers;
    public boolean ignore;

    public DecorationMaterialDataItem(Identifier textureIdentifier, Identifier itemIdentifier, boolean isNubbin, boolean isCollar, AttributeModifierDataItem[] attributeModifiers, boolean ignore) {
        this.textureIdentifier = textureIdentifier;
        this.itemIdentifier = itemIdentifier;
        this.isNubbin = isNubbin;
        this.isCollar = isCollar;
        this.attributeModifiers = attributeModifiers;
        this.ignore = ignore;
    }
}

