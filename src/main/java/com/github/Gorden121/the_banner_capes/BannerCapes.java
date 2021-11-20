package com.github.Gorden121.the_banner_capes;

import com.github.Gorden121.the_banner_capes.config.BannerCapesConfig;
import com.github.Gorden121.the_banner_capes.data.BannerCapeMaterialsData;
import com.github.Gorden121.the_banner_capes.data.BannerCapeSimpleSynchronousResourceReloadListener;
import com.github.Gorden121.the_banner_capes.item.BannerCapeItem;
import com.github.Gorden121.the_banner_capes.recipe.BannerCapeRecipe;
import dev.emi.trinkets.api.TrinketItem;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.resource.ResourceType;
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

    public static String MOD_ID;
    public static String MOD_NAME;
    public static String MOD_VERSION;
    public static final String ConfigVersionInternal = "V2";

    public static final Item BANNER_CAPE = new BannerCapeItem();
    public static final SpecialRecipeSerializer<BannerCapeRecipe> BANNER_CAPE_SERIALIZER = Registry.register(
            Registry.RECIPE_SERIALIZER,
            "banner_capes:crafting_special_bannercape",
            new SpecialRecipeSerializer<>(BannerCapeRecipe::new)
    );

    public static BannerCapesConfig config() {
        return AutoConfig.getConfigHolder(BannerCapesConfig.class).get();
    }
    public static BannerCapeMaterialsData bannerCapeMaterialsData = new BannerCapeMaterialsData();

    @Override
    public void onInitialize() {
        ModMetadata metadata = FabricLoader.getInstance().getModContainer("the_banner_capes").get().getMetadata();
        MOD_ID = metadata.getId().toString();
        MOD_NAME = metadata.getName().toString();
        MOD_VERSION = metadata.getVersion().getFriendlyString();


        log(Level.INFO, "Initializing " + MOD_NAME + " (" + MOD_ID + ") " + MOD_VERSION);

        AutoConfig.register(BannerCapesConfig.class, GsonConfigSerializer::new);
        ResourceManagerHelper.get(ResourceType.SERVER_DATA).registerReloadListener(new BannerCapeSimpleSynchronousResourceReloadListener());

        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "banner_cape"), BANNER_CAPE);


        /*try {
            Gson gson = new Gson();
            FileWriter writer = new FileWriter("output.json");
            writer.write(gson.toJson(bannerCapeMaterialsData.getMinimumDecorationMaterials()));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/

    }


    public static void log(Level level, String message){
        LOGGER.log(level, "["+MOD_NAME+"] " + message);
    }

}