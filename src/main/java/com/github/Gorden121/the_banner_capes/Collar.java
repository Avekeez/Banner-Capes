package com.github.Gorden121.the_banner_capes;

import com.github.Gorden121.the_banner_capes.data.DecorationMaterialDataItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Collar {

    // returns null if the index is invalid
    public static Identifier textureFromString(String identifier) {

        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(identifier)) {
            return new Identifier(BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(identifier)].textureIdentifier);
        }
        return null;
    }

    public static Identifier getFirstCollarableTexture() {
        for (DecorationMaterialDataItem item: BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray) {
            if(item.isCollar)
                return new Identifier(item.textureIdentifier);
        }

        return new Identifier(BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].textureIdentifier);
    }

    public static String nameFromString(String identifier) {

        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(identifier)) {
            return Registry.ITEM.get(new Identifier(BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(identifier)].itemIdentifier)).getTranslationKey();
        }
        return null;
    }

    public static boolean isStringCollarable(String id) {
        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(id))
            return BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(id)].isCollar;

        return false;
    }

    public static boolean isCollarable(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Identifier itemIdentifier = Registry.ITEM.getId(stack.getItem());
        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(itemIdentifier.toString()))
            return BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(itemIdentifier.toString())].isCollar;

        return false;
    }

    public static int getNextCollarable(int index) {
        DecorationMaterialDataItem[] materialArray = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray;
        for (int i = index+1; i < materialArray.length; i++) {
            if(materialArray[i].isCollar) {
                return i;
            }
        }
        for (int i = 0; i < materialArray.length; i++) {
            if(materialArray[i].isCollar) {
                return i;
            }
        }
        return 0;
    }

    public static int getFirstCollarable() {
        DecorationMaterialDataItem[] materialArray = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray;

        for (int i = 0; i < materialArray.length; i++) {
            if(materialArray[i].isCollar) {
                return i;
            }
        }
        return 0;
    }
    // returns the index or -1 if not found
    public static String identifierFromItem(ItemStack stack) {
        if (stack.isEmpty()) return null;

        String itemIdentifier = Registry.ITEM.getId(stack.getItem()).toString();

        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(itemIdentifier))
            return itemIdentifier;

        return null;
    }
}