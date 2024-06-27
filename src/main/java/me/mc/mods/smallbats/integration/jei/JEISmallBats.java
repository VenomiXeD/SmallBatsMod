package me.mc.mods.smallbats.integration.jei;

import de.teamlapen.vampirism.api.general.BloodConversionRegistry;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.integration.jei.categories.GrinderRecipeCategory;
import me.mc.mods.smallbats.recipes.GrinderRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

@JeiPlugin
public class JEISmallBats implements IModPlugin {

    static {
    }

    public static RecipeType<GrinderRecipe> GRINDING = RecipeType.create(ModSmallBats.MODID,"grinding", GrinderRecipe.class);

    public static ResourceLocation ID = new ResourceLocation(ModSmallBats.MODID, "jeiplugin");

    @Override
    public ResourceLocation getPluginUid() {
        return ID;
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(
                GRINDING, List.of(new GrinderRecipe(ForgeRegistries.ITEMS.
                                getValues().
                                stream().
                                filter(i->BloodConversionRegistry.getImpureBloodValue(i)>0).toList())));
        /*ForgeRegistries.ITEMS.getValues().stream().forEach(item -> {
            int blood = BloodConversionRegistry.getImpureBloodValue(item);
            if (blood > 0) {
                registration.addItemStackInfo(new ItemStack(item), Component.literal("Can be used to grind down in to blood: " + blood + " mb"));

            }
        });*/
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrinderRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }
}
