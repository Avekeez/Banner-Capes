package com.github.Gorden121.the_banner_capes.recipe;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.Collar;
import com.github.Gorden121.the_banner_capes.Nubbin;
import com.github.Gorden121.the_banner_capes.item.BannerCapeItem;
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

        boolean foundNubbinLeft = false;
        boolean foundNubbinRight = false;
        boolean foundCollar = false;
        boolean foundBanner = false;

        if (Slot4Stack.getItem() instanceof BannerItem
        )
        {

            if (!Slot0Stack.isEmpty()) {
                foundNubbinLeft = Nubbin.isNubbinable(Slot0Stack);
            }

            if( !Slot2Stack.isEmpty()) {
                foundNubbinRight = Nubbin.isNubbinable(Slot2Stack);
            }

            if (!Slot1Stack.isEmpty())
                foundCollar = Collar.isCollarable(Slot1Stack);

            int foundCosmetics = 0;
            if (foundNubbinLeft) foundCosmetics++;
            if (foundNubbinRight) foundCosmetics++;
            if (foundCollar) foundCosmetics++;

            return foundCosmetics > 0;
        }
        else if (Slot4Stack.getItem() instanceof BannerCapeItem)
        {

            if (!Slot0Stack.isEmpty()) {
                foundNubbinLeft = Nubbin.isNubbinable(Slot0Stack);
            }

            if( !Slot2Stack.isEmpty()) {
                foundNubbinRight = Nubbin.isNubbinable(Slot2Stack);
            }

            if (!Slot1Stack.isEmpty())
                foundCollar = Collar.isCollarable(Slot1Stack);

            if (!Slot7Stack.isEmpty()) {
                Item item = Slot7Stack.getItem();
                foundBanner = item instanceof BannerItem;
            }

            int foundCosmetics = 0;
            if (foundBanner) foundCosmetics++;
            if (foundNubbinLeft) foundCosmetics++;
            if (foundNubbinRight) foundCosmetics++;
            if (foundCollar) foundCosmetics++;

            return (foundCosmetics > 0);
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

        ) {
            ItemStack capeStack = new ItemStack(BannerCapes.BANNER_CAPE);

            if (!Slot0Stack.isEmpty() && Nubbin.isNubbinable(Slot0Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinLeft", Nubbin.identifierFromItem(Slot0Stack));
            }
            if (!Slot2Stack.isEmpty() && Nubbin.isNubbinable(Slot2Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinRight", Nubbin.identifierFromItem(Slot2Stack));
            }
            if (!Slot1Stack.isEmpty() && Collar.isCollarable(Slot1Stack)) {
                capeStack.getOrCreateNbt().putString("Collar", Collar.identifierFromItem(Slot1Stack));
            }
            if (!Slot4Stack.isEmpty() && Slot4Stack.getItem() instanceof BannerItem) {
                NbtCompound tag = Slot4Stack.getSubNbt("BlockEntityTag");
                tag = (tag == null) ? new NbtCompound() : tag.copy();
                tag.putInt("Base", ((BannerItem) Slot4Stack.getItem()).getColor().getId());
                capeStack.setSubNbt("BlockEntityTag", tag);
            }

            return capeStack;
        } else if (Slot4Stack.getItem() instanceof BannerCapeItem) {
            ItemStack capeStack = Slot4Stack.copy();
            if (!Slot0Stack.isEmpty() && Nubbin.isNubbinable(Slot0Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinLeft", Nubbin.identifierFromItem(Slot0Stack));
            }
            if (!Slot2Stack.isEmpty() && Nubbin.isNubbinable(Slot2Stack)
            ) {
                capeStack.getOrCreateNbt().putString("NubbinRight", Nubbin.identifierFromItem(Slot2Stack));
            }
            if (!Slot1Stack.isEmpty() && Collar.isCollarable(Slot1Stack)) {
                capeStack.getOrCreateNbt().putString("Collar", Collar.identifierFromItem(Slot1Stack));
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
