package com.github.Gorden121.the_banner_capes.compat.rei;

import com.github.Gorden121.the_banner_capes.compat.rei.common.display.BannerCapeCraftingDisplay;
import me.shedaniel.rei.api.client.ClientHelper;
import me.shedaniel.rei.api.client.registry.transfer.TransferHandler;
import me.shedaniel.rei.api.common.display.Display;
import me.shedaniel.rei.api.common.display.SimpleGridMenuDisplay;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.recipebook.RecipeBookProvider;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.TranslatableText;

@Environment(EnvType.CLIENT)
public class BannerCapeRecipeBookHandler implements TransferHandler {
    public BannerCapeRecipeBookHandler() {
    }

    public Result handle(Context context) {
        if (context.getDisplay() instanceof SimpleGridMenuDisplay && ClientHelper.getInstance().canUseMovePackets()) {
            return Result.createNotApplicable();
        } else {
            Display display = context.getDisplay();
            ScreenHandler var4 = context.getMenu();
            if (var4 instanceof AbstractRecipeScreenHandler) {
                AbstractRecipeScreenHandler<?> container = (AbstractRecipeScreenHandler)var4;
                if (container == null) {
                    return Result.createNotApplicable();
                } else {
                    if (display instanceof BannerCapeCraftingDisplay) {
                        BannerCapeCraftingDisplay<?> craftingDisplay = (BannerCapeCraftingDisplay)display;
                        if (craftingDisplay.getOptionalRecipe().isPresent()) {
                            int h = -1;
                            int w = -1;
                            if (container instanceof CraftingScreenHandler) {
                                h = 3;
                                w = 3;
                            } else if (container instanceof PlayerScreenHandler) {
                                h = 2;
                                w = 2;
                            }

                            if (h != -1 && w != -1) {
                                Recipe<?> recipe = (Recipe)craftingDisplay.getOptionalRecipe().get();
                                if (craftingDisplay.getHeight() <= h && craftingDisplay.getWidth() <= w) {
                                    if (!context.getMinecraft().player.getRecipeBook().contains(recipe)) {
                                        return Result.createNotApplicable();
                                    }

                                    if (!context.isActuallyCrafting()) {
                                        return Result.createSuccessful();
                                    }

                                    context.getMinecraft().setScreen(context.getContainerScreen());
                                    if (context.getContainerScreen() instanceof RecipeBookProvider) {
                                        ((RecipeBookProvider)context.getContainerScreen()).getRecipeBookWidget().reset();
                                    }

                                    context.getMinecraft().interactionManager.clickRecipe(container.syncId, recipe, Screen.hasShiftDown());
                                    return Result.createSuccessful();
                                }

                                return Result.createFailed(new TranslatableText("error.rei.transfer.too_small", new Object[]{Integer.valueOf(h), Integer.valueOf(w)}));
                            }

                            return Result.createNotApplicable();
                        }
                    }
                    return Result.createNotApplicable();
                }
            } else {
                return Result.createNotApplicable();
            }
        }
    }

    public double getPriority() {
        return -20.0D;
    }
}
