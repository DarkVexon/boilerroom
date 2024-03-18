package code.monsters;

import code.powers.LambdaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static code.BoilerRoomMod.makeID;

public class Stinky extends AbstractBoilerRoomMonster {
    //Stinky, STINK, , , , , 19, 24, NORMAL
    public static final String NAME = Stinky.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte STINK = 0;

    public Stinky(float x, float y) {
        super("stinky!!!", ID, 1, x, y, 115, 85);
        setHp(calcAscensionTankiness(21), calcAscensionTankiness(26));

        addMove(STINK, Intent.ATTACK_DEBUFF, calcAscensionDamage(4));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new LambdaPower("Smells Bad", AbstractPower.PowerType.BUFF, false, this, calcAscensionSpecial(2)) {
            @Override
            public void updateDescription() {
                description = "When #ystinky!!! dies, you gain #b" + amount + " #yWeak, #yVulnerable, and #yFrail.";
            }

            @Override
            public void onDeath() {
                applyToPlayer(new WeakPower(player(), amount, true));
                applyToPlayer(new VulnerablePower(player(), amount, true));
                applyToPlayer(new FrailPower(player(), amount, true));
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
        if (this.nextMove == STINK) {
            hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
            applyToPlayer(new WeakPower(player(), 1, true));
            applyToPlayer(new FrailPower(player(), 1, true));
        }
    }

    @Override
    protected void getMove(int i) {
        setMoveShortcut(STINK);
    }
}