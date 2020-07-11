package j5im.bannercapes.interfaces;

import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

// cool and groovy name for a cool and groovy interface
public interface BannerCapeable {
    static boolean canWearInSlot(String group, String slot) {
        return group.equals(SlotGroups.CHEST) && slot.equals(Slots.CAPE);
    }

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
