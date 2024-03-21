package boiler.monsters;

import basemod.ReflectionHacks;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.EscapeAction;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static boiler.BoilerRoomMod.makeID;

public class NinjaskBrother extends AbstractBoilerRoomMonster {
    public static final String NAME = NinjaskBrother.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ESCAPE = 0;
    public Shedinja brother;

    public NinjaskBrother(float x, float y, Shedinja brother, int brotherNum) {
        super("Ninjask Brother " + brotherNum, ID, 5, x, y, 120, 130);
        setHp(5 * brotherNum);
        this.brother = brother;
        addMove(ESCAPE, Intent.ESCAPE);
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case ESCAPE:
                addToBot(new EscapeAction(brother));
                talk("Alright, I'm getting you out of here.");
                brother.talk("NOOOOOO, I WAS SO CLOSE TO FINISHINGGGGGGGG!");
                addToBot(new EscapeAction(this));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        setMoveShortcut(ESCAPE);
    }

    @Override
    public void createIntent() {
        super.createIntent();
        PowerTip intentTip = ReflectionHacks.getPrivate(this, AbstractMonster.class, "intentTip");
        intentTip.body = "This creature intends to drag its brother kicking and screaming away from combat.";
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        brother.brotherDied();
    }

    public void talk(String text) {
        TalkAction action = new TalkAction(this, text, 2.5F, 2.5F);
        ReflectionHacks.setPrivateInherited(action, AbstractGameAction.class, "duration", 2.5F);
        addToBot(action);
    }
}
