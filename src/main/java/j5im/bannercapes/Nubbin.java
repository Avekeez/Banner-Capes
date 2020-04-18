package j5im.bannercapes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Nubbin {
    public static final Nubbin[] NUBBINS = {
            new Nubbin("textures/block/iron_block.png", Items.IRON_NUGGET, 15724543), // default value
            new Nubbin("textures/block/gold_block.png", Items.GOLD_NUGGET, 16772175),
            new Nubbin("textures/block/diamond_block.png", Items.DIAMOND, 6682083),
            new Nubbin("textures/block/lapis_block.png", Items.LAPIS_LAZULI, 1849488)
    };

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
        for (int i = 0; i < NUBBINS.length; i++) {
            if (NUBBINS[i].matches(stack.getItem())) {
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

    private Identifier srcTex;
    private Item srcItem;
    private int color;
    private Nubbin(String tex, Item item, int itemColor) {
        srcTex = new Identifier(tex);
        srcItem = item;
        color = itemColor;
    }

    public boolean matches(Item other) {
        return srcItem == other;
    }
}
