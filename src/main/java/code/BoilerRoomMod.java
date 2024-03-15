package code;

import basemod.AutoAdd;
import basemod.BaseMod;
import basemod.helpers.RelicType;
import basemod.interfaces.*;
import code.cards.AbstractEasyCard;
import code.monsters.*;
import code.monsters.special.GnatMonsterGroup;
import code.monsters.special.GnatSkittlerGroup;
import code.relics.AbstractEasyRelic;
import com.badlogic.gdx.Gdx;
import com.evacipated.cardcrawl.mod.stslib.Keyword;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.TheBeyond;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.unlock.UnlockTracker;

import java.nio.charset.StandardCharsets;

@SuppressWarnings({"unused", "WeakerAccess"})
@SpireInitializer
public class BoilerRoomMod implements EditCardsSubscriber, EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, AddAudioSubscriber, PostInitializeSubscriber {

    public static final String modID = "boiler";

    public static String makeID(String idText) {
        return modID + ":" + idText;
    }

    public static Settings.GameLanguage[] SupportedLanguages = {Settings.GameLanguage.ENG};

    private String getLangString() {
        for (Settings.GameLanguage lang : SupportedLanguages) {
            if (lang.equals(Settings.language)) {
                return Settings.language.name().toLowerCase();
            }
        }
        return "eng";
    }

    public BoilerRoomMod() {
        BaseMod.subscribe(this);
    }

    public static String makePath(String resourcePath) {
        return modID + "Resources/" + resourcePath;
    }

    public static String makeImagePath(String resourcePath) {
        return modID + "Resources/images/" + resourcePath;
    }

    public static String makeRelicPath(String resourcePath) {
        return modID + "Resources/images/relics/" + resourcePath;
    }

    public static String makePowerPath(String resourcePath) {
        return modID + "Resources/images/powers/" + resourcePath;
    }

    public static String makeCharacterPath(String resourcePath) {
        return modID + "Resources/images/char/" + resourcePath;
    }

    public static String makeCardPath(String resourcePath) {
        return modID + "Resources/images/cards/" + resourcePath;
    }

    public static void initialize() {
        BoilerRoomMod thismod = new BoilerRoomMod();
    }

    @Override
    public void receiveEditRelics() {
        new AutoAdd(modID).packageFilter(AbstractEasyRelic.class).any(AbstractEasyRelic.class, (info, relic) -> {
            if (relic.color == null) {
                BaseMod.addRelic(relic, RelicType.SHARED);
            } else {
                BaseMod.addRelicToCustomPool(relic, relic.color);
            }
            if (!info.seen) {
                UnlockTracker.markRelicAsSeen(relic.relicId);
            }
        });
    }

    @Override
    public void receiveEditCards() {
        new AutoAdd(modID).packageFilter(AbstractEasyCard.class).setDefaultSeen(true).cards();
    }

    @Override
    public void receiveEditStrings() {
        BaseMod.loadCustomStringsFile(CardStrings.class, modID + "Resources/localization/" + getLangString() + "/Cardstrings.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, modID + "Resources/localization/" + getLangString() + "/Relicstrings.json");
        BaseMod.loadCustomStringsFile(PowerStrings.class, modID + "Resources/localization/" + getLangString() + "/Powerstrings.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, modID + "Resources/localization/" + getLangString() + "/UIstrings.json");
    }

    @Override
    public void receiveEditKeywords() {
        Gson gson = new Gson();
        String json = Gdx.files.internal(modID + "Resources/localization/" + getLangString() + "/Keywordstrings.json").readString(String.valueOf(StandardCharsets.UTF_8));
        com.evacipated.cardcrawl.mod.stslib.Keyword[] keywords = gson.fromJson(json, com.evacipated.cardcrawl.mod.stslib.Keyword[].class);

        if (keywords != null) {
            for (Keyword keyword : keywords) {
                BaseMod.addKeyword(modID, keyword.PROPER_NAME, keyword.NAMES, keyword.DESCRIPTION);
            }
        }
    }

    @Override
    public void receiveAddAudio() {

    }

    @Override
    public void receivePostInitialize() {
        BoilerRoom boilerRoom = new BoilerRoom();
        boilerRoom.addAct(TheBeyond.ID);
        boilerRoom.addBoss(Phantasm.ID, Phantasm::new, "boilerResources/img/map/phantasm.png", "boilerResources/img/map/phantasmOutline.png");
        boilerRoom.addBoss(Globbleglibson.ID, Globbleglibson::new, "boilerResources/img/map/globbleglibson.png", "boilerResources/img/map/globbleglibsonOutline.png");
        boilerRoom.addBoss(SlingBoy.ID, SlingBoy::new, "boilerResources/img/map/slingboy.png", "boilerResources/img/map/slingboyOutline.png");

        BaseMod.addMonster(Craig.ID, () -> new MonsterGroup(new AbstractMonster[]{new Craig(100, 100)}));
        BaseMod.addMonster("boiler:FlagbearerTwoChompers", "Flagbearer and Two Chompers", () -> new MonsterGroup(new AbstractMonster[]{
                new CardChomper(-100, 0),
                new CardChomper(0, 0),
                new Flagbearer(125, 10)
        }));
        BaseMod.addMonster("boiler:TwoPotsAndWorm", "Two Pots + Worm", () -> new MonsterGroup(new AbstractMonster[]{
                new PotThing(-100, 0),
                new PotThing(0, 0),
                new Worm(120, 10)
        }));

        BaseMod.addMonster("boiler:FiftyGnats", "Fifty Fucking Gnats", GnatMonsterGroup::new);
        BaseMod.addMonster("boiler:IncenseClockPot", "Incence, Clock, Pot", () -> new MonsterGroup(new AbstractMonster[]{
                new IncenseSkittler(-100, 0),
                new Clockling(0, 0),
                new PotThing(100, 0)
        }));
        BaseMod.addMonster("boiler:GnatsSkittler", "Gnats + Skittler", GnatSkittlerGroup::new);
        BaseMod.addMonster("boiler:FlagbearerSludgeWorm", "Flagbearer Sludge Worm", () -> new MonsterGroup(new AbstractMonster[]{
                new Flagbearer(-100, 0),
                new SludgeMixer(0, 0),
                new Worm(100, 0)
        }));
        BaseMod.addMonster(Marisa.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Marisa(0, 0)
        }));
        BaseMod.addMonster("boiler:StinkyChompCraig", "Stinky Chomp Craig", () -> new MonsterGroup(new AbstractMonster[]{
                new Stinky(-100, 0),
                new CardChomper(0, 0),
                new Craig(100, 0)
        }));
        BaseMod.addMonster("boiler:IncenseSludgeStink", "Incense Sludge Stink", () -> new MonsterGroup(new AbstractMonster[]{
                new IncenseSkittler(-100, 0),
                new SludgeMixer(0, 0),
                new Stinky(100, 0)
        }));

        BaseMod.addMonster(GumGum.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new GumGum(0, 0)
        }));

        BaseMod.addMonster(Lagavulin2.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new Lagavulin2(0, 0)
        }));

        BaseMod.addMonster(WormLegate.ID, () -> new MonsterGroup(new AbstractMonster[]{
                new WormLegate(0, 0)
        }));
    }
}
