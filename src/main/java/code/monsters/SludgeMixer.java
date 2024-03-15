package code.monsters;

import code.powers.LambdaPower;
import code.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.status.Slimed;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.MalleablePower;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

import static code.BoilerRoomMod.makeID;
import static code.util.Wiz.shuffleIn;

public class SludgeMixer extends AbstractBoilerRoomMonster {
    //SludgeMixer, SLUDGE, ASSIST, SLIMESMACK, , , 60, 70, NORMAL
    public static final String NAME = SludgeMixer.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte SLUDGE = 0;
    private static final byte ASSIST = 1;
    private static final byte SLIMESMACK = 2;

    public SludgeMixer(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100, "img/foe/" + NAME + ".png");
        setHp(calcAscensionTankiness(65), calcAscensionTankiness(73));

        addMove(SLUDGE, Intent.STRONG_DEBUFF);
        addMove(ASSIST, Intent.BUFF);
        addMove(SLIMESMACK, Intent.ATTACK_DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new MalleablePower(this, calcAscensionSpecial(4)));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case SLUDGE:
                applyToPlayer(new LambdaPower("Sludged", AbstractPower.PowerType.DEBUFF, false, player(), 1) {
                    @Override
                    public void onUseCard(AbstractCard card, UseCardAction action) {
                        flash();
                        owner.decreaseMaxHealth(amount);
                    }

                    @Override
                    public void updateDescription() {
                        description = "Whenever you play a card, lose #b" + amount + " Max HP. Cannot reduce Max HP below 1, so don't worry!";
                    }
                });
                break;
            case ASSIST:
                for (AbstractMonster m : Wiz.getEnemies()) {
                    applyToAlly(m, new PlatedArmorPower(m, 5));
                    applyToAlly(m, new ArtifactPower(m, 1));
                }
                break;
            case SLIMESMACK:
                hitPlayer(AbstractGameAction.AttackEffect.POISON);
                shuffleIn(new Slimed(), 2);
        }
    }

    @Override
    protected void getMove(int i) {
        if (firstMove) {
            firstMove = false;
            setMoveShortcut(SLUDGE);
        } else {
            if (lastMove(SLIMESMACK)) {
                setMoveShortcut(ASSIST);
            } else {
                setMoveShortcut(SLIMESMACK, "Slimesmack");
            }
        }
    }
}