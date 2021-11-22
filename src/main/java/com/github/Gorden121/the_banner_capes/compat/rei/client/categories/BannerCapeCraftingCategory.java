package com.github.Gorden121.the_banner_capes.compat.rei.client.categories;

import com.github.Gorden121.the_banner_capes.compat.rei.BannerCapePlugin;
import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeCraftingDisplay;
import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeShapedDisplay;
import com.google.common.collect.Lists;
import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Slot;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.display.DisplayMerger;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.util.EntryStacks;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Blocks;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BannerCapeCraftingCategory implements DisplayCategory<BannerCapeCraftingDisplay<?>> {
    @Override
    public CategoryIdentifier<? extends BannerCapeCraftingDisplay<?>> getCategoryIdentifier() {
        return BannerCapePlugin.BANNER_CAPE_CRAFTING;
    }

    @Override
    public Renderer getIcon() {
        return EntryStacks.of(Blocks.CRAFTING_TABLE);
    }

    @Override
    public Text getTitle() {
        return new TranslatableText("category.rei.banner_cape_crafting");
    }

    @Override
    public List<Widget> setupDisplay(BannerCapeCraftingDisplay<?> display, Rectangle bounds) {
        Point startPoint = new Point(bounds.getCenterX() - 58, bounds.getCenterY() - 27);
        List<Widget> widgets = Lists.newArrayList();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(startPoint.x + 60, startPoint.y + 18)));
        widgets.add(Widgets.createResultSlotBackground(new Point(startPoint.x + 95, startPoint.y + 19)));
        List<? extends List<? extends EntryStack<?>>> input = display.getInputEntries();
        List<Slot> slots = Lists.newArrayList();
        for (int y = 0; y < 3; y++)
            for (int x = 0; x < 3; x++)
                slots.add(Widgets.createSlot(new Point(startPoint.x + 1 + x * 18, startPoint.y + 1 + y * 18)).markInput());
        for (int i = 0; i < input.size(); i++) {
            if (display instanceof BannerCapeShapedDisplay) {
                if (!input.get(i).isEmpty())
                    slots.get(BannerCapeCraftingDisplay.getSlotWithSize(display, i, 3)).entries(input.get(i));
            } else if (!input.get(i).isEmpty())
                slots.get(i).entries(input.get(i));
        }
        widgets.addAll(slots);
        widgets.add(Widgets.createSlot(new Point(startPoint.x + 95, startPoint.y + 19)).entries(display.getOutputEntries().get(0)).disableBackground().markOutput());
        return widgets;
    }

    @Override
    @Nullable
    public DisplayMerger<BannerCapeCraftingDisplay<?>> getDisplayMerger() {
        return DisplayCategory.getContentMerger();
    }
}
