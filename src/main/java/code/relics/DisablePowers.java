package code.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;

public class DisablePowers extends AbstractEasyRelic {
    public DisablePowers() {
        super("boiler:DisablePowers", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        counter = 0;
    }

    @Override
    public void atTurnStart() {
        counter += 1;
    }

    @Override
    public void onVictory() {
        counter = -1;
    }

    @Override
    public boolean canPlay(AbstractCard card) {
        if (card.type == AbstractCard.CardType.POWER) {
            return counter >= 3;
        }
        return true;
    }
}
