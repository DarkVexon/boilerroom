package code.monsters;

import basemod.abstracts.CustomMonster;
import code.util.Wiz;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RollMoveAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractBoilerRoomMonster extends CustomMonster {
    protected Map<Byte, EnemyMoveInfo> moves;
    protected boolean firstMove = true;
    protected int turns = 1;
    private static final float ASCENSION_DAMAGE_BUFF_PERCENT = 1.10f;
    private static final float ASCENSION_TANK_BUFF_PERCENT = 1.10f;
    private static final float ASCENSION_SPECIAL_BUFF_PERCENT = 1.5f;

    public AbstractBoilerRoomMonster(String name, String id, int maxHealth, float hb_x, float hb_y, float hb_w, float hb_h) {
        super(name, id, maxHealth, 0, 0, hb_w, hb_h, "boilerResources/images/foe/" + name + ".png", hb_x, hb_y);
        setUpMisc();
    }

    protected void setUpMisc() {
        moves = new HashMap<>();

        this.dialogX = (this.hb_x - 70.0F) * Settings.scale;
        this.dialogY -= (this.hb_y - 55.0F) * Settings.scale;
    }

    protected void addMove(byte moveCode, Intent intent) {
        this.addMove(moveCode, intent, -1);
    }

    protected void addMove(byte moveCode, Intent intent, int baseDamage) {
        this.addMove(moveCode, intent, baseDamage, 0, false);
    }

    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier) {
        this.addMove(moveCode, intent, baseDamage, multiplier, multiplier > 0);
    }

    protected void addMove(byte moveCode, Intent intent, int baseDamage, int multiplier, boolean isMultiDamage) {
        this.moves.put(moveCode, new EnemyMoveInfo(moveCode, intent, baseDamage, multiplier, isMultiDamage));
    }

    public void setMoveShortcut(byte next, String text) {
        EnemyMoveInfo info = this.moves.get(next);
        this.setMove(text, next, info.intent, info.baseDamage, info.multiplier, info.isMultiDamage);
    }

    public void setMoveShortcut(byte next) {
        this.setMoveShortcut(next, null);
    }

    protected int calcAscensionDamage(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 4) {
                    base *= ASCENSION_DAMAGE_BUFF_PERCENT;
                }
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 3) {
                    base *= ASCENSION_DAMAGE_BUFF_PERCENT;
                }
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 2) {
                    base *= ASCENSION_DAMAGE_BUFF_PERCENT;
                }
                break;
        }
        return Math.round(base);
    }

    protected int calcAscensionTankiness(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 9) {
                    base *= ASCENSION_TANK_BUFF_PERCENT;
                }
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 8) {
                    base *= ASCENSION_TANK_BUFF_PERCENT;
                }
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 7) {
                    base *= ASCENSION_TANK_BUFF_PERCENT;
                }
                break;
        }
        return Math.round(base);
    }

    protected int calcAscensionSpecial(float base) {
        switch (this.type) {
            case BOSS:
                if (AbstractDungeon.ascensionLevel >= 19) {
                    base *= ASCENSION_SPECIAL_BUFF_PERCENT;
                }
                break;
            case ELITE:
                if (AbstractDungeon.ascensionLevel >= 18) {
                    base *= ASCENSION_SPECIAL_BUFF_PERCENT;
                }
                break;
            case NORMAL:
                if (AbstractDungeon.ascensionLevel >= 17) {
                    base *= ASCENSION_SPECIAL_BUFF_PERCENT;
                }
                break;
        }
        return Math.round(base);
    }

    @Override
    public void die(boolean triggerRelics) {
        // Makes the monster shake when it dies
        this.useShakeAnimation(5.0F);
        super.die(triggerRelics);
    }

    protected DamageInfo info;
    protected int multiplier;

    @Override
    public void takeTurn() {
        info = new DamageInfo(this, this.moves.get(nextMove).baseDamage, DamageInfo.DamageType.NORMAL);
        multiplier = this.moves.get(nextMove).multiplier;

        if (info.base > -1) {
            info.applyPowers(this, AbstractDungeon.player);
        }

        executeTurn();

        addToBot(new RollMoveAction(this));
    }

    protected void hitPlayer(AbstractGameAction.AttackEffect effect) {
        useFastAttackAnimation();
        for (int i = 0; i < Math.max(multiplier, 1); i++) {
            addToBot(new DamageAction(AbstractDungeon.player, info, effect));
        }
    }

    protected static AbstractMonster randomAlly() {
        return Wiz.getRandomItem(Wiz.getEnemies());
    }

    protected AbstractPlayer player() {
        return AbstractDungeon.player;
    }

    protected void applyToPlayer(AbstractPower p) {
        addToBot(new ApplyPowerAction(player(), this, p, p.amount));
    }

    protected void applyToSelf(AbstractPower p) {
        addToBot(new ApplyPowerAction(this, this, p, p.amount));
    }

    protected void applyToAlly(AbstractMonster m, AbstractPower p) {
        addToBot(new ApplyPowerAction(m, this, p, p.amount));
    }

    public abstract void executeTurn();
}
