package code.monsters;

import code.powers.LambdaPower;
import code.util.Wiz;
import com.badlogic.gdx.Gdx;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.cards.status.VoidCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static code.BoilerRoomMod.makeID;
import static code.util.Wiz.shuffleIn;

public class Clockling extends AbstractBoilerRoomMonster {
    //Clockling, TICK, TOCK, , , , 72, 72, NORMAL
    public static final String NAME = Clockling.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte TICK = 0;
    private static final byte TOCK = 1;

    public Clockling(float x, float y) {
        super(NAME, ID, 1, x, y, 250, 275);
        setHp(calcAscensionTankiness(74), calcAscensionTankiness(82));

        addMove(TICK, Intent.BUFF);
        addMove(TOCK, Intent.DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new LambdaPower("Active Time Battler", AbstractPower.PowerType.BUFF, false, this, 1) {

            @Override
            protected void onCreated() {
                val = 180;
            }

            @Override
            public void update(int slot) {
                super.update(slot);
                if (!owner.isDead && !AbstractDungeon.isScreenUp && !(owner.currentHealth <= 0) && !owner.isDying && !owner.isDeadOrEscaped()) {
                    val -= Gdx.graphics.getDeltaTime();
                    if (val <= 0) {
                        val = 180;
                        addToTop(new DamageAction(player(), new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), AbstractGameAction.AttackEffect.BLUNT_LIGHT));
                    }
                }
            }

            @Override
            public void updateDescription() {
                description = "#yClockling deals #b" + amount + " damage to you every #b3 seconds. Yes, this counts animations. No, it doesn't pause at any point. Why are you still reading this? I mean if you want to keep reading instead of playing cards, be my guest. I hope you had a good breakfast today, like it was REALLY good. I'm gonna have a chocolate chip cookie later. That's gonna be great. Do you like chocolate chip cookies?";
            }
        });
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case TICK:
                for (AbstractMonster m : Wiz.getEnemies()) {
                    addToBot(new HealAction(m, this, calcAscensionSpecial(6)));
                    addToBot(new GainBlockAction(m, this, 12));
                }
                break;
            case TOCK:
                shuffleIn(new VoidCard());
                shuffleIn(new Slimed());
        }
    }

    @Override
    protected void getMove(int i) {
        if (lastMove(TICK)) {
            setMoveShortcut(TOCK);
        } else {
            setMoveShortcut(TICK);
        }
    }
}