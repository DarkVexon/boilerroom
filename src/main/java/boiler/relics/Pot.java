package boiler.relics;

import boiler.cards.PottedCard;
import boiler.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;

import static boiler.monsters.PotThing.potCard;

public class Pot extends AbstractEasyRelic {
    public Pot() {
        super("boiler:Pot", RelicTier.SPECIAL, LandingSound.FLAT);
    }

    @Override
    public void atBattleStart() {
        flash();
        int numPotted = 7;
        ArrayList<AbstractCard> toAdd = new ArrayList<>();
        ArrayList<AbstractCard> valids = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
            if (c.cost >= 0 && c.cost <= 3 && !(c instanceof PottedCard)) {
                valids.add(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
            if (c.cost >= 0 && c.cost <= 3 && !(c instanceof PottedCard)) {
                valids.add(c);
            }
        }
        for (AbstractCard c : AbstractDungeon.player.hand.group) {
            if (c.cost >= 0 && c.cost <= 3 && !(c instanceof PottedCard)) {
                valids.add(c);
            }
        }
        for (int i = 0; i < numPotted; i++) {
            if (valids.isEmpty()) {
                break;
            }
            AbstractCard c = Wiz.getRandomItem(valids);
            valids.remove(c);
            toAdd.add(c);
        }
        for (AbstractCard c : toAdd) {
            potCard(c);
        }
    }
}
