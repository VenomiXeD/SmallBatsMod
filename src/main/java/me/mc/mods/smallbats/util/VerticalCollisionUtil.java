package me.mc.mods.smallbats.util;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class VerticalCollisionUtil {
	public static boolean verticalCollisionUp(Entity e) {
		if (e.level().getBlockState(e.blockPosition().above(Mth.floor(e.getBbHeight())+1)).isAir()) {
			return false;
		}
		Vec3 start = e.position().add(0d,e.getBbHeight(),0d);
		Vec3 end = start.add(0,.01,0);
		BlockHitResult hitResult = e.level().clip(new ClipContext(start,end,ClipContext.Block.COLLIDER,ClipContext.Fluid.NONE,e));
		Vec3 diff = hitResult.getLocation().subtract(start);
		return diff.y <= .0001f;
	}
}
