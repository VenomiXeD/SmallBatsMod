package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.entity.player.vampire.VampireLeveling;
import de.teamlapen.vampirism.items.PureBloodItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(value = PureBloodItem.class, remap = false)
public abstract class PureBloodItemMixin {
	@Shadow public abstract int getLevel();

	// Re-enables the ability to drink pure blood - higher levels allows you to consume pure blood of higher purity.
	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"))
	public Optional<VampireLeveling.AltarInfusionRequirements> modifyConsumptionFilterLevelRequirement(Optional<VampireLeveling.AltarInfusionRequirements> instance, Predicate<? super VampireLeveling.AltarInfusionRequirements> predicate) {
		return instance.filter(x->x.pureBloodLevel()>=this.getLevel());
	}
}
