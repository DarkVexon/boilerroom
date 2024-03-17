package code.monsters;

import code.cards.SpecterCard;
import code.powers.LambdaPower;
import code.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;

import static code.BoilerRoomMod.makeID;

public class Marisa extends AbstractBoilerRoomMonster {
    //Marisa, ATTACK, , , , , 192, 201, NORMAL
    public static final String NAME = Marisa.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;

    private int attackAmt = 4;

    public Marisa(float x, float y) {
        super("Marisa from Touhou", ID, 1, x, y, 333, 325);
        setHp(calcAscensionTankiness(192), calcAscensionTankiness(201));

        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(4));
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
            public void onDeath() {
                flash();
                AbstractDungeon.effectsQueue.add(new ShowCardAndObtainEffect(new SpecterCard(), Settings.WIDTH / 2F, Settings.HEIGHT / 2F));
            }

            @Override
            public void updateDescription() {
                description = "I'm Marisa from Touhou. Also, I can't be debuffed, and I take 1 less damage from all sources, and you can't use potions, and if you beat me you get cursed. Hee hee!";
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
        Wiz.shuffleIn(new Burn());
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