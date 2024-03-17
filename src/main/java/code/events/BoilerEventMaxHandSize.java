package code.events;

import basemod.BaseMod;
import code.relics.GiveBossStrength;
import com.megacrit.cardcrawl.cards.red.Defend_Red;
import com.megacrit.cardcrawl.cards.red.Strike_Red;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class BoilerEventMaxHandSize extends AbstractBoilerRoomEvent {
    public BoilerEventMaxHandSize() {
        super("4");
    }

    @Override
    protected String getADoneText() {
        return "You climb out of the boiler, but on the way, you shrunk to smaller size. ohno!.";
    }

    @Override
    protected String getBDoneText() {
        return "You climb out of the boiler, but it took so long the boss got strong.";
    }

    @Override
    protected String getCDoneText() {
        return "You climb out of the boiler, but some bad cards came along with you.";
    }

    @Override
    protected void doA() {
        BaseMod.MAX_HAND_SIZE = 5;
    }

    @Override
    protected void doB() {
        AbstractDungeon.getCurrRoom().spawnRelicAndObtain(Settings.WIDTH / 2F, Settings.HEIGHT / 2F, new GiveBossStrength());
    }

    @Override
    protected void doC() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Strike_Red(), Settings.WIDTH / 3F, Settings.HEIGHT / 2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Defend_Red(), (Settings.WIDTH / 3F) * 2, Settings.HEIGHT / 2F));
    }

    @Override
    protected String getAText() {
        return "Your maximum hand size becomes 5.";
    }

    @Override
    protected String getBText() {
        return "When you reach the Boss, it gains 2 Strength.";
    }

    @Override
    protected String getCText() {
        return "Obtain a Strike and a Defend.";
    }
}
