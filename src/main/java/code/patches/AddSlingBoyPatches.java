package code.patches;

import code.monsters.InvisibleSlingBoy;
import code.monsters.SlingBoy;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AddSlingBoyPatches {
    @SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfCombatLogic")
    public static class StartOfCombatPatch {
        @SpirePrefixPatch
        public static void startOfCombat(AbstractPlayer __instance) {
            if (AbstractDungeon.bossKey.equals(SlingBoy.ID)) {
                AbstractDungeon.actionManager.addToBottom(new SpawnMonsterAction(new InvisibleSlingBoy(Settings.WIDTH - 100, Settings.HEIGHT / 4F), false));
            }
        }
    }
}