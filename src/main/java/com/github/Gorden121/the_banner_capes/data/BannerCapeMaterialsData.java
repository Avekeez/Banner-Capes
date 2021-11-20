package com.github.Gorden121.the_banner_capes.data;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BannerCapeMaterialsData {

    public DecorationMaterialDataItem[] decorationMaterialsArray = getMinimumDecorationMaterials();
    public Map<String, Integer> materialMapping;

    public void validateNewDataSet(DecorationMaterialDataItem[] decorationMaterialsArray) {

        if(decorationMaterialsArray.length <= 0)
            return;

        this.decorationMaterialsArray = decorationMaterialsArray;

        if (BannerCapes.config().enableModSafetyCheck) {
            CheckIfModExists();
        }

        if(materialMapping == null)
            materialMapping = new HashMap<>();

        materialMapping.clear();

        for (int i = 0; i < decorationMaterialsArray.length; i++) {
            materialMapping.put(decorationMaterialsArray[i].itemIdentifier.toString(), i);
        };
    }

    public void CheckIfModExists() {

        for (DecorationMaterialDataItem decorationMaterial : decorationMaterialsArray) {
            Identifier identifier = new Identifier(decorationMaterial.textureIdentifier);

            if (identifier.getNamespace() != null) {
                if (!BannerCapes.config().knownMods.contains(identifier.getNamespace()) && !identifier.getNamespace().equals("minecraft")) {
                    decorationMaterial.ignore = true;
                } else {
                    decorationMaterial.ignore = false;
                }
            }

            for (AttributeModifierDataItem attributeModifier : decorationMaterial.attributeModifiers) {
                identifier = new Identifier(attributeModifier.attributeName);
                if (identifier.getNamespace() != null) {
                    if (attributeModifier.attributeVariant != AttributeModifierDataItem.AttributeVariant._IGNORE) {
                        if (!BannerCapes.config().knownMods.contains(identifier.getNamespace()) && !identifier.getNamespace().equals("minecraft")) {
                            attributeModifier.attributeVariant = AttributeModifierDataItem.AttributeVariant.IGNORE;
                        } else {
                            attributeModifier.attributeVariant = AttributeModifierDataItem.AttributeVariant.DEFAULT;
                        }
                    }
                }
            }
        }
    }

    public DecorationMaterialDataItem[] getMinimumDecorationMaterials() {
        DecorationMaterialDataItem[] newMaterialArray = new DecorationMaterialDataItem[]{
                //Wood
                new DecorationMaterialDataItem("minecraft:block/oak_planks", "minecraft:oak_planks", true, true, generateAttributeModifierConfigItem(), false)
        };

        materialMapping = new HashMap<>();
        for (int i = 0; i < newMaterialArray.length; i++) {
            materialMapping.put(newMaterialArray[i].itemIdentifier.toString(), i);
        };

        return newMaterialArray;
    }

    private AttributeModifierDataItem[] generateAttributeModifierConfigItem() {

        List<AttributeModifierDataItem> attributeModifierDataItemList = new ArrayList<>();
        attributeModifierDataItemList.add(new AttributeModifierDataItem(AttributeModifierDataItem.AttributeVariant.DEFAULT, "minecraft:generic.armor", 1, EntityAttributeModifier.Operation.ADDITION.toString()));
        //attributeModifierDataItemList.add(new AttributeModifierDataItem(AttributeModifierDataItem.AttributeVariant.DEFAULT, new Identifier("minecraft:generic.armor_toughness"), 1, EntityAttributeModifier.Operation.ADDITION));

        return attributeModifierDataItemList.toArray(new AttributeModifierDataItem[0]);
    }
}

