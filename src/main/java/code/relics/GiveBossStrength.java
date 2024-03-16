package code.relics;

import code.util.Wiz;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;

public class GiveBossStrength extends AbstractEasyRelic {
    public GiveBossStrength() {
        super("boiler:GiveBossStrength", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        if (room instanceof MonsterRoomBoss) {
            flash();
            Wiz.forAllMonstersLiving(q -> addToBot(new ApplyPowerAction(q, q, new StrengthPower(q, 2), 2)));
        }
    }
}
