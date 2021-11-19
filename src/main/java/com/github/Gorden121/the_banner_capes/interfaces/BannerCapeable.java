package com.github.Gorden121.the_banner_capes.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

// cool and groovy name for a cool and groovy interface
public interface BannerCapeable {

    static boolean hasCollar(ItemStack stack) {
        if(stack.getNbt() == null)
            return  false;

        return stack.getNbt().contains("Collar") && stack.getNbt().getInt("Collar") >= 0;
    }

    static boolean hasLeftNubbin(ItemStack stack) {
        if(stack.getNbt() == null)
            return  false;

        return stack.getNbt().contains("NubbinLeft") && stack.getNbt().getInt("NubbinLeft") >= 0;
    }

    static boolean hasRightNubbin(ItemStack stack) {
        if(stack.getNbt() == null)
            return  false;

        return stack.getNbt().contains("NubbinRight") && stack.getNbt().getInt("NubbinRight") >= 0;
    }

    static boolean hasBanner(ItemStack stack) {
        return stack.getSubNbt("BlockEntityTag") != null;
    }

    DyeColor getDyeColor(ItemStack stack);
}