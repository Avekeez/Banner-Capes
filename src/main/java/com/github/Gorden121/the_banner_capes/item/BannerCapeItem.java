package com.github.Gorden121.the_banner_capes.item;

import com.github.Gorden121.the_banner_capes.BannerCapes;
import com.github.Gorden121.the_banner_capes.Collar;
import com.github.Gorden121.the_banner_capes.Nubbin;
import com.github.Gorden121.the_banner_capes.data.AttributeModifierDataItem;
import com.github.Gorden121.the_banner_capes.interfaces.BannerCapeable;
import com.github.clevernucleus.dataattributes.api.API;
import com.google.common.collect.Multimap;
import dev.emi.trinkets.api.SlotReference;
import dev.emi.trinkets.api.TrinketItem;
import com.github.Gorden121.the_banner_capes.BannerPatternMatcher;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.*;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.apache.logging.log4j.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BannerCapeItem extends TrinketItem implements BannerCapeable {
    public BannerCapeItem() {
        super(new Settings().group(ItemGroup.COMBAT).maxCount(1).rarity(Rarity.RARE));
        DispenserBlock.registerBehavior(this, BannerCapes.STACKABLE_TRINKET_DISPENSER_BEHAVIOR);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        ItemStack handStack = player.getStackInHand(hand);
        if (TrinketItem.equipItem(player, handStack)) {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
            handStack.decrement(1);
            return new TypedActionResult<>(ActionResult.SUCCESS, handStack);
        }
        else
        {
            return new TypedActionResult<>(ActionResult.FAIL, handStack);
        }
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {

        NbtCompound patterns = stack.getSubNbt("BlockEntityTag");

        if(BannerCapeable.hasLeftNubbin(stack)) {
            String nubbinLeft = stack.getOrCreateNbt().getString("NubbinLeft");
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.nubbin_left", new TranslatableText(Nubbin.nameFromString(nubbinLeft))).formatted(Formatting.GRAY));
        }
        else
        {
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.no_nubbin_left").formatted(Formatting.GRAY));
        }

        if(BannerCapeable.hasRightNubbin(stack)) {
            String nubbinRight = stack.getOrCreateNbt().getString("NubbinRight");
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.nubbin_right", new TranslatableText(Nubbin.nameFromString(nubbinRight))).formatted(Formatting.GRAY));
        }
        else
        {
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.no_nubbin_right").formatted(Formatting.GRAY));
        }

        if(BannerCapeable.hasCollar(stack)) {
            String collar = stack.getOrCreateNbt().getString("Collar");
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.collar", new TranslatableText(Collar.nameFromString(collar))).formatted(Formatting.GRAY));
        }
        else
        {
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.no_collar").formatted(Formatting.GRAY));
        }

        if (patterns == null) {
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.no_banner").formatted(Formatting.GRAY));
        }
        else {
            String baseColor = DyeColor.byId(patterns.getInt("Base")).getName();
            tooltip.add(new TranslatableText("item.the_banner_capes.banner_cape.banner", new TranslatableText("block.minecraft." + baseColor + "_banner")).formatted(Formatting.GRAY));
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
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks) {
        if (this.isIn(group)) {
            ItemStack stack = new ItemStack(this);
            stack.getOrCreateNbt().putString("NubbinLeft", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
            stack.getOrCreateNbt().putString("NubbinRight", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
            stack.getOrCreateNbt().putString("Collar", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
            stacks.add(stack);
        }
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

    public static Identifier getNubbinTextureStatic(ItemStack stack, boolean leftRight) {
        NbtCompound tag = stack.getOrCreateNbt();

        String nubbinIdentifier;

        if(leftRight) {

            if(!tag.contains("NubbinLeft")) {
                return null;

            }

            if(tag.getType("NubbinLeft") == NbtElement.INT_TYPE) {
                String name = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[tag.getInt("NubbinLeft")].itemIdentifier;
                tag.remove("NubbinLeft");
                tag.putString("NubbinLeft", name);
            }

            if(!Nubbin.isStringNubbinable(tag.getString("NubbinLeft")))
            {
                tag.putString("NubbinLeft", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
            }

            nubbinIdentifier = tag.getString("NubbinLeft");
        }
        else {

            if(!tag.contains("NubbinRight")) {
                return null;
            }

            if(tag.getType("NubbinRight") == NbtElement.INT_TYPE) {
                String name = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[tag.getInt("NubbinRight")].itemIdentifier;
                tag.remove("NubbinRight");
                tag.putString("NubbinRight", name);
            }

            if(!Nubbin.isStringNubbinable(tag.getString("NubbinRight")))
            {
                tag.putString("NubbinRight", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
            }

            nubbinIdentifier = tag.getString("NubbinRight");
        }

        return Nubbin.textureFromString(nubbinIdentifier);
    }

    public static Identifier getCollarTextureStatic(ItemStack stack) {
        NbtCompound tag = stack.getOrCreateNbt();

        if(!tag.contains("Collar")) {
            return null;

        }
        if(tag.getType("Collar") == NbtElement.INT_TYPE) {
            String name = BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[tag.getInt("Collar")].itemIdentifier.toString();
            tag.remove("Collar");
            tag.putString("Collar", name);
        }

        if(!Collar.isStringCollarable(tag.getString("Collar")))
        {
            tag.putString("Collar", BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[0].itemIdentifier);
        }

        String collarIdentifier = tag.getString("Collar");

        return Collar.textureFromString(collarIdentifier);
    }

    public Multimap<EntityAttribute, EntityAttributeModifier> getModifiers(ItemStack stack, SlotReference slot, LivingEntity entity, UUID uuid) {
        var modifiers = super.getModifiers(stack, slot, entity, uuid);

        if(BannerCapes.config().enableAttributeModifiers) {
            AttributeModifierDataItem[] attributeModifierDataItems = getAttributeModifiersFromConfig(stack);

            for (AttributeModifierDataItem item : attributeModifierDataItems) {
                modifiers.put(API.getAttribute(new Identifier(item.attributeName)).get(), new EntityAttributeModifier(uuid, "the_banner_capes:" + new Identifier(item.attributeName).getPath(), item.modifierValue, EntityAttributeModifier.Operation.valueOf(item.operation)));
            }
        }
        return modifiers;
    }

    public AttributeModifierDataItem[] getAttributeModifiersFromConfig(ItemStack stack)
    {
        List<AttributeModifierDataItem> attributeModifierDataItems = new ArrayList<>();
        NbtCompound tag = stack.getOrCreateNbt();
        loopThroughAttributeModifiersInConfigAtIdentifier(tag.getString("NubbinLeft"), attributeModifierDataItems);
        loopThroughAttributeModifiersInConfigAtIdentifier(tag.getString("NubbinRight"), attributeModifierDataItems);
        loopThroughAttributeModifiersInConfigAtIdentifier(tag.getString("Collar"), attributeModifierDataItems);

        return attributeModifierDataItems.toArray(AttributeModifierDataItem[]::new);
    }

    public void loopThroughAttributeModifiersInConfigAtIdentifier(String identifier, List<AttributeModifierDataItem> attributeModifierDataItems) {

        if(identifier == null)
            return;

        if(!BannerCapes.bannerCapeMaterialsData.materialMapping.containsKey(identifier))
            return;

        int index = BannerCapes.bannerCapeMaterialsData.materialMapping.get(identifier);
        for (AttributeModifierDataItem item : BannerCapes.bannerCapeMaterialsData.decorationMaterialsArray[index].attributeModifiers)
        {
            if(item.attributeVariant != AttributeModifierDataItem.AttributeVariant.IGNORE && item.attributeVariant != AttributeModifierDataItem.AttributeVariant._IGNORE ) {

                    if(AttributeModifierDataItem.listContainsAttributeName(attributeModifierDataItems, item)) {
                      AttributeModifierDataItem listItem = attributeModifierDataItems.get(AttributeModifierDataItem.indexOfAttributeName(attributeModifierDataItems, item));
                      AttributeModifierDataItem newItem = new AttributeModifierDataItem(listItem.attributeVariant,listItem.attributeName,listItem.modifierValue+item.modifierValue, listItem.operation);
                      attributeModifierDataItems.remove(listItem);
                      attributeModifierDataItems.add(newItem);
                    }
                    else
                    {
                        attributeModifierDataItems.add(item.clone());
                    }
            }
        }

    }
}
