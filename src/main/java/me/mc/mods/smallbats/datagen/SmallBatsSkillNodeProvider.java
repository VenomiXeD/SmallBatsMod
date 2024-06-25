package me.mc.mods.smallbats.datagen;

import de.teamlapen.vampirism.data.provider.SkillNodeProvider;
import de.teamlapen.vampirism.data.recipebuilder.FinishedSkillNode;
import de.teamlapen.vampirism.data.recipebuilder.SkillNodeBuilder;
import de.teamlapen.vampirism.entity.player.vampire.skills.VampireSkills;
import me.mc.mods.smallbats.ModSmallBats;
import me.mc.mods.smallbats.vampire.SmallBatsVampireSkills;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class SmallBatsSkillNodeProvider extends SkillNodeProvider {

    public SmallBatsSkillNodeProvider(PackOutput packOutput, String modId) {
        super(packOutput, modId);
    }

    @Override
    public @NotNull CompletableFuture<?> run(@NotNull CachedOutput cache) {
        ModSmallBats.INSTANCE.Logger.info("Running datagen: " + this.getClass().getName());
        return super.run(cache);
    }

    @Override
    public @NotNull String getName() {
        return "BatSleepModSkillNodeProvider";
    }

    @Override
    protected void registerSkillNodes(@NotNull Consumer<FinishedSkillNode> consumer) {
        System.out.println(VampireSkills.FLEDGLING.getId());
        SkillNodeBuilder.vampire(new ResourceLocation("vampirism","vampire/skill4"), SmallBatsVampireSkills.BATSLEEP_SKILL.get()).build(consumer,new ResourceLocation(ModSmallBats.MODID,"batsleep"));
    }

    @Override
    protected @NotNull ResourceLocation modId(@NotNull String string) {
        ModSmallBats.INSTANCE.Logger.info("skill provider modid: " + string);
        return new ResourceLocation(ModSmallBats.MODID);
    }
}
