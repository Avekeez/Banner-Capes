package j5im.banner_capes.recipe;

import j5im.banner_capes.BannerCapes;
import j5im.banner_capes.Collar;
import j5im.banner_capes.Nubbin;
import j5im.banner_capes.item.BannerCapeItem;
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
public class BannerCapeRecipe extends SpecialCraftingRecipe {
    public BannerCapeRecipe(Identifier id) {
        super(id);
    }

    @Override
    public boolean matches(CraftingInventory inv, World world) {
        ItemStack Slot0Stack = inv.getStack(0);
        ItemStack Slot1Stack = inv.getStack(1);
        ItemStack Slot2Stack = inv.getStack(2);
        ItemStack Slot4Stack = inv.getStack(4);
        ItemStack Slot7Stack = inv.getStack(7);
        if (Slot4Stack.getItem() instanceof BannerItem
                && Nubbin.isNubbinable(Slot0Stack)
                && Nubbin.isNubbinable(Slot2Stack)
                && Collar.isCollarable(Slot1Stack)
                && Slot0Stack.getItem().equals(Slot2Stack.getItem())
        )
        {
            return true;
        }
        else if (Slot4Stack.getItem() instanceof BannerCapeItem)
        {
            boolean foundCape = false;
            boolean foundNubbin = false;
            boolean foundCollar = false;
            boolean foundBanner = false;

            if (!Slot4Stack.isEmpty()) {
                Item item = Slot4Stack.getItem();
                if (item == BannerCapes.BANNER_CAPE) {
                    foundCape = true;
                }
            }

            if (!Slot0Stack.isEmpty() && !Slot2Stack.isEmpty()) {
                foundNubbin = Nubbin.isNubbinable(Slot0Stack) && Nubbin.isNubbinable(Slot2Stack) && Slot0Stack.getItem().equals(Slot2Stack.getItem());
            }

            if (!Slot1Stack.isEmpty())
                foundCollar = Collar.isCollarable(Slot1Stack);

            if (!Slot7Stack.isEmpty()) {
                Item item = Slot7Stack.getItem();
                foundBanner = item instanceof BannerItem;
            }

            int foundCosmetics = 0;
            if (foundBanner) foundCosmetics++;
            if (foundNubbin) foundCosmetics++;
            if (foundCollar) foundCosmetics++;

            return (foundCosmetics >= 1) && foundCape;
        }
        return false;
    }

    @Override
    public ItemStack craft(CraftingInventory inv) {

        ItemStack Slot0Stack = inv.getStack(0);
        ItemStack Slot1Stack = inv.getStack(1);
        ItemStack Slot2Stack = inv.getStack(2);
        ItemStack Slot4Stack = inv.getStack(4);
        ItemStack Slot7Stack = inv.getStack(7);

        if (Slot4Stack.isEmpty()) {
            return Slot4Stack;
        } else if (Slot4Stack.getItem() instanceof BannerItem
                && Nubbin.isNubbinable(Slot0Stack)
                && Nubbin.isNubbinable(Slot2Stack)
                && Collar.isCollarable(Slot1Stack)
                && Slot0Stack.getItem().equals(Slot2Stack.getItem())
        ) {
            ItemStack cape = new ItemStack(BannerCapes.BANNER_CAPE);
            NbtCompound tag = Slot4Stack.getSubNbt("BlockEntityTag");
            tag = (tag == null) ? new NbtCompound() : tag.copy();
            tag.putInt("Base", ((BannerItem) Slot4Stack.getItem()).getColor().getId());
            cape.setSubNbt("BlockEntityTag", tag);
            cape.getOrCreateNbt().putInt("Nubbins", Nubbin.indexFromItem(Slot0Stack));
            cape.getOrCreateNbt().putInt("Collar", Collar.indexFromItem(Slot1Stack));

            return cape;
        } else if (Slot4Stack.getItem() instanceof BannerCapeItem) {
            ItemStack capeStack = Slot4Stack.copy();
            if (!Slot0Stack.isEmpty() && !Slot2Stack.isEmpty()
                    && Slot0Stack.getItem().equals(Slot2Stack.getItem())
                    && Nubbin.isNubbinable(Slot0Stack)
            ) {
                capeStack.getOrCreateNbt().putInt("Nubbins", Nubbin.indexFromItem(Slot0Stack));
            }
            if (!Slot1Stack.isEmpty() && Collar.isCollarable(Slot1Stack)) {
                capeStack.getOrCreateNbt().putInt("Collar", Collar.indexFromItem(Slot1Stack));
            }
            if (!Slot7Stack.isEmpty() && Slot7Stack.getItem() instanceof BannerItem) {
                NbtCompound tag = Slot7Stack.getSubNbt("BlockEntityTag");
                tag = (tag == null) ? new NbtCompound() : tag.copy();
                tag.putInt("Base", ((BannerItem) Slot7Stack.getItem()).getColor().getId());
                capeStack.setSubNbt("BlockEntityTag", tag);
            }
            return capeStack;
        }

        return Slot4Stack;
    }

    @Override
    @Environment(EnvType.CLIENT)
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return BannerCapes.BANNER_CAPE_SERIALIZER;
    }
}
