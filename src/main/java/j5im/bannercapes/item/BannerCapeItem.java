package j5im.bannercapes.item;

import com.sun.istack.internal.Nullable;

import dev.emi.trinkets.api.ITrinket;
import dev.emi.trinkets.api.SlotGroups;
import dev.emi.trinkets.api.Slots;

import j5im.bannercapes.BannerCapes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import net.minecraft.block.DispenserBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.model.ModelPart;
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
import net.minecraft.world.World;

import java.util.List;

public class BannerCapeItem extends Item implements ITrinket {
    private final ModelPart collar;
    private final ModelPart cape;
    public BannerCapeItem() {
        super(new Settings().group(ItemGroup.MISC).maxCount(1));
        DispenserBlock.registerBehavior(this, TRINKET_DISPENSER_BEHAVIOR);

        this.collar = new ModelPart(64, 64, 0, 0);
        this.collar.addCuboid(0.0F, 0.0F, 0.0F, 10.0F, 1.0F, 1.0F, 0.0F);
        this.cape = new ModelPart(64, 64, 0, 0);
        this.cape.addCuboid(0.0F, 0.0F, 0.0F, 10.0F, 20.0F, 1.0F, 0.0F);

        this.addPropertyGetter(new Identifier(BannerCapes.MOD_ID, "has_banner"), (stack, world, entity) -> stack.getSubTag("BlockEntityTag") != null ? 1 : 0);
        this.addPropertyGetter(new Identifier(BannerCapes.MOD_ID, "cape_color"), (stack, world, entity) -> {
            DyeColor color = getDyeColor(stack);
            return color == null ? -1 : color.getId();
        });
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity player, Hand hand) {
        TypedActionResult<ItemStack> result = ITrinket.equipTrinket(player, hand);
        if (result.getResult().isAccepted()) {
            player.playSound(SoundEvents.ITEM_ARMOR_EQUIP_LEATHER, 1.0f, 1.0f);
        }
        return result;
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return group.equals(SlotGroups.CHEST) && slot.equals(Slots.CAPE);
    }

    @Environment(EnvType.CLIENT)
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        CompoundTag patterns = stack.getSubTag("BlockEntityTag");
        if (patterns == null) {
            tooltip.add(new TranslatableText("item.banner_capes.banner_cape.empty").formatted(Formatting.GRAY));
        }
        if (patterns != null) {
            String baseColor = DyeColor.byId(patterns.getInt("Base")).getName();
            tooltip.add(new TranslatableText("block.minecraft." + baseColor + "_banner").formatted(Formatting.GRAY));
            BannerItem.appendBannerTooltip(stack, tooltip);
        }
    }

    public static DyeColor getDyeColor(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateSubTag("BlockEntityTag");
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
