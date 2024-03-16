package code.monsters;

import code.powers.LambdaPower;
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

        addMove(STINK, Intent.DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new LambdaPower("Smells Bad", AbstractPower.PowerType.BUFF, false, this, calcAscensionSpecial(2)) {
            @Override
            public void updateDescription() {
                description = "When #ystinky!!! dies, gain #b" + amount + " #yWeak, #yVulnerable, and #yFrail.";
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
            case STINK:
                applyToPlayer(new WeakPower(player(), 1, true));
                applyToPlayer(new FrailPower(player(), 1, true));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        setMoveShortcut(STINK);
    }
}