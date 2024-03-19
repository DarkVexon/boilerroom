package boiler.events;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.GremlinMask;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;

public class BoilerEventMarkOfBloom extends AbstractBoilerRoomEvent {
    public BoilerEventMarkOfBloom() {
        super("3");
    }

    @Override
    protected String getADoneText() {
        return "You climb out of the boiler, but on the way, you got corrupted by Mark of the Bloom and also someone put Gremlin Visage on your face. LOL.";
    }

    @Override
    protected String getBDoneText() {
        return "You climb out of the boiler, but you left your leftmost two relics behind. Oops.";
    }

    @Override
    protected String getCDoneText() {
        return "You climb out of the boiler, but all your potions broke.";
    }

    @Override
    protected void doA() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, new MarkOfTheBloom());
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, new GremlinMask());
    }

    @Override
    protected void doB() {
        if (!AbstractDungeon.player.relics.isEmpty())
            AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(0).relicId);
        if (!AbstractDungeon.player.relics.isEmpty())
            AbstractDungeon.player.loseRelic(AbstractDungeon.player.relics.get(0).relicId);
    }

    @Override
    protected void doC() {
        AbstractDungeon.player.potionSlots = 0;
        AbstractDungeon.player.potions.clear();
    }

    @Override
    protected String getAText() {
        return "You can no longer heal. Start combats with 1 Weak.";
    }

    @Override
    protected String getBText() {
        return "Lose your leftmost 2 relics.";
    }

    @Override
    protected String getCText() {
        return "Lose all your potion slots.";
    }
}
