package boiler.patches;

import boiler.util.Wiz;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;

@SpirePatch2(clz = PotionPopUp.class, method = "open", paramtypez = {int.class, AbstractPotion.class})
public class MarisaNoPotionsPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> Prefix() {
        if (Wiz.isInCombat() && Wiz.getEnemies().stream().anyMatch(q -> q.hasPower("boiler:I'm Marisa from Touhou")))
            return SpireReturn.Return();
        return SpireReturn.Continue();
    }
}