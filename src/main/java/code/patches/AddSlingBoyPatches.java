package code.patches;

import code.monsters.InvisibleSlingBoy;
import code.monsters.SlingBoy;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class AddSlingBoyPatches {
    @SpirePatch(clz = MonsterGroup.class, method = "init")
    public static class StartOfCombatPatch {
        @SpirePrefixPatch
        public static void startOfCombat(MonsterGroup __instance) {
            if (AbstractDungeon.bossKey.equals(SlingBoy.ID) && !(AbstractDungeon.getCurrRoom() instanceof MonsterRoomBoss)) {
                __instance.monsters.add(0, new InvisibleSlingBoy(-766, 475));
            }
        }
    }
}