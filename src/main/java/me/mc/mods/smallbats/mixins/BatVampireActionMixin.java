package me.mc.mods.smallbats.mixins;

import de.teamlapen.vampirism.entity.player.vampire.actions.BatVampireAction;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.annotation.Target;

@Mixin(value = BatVampireAction.class, remap = false)
public abstract class BatVampireActionMixin {
    @Shadow @Final @Mutable
    public static final EntityDimensions BAT_SIZE = EntityDimensions.fixed(0.6f,0.875f);
}
