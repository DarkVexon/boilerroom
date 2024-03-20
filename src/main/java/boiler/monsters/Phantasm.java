package boiler.monsters;

import boiler.powers.LambdaPower;
import boiler.util.Wiz;
import boiler.vfx.StealRelicEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.ClearCardQueueAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ExhaustAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.SetMoveAction;
import com.megacrit.cardcrawl.actions.unique.CanLoseAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import static boiler.BoilerRoomMod.makeID;

public class Phantasm extends AbstractBoilerRoomMonster {
    //Phantasm, FORGETTING, POISON, MULTIHIT, MEGADEBUFF, NUKE, 300, 325, BOSS
    public static final String NAME = Phantasm.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte FORGETTING = 0;
    private static final byte POISON = 1;
    private static final byte MULTIHIT = 2;
    private static final byte MEGADEBUFF = 3;
    private static final byte NUKE = 4;
    private static final byte RETURN = 5;

    private int revives = 4;

    public Phantasm() {
        super(NAME, ID, 1, 0, 0, 350, 410);
        setHp(calcAscensionTankiness(155), calcAscensionTankiness(162));

        addMove(FORGETTING, Intent.ATTACK_DEBUFF, calcAscensionDamage(12));
        addMove(POISON, Intent.STRONG_DEBUFF);
        addMove(MULTIHIT, Intent.ATTACK, calcAscensionDamage(4), 8);
        addMove(MEGADEBUFF, Intent.DEFEND_DEBUFF);
        addMove(NUKE, Intent.ATTACK, calcAscensionDamage(15), calcAscensionDamage(15));
        addMove(RETURN, Intent.UNKNOWN);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("boiler_boss");
        AbstractDungeon.getCurrRoom().cannotLose = true;
    }

    public void atStartOfTurnPostDrawTurnOne() {
        addToBot(new TalkAction(this, "Nice hand... SAY GOODBYE TO IT!!", 0.5F, 2.0F));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                addToTop(new ExhaustAction(AbstractDungeon.player.hand.size(), true, false));
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
            case FORGETTING:
                hitPlayer(AbstractGameAction.AttackEffect.FIRE);
                applyToPlayer(new LambdaPower("Forgetting", AbstractPower.PowerType.DEBUFF, false, player(), 2) {
                    @Override
                    public void atStartOfTurnPostDraw() {
                        flash();
                        addToBot(new AbstractGameAction() {
                            @Override
                            public void update() {
                                isDone = true;
                                addToTop(new ExhaustAction(Math.min(AbstractDungeon.player.hand.size(), 2), true, false));
                            }
                        });
                    }

                    @Override
                    public void updateDescription() {
                        if (amount == 1) {
                            description = "At the start of your turn, #yExhaust #b" + amount + " random card.";
                        } else {
                            description = "At the start of your turn, #yExhaust #b" + amount + " random cards.";
                        }
                    }
                });
                break;
            case POISON:
                applyToPlayer(new PoisonPower(player(), this, calcAscensionSpecial(3)));
                break;
            case MULTIHIT:
                hitPlayer(AbstractGameAction.AttackEffect.FIRE);
                break;
            case MEGADEBUFF:
                addToBot(new GainBlockAction(this, 15));
                int value = AbstractDungeon.monsterRng.random(0, 2);
                switch (value) {
                    case 0:
                        applyToPlayer(new WeakPower(player(), calcAscensionSpecial(6), true));
                        break;
                    case 1:
                        applyToPlayer(new VulnerablePower(player(), calcAscensionSpecial(6), true));
                        break;
                    case 2:
                        applyToPlayer(new FrailPower(player(), calcAscensionSpecial(6), true));
                        break;
                }
                break;
            case NUKE:
                hitPlayer(AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                break;
            case RETURN:
                switch (revives) {
                    case 4:
                        maxHealth = Math.round(maxHealth / 2F);
                        halfDead = false;
                        addToBot(new HealAction(this, this, this.maxHealth));
                        applyToSelf(new ArtifactPower(this, 5));
                        revives -= 1;
                        break;
                    case 3:
                        maxHealth = Math.round(maxHealth / 2F);
                        halfDead = false;
                        addToBot(new HealAction(this, this, this.maxHealth));
                        applyToSelf(new ArtifactPower(this, 5));
                        applyToSelf(new MalleablePower(this, 10));
                        revives -= 1;
                        break;
                    case 2:
                        maxHealth = Math.round(maxHealth / 2F);
                        halfDead = false;
                        addToBot(new HealAction(this, this, this.maxHealth));
                        applyToSelf(new ArtifactPower(this, 5));
                        applyToSelf(new MalleablePower(this, 10));
                        applyToSelf(new BufferPower(this, 5));
                        revives -= 1;
                        break;
                    case 1:
                        maxHealth = Math.round(maxHealth / 2F);
                        halfDead = false;
                        addToBot(new HealAction(this, this, this.maxHealth));
                        applyToSelf(new ArtifactPower(this, 5));
                        applyToSelf(new MalleablePower(this, 10));
                        applyToSelf(new BufferPower(this, 5));
                        applyToSelf(new IntangiblePower(this, 5));
                        revives -= 1;
                        addToBot(new CanLoseAction());
                        break;
                }
        }
    }

    @Override
    public void damage(DamageInfo info) {
        super.damage(info);
        if (currentHealth <= 0 && !halfDead) {
            if (AbstractDungeon.getCurrRoom().cannotLose) {
                halfDead = true;
            }
            for (AbstractPower p : powers) {
                p.onDeath();
            }
            for (AbstractRelic r : AbstractDungeon.player.relics) {
                r.onMonsterDeath(this);
            }
            addToTop(new ClearCardQueueAction());
            this.powers.clear();
            setMoveShortcut(RETURN);
            createIntent();
            switch (revives) {
                case 4:
                    addToBot(new TalkAction(this, "You REALLY thought that was enough to best me?", 0.5F, 2F));
                    break;
                case 3:
                    addToBot(new TalkAction(this, "You're one annoying pest...", 0.5F, 2F));
                    break;
                case 2:
                    addToBot(new TalkAction(this, "I've nearly got you now. I can outlast ANYONE!", 0.5F, 2F));
                    break;
                case 1:
                    addToBot(new TalkAction(this, "Please... just a few more turns... I'll win...", 0.5F, 2F));
                    addToBot(new TalkAction(this, "how will you fare... with no Relics...?", 0.5F, 2F));
                    for (AbstractRelic r : AbstractDungeon.player.relics) {
                        AbstractDungeon.actionManager.addToBottom(new AbstractGameAction() {
                            @Override
                            public void update() {
                                isDone = true;
                                AbstractDungeon.player.loseRelic(r.relicId);
                                AbstractDungeon.effectList.add(new StealRelicEffect(r, Phantasm.this));
                            }
                        });
                    }
                    break;
            }
            addToBot(new SetMoveAction(this, RETURN, Intent.UNKNOWN));
            applyPowers();
        }
    }

    @Override
    public void die(boolean triggerRelics) {
        if (!AbstractDungeon.getCurrRoom().cannotLose) {
            super.die(triggerRelics);
            this.useFastShakeAnimation(5.0F);
            CardCrawlGame.screenShake.rumble(4.0F);
            this.onBossVictoryLogic();
            this.onFinalBossVictoryLogic();
        }
    }

    @Override
    protected void getMove(int i) {
        if (Wiz.getAllCardsInCardGroups(true, false).isEmpty() && GameActionManager.turn != 1) {
            setMoveShortcut(NUKE, "Forgotten");
        } else {
            if (!player().hasPower("boiler:Forgetting")) {
                setMoveShortcut(FORGETTING, "Forgetting");
            } else {
                if (turns == 1) {
                    setMoveShortcut(POISON, "Unblockable Poison");
                    turns += 1;
                } else if (turns == 2) {
                    setMoveShortcut(MULTIHIT);
                    turns += 1;
                } else if (turns == 3) {
                    setMoveShortcut(MEGADEBUFF);
                    turns = 1;
                }
            }
        }
    }
}