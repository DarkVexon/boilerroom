package boiler.patches;

import boiler.monsters.Phantasm;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class VoyagedPatch {

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfTurnPostDrawRelics"
    )
    public static class AbstractPlayerApplyStartOfTurnPostDrawRelicsPatch {
        public static void Prefix(AbstractPlayer __instance) {
            for (AbstractMonster m : AbstractDungeon.getCurrRoom().monsters.monsters) {
                if (m instanceof Phantasm && GameActionManager.turn == 1) {
                    ((Phantasm) m).atStartOfTurnPostDrawTurnOne();
                }
            }
        }
    }
}
