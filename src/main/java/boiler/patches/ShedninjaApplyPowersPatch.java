package boiler.patches;

import boiler.monsters.Shedinja;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;

@SpirePatch(
        clz = ApplyPowerAction.class,
        method = "update"
)
public class ShedninjaApplyPowersPatch {
    @SpirePrefixPatch()
    public static SpireReturn<Void> StopPokemonPowers(ApplyPowerAction instance) {
        if (instance.target instanceof Shedinja) {
            instance.isDone = true;
            return SpireReturn.Return(null);
        }
        return SpireReturn.Continue();
    }
}