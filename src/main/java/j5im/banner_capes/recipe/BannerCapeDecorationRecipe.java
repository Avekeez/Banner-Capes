package j5im.banner_capes.recipe;

import j5im.banner_capes.BannerCapes;
import j5im.banner_capes.Nubbin;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

// one banner cape and/or at most one banner or nubbin = banner cape with the banner patterns and nubbin
public class BannerCapeDecorationRecipe extends SpecialCraftingRecipe {
    public BannerCapeDecorationRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        boolean foundCape = false;
        boolean foundBanner = false;
        boolean foundNubbin = false;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack cur = inv.getStack(i);
            if (!cur.isEmpty()) {
                Item item = cur.getItem();
                if (item instanceof BannerItem) {
                    if (foundBanner) {
                        return false;
                    }
                    foundBanner = true;
                }
                else if (item == BannerCapes.BANNER_CAPE) {
                    if (foundCape) {
                        return false;
                    }
                    foundCape = true;
                }
                else if (Nubbin.isNubbinable(cur)) {
                    if (foundNubbin) {
                        return false;
                    }
                    foundNubbin = true;
                } else {
                    return false;
                }
            }
        }
        return foundCape;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {
        ItemStack banner = ItemStack.EMPTY;
        ItemStack cape = ItemStack.EMPTY;
        ItemStack nubbin = ItemStack.EMPTY;
        for (int i = 0; i < inv.size(); i++) {
            ItemStack cur = inv.getStack(i);
            if (!cur.isEmpty()) {
                if (cur.getItem() instanceof BannerItem) {
                    banner = cur;
                } else if (cur.getItem() == BannerCapes.BANNER_CAPE){
                    cape = cur.copy();
                } else if (Nubbin.isNubbinable(cur)) {
                    nubbin = cur;
                }
            }
        }
        if (cape.isEmpty()) {
            return cape;
        } else {
            if (!banner.isEmpty()) {
                NbtCompound tag = banner.getSubNbt("BlockEntityTag");
                tag = (tag == null) ? new NbtCompound() : tag.copy();
                tag.putInt("Base", ((BannerItem)banner.getItem()).getColor().getId());
                cape.setSubNbt("BlockEntityTag", tag);
            }
            if (!nubbin.isEmpty()) {
                cape.getOrCreateNbt().putInt("Nubbins", Nubbin.indexFromItem(nubbin));
            }
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
