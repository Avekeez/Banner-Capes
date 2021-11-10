package j5im.banner_capes;

import j5im.banner_capes.config.DecorationMaterialConfig;
import j5im.banner_capes.utils.ColorUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Nubbin {

    public static Nubbin[] initNubbins() {
        List<Nubbin> nubbinList = new ArrayList<>();
        for (DecorationMaterialConfig configItem : BannerCapes.config().decorationMaterials) {
            if(configItem.isNubbin) {
                nubbinList.add(new Nubbin(configItem.texturePath, configItem.itemIdentifier , ColorUtils.RGBtoDecimalColor(configItem.colorR, configItem.colorG, configItem.colorB)));
            }
        }

        return nubbinList.toArray(new Nubbin[nubbinList.size()]);
    }

    public static final Nubbin[] NUBBINS = initNubbins();

    // returns null if the index is invalid
    public static Identifier textureFromIndex(int nubbinIndex) {
        if (nubbinIndex < 0 || nubbinIndex >= NUBBINS.length) {
            return null;
        }
        return NUBBINS[nubbinIndex].srcTex;
    }

    public static int itemColorFromIndex(int nubbinIndex) {
        if (nubbinIndex < 0 || nubbinIndex >= NUBBINS.length) {
            return -1;
        }
        return NUBBINS[nubbinIndex].color;
    }

    public static String nameFromIndex(int nubbinIndex) {
        if (nubbinIndex < 0 || nubbinIndex >= NUBBINS.length) {
            return null;
        }
        return NUBBINS[nubbinIndex].srcItem.getTranslationKey();
    }

    public static boolean isNubbinable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (Nubbin nubbin : NUBBINS) {
            if (nubbin.matches(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    // returns the index or -1 if not found
    public static int indexFromItem(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        for (int i = 0; i < NUBBINS.length; i++) {
            if (NUBBINS[i].matches(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }

    private final Identifier srcTex;
    private final Item srcItem;
    private final int color;
    private Nubbin(String tex, String item, int itemColor) {
        srcTex = new Identifier(tex);
        srcItem = Registry.ITEM.get(Identifier.tryParse(item));
        color = itemColor;
    }

    public boolean matches(Item other) {
        return srcItem == other;
    }
}