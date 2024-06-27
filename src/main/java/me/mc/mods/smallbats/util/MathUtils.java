package me.mc.mods.smallbats.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class MathUtils {
    public static Vec3 randomSpherePositions(RandomSource r, Vec3 basePos, float radius) {
        float y = r.nextFloat() * 360f;
        float p = 2*(r.nextFloat()-0.5f) * 180f;

        return basePos.add(Vec3.directionFromRotation(p,y).scale(radius));
    }
}
