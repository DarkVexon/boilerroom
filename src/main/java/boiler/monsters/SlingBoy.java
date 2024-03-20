package boiler.monsters;

import boiler.cards.PottedCard;
import boiler.powers.LambdaPower;
import boiler.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.*;

import static boiler.BoilerRoomMod.makeID;
import static boiler.monsters.PotThing.potCard;

public class SlingBoy extends AbstractBoilerRoomMonster {
    //SlingBoy, ATTACK, DEBUFF, , , , 200, 222, BOSS
    public static final String NAME = SlingBoy.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;

    public SlingBoy() {
        super("Sling Boy", ID, 1, 0, 0, 200, 200);
        setHp(calcAscensionTankiness(222), calcAscensionTankiness(222));

        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(6));
        addMove(DEBUFF, Intent.DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        CardCrawlGame.music.unsilenceBGM();
        AbstractDungeon.scene.fadeOutAmbiance();
        AbstractDungeon.getCurrRoom().playBgmInstantly("boiler_boss");
        addToBot(new TalkAction(this, "pshh-vrttt... ERROR! FOE HAS APPROACHED!", 0.5F, 2.0F));
        applyToSelf(new ArtifactPower(this, calcAscensionSpecial(12)));
        applyToSelf(new MetallicizePower(this, calcAscensionSpecial(15)));
        applyToSelf(new PlatedArmorPower(this, calcAscensionSpecial(20)));
        applyToSelf(new ThornsPower(this, 1));
        applyToSelf(new LambdaPower("No Fun Allowed", AbstractPower.PowerType.BUFF, false, this, -1) {
            @Override
            protected boolean canGoNegative() {
                return false;
            }

            @Override
            public void updateDescription() {
                description = "You can't draw cards or gain Energy during your turn.";
            }
        });
        addToBot(new GainBlockAction(this, calcAscensionSpecial(35)));
        applyToSelf(new BarricadePower(this));
        addToBot(new TalkAction(this, "RUNNING PROCESS: POT_CARDS...", 0.5F, 2.0F));
        for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, false)) {
            if (!(c instanceof PottedCard) && c.cost > -1)
                potCard(c);
        }
        addToBot(new TalkAction(this, "RUNNING PROCESS: ADD_SCALING...", 0.5F, 2.0F));
        applyToSelf(new GenericStrengthUpPower(this, "Scaling is Win", calcAscensionSpecial(3)));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.BOSS;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case ATTACK:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_LIGHT);
                break;
            case DEBUFF:
                if (AbstractDungeon.monsterRng.randomBoolean()) {
                    applyToPlayer(new WeakPower(player(), 1, true));
                } else {
                    applyToPlayer(new FrailPower(player(), 1, true));
                }
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (lastTwoMoves(ATTACK)) {
            setMoveShortcut(DEBUFF);
        } else {
            setMoveShortcut(ATTACK);
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