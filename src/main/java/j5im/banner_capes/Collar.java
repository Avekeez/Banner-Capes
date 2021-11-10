package j5im.banner_capes;

import j5im.banner_capes.config.DecorationMaterialConfig;
import j5im.banner_capes.utils.ColorUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class Collar {

    public static Collar[] initCollars() {
        List<Collar> collarList = new ArrayList<>();
        for (DecorationMaterialConfig configItem : BannerCapes.config().decorationMaterials) {
            if(configItem.isCollar) {
                collarList.add(new Collar(configItem.texturePath, configItem.itemIdentifier, ColorUtils.RGBtoDecimalColor(configItem.colorR, configItem.colorG, configItem.colorB)));
            }
        }

        return collarList.toArray(new Collar[collarList.size()]);
    }

    public static final Collar[] COLLARS = initCollars();

    // returns null if the index is invalid
    public static Identifier textureFromIndex(int CollarIndex) {
        if (CollarIndex < 0 || CollarIndex >= COLLARS.length) {
            return null;
        }
        return COLLARS[CollarIndex].srcTex;
    }

    public static int itemColorFromIndex(int CollarIndex) {
        if (CollarIndex < 0 || CollarIndex >= COLLARS.length) {
            return -1;
        }
        return COLLARS[CollarIndex].color;
    }

    public static String nameFromIndex(int CollarIndex) {
        if (CollarIndex < 0 || CollarIndex >= COLLARS.length) {
            return null;
        }
        return COLLARS[CollarIndex].srcItem.getTranslationKey();
    }

    public static boolean isCollarable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (Collar collar : COLLARS) {
            if (collar.matches(stack.getItem())) {
                return true;
            }
        }
        return false;
    }

    // returns the index or -1 if not found
    public static int indexFromItem(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        for (int i = 0; i < COLLARS.length; i++) {
            if (COLLARS[i].matches(stack.getItem())) {
                return i;
            }
        }
        return -1;
    }

    private final Identifier srcTex;
    private final Item srcItem;
    private final int color;
    private Collar(String tex, String item, int itemColor) {
        srcTex = new Identifier(tex);
        srcItem = Registry.ITEM.get(Identifier.tryParse(item));
        color = itemColor;
    }

    public boolean matches(Item other) {
        return srcItem == other;
    }
}