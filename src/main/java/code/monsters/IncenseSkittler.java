package code.monsters;

import code.powers.LambdaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import static code.BoilerRoomMod.makeID;

public class IncenseSkittler extends AbstractBoilerRoomMonster {
    //IncenseSkittler, INCENSE, ATTACK, INTANGIBLE, , , 45, 52, NORMAL
    public static final String NAME = IncenseSkittler.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte INCENSE = 0;
    private static final byte ATTACK = 1;
    private static final byte INTANGIBLE = 2;

    public IncenseSkittler(float x, float y) {
        super("Incense Skittler", ID, 1, x, y, 200, 300);
        setHp(calcAscensionTankiness(45), calcAscensionTankiness(52));

        addMove(INCENSE, Intent.STRONG_DEBUFF);
        addMove(ATTACK, Intent.ATTACK, 1);
        addMove(INTANGIBLE, Intent.BUFF);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new IntangiblePower(this, 1));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case INCENSE:
                applyToPlayer(new LambdaPower("Incense", AbstractPower.PowerType.DEBUFF, false, AbstractDungeon.player, calcAscensionSpecial(2)) {
                    @Override
                    public int onAttacked(DamageInfo info, int damageAmount) {
                        if (info.owner != null && info.owner != this.owner && info.type != DamageInfo.DamageType.HP_LOSS && info.type != DamageInfo.DamageType.THORNS && damageAmount <= 0 && info.output > 0) {
                            this.flash();
                            AbstractDungeon.actionManager.addToTop(new LoseHPAction(owner, owner, amount));
                        }
                        return damageAmount;
                    }

                    @Override
                    public void updateDescription() {
                        description = "Whenever you fully block damage, lose #b" + amount + " HP.";
                    }
                });
                break;
            case ATTACK:
                hitPlayer(AbstractGameAction.AttackEffect.SLASH_DIAGONAL);
                break;
            case INTANGIBLE:
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        AbstractMonster target = randomAlly();
                        applyToAlly(target, new IntangiblePower(target, 1));
                    }
                });
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (!player().hasPower("boiler:Incense")) {
            setMoveShortcut(INCENSE);
        } else {
            if (lastMove(ATTACK)) {
                setMoveShortcut(INTANGIBLE);
            } else {
                setMoveShortcut(ATTACK);
            }
        }
    }
}