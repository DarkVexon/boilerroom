package boiler.events;

import boiler.relics.DisablePowers;
import boiler.relics.Pot;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoomElite;
import com.megacrit.cardcrawl.rooms.ShopRoom;

import java.util.ArrayList;

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
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2, Settings.HEIGHT / 2, new Pot());
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
        return "At the start of each combat, Pot 7 random cards.";
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
