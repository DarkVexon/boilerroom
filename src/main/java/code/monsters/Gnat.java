package code.monsters;

import basemod.ReflectionHacks;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.AnimateHopAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.powers.IntangiblePower;

import static code.BoilerRoomMod.makeID;

public class Gnat extends AbstractBoilerRoomMonster {
    public static final String NAME = Gnat.class.getSimpleName();
    public static final String ID = makeID(NAME);

    public static final byte SKIP = 0;
    public static final byte ATTACK_TINY = 1;
    public static final byte ATTACK_REGULAR = 2;

    private final float baseDrawY;

    public Gnat(float x, float y) {
        super(NAME, ID, 1, x, y, 55, 40);
        baseDrawY = this.drawY;
        setHp(calcAscensionTankiness(2), calcAscensionTankiness(4));

        addMove(SKIP, Intent.UNKNOWN);
        addMove(ATTACK_TINY, Intent.ATTACK, calcAscensionDamage(1));
        addMove(ATTACK_REGULAR, Intent.ATTACK, calcAscensionDamage(2));
    }

    @Override
    public void usePreBattleAction() {
        AbstractPower p = new IntangiblePower(this, 99);
        ReflectionHacks.setPrivate(p, IntangiblePower.class, "justApplied", false);
        applyToSelf(p);
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

    public void update() {
        super.update();
        if (this.maxHealth == 2) {
            this.drawY = baseDrawY + MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
        } else {
            this.drawY = baseDrawY + -MathUtils.cosDeg((float) (System.currentTimeMillis() / 6L % 360L)) * 6.0F * Settings.scale;
        }
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
        if (i <= 40) {
            setMoveShortcut(SKIP);
        } else if (i <= 70) {
            setMoveShortcut(ATTACK_TINY);
        } else {
            setMoveShortcut(ATTACK_REGULAR);
        }
    }
}
