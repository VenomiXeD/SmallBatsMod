package me.mc.mods.smallbats.mixins;

import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviorBlockStateBaseMixin {
    @Shadow public abstract VoxelShape getCollisionShape(BlockGetter pLevel, BlockPos pPos);

    @Shadow public abstract boolean isCollisionShapeFullBlock(BlockGetter pLevel, BlockPos pPos);

    private static final VoxelShape NoCollision = Shapes.empty();
    @Inject(method = "getCollisionShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;", at = @At("RETURN"), cancellable = true)
    public void getCollisionShape(BlockGetter pLevel, BlockPos pPos, CollisionContext pContext, CallbackInfoReturnable<VoxelShape> cir) {
        if (pContext instanceof EntityCollisionContext) {
            Entity e = ((EntityCollisionContext)pContext).getEntity();
            if (e instanceof Player) {
                if(!isCollisionShapeFullBlock(pLevel,pPos) && MistShapeAction.isMistShape((Player)e)) {
                    cir.setReturnValue(NoCollision);
                }
            }
        }
    }
}
