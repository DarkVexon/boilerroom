package code.monsters;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.SpawnMonsterAction;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.MalleablePower;

import static code.BoilerRoomMod.makeID;

public class WormLegate extends AbstractBoilerRoomMonster {
    //WormLegate, SUMMON_WORMS, SUMMON_WORM, , , , 250, 270, ELITE
    public static final String NAME = WormLegate.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte SUMMON_WORMS = 0;
    private static final byte SUMMON_WORM = 1;

    private static final float[] POSX = new float[]{210.0F, -220.0F, 180.0F, -250.0F};
    private static final float[] POSY = new float[]{75.0F, 115.0F, 345.0F, 335.0F};
    private AbstractMonster[] worms;

    public WormLegate(float x, float y) {
        super(NAME, ID, 1, x, y, 180, 190);
        setHp(calcAscensionTankiness(250), calcAscensionTankiness(261));

        addMove(SUMMON_WORMS, Intent.UNKNOWN);
        addMove(SUMMON_WORM, Intent.ATTACK_BUFF, calcAscensionDamage(14));
    }

    @Override
    public void usePreBattleAction() {
        applyToSelf(new MalleablePower(this, calcAscensionSpecial(4)));
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case SUMMON_WORMS:
                for (int i = 0; i < 2; i++) {
                    Worm newWorm = new Worm(POSX[i], POSY[i]);
                    worms[i] = newWorm;
                    addToBot(new SpawnMonsterAction(newWorm, false));
                }
                break;
            case SUMMON_WORM:
                hitPlayer(AbstractGameAction.AttackEffect.SLASH_VERTICAL);
                int i = 0;
                while (i < worms.length) {
                    if (worms[i] == null || worms[i].isDeadOrEscaped()) {
                        Worm newWorm = new Worm(POSX[i], POSY[i]);
                        worms[i] = newWorm;
                        addToBot(new SpawnMonsterAction(newWorm, false));
                        break;
                    } else {
                        i++;
                    }
                }
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (firstMove) {
            firstMove = false;
            setMoveShortcut(SUMMON_WORMS, "SUMMON WORMS!!!");
        }else {
            setMoveShortcut(SUMMON_WORM, "TRANSMOGRIFY FLESH INTO WORMS!!!");
        }
    }
}