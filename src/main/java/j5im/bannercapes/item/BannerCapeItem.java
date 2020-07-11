package j5im.bannercapes.item;

import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;

import j5im.bannercapes.BannerCapes;
import j5im.bannercapes.Nubbin;

import j5im.bannercapes.interfaces.BannerCapeable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;

import java.util.List;

public class BannerCapeItem extends Item implements Trinket, BannerCapeable {
    public BannerCapeItem() {
        super(new Settings().group(ItemGroup.MISC).maxCount(1).rarity(Rarity.RARE));
        DispenserBlock.registerBehavior(this, BannerCapes.STACKABLE_TRINKET_DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean hasCollar() {
        return true;
    }

    @Override
    public boolean hasNubbins() {
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        TypedActionResult<ItemStack> result = Trinket.equipTrinket(player, hand);
        if (result.getResult().isAccepted()) {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        }
        return result;
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return BannerCapeable.canWearInSlot(group, slot);
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag patterns = stack.getSubTag("BlockEntityTag");

        if (patterns == null) {
            tooltip.add(new TranslatableText("item.banner_capes.banner_cape.empty").formatted(Formatting.GRAY));
        }
        int nubbins = stack.getOrCreateTag().getInt("Nubbins");
        tooltip.add(new TranslatableText("item.banner_capes.banner_cape.nubbins", new TranslatableText(Nubbin.nameFromIndex(nubbins))).formatted(Formatting.GRAY));
        if (patterns != null) {
            String baseColor = DyeColor.byId(patterns.getInt("Base")).getName();
            tooltip.add(new TranslatableText("block.minecraft." + baseColor + "_banner").formatted(Formatting.GRAY));
            BannerItem.appendBannerTooltip(stack, tooltip);
        }
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        super.onCraft(stack, world, player);
        /*
        if (BannerPatternMatcher.MOJANGSTA.matches(stack)) {
            BannerCapes.log(Level.DEBUG, "Matched crafted cape to MOJANGSTA");
        }
        if (BannerPatternMatcher.PILLAGER.matches(stack)) {
            BannerCapes.log(Level.DEBUG, "Matched crafted cape to PILLAGER");
        }
        */
    }

    @Override
    public ItemStack getStackForRender() {
        ItemStack stack = super.getStackForRender();
        stack.getOrCreateTag().putInt("Nubbins", 0);
        stack.getOrCreateSubTag("BlockEntityTag").putInt("Base", 14);
        return stack;
    }

    @Override
    public boolean hasBanner(ItemStack stack) {
        return stack.getSubTag("BlockEntityTag") != null;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateTag().putInt("Nubbins", 0);
            stacks.add(stack);
        }
    }

    public int getBaseColor(ItemStack stack) {
        return getBaseColor(stack);
    }

    public DyeColor getDyeColor(ItemStack stack) {
        return getDyeColorStatic(stack);
    }

    public static int getBaseColorStatic(ItemStack stack) {
        DyeColor color = getDyeColorStatic(stack);
        if (color == null) {
            return -1;
        }
        return color.getMaterialColor().color;
    }

    public static DyeColor getDyeColorStatic(ItemStack stack) {
        CompoundTag tag = stack.getSubTag("BlockEntityTag");
        if (tag == null) {
            return null;
        }
        return DyeColor.byId(tag.getInt("Base"));
    }

    public static int getNubbinColorStatic(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        if (!tag.contains("Nubbins")) {
            tag.putInt("Nubbins", 0);
        }
        int nubbinIndex = tag.getInt("Nubbins");
        return Nubbin.itemColorFromIndex(nubbinIndex);
    }
}
