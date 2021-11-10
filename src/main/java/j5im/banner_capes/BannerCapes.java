package j5im.banner_capes;

import dev.emi.trinkets.api.TrinketItem;
import j5im.banner_capes.config.BannerCapesConfig;
import j5im.banner_capes.item.BannerCapeItem;
import j5im.banner_capes.recipe.BannerCapeRecipe;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
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
            BlockPos pos = pointer.getPos().offset(pointer.getBlockState().get(DispenserBlock.FACING));
            List<LivingEntity> entities = pointer.getWorld().getEntitiesByClass(LivingEntity.class, new Box(pos), EntityPredicates.EXCEPT_SPECTATOR);
            if (!entities.isEmpty()) {
                LivingEntity entity = entities.get(0);
                if (entity instanceof PlayerEntity) {
                    if (TrinketItem.equipItem((PlayerEntity) entity,stack)) {
                        stack.decrement(1);
                        return stack;
                    }
                }
            }
            return super.dispenseSilently(pointer, stack);
        }
    };

    public static final Logger LOGGER = LogManager.getLogger();

    public static final String MOD_ID = "banner_capes";
    public static final String MOD_NAME = "Banner Capes";

    public static final Item BANNER_CAPE = new BannerCapeItem();
    public static final SpecialRecipeSerializer<BannerCapeRecipe> BANNER_CAPE_SERIALIZER = Registry.register(
            Registry.RECIPE_SERIALIZER,
            "crafting_special_bannercape",
            new SpecialRecipeSerializer<>(BannerCapeRecipe::new));

    public static BannerCapesConfig config() {
        return AutoConfig.getConfigHolder(BannerCapesConfig.class).get();
    }

    @Override
    public void onInitialize() {
        log(Level.INFO, "Initializing");
        AutoConfig.register(BannerCapesConfig.class, GsonConfigSerializer::new);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "banner_cape"), BANNER_CAPE);
    }

    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }
}