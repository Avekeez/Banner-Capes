package j5im.banner_capes;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import j5im.banner_capes.item.BannerCapeItem;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;

import java.util.List;

// unused but functional
public class BannerPatternMatcher {
    public static final BannerPatternPattern MOJANGSTA = new BannerPatternPattern(
            Pair.of(BannerPattern.BASE, DyeColor.RED),
            Pair.of(BannerPattern.MOJANG, DyeColor.WHITE)
    );
    public static final BannerPatternPattern PILLAGER = new BannerPatternPattern(
            Pair.of(BannerPattern.BASE, DyeColor.WHITE),
            Pair.of(BannerPattern.RHOMBUS_MIDDLE, DyeColor.CYAN),
            Pair.of(BannerPattern.STRIPE_BOTTOM, DyeColor.LIGHT_GRAY),
            Pair.of(BannerPattern.STRIPE_CENTER, DyeColor.GRAY),
            Pair.of(BannerPattern.BORDER, DyeColor.LIGHT_GRAY),
            Pair.of(BannerPattern.STRIPE_MIDDLE, DyeColor.BLACK),
            Pair.of(BannerPattern.STRIPE_TOP, DyeColor.LIGHT_GRAY),
            Pair.of(BannerPattern.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY),
            Pair.of(BannerPattern.BORDER, DyeColor.BLACK)
    );

    // stores a list of pattern/dye pairs
    // able to tell if a banner matches with the pattern pattern - i.e, has the correct pairs
    // in the right order
    // pairs do not need to be adjacent in the banner to be matched
    // cool and groovy name
    // it was a toss-up between this and bannerbannerpatternpattern
    public static class BannerPatternPattern {
        List<Pair<BannerPattern, DyeColor>> template;

        BannerPatternPattern(Pair<BannerPattern, DyeColor>... patterns) {
            this.template = Lists.newArrayList(patterns);
        }

        public boolean matches(ItemStack stack) {
            List<Pair<BannerPattern, DyeColor>> stackPatterns = BannerBlockEntity.getPatternsFromNbt(
                    BannerCapeItem.getDyeColorStatic(stack),
                    BannerBlockEntity.getPatternListTag(stack));
            int templateIndex = 0;
            for (Pair<BannerPattern, DyeColor> pattern : stackPatterns) {
                if(pattern.getFirst() == null || pattern.getSecond() == null) continue;

                Pair<BannerPattern, DyeColor> tempPattern = template.get(templateIndex);
                if (pattern.getFirst().equals(tempPattern.getFirst()) &&
                        pattern.getSecond().equals(tempPattern.getSecond())) {
                    templateIndex++;
                    if (templateIndex >= template.size()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
