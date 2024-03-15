package code.monsters;

import code.powers.LambdaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;

import static code.BoilerRoomMod.makeID;

public class Marisa extends AbstractBoilerRoomMonster {
    //Marisa, ATTACK, , , , , 192, 201, NORMAL
    public static final String NAME = Marisa.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;

    private int attackAmt = 8;

    public Marisa(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100, "img/foe/" + NAME + ".png");
        setHp(calcAscensionTankiness(192), calcAscensionTankiness(201));

        addMove(ATTACK, Intent.ATTACK, 8);
    }

    @Override
    public void usePreBattleAction() {
        addToBot(new TalkAction(this, "I'm Marisa from Touhou", 0.5F, 2.0F));
        applyToSelf(new LambdaPower("I'm Marisa from Touhou", AbstractPower.PowerType.BUFF, false, this, -1) {
            @Override
            public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
                if (type != DamageInfo.DamageType.HP_LOSS) {
                    return damage - 1;
                }
                return damage;
            }

            @Override
            public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
                if (abstractPower.type == PowerType.DEBUFF) {
                    return false;
                }
                return true;
            }

            @Override
            protected boolean canGoNegative() {
                return false;
            }

            @Override
            public void updateDescription() {
                description = "I'm Marisa from Touhou. Also, I can't be debuffed, and I take 1 less damage from all sources, and you can't use potions. Hee hee!";
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
        addToBot(new TalkAction(this, "I'm Marisa from Touhou", 0.5F, 2.0F));
        useFastAttackAnimation();
        addToBot(new DamageAction(AbstractDungeon.player, new DamageInfo(this, attackAmt, DamageInfo.DamageType.NORMAL), AbstractGameAction.AttackEffect.FIRE));
        attackAmt *= 2;
    }

    @Override
    public void die(boolean triggerRelics) {
        super.die(triggerRelics);
        addToBot(new TalkAction(this, "I'm Marisa from Touhou", 0.5F, 2.0F));
    }

    @Override
    protected void getMove(int i) {
        setMove("I'm Marisa from Touhou", ATTACK, Intent.ATTACK, attackAmt);
    }
}