package me.mc.mods.smallbats.vampire.skills;

import de.teamlapen.vampirism.api.VReference;
import de.teamlapen.vampirism.api.entity.factions.IPlayableFaction;
import de.teamlapen.vampirism.api.entity.player.actions.IAction;
import de.teamlapen.vampirism.api.entity.player.vampire.IVampirePlayer;
import de.teamlapen.vampirism.entity.player.skills.VampirismSkill;
import me.mc.mods.smallbats.vampire.SmallBatsVampireActions;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class MistShapeSkill extends VampirismSkill<IVampirePlayer> {
    @Override
    protected void getActions(Collection<IAction<IVampirePlayer>> list) {
        list.add(SmallBatsVampireActions.MISTFORM.get());
    }

    public MistShapeSkill(int skillPointCost, boolean desc) {
        super(skillPointCost, desc);
    }

    @Override
    public @NotNull Optional<IPlayableFaction<?>> getFaction() {
        return Optional.ofNullable(VReference.VAMPIRE_FACTION);
    }

}
