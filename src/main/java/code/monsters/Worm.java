package code.monsters;

import code.cards.WormCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.LoseHPAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.powers.WeakPower;

import static code.BoilerRoomMod.makeID;

public class Worm extends AbstractBoilerRoomMonster {
    public static final String NAME = Worm.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;
    private static final byte ATTACK_DEBUFF = 1;
    private static final byte BURROW = 2;

    public Worm(float x, float y) {
        super(NAME, ID, 1, x, y, 120, 130);
        setHp(calcAscensionTankiness(41), calcAscensionTankiness(47));

        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(6), 2);
        addMove(ATTACK_DEBUFF, Intent.ATTACK_DEBUFF, calcAscensionDamage(8));
        addMove(BURROW, Intent.STRONG_DEBUFF);
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case ATTACK:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                break;
            case ATTACK_DEBUFF:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                applyToPlayer(new WeakPower(player(), 1, true));
                break;
            case BURROW:
                addToBot(new LoseHPAction(this, this, this.currentHealth));
                addToBot(new AddCardToDeckAction(new WormCard()));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (turns == 3) {
            setMoveShortcut(BURROW, "Burrow");
        } else {
            if (lastMove(ATTACK)) {
                setMoveShortcut(ATTACK_DEBUFF);
            } else {
                setMoveShortcut(ATTACK);
            }
        }

        turns += 1;
    }
}
