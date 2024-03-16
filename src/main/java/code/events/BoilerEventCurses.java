package code.events;

import com.megacrit.cardcrawl.cards.curses.Normality;
import com.megacrit.cardcrawl.cards.curses.Regret;
import com.megacrit.cardcrawl.cards.curses.Writhe;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.GremlinMask;
import com.megacrit.cardcrawl.relics.MarkOfTheBloom;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

public class BoilerEventCurses extends AbstractBoilerRoomEvent{
    public BoilerEventCurses() {
        super("1");
    }

    @Override
    protected String getADoneText() {
        return "You climb out of the boiler, but you feel normal doing so and then you regret itf djnjdnf";
    }

    @Override
    protected String getBDoneText() {
        return "You climb out of the boiler, but it made you get tired or something, I dunno";
    }

    @Override
    protected String getCDoneText() {
        return "You climb out of the boiler, but IT WAS A SLIMY BOILER AAAHHH";
    }

    @Override
    protected void doA() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Normality(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Regret(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
    }

    @Override
    protected void doB() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Writhe(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
    }

    @Override
    protected void doC() {
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Slimed(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Slimed(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Slimed(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
        AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new Slimed(), Settings.WIDTH/2F, Settings.HEIGHT/2F));
    }

    @Override
    protected String getAText() {
        return "Obtain Regret and Normality.";
    }

    @Override
    protected String getBText() {
        return "Obtain two Writhes.";
    }

    @Override
    protected String getCText() {
        return "Obtain four Slimed.";
    }
}
