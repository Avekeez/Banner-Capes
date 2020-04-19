package j5im.bannercapes;

import j5im.bannercapes.item.BannerCapeItem;
import j5im.bannercapes.recipe.BannerCapeDecorationRecipe;

import net.fabricmc.api.ModInitializer;

import net.minecraft.item.Item;
import net.minecraft.recipe.SpecialRecipeSerializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BannerCapes implements ModInitializer {

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