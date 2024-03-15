package code.patches;

import code.monsters.InvisibleSlingBoy;
import code.monsters.SlingBoy;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddSlingBoyPatches {
    private InvisibleSlingBoy boy = new InvisibleSlingBoy(0, 0);

    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class StartOfCombatPatch {
        @SpirePrefixPatch
        public static void startOfCombat(AbstractPlayer __instance) {
            if (AbstractDungeon.bossKey.equals(SlingBoy.ID)) {

            }
        }
    }


}