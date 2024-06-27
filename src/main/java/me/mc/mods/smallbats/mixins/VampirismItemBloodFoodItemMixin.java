package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.items.VampirismItemBloodFoodItem;
import me.mc.mods.smallbats.mixininterfaces.IVampirismItemBloodFoodAccessor;
import net.minecraft.world.food.FoodProperties;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(VampirismItemBloodFoodItem.class)
public abstract class VampirismItemBloodFoodItemMixin implements IVampirismItemBloodFoodAccessor {

    @Shadow(remap = false) @Final private FoodProperties vampireFood;

    @Override
    public FoodProperties getVampireFood() {
        return vampireFood;
    }
}
