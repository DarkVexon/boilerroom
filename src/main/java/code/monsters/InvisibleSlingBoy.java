package code.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static code.BoilerRoomMod.makeID;

public class InvisibleSlingBoy extends AbstractBoilerRoomMonster {
    //SlingBoy, ATTACK, DEBUFF, , , , 200, 222, BOSS
    public static final String NAME = InvisibleSlingBoy.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;

    public InvisibleSlingBoy(float x, float y) {
        super(NAME, ID, 1, x, y, 1, 1, "img/foe/" + NAME + ".png");
        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(7));
        addMove(DEBUFF, Intent.DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        halfDead = true;
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.NORMAL;
    }

    @Override
    public void takeTurn() {
        halfDead = false;
        info = new DamageInfo(this, this.moves.get(nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        multiplier = this.moves.get(nextMove).multiplier;

        if (info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        executeTurn();

        addToBot(new RollMoveAction(this));
        addToBot(new AbstractGameAction() {
            @Override
            public void update() {
                isDone = true;
                InvisibleSlingBoy.this.halfDead = true;
            }
        });
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
    public void damage(DamageInfo info) {

    }

    @Override
    public void die(boolean triggerRelics) {

    }

    @Override
    protected void getMove(int i) {
        if (lastTwoMoves(ATTACK)) {
            setMoveShortcut(DEBUFF);
        } else {
            setMoveShortcut(ATTACK);
        }
    }
}