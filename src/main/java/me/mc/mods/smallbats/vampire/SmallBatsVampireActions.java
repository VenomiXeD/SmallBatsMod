package me.mc.mods.smallbats.vampire;

import de.teamlapen.vampirism.api.VampirismRegistries;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.actions.ILastingAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.vampire.actions.BatSleepAction;
import me.mc.mods.smallbats.vampire.actions.MistShapeAction;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class SmallBatsVampireActions {
    public static final DeferredRegister<IAction<IVampirePlayer>> VAMPIRE_ACTIONS = DeferredRegister.create(VampirismRegistries.ACTIONS_ID.location(), ModSmallBats.MODID);
    public static final RegistryObject<ILastingAction<IVampirePlayer>> BATSLEEP = VAMPIRE_ACTIONS.register("batsleep",BatSleepAction::new);
    public static final RegistryObject<ILastingAction<IVampirePlayer>> MISTFORM = VAMPIRE_ACTIONS.register("mistform", MistShapeAction::new);
}
