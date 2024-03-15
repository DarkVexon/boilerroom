package code.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static code.BoilerRoomMod.makeID;

public abstract class LambdaPower extends AbstractEasyPower {
    public LambdaPower(String name, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(makeID(name), name, powerType, isTurnBased, owner, amount);
        canGoNegative = canGoNegative();
    }

    public abstract void updateDescription();

    public boolean canGoNegative() {
        return true;
    }
}
