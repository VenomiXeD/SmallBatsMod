package me.mc.mods.smallbats.vampire;

import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.skills.ISkill;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.vampire.skills.BatSleepSkill;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SmallBatsVampireSkills {
    static {
        System.out.println(VampirismRegistries.SKILLS_ID.registry().toDebugFileName());
    }
    public static final DeferredRegister<ISkill<IVampirePlayer>> VAMPIRE_SKILLS = DeferredRegister.create(VampirismRegistries.SKILLS_ID.location(), ModSmallBats.MODID);
    public static final RegistryObject<BatSleepSkill> BATSLEEP_SKILL = VAMPIRE_SKILLS.register("batsleep", () -> new BatSleepSkill(1,true));
}
