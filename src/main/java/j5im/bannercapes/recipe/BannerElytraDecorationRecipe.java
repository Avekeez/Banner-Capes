package j5im.bannercapes.recipe;

import j5im.bannercapes.BannerCapes;

import j5im.bannercapes.Nubbin;
import j5im.bannercapes.item.IBannerDecoratable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class BannerElytraDecorationRecipe extends SpecialCraftingRecipe {
    public BannerElytraDecorationRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean foundElytra = false;
        boolean foundBanner = false;
        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack cur = inv.getInvStack(i);
            if (!cur.isEmpty()) {
                Item item = cur.getItem();
                if (item instanceof BannerItem) {
                    if (foundBanner) {
                        return false;
                    }
                    foundBanner = true;
                } else if (item == Items.ELYTRA) {
                    if (foundElytra) {
                        return false;
                    }
                    foundElytra = true;
                } else {
                    return false;
                }
            }
        }
        return foundElytra && foundBanner;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack elytra = ItemStack.EMPTY;
        for (int i = 0; i < inv.getInvSize(); i++) {
            ItemStack cur = inv.getInvStack(i);
            if (!cur.isEmpty()) {
                if (cur.getItem() instanceof BannerItem) {
                    banner = cur;
                } else if (cur.getItem() == Items.ELYTRA){
                    elytra = cur.copy();
                }
            }
        }
        if (elytra.isEmpty() || banner.isEmpty()) {
            return elytra;
        } else {
            CompoundTag tag = banner.getSubTag("BlockEntityTag");
            tag = (tag == null) ? new CompoundTag() : tag.copy();
            tag.putInt("Base", ((BannerItem)banner.getItem()).getColor().getId());
            elytra.putSubTag("BlockEntityTag", tag);
            return elytra;
        }
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BannerCapes.BANNER_ELYTRA_DECORATION_SERIALIZER;
    }
}
