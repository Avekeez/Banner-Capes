package com.github.Gorden121.the_banner_capes.data;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

import java.util.*;

public class AttributeModifierDataItem implements Cloneable {

    public enum AttributeVariant {_IGNORE, IGNORE, DEFAULT}

    public AttributeVariant attributeVariant;
   public String attributeName;
   public int modifierValue;
   public String operation;

    public AttributeModifierDataItem(AttributeVariant attributeVariant, String attributeName, int modifierValue, String operation) {
        this.attributeVariant = attributeVariant;
        this.attributeName = attributeName;
        this.modifierValue = modifierValue;
        this.operation = operation;

    }

    public boolean equalAttributeName(AttributeModifierDataItem item) {
        return this.attributeName.equals(item.attributeName);
    }

    public static boolean listContainsAttributeName(List<AttributeModifierDataItem> list, AttributeModifierDataItem item) {
        return indexOfAttributeName(list, item) >= 0;
    }

    public static int indexOfAttributeName(List<AttributeModifierDataItem> list, AttributeModifierDataItem item) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).equalAttributeName(item)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public AttributeModifierDataItem clone() {
        try {
            AttributeModifierDataItem clone = (AttributeModifierDataItem) super.clone();
            // TODO: copy mutable state here, so the clone can't change the internals of the original
            clone.attributeVariant = attributeVariant;
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}

