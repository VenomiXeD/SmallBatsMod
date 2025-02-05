package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.REFERENCE;
import de.teamlapen.vampirism.entity.player.vampire.VampireLeveling;
import de.teamlapen.vampirism.items.PureBloodItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(PureBloodItem.class)
public abstract class PureBloodItemMixin {
	@Shadow public abstract int getLevel();

	// Re-enables the ability to drink pure blood - higher levels allows you to consume pure blood of higher purity.
	@Redirect(method = "use", at = @At(value = "INVOKE", target = "Ljava/util/Optional;filter(Ljava/util/function/Predicate;)Ljava/util/Optional;"))
	public Optional<VampireLeveling.AltarInfusionRequirements> modifyInfusionRequirement(Optional<VampireLeveling.AltarInfusionRequirements> instance, Predicate<? super VampireLeveling.AltarInfusionRequirements> predicate) {
		return instance.filter(x->x.pureBloodLevel()>=this.getLevel());
	}
}
