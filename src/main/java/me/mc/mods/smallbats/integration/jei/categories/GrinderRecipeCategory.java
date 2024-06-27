package me.mc.mods.smallbats.integration.jei.categories;

import de.teamlapen.vampirism.core.ModBlocks;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.integration.jei.JEISmallBats;
import me.mc.mods.smallbats.recipes.GrinderRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class GrinderRecipeCategory implements IRecipeCategory<GrinderRecipe> {
    private static ResourceLocation uiLocation = new ResourceLocation(ModSmallBats.MODID,"textures/containers/bloodgrinderrecipe.png");
    private final IDrawable icon;
    private final IDrawableStatic background;
    public GrinderRecipeCategory(IGuiHelper h) {
        background = h.drawableBuilder(uiLocation, 6, 32, 164, 128).addPadding(-2,-3,0,0).build();//.addPadding(0, 30, 0, 0).build();
        icon = h.createDrawableItemStack(new ItemStack(ModBlocks.BLOOD_GRINDER.get()));
    }
    @Override
    public RecipeType<GrinderRecipe> getRecipeType() {
        return JEISmallBats.GRINDING;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("block.vampirism.blood_grinder");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder iRecipeLayoutBuilder, GrinderRecipe grinderRecipe, IFocusGroup iFocusGroup) {
        //iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT,7,33).addIngredients(Ingredient.of(grinderRecipe.bloodItems));
        int bloodItemEntryIndex = 0;
        for(Item i : grinderRecipe.bloodItems) {
            iRecipeLayoutBuilder.addSlot(RecipeIngredientRole.INPUT, (bloodItemEntryIndex % 9) * 18, 18 * (Mth.floor((float)bloodItemEntryIndex /9))).addIngredients(Ingredient.of(i));
            bloodItemEntryIndex++;
        }
    }

    @Override
    public void draw(GrinderRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // First draw the first slot
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
    }
}
