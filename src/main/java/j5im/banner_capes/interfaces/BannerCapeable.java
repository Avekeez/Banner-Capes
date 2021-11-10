package j5im.banner_capes.interfaces;

import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

// cool and groovy name for a cool and groovy interface
public interface BannerCapeable {

    default boolean hasCollar () {
        return false;
    }

    default boolean hasNubbins () {
        return false;
    }

    boolean hasBanner(ItemStack stack);

    int getBaseColor(ItemStack stack);

    DyeColor getDyeColor(ItemStack stack);
}