package boiler.patches;

import boiler.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public class NoFunPatch2 {
    @SpirePatch(clz = EnergyPanel.class, method = "addEnergy")
    public static class DisableDraws {
        public static SpireReturn Prefix(int amount) {
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
