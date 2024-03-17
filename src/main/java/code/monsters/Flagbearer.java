package code.monsters;

import code.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.IntentFlashAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ShowMoveNameAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static code.BoilerRoomMod.makeID;

public class Flagbearer extends AbstractBoilerRoomMonster {
    public static final String NAME = Flagbearer.class.getSimpleName();
    public static final String ID = makeID(NAME);

    //name of the monster's moves
    private static final byte ATTACK = 0;
    private static final byte RALLYING_CRY = 1;

    public Flagbearer(float x, float y) {
        super(NAME, ID, 1, x, y, 160F, 150F);
        setHp(calcAscensionTankiness(54), calcAscensionTankiness(59));

        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(11));
        addMove(RALLYING_CRY, Intent.BUFF);
    }

    @Override
    public void usePreBattleAction() {
        this.setMoveShortcut(ATTACK, "First Strike!");
        this.createIntent();
        addToBot(new TalkAction(this, "I get to go first."));
        addToBot(new ShowMoveNameAction(this, "First Strike!"));
        addToBot(new IntentFlashAction(this));
        this.takeTurn();
        this.applyTurnPowers();
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
                for (int i = 0; i < Math.max(multiplier, 1); i++) {
                    addToBot(new DamageAction(AbstractDungeon.player, info, AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
                }
                break;
            case RALLYING_CRY:
                for (AbstractMonster m : Wiz.getEnemies()) {
                    addToBot(new ApplyPowerAction(m, this, new StrengthPower(m, calcAscensionSpecial(2)), calcAscensionSpecial(2)));
                }
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (lastMove(ATTACK)) {
            setMoveShortcut(RALLYING_CRY, "Rally");
        } else {
            setMoveShortcut(ATTACK);
        }
    }
}
