package code.monsters;

import code.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.WeakPower;

import static code.BoilerRoomMod.makeID;
import static code.monsters.PotThing.potCard;

public class SlingBoy extends AbstractBoilerRoomMonster {
    //SlingBoy, ATTACK, DEBUFF, , , , 200, 222, BOSS
    public static final String NAME = SlingBoy.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte ATTACK = 0;
    private static final byte DEBUFF = 1;

    public SlingBoy(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100, "img/foe/" + NAME + ".png");
        setHp(calcAscensionTankiness(200), calcAscensionTankiness(222));

        addMove(ATTACK, Intent.ATTACK, calcAscensionDamage(7));
        addMove(DEBUFF, Intent.DEBUFF);
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new ArtifactPower(this, 150));
        applyToSelf(new MetallicizePower(this, calcAscensionSpecial(25)));
        for (AbstractCard c : Wiz.getAllCardsInCardGroups(true, false)) {
            potCard(c);
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
}