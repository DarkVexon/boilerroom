package code.monsters;

import code.powers.LambdaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.CuriosityPower;

import static code.BoilerRoomMod.makeID;
import static code.util.Wiz.shuffleIn;

public class GumGum extends AbstractBoilerRoomMonster {
    //GumGum, DUMDUM, CURIOUS, ATTACK_BLOCK, SLIMER, , 250, 261, ELITE
    public static final String NAME = GumGum.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte DUMDUM = 0;
    private static final byte CURIOUS = 1;
    private static final byte ATTACK_BLOCK = 2;

    public GumGum(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100);
        setHp(calcAscensionTankiness(250), calcAscensionTankiness(261));

        addMove(DUMDUM, Intent.STRONG_DEBUFF);
        addMove(CURIOUS, Intent.UNKNOWN);
        addMove(ATTACK_BLOCK, Intent.ATTACK_DEFEND, calcAscensionDamage(15));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new CuriosityPower(this, calcAscensionSpecial(6)));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case DUMDUM:
                applyToPlayer(new LambdaPower("Dum Dum", AbstractPower.PowerType.DEBUFF, false, player(), -1) {
                    @Override
                    protected boolean canGoNegative() {
                        return false;
                    }

                    @Override
                    public void updateDescription() {
                        description = "You play cards from left to right automatically.";
                    }
                });
                break;
            case CURIOUS:
                for (int i = 0; i < 2; i++) {
                    shuffleIn(AbstractDungeon.returnTrulyRandomCardInCombat(AbstractCard.CardType.POWER));
                }
                break;
            case ATTACK_BLOCK:
                addToBot(new GainBlockAction(this, 15));
                hitPlayer(AbstractGameAction.AttackEffect.SLASH_HEAVY);
        }
    }

    @Override
    protected void getMove(int i) {
        if (turns == 1) {
            setMoveShortcut(DUMDUM, "Dum Dum");
            turns += 1;
        } else if (turns == 2) {
            setMoveShortcut(CURIOUS, "What Id That?");
            turns += 1;
        } else if (turns == 3) {
            setMoveShortcut(ATTACK_BLOCK);
            turns -= 1;
        }
    }
}