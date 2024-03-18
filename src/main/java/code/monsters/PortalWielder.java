package code.monsters;

import code.util.Wiz;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.vfx.FadeWipeParticle;

import java.util.ArrayList;

import static code.BoilerRoomMod.makeID;

public class PortalWielder extends AbstractBoilerRoomMonster {
    //PortalWielder, DEBUFFS, MAXHPDRAIN, SETBACK, , , 240, 252, ELITE
    public static final String NAME = PortalWielder.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte DEBUFFS = 0;
    private static final byte MAXHPDRAIN = 1;
    private static final byte SETBACK = 2;

    public PortalWielder(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100);
        setHp(calcAscensionTankiness(240), calcAscensionTankiness(252));

        addMove(DEBUFFS, Intent.DEBUFF);
        addMove(MAXHPDRAIN, Intent.ATTACK_DEBUFF, calcAscensionDamage(10));
        addMove(SETBACK, Intent.UNKNOWN);
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case DEBUFFS:
                applyToPlayer(new WeakPower(player(), calcAscensionSpecial(1), true));
                applyToPlayer(new VulnerablePower(player(), calcAscensionSpecial(1), true));
                applyToPlayer(new FrailPower(player(), calcAscensionSpecial(1), true));
                applyToPlayer(new EnergyDownPower(player(), 1, true));
                break;
            case MAXHPDRAIN:
                hitPlayer(AbstractGameAction.AttackEffect.FIRE);
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        AbstractDungeon.player.decreaseMaxHealth(calcAscensionDamage(10));
                    }
                });
                break;
            case SETBACK:
                addToBot(new TalkAction(this, "~Goodbye...~", 2.0F, 2.0F));
                addToBot(new AbstractGameAction() {
                    @Override
                    public void update() {
                        isDone = true;
                        int targetHeight;
                        if (AbstractDungeon.getCurrMapNode().y > 5) {
                            targetHeight = AbstractDungeon.getCurrMapNode().y - 4;
                        } else {
                            targetHeight = 0;
                        }
                        ArrayList<MapRoomNode> valids = new ArrayList<>();
                        while (valids.isEmpty() && targetHeight >= 0) {
                            for (ArrayList<MapRoomNode> r : AbstractDungeon.map) {
                                for (MapRoomNode n : r) {
                                    if (n.y == targetHeight) {
                                        valids.add(n);
                                    }
                                }
                            }
                            targetHeight -= 1;
                        }
                        if (!valids.isEmpty()) {
                            AbstractDungeon.getCurrRoom().endBattle();
                            MapRoomNode toTravelTo = Wiz.getRandomItem(valids, AbstractDungeon.monsterRng);
                            playMapNodeSelectedSound();
                            AbstractDungeon.dungeonMapScreen.clicked = false;
                            AbstractDungeon.dungeonMapScreen.clickTimer = 0.0F;
                            AbstractDungeon.topLevelEffects.add(new FadeWipeParticle());
                            AbstractDungeon.setCurrMapNode(toTravelTo);
                            AbstractDungeon.nextRoom = toTravelTo;
                            AbstractDungeon.nextRoomTransitionStart();
                            CardCrawlGame.music.fadeOutTempBGM();
                        }
                    }
                });
                break;
        }
    }

    private void playMapNodeSelectedSound() {
        int roll = MathUtils.random(3);
        switch (roll) {
            case 0:
                CardCrawlGame.sound.play("MAP_SELECT_1");
                break;
            case 1:
                CardCrawlGame.sound.play("MAP_SELECT_2");
                break;
            case 2:
                CardCrawlGame.sound.play("MAP_SELECT_3");
                break;
            default:
                CardCrawlGame.sound.play("MAP_SELECT_4");
        }
    }

    @Override
    protected void getMove(int i) {
        if (firstMove) {
            firstMove = false;
            setMoveShortcut(DEBUFFS);
        } else {
            if (lastTwoMoves(MAXHPDRAIN)) {
                setMoveShortcut(SETBACK);
            } else {
                setMoveShortcut(MAXHPDRAIN, "Max-HP Burn");
            }
        }
    }
}