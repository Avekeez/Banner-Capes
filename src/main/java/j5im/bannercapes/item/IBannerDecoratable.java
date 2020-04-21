package j5im.bannercapes.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.util.DyeColor;
import net.minecraft.world.World;

import java.util.List;

public interface IBannerDecoratable {

    public boolean acceptsNubbins();

    public static DyeColor getDyeColor(ItemStack stack) {
        CompoundTag tag = stack.getSubTag("BlockEntityTag");
        if (tag == null) {
            return null;
        }
        return DyeColor.byId(tag.getInt("Base"));
    }

    public static int getBaseColor(ItemStack stack) {
        DyeColor color = getDyeColor(stack);
        if (color == null) {
            return -1;
        }
        return color.getMaterialColor().color;
    }
}
