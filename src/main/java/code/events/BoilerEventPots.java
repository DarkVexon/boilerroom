package code.events;

import code.cards.PottedCard;
import code.relics.DisablePowers;
import code.util.Wiz;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;

import static code.monsters.PotThing.potCard;

public class BoilerEventPots extends AbstractBoilerRoomEvent {
    public BoilerEventPots() {
        super("5");
    }

    @Override
    protected String getADoneText() {
        return "You climb out of the boiler, but on the way, your leg got stuck in a pot!! Oh No!!";
    }

    @Override
    protected String getBDoneText() {
        return "You climb out of the boiler, but you took too long and the Merchant left.";
    }

    @Override
    protected String getCDoneText() {
        return "You climb out of the boiler, but you feel powerless due to falling in.";
    }

    @Override
    protected void doA() {
        int numPotted = 8;
        ArrayList<AbstractCard> toAdd = new ArrayList<>();
        ArrayList<AbstractCard> valids = new ArrayList<>();
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
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

    @Override
    protected void doB() {
        for (ArrayList<MapRoomNode> r : AbstractDungeon.map) {
            for (MapRoomNode n : r) {
                if (n.room instanceof ShopRoom) {
                    n.setRoom(new MonsterRoomElite());
                }
            }
        }
    }

    @Override
    protected void doC() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, new DisablePowers());
    }

    @Override
    protected String getAText() {
        return "Put 8 random cards in your deck in Pots.";
    }

    @Override
    protected String getBText() {
        return "Shop rooms become Elite rooms.";
    }

    @Override
    protected String getCText() {
        return "You can't play Powers until turn 3.";
    }
}
