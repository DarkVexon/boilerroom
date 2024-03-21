package boiler.monsters;

import basemod.ReflectionHacks;
import boiler.powers.LambdaPower;
import boiler.util.TalkActionPositional;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static boiler.BoilerRoomMod.makeID;

public class Shedinja extends AbstractBoilerRoomMonster {
    public static final String NAME = Shedinja.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;
    private final int ATTACK_DAMAGE_INCREASE = calcAscensionDamage(5);

    public int brotherNum = 1;

    public Shedinja(float x, float y) {
        super("shedinja", ID, 1, x, y, 120, 130);
        setHp(1);
        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(30));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void usePreBattleAction() {
        addPower(new LambdaPower("wonder guard but with no downsides", AbstractPower.PowerType.BUFF, false, this, 0) {
            @Override
            protected boolean canGoNegative() {
                return false;
            }

            @Override
            public void updateDescription() {
                description = "protection from EVERYTHING";
            }
        });
        talk("BEHOLD the perfection of my being!");
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case ATTACK:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                int newDamage = moves.get(ATTACK).baseDamage += ATTACK_DAMAGE_INCREASE;
                addMove(ATTACK, Intent.ATTACK, newDamage);
                break;
        }
        switch (turns) {
            case 0:
                talk("YOU STAND NO CHANCE AGAINST ME!");
                break;
            case 1:
                talk("CEASE YOUR FOOLISH RESISTANCE!");
                break;
            case 2:
                if (brotherNum > 1) {
                    brotherTalk("Sheddy, why are you still out there?");
                    talk("I AM BUSY AVENGING MY BROTHER, OTHER BROTHER OF MINE!");
                } else {
                    brotherTalk("Sheddy, have you gotten the soup yet?");
                    talk("HOW MANY TIMES DO I HAVE TO TELL YOU TO NOT CALL ME SHEDDY, BROTHER OF MINE?");
                }
                break;
            case 3:
                if (brotherNum > 1) {
                    brotherTalk("Sheddy, you have so many brothers that die all the time, you don't have to avenge them.");
                    talk("I CARE DEEPLY ABOUT EACH AND EVERY ONE OF MY SIBLINGS, WHAT ARE YOU TALKING ABOUT?");
                } else {
                    brotherTalk("Sheddy, mom is getting impatient");
                    talk("BE SILENT! I AM ALMOST FINISHED DISPOSING OF THIS HEATHEN!");
                }
                break;
            case 4:
                NinjaskBrother ninjaskBrother = new NinjaskBrother(300.0f, 100.0f, this, brotherNum);
                addToBot(new SpawnMonsterAction(ninjaskBrother, false));
                ninjaskBrother.talk("Sheddy, I've come to bring you back");
                talk("NOT YET! I STILL HAVE UNFINISHED BUSINESS HERE");
                break;
        }
        turns += 1;
        if (turns > 5) {
            turns = 0;
        }
    }

    @Override
    protected void getMove(int i) {
        setMoveShortcut(ATTACK);
    }

    @Override
    public void update() {
        super.update();
        if (this.currentHealth != 1) {
            this.currentHealth = this.maxHealth = 1;
            healthBarUpdatedEvent();
        }
        this.isDead = false;
        this.isDying = false;
    }

    @Override
    public void damage(DamageInfo info) {
        switch (AbstractDungeon.miscRng.random(0, 2)) {
            case 0:
                talk("WEAK!");
                break;
            case 1:
                talk("FOOLISH!");
                break;
            case 2:
                talk("PATHETIC!");
                break;
        }
    }

    @Override
    public void die(boolean triggerRelics) {

    }

    public void talk(String text) {
        TalkAction action = new TalkAction(this, text, 1.75F, 1.75F);
        ReflectionHacks.setPrivateInherited(action, AbstractGameAction.class, "duration", 1.75F);
        addToBot(action);
    }

    private void brotherTalk(String text) {
        TalkActionPositional action = new TalkActionPositional(1700.0F * Settings.scale, this.hb.cY + this.dialogY, text, 2.5F, 2.5F);
        addToBot(action);
    }

    public void brotherDied() {
        talk("BROTHER NOOOOOO! I SHALL AVENGE YOUR DEATH!");
        brotherNum++;
    }
}
