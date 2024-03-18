package code.events;

import code.cards.SpecterCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.rooms.MonsterRoom;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import java.util.ArrayList;

public class BoilerEventSpecters extends AbstractBoilerRoomEvent {
    public BoilerEventSpecters() {
        super("6");
    }

    @Override
    protected String getADoneText() {
        return "You climb out of the boiler, but you got haunted by spooky ghosts on your way out ooOoooOoo!!!";
    }

    @Override
    protected String getBDoneText() {
        return "You climb out of the boiler, but it was really hot and you lost HP owie!";
    }

    @Override
    protected String getCDoneText() {
        return "You climb out of the boiler, but it took so long that the campfires went out!! Oh No!";
    }

    @Override
    protected void doA() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new SpecterCard(), Settings.WIDTH / 3, Settings.HEIGHT / 2));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new SpecterCard(), (Settings.WIDTH / 3) * 2, Settings.HEIGHT / 2));
    }

    @Override
    protected void doB() {
        AbstractDungeon.player.damage(new DamageInfo(null, AbstractDungeon.player.getAscensionMaxHPLoss() * 4));
    }

    @Override
    protected void doC() {
        for (ArrayList<MapRoomNode> r : AbstractDungeon.map) {
            for (MapRoomNode n : r) {
                if (n.room instanceof RestRoom) {
                    n.setRoom(new MonsterRoom());
                }
            }
        }
    }

    @Override
    protected String getAText() {
        return "Obtain 2 Phantasms.";
    }

    @Override
    protected String getBText() {
        return "Lose " + AbstractDungeon.player.getAscensionMaxHPLoss() * 4 + " HP.";
    }

    @Override
    protected String getCText() {
        return "Campfires become Monster Rooms.";
    }
}
