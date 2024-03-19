package boiler.monsters;

import boiler.util.Wiz;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.map.MapEdge;
import com.megacrit.cardcrawl.map.MapRoomNode;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.powers.watcher.EnergyDownPower;
import com.megacrit.cardcrawl.rooms.*;

import java.lang.reflect.Method;
import java.util.ArrayList;

import static boiler.BoilerRoomMod.makeID;

public class PortalWielder extends AbstractBoilerRoomMonster {
    //PortalWielder, DEBUFFS, MAXHPDRAIN, SETBACK, , , 240, 252, ELITE
    public static final String NAME = PortalWielder.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte DEBUFFS = 0;
    private static final byte MAXHPDRAIN = 1;
    private static final byte SETBACK = 2;

    public PortalWielder(float x, float y) {
        super("Portal Wielder", ID, 1, x, y, 290, 375);
        setHp(calcAscensionTankiness(131), calcAscensionTankiness(140));

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
                            AbstractRoom room = AbstractDungeon.getCurrRoom();
                            room.rewardAllowed = false;
                            AbstractDungeon.getCurrRoom().endBattle();
                            MapRoomNode toTravelTo = Wiz.getRandomItem(valids, AbstractDungeon.monsterRng);
                            for (ArrayList<MapRoomNode> r : AbstractDungeon.map) {
                                for (MapRoomNode n : r) {
                                    if (n != toTravelTo && n.y >= toTravelTo.y && n.taken) {
                                        n.taken = false;
                                        for (MapEdge e : n.getEdges()) {
                                            e.taken = false;
                                            e.color = new Color(0.34F, 0.34F, 0.34F, 1.0F);
                                        }
                                        if (n.room instanceof MonsterRoomElite) {
                                            n.room = new MonsterRoomElite();
                                        } else if (n.room instanceof ShopRoom) {
                                            n.room = new ShopRoom();
                                        } else if (n.room instanceof MonsterRoom) {
                                            n.room = new MonsterRoom();
                                        } else if (n.room instanceof EventRoom) {
                                            n.room = new EventRoom();
                                        } else if (n.room instanceof TreasureRoom) {
                                            n.room = new MonsterRoom();
                                        } else if (n.room instanceof RestRoom) {
                                            n.room = new MonsterRoom();
                                        } else {
                                            try {
                                                n.room = n.room.getClass().newInstance();
                                            } catch (Exception e) {
                                                n.room = new MonsterRoom();
                                            }
                                        }
                                    }
                                }
                            }
                            MapRoomNode prevNode = AbstractDungeon.currMapNode;
                            prevNode.taken = false;
                            AbstractDungeon.currMapNode = toTravelTo;
                            AbstractDungeon.nextRoom = toTravelTo;
                            toTravelTo.taken = true;
                            AbstractDungeon.currMapNode.room = room;
                            AbstractDungeon.pathX.clear();
                            AbstractDungeon.pathY.clear();
                            AbstractDungeon.pathX.add(toTravelTo.x);
                            AbstractDungeon.pathY.add(toTravelTo.y);
                            prevNode.room = new MonsterRoomElite();

                            if (AbstractDungeon.monsterList.size() <= 1 && CardCrawlGame.dungeon != null) {
                                try {
                                    Method m = AbstractDungeon.class.getDeclaredMethod("generateStrongEnemies", int.class);

                                    m.setAccessible(true);

                                    m.invoke(CardCrawlGame.dungeon, 12);

                                    if (AbstractDungeon.monsterList.size() == 1) //this did nothing. Add a copy.
                                    {
                                        String theOnlyMonster = "" + AbstractDungeon.monsterList.get(0);
                                        AbstractDungeon.monsterList.add(theOnlyMonster);
                                    }
                                } catch (Exception e) {

                                }
                            }
                        } else {
                            addToTop(new ApplyPowerAction(PortalWielder.this, PortalWielder.this, new StrengthPower(PortalWielder.this, 10), 10));
                            addToTop(new TalkAction(PortalWielder.this, "You're too close to the floor to portal you... I'll just gain Strength.", 2.0F, 2.0F));
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