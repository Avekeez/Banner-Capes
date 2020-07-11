package j5im.bannercapes;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import j5im.bannercapes.item.BannerCapeItem;
import j5im.bannercapes.recipe.BannerCapeDecorationRecipe;

import net.fabricmc.api.ModInitializer;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class BannerCapes implements ModInitializer {
    // nasty, dirty, filthy fix to have trinkets not get eaten
    // up all at once or on failure
    public static final DispenserBehavior STACKABLE_TRINKET_DISPENSER_BEHAVIOR = new ItemDispenserBehavior() {
        protected ItemStack dispenseSilently(BlockPointer pointer, ItemStack stack) {
            BlockPos pos = pointer.getBlockPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            List<LivingEntity> entities = pointer.getWorld().getEntities(LivingEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR.and(new EntityPredicates.CanPickup(stack)));
            if (!entities.isEmpty()) {
                LivingEntity entity = entities.get(0);
                if (entity instanceof PlayerEntity) {
                    TrinketComponent comp = TrinketsApi.getTrinketComponent((PlayerEntity)entity);
                    ItemStack test = stack.copy().split(1);
                    if (comp.equip(test)) {
                        stack.decrement(1);
                        return stack;
                    }
                }
            }
            return super.dispenseSilently(pointer, stack);
        }
    };

    public static Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "banner_capes";
    public static final String MOD_NAME = "Banner Capes";

    public static final Item BANNER_CAPE = new BannerCapeItem();
    public static final SpecialRecipeSerializer BANNER_CAPE_DECORATION_SERIALIZER = Registry.register(
            Registry.RECIPE_SERIALIZER,
            "crafting_special_bannercapedecoration",
            new SpecialRecipeSerializer<>(BannerCapeDecorationRecipe::new));

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "banner_cape"), BANNER_CAPE);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }
}