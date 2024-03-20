package boiler.patches;

import boiler.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NoFunPatch1 {
    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "draw",
            paramtypez = {
                    int.class
            }
    )
    public static class DisableDraws {
        public static SpireReturn Prefix(AbstractPlayer __instance, int amount) {
            if (PreDrawPatch.DRAWN_DURING_TURN) {
                for (AbstractMonster m : Wiz.getEnemies()) {
                    AbstractPower result = m.getPower("boiler:No Fun Allowed");
                    if (result != null) {
                        result.flash();
                        return SpireReturn.Return(null);
                    }
                }
            }
            return SpireReturn.Continue();
        }
    }
}
