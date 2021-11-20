package com.github.Gorden121.the_banner_capes;

import com.github.Gorden121.the_banner_capes.data.DecorationMaterialDataItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class Nubbin {

    // returns null if the index is invalid
    public static Identifier textureFromString(String identifier) {

        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(identifier)) {
            return new Identifier(BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(identifier)].textureIdentifier);
        }

        return null;
    }

    public static Identifier getFirstNubbinableTexture() {
        for (DecorationMaterialDataItem item: BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray) {
            if(item.isNubbin)
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

    public static boolean isStringNubbinable(String id) {
        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(id))
            return BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(id)].isNubbin;

        return false;
    }

    public static boolean isNubbinable(ItemStack stack) {
        if (stack.isEmpty()) return false;

        Identifier itemIdentifier = Registry.ITEM.getId(stack.getItem());
        if(BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(itemIdentifier.toString()))
            return BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[BannerCapes.bannerCapeMaterialsData.materialMapping.get(itemIdentifier.toString())].isNubbin;

        return false;
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