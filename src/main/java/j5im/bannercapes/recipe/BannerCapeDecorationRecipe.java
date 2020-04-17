package j5im.bannercapes.recipe;

import j5im.bannercapes.BannerCapes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// one banner cape + one banner = banner cape with the banner patterns
public class BannerCapeDecorationRecipe extends SpecialCraftingRecipe {
    public BannerCapeDecorationRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack out = ItemStack.EMPTY;
        ItemStack banner = ItemStack.EMPTY;
        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack cur = inv.getInvStack(i);
            if (!cur.isEmpty()) {
                if (cur.getItem() instanceof BannerItem) {
                    if (!banner.isEmpty()) {
                        return false;
                    }
                    banner = cur;
                } else {
                    if (cur.getItem() != BannerCapes.BANNER_CAPE || !out.isEmpty()) {
                        return false;
                    }
                    out = cur;
                }
            }
        }
        return !out.isEmpty() && !banner.isEmpty();
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack cape = ItemStack.EMPTY;
        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack cur = inv.getInvStack(i);
            if (!cur.isEmpty()) {
                if (cur.getItem() instanceof BannerItem) {
                    banner = cur;
                } else if (cur.getItem() == BannerCapes.BANNER_CAPE){
                    cape = cur.copy();
                }
            }
        }
        if (cape.isEmpty()) {
            return cape;
        } else {
            CompoundTag tag = banner.getSubTag("BlockEntityTag");
            tag = (tag == null) ? new CompoundTag() : tag.copy();
            tag.putInt("Base", ((BannerItem)banner.getItem()).getColor().getId());
            cape.putSubTag("BlockEntityTag", tag);
            cape.getOrCreateTag().putInt("Nubbins", 0); // put the default nubbins in
            return cape;
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BannerCapes.BANNER_CAPE_DECORATION_SERIALIZER;
    }
}
