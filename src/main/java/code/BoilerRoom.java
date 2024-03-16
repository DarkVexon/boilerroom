package code;

import actlikeit.dungeons.CustomDungeon;
import code.monsters.*;
import code.scenes.BoilerRoomScene;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterInfo;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.scenes.AbstractScene;

import java.util.ArrayList;

import static code.BoilerRoomMod.makeID;

public class BoilerRoom extends CustomDungeon {
    public static final String NAME = "BoilerRoom";
    public static final String ID = makeID(NAME);

    public BoilerRoom() {
        super("Boiler Room", ID, "boilerResources/images/ui/panel.png", true, 2, 12, 10);
    }

    public BoilerRoom(CustomDungeon cd, AbstractPlayer p, ArrayList<String> emptyList) {
        super(cd, p, emptyList);
    }

    public BoilerRoom(CustomDungeon cd, AbstractPlayer p, SaveFile saveFile) {
        super(cd, p, saveFile);
    }


    @Override
    public String getOptionText() {
        return "boiler room";
    }

    @Override
    protected void initializeLevelSpecificChances() {
        shopRoomChance = 0.05F;
        restRoomChance = 0.12F;
        treasureRoomChance = 0.0F;
        eventRoomChance = 0.22F;
        eliteRoomChance = 0.08F;
        smallChestChance = 50;
        mediumChestChance = 33;
        largeChestChance = 17;
        commonRelicChance = 50;
        uncommonRelicChance = 33;
        rareRelicChance = 17;
        colorlessRareChance = 0.3F;
        if (AbstractDungeon.ascensionLevel >= 12) {
            cardUpgradedChance = 0.25F;
        } else {
            cardUpgradedChance = 0.5F;
        }
    }

    @Override
    protected void generateWeakEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(Craig.ID, 1.0F));
        monsters.add(new MonsterInfo(makeID("FlagbearerTwoChompers"), 1.0F));
        monsters.add(new MonsterInfo(makeID("TwoPotsAndWorm"), 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateStrongEnemies(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(makeID("FiftyGnats"), 1.0F));
        monsters.add(new MonsterInfo(makeID("IncenseClockPot"), 1.0F));
        monsters.add(new MonsterInfo(makeID("GnatsSkittler"), 1.0F));
        monsters.add(new MonsterInfo(makeID("FlagbearerSludgeWorm"), 1.0F));
        monsters.add(new MonsterInfo(Marisa.ID, 1.0F));
        monsters.add(new MonsterInfo(makeID("StinkyChompCraig"), 1.0F));
        monsters.add(new MonsterInfo(makeID("IncenseSludgeStink"), 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateFirstStrongEnemy(monsters, this.generateExclusions());
        this.populateMonsterList(monsters, count, false);
    }

    @Override
    protected void generateElites(int count) {
        ArrayList<MonsterInfo> monsters = new ArrayList<>();
        monsters.add(new MonsterInfo(GumGum.ID, 1.0F));
        monsters.add(new MonsterInfo(Lagavulin2.ID, 1.0F));
        monsters.add(new MonsterInfo(WormLegate.ID, 1.0F));
        MonsterInfo.normalizeWeights(monsters);
        this.populateMonsterList(monsters, count, true);
    }

    @Override
    public AbstractScene DungeonScene() {
        return new BoilerRoomScene();
    }
}
