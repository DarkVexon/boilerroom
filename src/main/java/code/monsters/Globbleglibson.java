package code.monsters;

import code.powers.LambdaPower;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import static code.BoilerRoomMod.makeID;

public class Globbleglibson extends AbstractBoilerRoomMonster {
    //Globbleglibson, TOTHIRTY, LAMEIFY, SUMMONSLIMEBOSS, SQUASH, DEBUFFS, 500, 506, BOSS
    public static final String NAME = Globbleglibson.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte TOTHIRTY = 0;
    private static final byte LAMEIFY = 1;
    private static final byte SUMMONSLIMEBOSS = 2;
    private static final byte SQUASH = 3;
    private static final byte DEBUFFS = 4;

    public Globbleglibson() {
        super("Motherthing", ID, 1, 150, -1, 333, 444);
        setHp(calcAscensionTankiness(300), calcAscensionTankiness(320));

        addMove(TOTHIRTY, Intent.ATTACK, -1);
        addMove(LAMEIFY, Intent.DEBUFF);
        addMove(SUMMONSLIMEBOSS, Intent.UNKNOWN);
        addMove(SQUASH, Intent.DEBUFF);
        addMove(DEBUFFS, Intent.ATTACK_DEBUFF, calcAscensionDamage(16));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new InvinciblePower(this, 100));
        addToBot(new TalkAction(this, "I won't allow it...", 0.5F, 1.5F));
        applyToPlayer(new LambdaPower("Demoted", AbstractPower.PowerType.BUFF, false, player(), -1) {

            @Override
            protected boolean canGoNegative() {
                return false;
            }

            @Override
            public boolean onReceivePower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
                if (abstractPower.type == PowerType.BUFF) {
                    flash();
                    return false;
                }
                return true;
            }

            @Override
            public boolean betterOnApplyPower(AbstractPower abstractPower, AbstractCreature target, AbstractCreature source) {
                if (abstractPower.type == PowerType.DEBUFF && target != owner) {
                    flash();
                    return false;
                }
                return true;
            }

            @Override
            public void updateDescription() {
                description = "You can't gain buffs or debuff others.";
            }
        });
        if (AbstractDungeon.player.id.toLowerCase().contains("thorton")) {
            if (AbstractDungeon.ascensionLevel <= 19) {
                addToBot(new TalkAction(this, "Greed... is bad... Thorton.", 0.5F, 1.5F));
            } else {
                addToBot(new TalkAction(this, "In time, you will understand.", 0.5F, 1.5F));
            }
        }
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.BOSS;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case TOTHIRTY:
                if (this.moves.get(TOTHIRTY).baseDamage > 0) {
                    hitPlayer(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                } else {
                    addToBot(new TalkAction(this, "I think... you should... have rested.", 0.5F, 1.5F));
                    applyToPlayer(new WeakPower(player(), calcAscensionSpecial(5), true));
                }
                break;
            case LAMEIFY:
                applyToPlayer(new LambdaPower("Lame", AbstractPower.PowerType.DEBUFF, false, player(), -1) {
                    @Override
                    public void onUseCard(AbstractCard card, UseCardAction action) {
                        if (card.costForTurn < card.cost || card.freeToPlay()) {
                            flash();
                            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                                c.freeToPlayOnce = false;
                                c.updateCost(2);
                            }
                        }
                    }

                    @Override
                    protected boolean canGoNegative() {
                        return false;
                    }

                    @Override
                    public void updateDescription() {
                        description = "Whenever you play a card for a reduced cost, reset the cost of cards in your hand, then increase them by 2 for the rest of combat.";
                    }
                });
                break;
            case SUMMONSLIMEBOSS:
                addToBot(new SpawnMonsterAction(new SlimeBossAsEnemy(this.hb_x - 150, this.hb_y), false));
                break;
            case SQUASH:
                applyToPlayer(new ConstrictedPower(player(), this, calcAscensionSpecial(6)));
                addToBot(new HealAction(this, this, calcAscensionSpecial(6)));
                break;
            case DEBUFFS:
                hitPlayer(AbstractGameAction.AttackEffect.FIRE);
                if (AbstractDungeon.monsterRng.randomBoolean())
                    applyToPlayer(new WeakPower(player(), 2, true));
                else
                    applyToPlayer(new FrailPower(player(), 2, true));
        }
    }

    @Override
    protected void getMove(int i) {
        switch (turns) {
            case 1:
                if (AbstractDungeon.player.currentHealth > 28) {
                    this.moves.get(TOTHIRTY).baseDamage = AbstractDungeon.player.currentHealth - 28;
                    setMoveShortcut(TOTHIRTY);
                } else {
                    setMove(TOTHIRTY, Intent.BUFF);
                }
                turns += 1;
                break;
            case 2:
                setMoveShortcut(LAMEIFY, "Lameify");
                turns += 1;
                break;
            case 3:
                setMoveShortcut(SUMMONSLIMEBOSS);
                turns += 1;
                break;
            case 4:
                setMoveShortcut(SQUASH);
                turns += 1;
                break;
            case 5:
                setMoveShortcut(DEBUFFS);
                turns -= 1;
                break;
        }
    }
}