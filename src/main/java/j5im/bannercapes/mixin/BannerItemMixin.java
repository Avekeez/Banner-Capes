package j5im.bannercapes.mixin;

import dev.emi.trinkets.api.Trinket;
import j5im.bannercapes.BannerCapes;
import j5im.bannercapes.interfaces.BannerCapeable;
import net.minecraft.block.Block;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.BannerItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.util.DyeColor;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BannerItem.class)
public abstract class BannerItemMixin extends WallStandingBlockItem implements Trinket, BannerCapeable {
    public BannerItemMixin(Block standingBlock, Block wallBlock, Settings settings) {
        super(standingBlock, wallBlock, settings);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        BannerCapes.log(Level.DEBUG, "BannerItem mix'd-in!");
        DispenserBlock.registerBehavior(this, BannerCapes.STACKABLE_TRINKET_DISPENSER_BEHAVIOR);
    }

    @Override
    public boolean canWearInSlot(String group, String slot) {
        return BannerCapeable.canWearInSlot(group, slot);
    }

    @Override
    public boolean hasBanner(ItemStack stack) {
        return true;
    }

    @Override
    public int getBaseColor(ItemStack stack) {
        return getDyeColor(stack).getMaterialColor().color;
    }

    @Override
    public DyeColor getDyeColor(ItemStack stack) {
        return getColor();
    }

    @Shadow
    abstract DyeColor getColor();
}
