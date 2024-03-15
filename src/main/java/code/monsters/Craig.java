package code.monsters;

import code.actions.TimedVFXAction;
import code.util.Wiz;
import code.vfx.StealRelicEffect;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.RegenerateMonsterPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.ArrayList;

import static code.BoilerRoomMod.makeID;

public class Craig extends AbstractBoilerRoomMonster {
    //Craig, SMACK, NOMNOMNOM, BLOCK_BONK, , , 115, 125, NORMAL
    public static final String NAME = Craig.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte SMACK = 0;
    private static final byte NOMNOMNOM = 1;
    private static final byte BLOCK_BONK = 2;

    public Craig(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100, "img/foe/" + NAME + ".png");
        setHp(calcAscensionTankiness(115), calcAscensionTankiness(125));

        addMove(SMACK, Intent.ATTACK_DEBUFF, calcAscensionDamage(14));
        addMove(NOMNOMNOM, Intent.STRONG_DEBUFF);
        addMove(BLOCK_BONK, Intent.ATTACK_DEFEND, calcAscensionDamage(14));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new RegenerateMonsterPower(this, calcAscensionSpecial(8)));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case SMACK:
                hitPlayer(AbstractGameAction.AttackEffect.SMASH);
                if (AbstractDungeon.monsterRng.randomBoolean()) {
                    applyToPlayer(new WeakPower(player(), calcAscensionSpecial(2), true));
                } else {
                    applyToPlayer(new FrailPower(player(), calcAscensionSpecial(2), true));
                }
                break;
            case NOMNOMNOM:
                ArrayList<AbstractRelic> eligibleRelics = new ArrayList<>();
                for (AbstractRelic r : AbstractDungeon.player.relics) {
                    if (r.tier == AbstractRelic.RelicTier.COMMON || r.tier == AbstractRelic.RelicTier.UNCOMMON || r.tier == AbstractRelic.RelicTier.RARE) {
                        eligibleRelics.add(r);
                    }
                }
                if (eligibleRelics.isEmpty()) {
                    addToBot(new TalkAction(this, "NO ... FOOD FOR CRAIG?", 0.5F, 2.0F));
                } else {
                    AbstractRelic target = Wiz.getRandomItem(eligibleRelics, AbstractDungeon.monsterRng);
                    addToBot(new TalkAction(this, "CRAIG... WANT... " + target.name + "...", 0.5F, 2.0F));
                    AbstractDungeon.player.loseRelic(target.relicId);
                    addToBot(new TimedVFXAction(new StealRelicEffect(target, this)));
                    applyToSelf(new StrengthPower(this, (target.tier == AbstractRelic.RelicTier.RARE) ? 3 : (target.tier == AbstractRelic.RelicTier.UNCOMMON ? 2 : 1)));
                }
                break;
            case BLOCK_BONK:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                addToBot(new GainBlockAction(this, calcAscensionDamage(14)));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (lastMove(SMACK)) {
            setMoveShortcut(NOMNOMNOM);
        } else if (lastMove(NOMNOMNOM)) {
            setMoveShortcut(BLOCK_BONK);
        } else {
            setMoveShortcut(SMACK);
        }
    }
}