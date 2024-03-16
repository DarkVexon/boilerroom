package code.events;

import com.megacrit.cardcrawl.cards.colorless.Finesse;
import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class BoilerEventGood extends AbstractBoilerRoomEvent{
    public BoilerEventGood() {
        super("2");
    }

    @Override
    protected String getADoneText() {
        return "Holy Crap Lois!!! There was GOLD in the Boiler!!!!";
    }

    @Override
    protected String getBDoneText() {
        return "Ther e was a HEAL in the BOILER??? No freakin' way!!!!";
    }

    @Override
    protected String getCDoneText() {
        return "FINESSE?? In the BOILER? am I dreamig";
    }

    @Override
    protected void doA() {
        AbstractDungeon.player.gainGold(100);
    }

    @Override
    protected void doB() {
        AbstractDungeon.player.heal(25);
    }

    @Override
    protected void doC() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Finesse(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
    }

    @Override
    protected String getAText() {
        return "Gain 100 Gold.";
    }

    @Override
    protected String getBText() {
        return "Heal 25 HP.";
    }

    @Override
    protected String getCText() {
        return "Obtain a Finesse.";
    }
}
