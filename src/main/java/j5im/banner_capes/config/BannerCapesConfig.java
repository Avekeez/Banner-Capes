package j5im.banner_capes.config;

import j5im.banner_capes.BannerCapes;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = BannerCapes.MOD_ID)
public final class BannerCapesConfig implements ConfigData {

// --Commented out by Inspection START (10.11.2021 20:17):
//    @ConfigEntry.BoundedDiscrete(min = 0, max = 100)
//    @ConfigEntry.Gui.Tooltip
//    public int monstersDropRelicsChance = 15;
// --Commented out by Inspection STOP (10.11.2021 20:17)

    @ConfigEntry.Gui.Tooltip
    public final DecorationMaterialConfig[] decorationMaterials = {
            //Wood
            new DecorationMaterialConfig("textures/block/oak_planks.png", "oak_planks", 136, 107, 67,true,true),
            new DecorationMaterialConfig("textures/block/spruce_planks.png", "spruce_planks", 96, 70, 41,true,true),
            new DecorationMaterialConfig("textures/block/birch_planks.png", "birch_planks", 165, 152, 102,true,true),
            new DecorationMaterialConfig("textures/block/jungle_planks.png", "jungle_planks", 132, 97, 70,true,true),
            new DecorationMaterialConfig("textures/block/acacia_planks.png", "acacia_planks", 147, 77, 42,true,true),
            new DecorationMaterialConfig("textures/block/dark_oak_planks.png", "dark_oak_planks", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/crimson_planks.png", "crimson_planks", 255,0,0,true,true),
            new DecorationMaterialConfig("textures/block/warped_planks.png", "warped_planks", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/oak_wood.png", "oak_wood", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/spruce_wood.png", "spruce_wood", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/birch_wood.png", "birch_wood", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/jungle_wood.png", "jungle_wood", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/acacia_wood.png", "acacia_wood", 255,0,0,true,true),
            new DecorationMaterialConfig("textures/block/dark_oak_wood.png", "dark_oak_wood", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/crimson_hyphae.png", "crimson_hyphae", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/warped_hyphae.png", "warped_hyphae", 0,0,0,true,true),
            //Stone
            new DecorationMaterialConfig("textures/block/deepslate.png", "deepslate", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/cobbled_deepslate.png", "cobbled_deepslate", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/deepslate_bricks.png", "deepslate_bricks", 255,0,0,true,true),
            new DecorationMaterialConfig("textures/block/deepslate_tiles.png", "deepslate_tiles", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/chiseled_deepslate.png", "chiseled_deepslate", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/gold_block.png", "gold_nugget", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/diamond_block.png", "diamond", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/lapis_block.png", "lapis_lazuli", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/redstone_block.png", "redstone", 255,0,0,true,true),
            new DecorationMaterialConfig("textures/block/birch_planks.png", "birch_planks", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/iron_block.png", "iron_nugget", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/gold_block.png", "gold_nugget", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/diamond_block.png", "diamond", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/lapis_block.png", "lapis_lazuli", 0,0,0,true,true),
            new DecorationMaterialConfig("textures/block/redstone_block.png", "redstone", 255,0,0,true,true)
    };

}