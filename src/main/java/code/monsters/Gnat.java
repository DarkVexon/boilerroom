package code.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import static code.BoilerRoomMod.makeID;

public class Gnat extends AbstractBoilerRoomMonster {
    public static final String NAME = Gnat.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public static final byte SKIP = 0;
    public static final byte ATTACK_TINY = 1;
    public static final byte ATTACK_REGULAR = 2;

    public Gnat(float x, float y) {
        super(NAME, ID, 1, x, y, 20, 20, "img/foe/gnat.png");
        setHp(calcAscensionTankiness(3), calcAscensionTankiness(5));

        addMove(SKIP, Intent.UNKNOWN);
        addMove(ATTACK_TINY, Intent.ATTACK, calcAscensionDamage(2));
        addMove(ATTACK_REGULAR, Intent.ATTACK, calcAscensionDamage(4));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new IntangiblePower(this, 99));
        int bufferAmount = AbstractDungeon.monsterRng.random(0, 3);
        if (AbstractDungeon.ascensionLevel >= 17) {
            bufferAmount += 1;
        }
        if (bufferAmount > 0) {
            applyToSelf(new BufferPower(this, bufferAmount));
        }
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case SKIP:
                addToBot(new AnimateHopAction(this));
                break;
            case ATTACK_TINY:
            case ATTACK_REGULAR:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (i <= 33) {
            setMoveShortcut(SKIP);
        } else if (i <= 66) {
            setMoveShortcut(ATTACK_TINY);
        } else {
            setMoveShortcut(ATTACK_REGULAR);
        }
    }
}