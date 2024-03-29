package boiler.monsters;

import boiler.powers.LambdaPower;
import boiler.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.*;

import static boiler.BoilerRoomMod.makeID;

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
        setHp(calcAscensionTankiness(301), calcAscensionTankiness(320));

        addMove(TOTHIRTY, Intent.ATTACK, -1);
        addMove(LAMEIFY, Intent.DEFEND_DEBUFF);
        addMove(SUMMONSLIMEBOSS, Intent.UNKNOWN);
        addMove(SQUASH, Intent.DEBUFF);
        addMove(DEBUFFS, Intent.ATTACK_DEBUFF, calcAscensionDamage(15));
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("boiler_boss");
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
                    applyToSelf(new AngerPower(this, 1));
                }
                break;
            case LAMEIFY:
                addToBot(new GainBlockAction(this, 18));
                applyToPlayer(new LambdaPower("Lame", AbstractPower.PowerType.DEBUFF, false, player(), -1) {
                    @Override
                    public void onPlayCard(AbstractCard card, AbstractMonster m) {
                        if (((card.cost < card.makeCopy().cost || card.costForTurn < card.makeCopy().costForTurn) && card.isCostModified) || card.freeToPlay()) {
                            flash();
                            for (AbstractCard c : AbstractDungeon.player.hand.group) {
                                c.freeToPlayOnce = false;
                                c.updateCost(1);
                            }
                        }
                    }

                    @Override
                    protected boolean canGoNegative() {
                        return false;
                    }

                    @Override
                    public void updateDescription() {
                        description = "Whenever you play a card for a reduced cost, increase the cost of cards in your hand by 1.";
                    }
                });
                break;
            case SUMMONSLIMEBOSS:
                addToBot(new SpawnMonsterAction(new SlimeBossAsEnemy(this.hb_x - 320, this.hb_y), false));
                applyToSelf(new LambdaPower("Guarded", AbstractPower.PowerType.BUFF, false, this, -1) {
                    @Override
                    protected boolean canGoNegative() {
                        return false;
                    }

                    @Override
                    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
                        if (damageAmount >= owner.currentHealth && Wiz.getEnemies().size() > 1) {
                            this.flash();
                            return owner.currentHealth - 1;
                        } else {
                            return damageAmount;
                        }
                    }

                    @Override
                    public void updateDescription() {
                        description = "#yMotherthing cannot fall below 1 HP if other enemies exist.";
                    }
                });
                break;
            case SQUASH:
                applyToPlayer(new ConstrictedPower(player(), this, calcAscensionSpecial(6)));
                addToBot(new HealAction(this, this, calcAscensionSpecial(12)));
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

    @Override
    public void die() {
        this.useFastShakeAnimation(5.0F);
        CardCrawlGame.screenShake.rumble(4.0F);
        super.die();
        this.onBossVictoryLogic();
        this.onFinalBossVictoryLogic();
    }
}