package boiler.monsters;

import boiler.powers.LambdaPower;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.BufferPower;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;

import static boiler.BoilerRoomMod.makeID;

public class CardChomper extends AbstractBoilerRoomMonster {
    //CardChomper, CHOMP, , , , , 25, 32, NORMAL
    public static final String NAME = CardChomper.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte CHOMP = 0;

    public CardChomper(float x, float y) {
        super("Chomperchompy", ID, 1, x, y, 120, 120);
        setHp(calcAscensionTankiness(35), calcAscensionTankiness(40));

        addMove(CHOMP, Intent.ATTACK_DEBUFF, calcAscensionDamage(3), 2);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new BufferPower(this, 2));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        if (this.nextMove == CHOMP) {
            addToBot(new VFXAction(new BiteEffect(player().hb.cX, player().hb.cY, Color.YELLOW)));
            hitPlayer(AbstractGameAction.AttackEffect.NONE);
            applyToPlayer(new LambdaPower("Hungry", AbstractPower.PowerType.DEBUFF, false, player(), 1) {
                @Override
                public void onCardDraw(AbstractCard card) {
                    if (amount > 0) {
                        flash();
                        amount -= 1;
                        if (amount <= 0) {
                            addToTop(new RemoveSpecificPowerAction(owner, owner, this));
                        }
                        addToTop(new ExhaustSpecificCardAction(card, AbstractDungeon.player.hand));
                    }
                }

                @Override
                public void updateDescription() {
                    if (amount == 1) {
                        description = "#yExhaust the next card you draw.";
                    } else {
                        description = "#yExhaust the next #b" + amount + " cards you draw.";
                    }
                }
            });
        }
    }

    @Override
    protected void getMove(int i) {
        setMoveShortcut(CHOMP, "Chomp");
    }
}