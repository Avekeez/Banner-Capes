package j5im.bannercapes;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Nubbin {
    public static final Nubbin[] NUBBINS = {
            new Nubbin("textures/block/iron_block.png", Items.IRON_NUGGET), // default value
            new Nubbin("textures/block/gold_block.png", Items.GOLD_NUGGET)
    };

    // returns null if the index is invalid
    public static Identifier nubbinTextureFromIndex(int nubbinIndex) {
        if (nubbinIndex < 0 || nubbinIndex >= NUBBINS.length) {
            return null;
        }
        return NUBBINS[nubbinIndex].srcTex;
    }

    public static boolean isNubbinable(ItemStack stack) {
        if (stack.isEmpty()) return false;
        for (int i = 0; i < NUBBINS.length; i++) {
            if (NUBBINS[i].srcItem == stack.getItem()) {
                return true;
            }
        }
        return false;
    }

    // returns the index or -1 if not found
    public static int nubbinIndexFromItem(ItemStack stack) {
        if (stack.isEmpty()) return -1;
        for (int i = 0; i < NUBBINS.length; i++) {
            if (NUBBINS[i].srcItem == stack.getItem()) {
                return i;
            }
        }
        return -1;
    }

    private Identifier srcTex;
    private Item srcItem;
    private Nubbin(String tex, Item item) {
        srcTex = new Identifier(tex);
        srcItem = item;
    }

    public boolean matches(Item other) {
        return srcItem == other;
    }
}
