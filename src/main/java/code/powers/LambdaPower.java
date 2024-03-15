package code.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;

import static code.BoilerRoomMod.makeID;

public abstract class LambdaPower extends AbstractEasyPower {
    protected float val;
    public LambdaPower(String name, PowerType powerType, boolean isTurnBased, AbstractCreature owner, int amount) {
        super(makeID(name), name, powerType, isTurnBased, owner, amount);
        onCreated();
        canGoNegative = canGoNegative();
    }

    protected void onCreated() {

    }

    public abstract void updateDescription();

    protected boolean canGoNegative() {
        return true;
    }
}
