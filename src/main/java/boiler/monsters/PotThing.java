package boiler.monsters;

import boiler.cards.PottedCard;
import boiler.powers.LambdaPower;
import boiler.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ThornsPower;

import java.util.ArrayList;

import static boiler.BoilerRoomMod.makeID;

public class PotThing extends AbstractBoilerRoomMonster {
    //PotThing, POT_STUFF, ATTACK_BUFF, , , , 58, 65, NORMAL
    public static final String NAME = PotThing.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte POT_STUFF = 0;
    private static final byte ATTACK_BUFF = 1;
    private boolean attacksFirst;

    public PotThing(float x, float y, boolean attacksFirst) {
        super("Pot Thing", ID, 1, x, y, 140, 160);
        this.attacksFirst = attacksFirst;
        setHp(calcAscensionTankiness(46), calcAscensionTankiness(54));

        addMove(POT_STUFF, Intent.DEBUFF);
        addMove(ATTACK_BUFF, Intent.ATTACK_BUFF, calcAscensionDamage(12));
    }

    public static void potCard(AbstractCard c) {
        AbstractCard replacement = new PottedCard(c);
        if (AbstractDungeon.player.drawPile.contains(c)) {
            AbstractDungeon.player.drawPile.group.set(AbstractDungeon.player.drawPile.group.indexOf(c), replacement);
        } else if (AbstractDungeon.player.discardPile.contains(c)) {
            AbstractDungeon.player.discardPile.group.set(AbstractDungeon.player.discardPile.group.indexOf(c), replacement);
        } else if (AbstractDungeon.player.hand.contains(c)) {
            AbstractDungeon.player.hand.group.set(AbstractDungeon.player.hand.group.indexOf(c), replacement);
        } else if (AbstractDungeon.player.masterDeck.contains(c)) {
            AbstractDungeon.player.masterDeck.removeCard(c);
            AbstractDungeon.player.masterDeck.addToRandomSpot(replacement);
        }
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new LambdaPower("Ceramic Shield", AbstractPower.PowerType.BUFF, false, this, 9) {
            @Override
            public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
                if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS && damageAmount > amount) {
                    this.flash();
                    return amount;
                } else {
                    return damageAmount;
                }
            }

            @Override
            public void updateDescription() {
                description = "Cannot take more than #b" + amount + " damage at once.";
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
            case POT_STUFF:
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        int numPotted = calcAscensionSpecial(4);
                        ArrayList<AbstractCard> toAdd = new ArrayList<>();
                        ArrayList<AbstractCard> valids = new ArrayList<>();
                        for (AbstractCard c : AbstractDungeon.player.drawPile.group) {
                            if (c.cost >= 0 && c.cost <= 3 && !(c instanceof PottedCard)) {
                                valids.add(c);
                            }
                        }
                        for (AbstractCard c : AbstractDungeon.player.discardPile.group) {
                            if (c.cost >= 0 && c.cost <= 3 && !(c instanceof PottedCard)) {
                                valids.add(c);
                            }
                        }
                        for (int i = 0; i < numPotted; i++) {
                            if (valids.isEmpty()) {
                                break;
                            }
                            AbstractCard c = Wiz.getRandomItem(valids);
                            valids.remove(c);
                            toAdd.add(c);
                        }
                        for (AbstractCard c : toAdd) {
                            potCard(c);
                        }
                    }
                });
                break;
            case ATTACK_BUFF:
                hitPlayer(AbstractGameAction.AttackEffect.SLASH_HEAVY);
                applyToSelf(new ThornsPower(this, calcAscensionSpecial(2)));
        }
    }

    @Override
    protected void getMove(int i) {
        if (attacksFirst) {
            if (lastMove(ATTACK_BUFF)) {
                setMoveShortcut(POT_STUFF, "Pottery");
            } else {
                setMoveShortcut(ATTACK_BUFF);
            }
        } else {
            if (lastMove(POT_STUFF)) {
                setMoveShortcut(ATTACK_BUFF);
            } else {
                setMoveShortcut(POT_STUFF, "Pottery");
            }
        }
    }
}