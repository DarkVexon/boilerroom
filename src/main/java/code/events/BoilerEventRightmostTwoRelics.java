package code.events;

import basemod.ReflectionHacks;
import code.cards.DexDownCard;
import code.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class BoilerEventRightmostTwoRelics extends AbstractBoilerRoomEvent {
    public BoilerEventRightmostTwoRelics() {
        super("2");
    }

    @Override
    protected String getADoneText() {
        return "You got out of the boiler but some relics broke!!! :(";
    }

    @Override
    protected String getBDoneText() {
        return "You got out of the boiler but the steam made some cards cost more!!! :X";
    }

    @Override
    protected String getCDoneText() {
        return "FINESSE?? In the BOILER? am I dreamig";
    }

    @Override
    protected void doA() {
        AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(AbstractDungeon.player.relics.size() - 1).relicId);
        AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(AbstractDungeon.player.relics.size() - 1).relicId);
    }

    @Override
    protected void doB() {
        ArrayList<AbstractCard> toHit = new ArrayList<>();
        ArrayList<AbstractCard> valid = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.cost > -1 && c.cost < 3) {
                valid.add(c);
            }
        }
        for (int i = 0; i < 3; i++) {
            if (!valid.isEmpty()) {
                AbstractCard found = Wiz.getRandomItem(valid);
                toHit.add(found);
                valid.remove(found);
            }
        }
        for (AbstractCard c : toHit) {
            c.updateCost(1);
            c.name = c.name + " But it Costs 1 More";
            ReflectionHacks.privateMethod(AbstractCard.class, "initializeTitle").invoke(c);
        }
    }

    @Override
    protected void doC() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new DexDownCard(), Settings.WIDTH / 2, Settings.HEIGHT / 2));
    }

    @Override
    protected String getAText() {
        return "Lose your rightmost two relics.";
    }

    @Override
    protected String getBText() {
        return "Increase the cost of 3 random cards in your deck by 1.";
    }

    @Override
    protected String getCText() {
        return "Obtain a Dexterity Down.";
    }
}
