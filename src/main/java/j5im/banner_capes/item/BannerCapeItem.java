package j5im.banner_capes.item;

import dev.emi.trinkets.api.Trinket;
import dev.emi.trinkets.api.TrinketItem;

import j5im.banner_capes.BannerCapes;
import j5im.banner_capes.BannerPatternMatcher;
import j5im.banner_capes.Nubbin;
import j5im.banner_capes.interfaces.BannerCapeable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.AbstractBannerBlock;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;
import org.jetbrains.annotations.Debug;

import java.util.List;

public class BannerCapeItem extends TrinketItem implements BannerCapeable {
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
        ItemStack handStack = player.getStackInHand(hand);
        if (TrinketItem.equipItem(player, handStack)) {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            handStack.decrement(1);
            return new TypedActionResult<ItemStack>(ActionResult.SUCCESS, handStack);
        }
        else
        {
            return new TypedActionResult<ItemStack>(ActionResult.FAIL, handStack);
        }
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound patterns = stack.getSubNbt("BlockEntityTag");

        if (patterns == null) {
            tooltip.add(new TranslatableText("item.banner_capes.banner_cape.empty").formatted(Formatting.GRAY));
        }
        int nubbins = stack.getOrCreateNbt().getInt("Nubbins");
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

        if (BannerPatternMatcher.MOJANGSTA.matches(stack)) {
            BannerCapes.log(Level.DEBUG, "Matched crafted cape to MOJANGSTA");
        }
        if (BannerPatternMatcher.PILLAGER.matches(stack)) {
            BannerCapes.log(Level.DEBUG, "Matched crafted cape to PILLAGER");
        }

    }

    @Override
    public boolean hasBanner(ItemStack stack) {
        return stack.getSubNbt("BlockEntityTag") != null;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateNbt().putInt("Nubbins", 0);
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
        return color.getMapColor().color;
    }

    public static DyeColor getDyeColorStatic(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateSubNbt("BlockEntityTag");
        if (tag == null) {
            BannerCapes.log(Level.ERROR, "NoTag");
            return null;
        }
        return DyeColor.byId(tag.getInt("Base"));
    }

    public static int getNubbinColorStatic(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();
        if (!tag.contains("Nubbins")) {
            tag.putInt("Nubbins", 0);
        }
        int nubbinIndex = tag.getInt("Nubbins");
        return Nubbin.itemColorFromIndex(nubbinIndex);
    }
}
