package code.patches;

import code.monsters.InvisibleSlingBoy;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CtBehavior;

@SpirePatch(clz = PotionPopUp.class, method = "updateTargetMode"

)
// A patch to make allies untargetable by POTIONS
public class MakeAlliesUntargetableByPOTIONS {
    @SpireInsertPatch(locator = Locator.class, localvars = {"hoveredMonster"})
    public static void MakeHoveredMonsterNull(PotionPopUp instance, @ByRef AbstractMonster[] hoveredMonster) {
        if (hoveredMonster[0] instanceof InvisibleSlingBoy) {
            hoveredMonster[0] = null;
        }
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
            Matcher finalMatcher = new Matcher.FieldAccessMatcher(InputHelper.class, "justClickedLeft");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }
}