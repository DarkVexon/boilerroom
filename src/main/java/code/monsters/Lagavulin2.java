package code.monsters;

import basemod.ReflectionHacks;
import code.cards.DexDownCard;
import code.cards.StrengthDownCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.TalkAction;
import com.megacrit.cardcrawl.actions.unique.AddCardToDeckAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.MetallicizePower;
import com.megacrit.cardcrawl.powers.StrengthPower;

import static code.BoilerRoomMod.makeID;

public class Lagavulin2 extends AbstractBoilerRoomMonster {
    //Lagavulin2, STR_DOWN, DEX_DOWN, , , , 175, 181, ELITE
    public static final String NAME = Lagavulin2.class.getSimpleName();
    public static final String ID = makeID(NAME);

    private static final byte STR_DOWN = 0;
    private static final byte DEX_DOWN = 1;

    public Lagavulin2(float x, float y) {
        super(NAME, ID, 1, x, y, 100, 100, "img/foe/" + NAME + ".png");
        setHp(calcAscensionTankiness(175), calcAscensionTankiness(181));

        addMove(STR_DOWN, Intent.ATTACK_DEBUFF, calcAscensionDamage(18));
        addMove(DEX_DOWN, Intent.ATTACK_DEBUFF, calcAscensionDamage(18));
    }

    private void talk(String text) {
        TalkAction action = new TalkAction(this, text, 1.5F, 1.5F);
        ReflectionHacks.setPrivateInherited(action, AbstractGameAction.class, "duration", 1.5F);
        addToBot(action);
    }

    @Override
    public void usePreBattleAction() {
        talk("PSYCHE! You thought I was gonna SLEEP???");
        talk("I'm DONE sleeping through fights.");
        talk("That was some ACT 1 behavior!!!");
        talk("We are in ACT 3 now, this is SERIOUS!");
        talk("SO don't walk in here thinking I'm gonna SLEEP!!!");
        talk("NAH. NO HOW. I'm more WOKE than AWAKENED ONE.");
        talk("You know who's gonna be sleeping? Huh? Huh???");
        talk("YOU! After I BEAT you!");
        talk("You know that event where you loot a dead adventurer?");
        talk("That's gonna be YOU! After this fight, mark my words!");
        talk("Yeah! Someone's gonna come along and loot YOU! How's that feel?");
        talk("I thought so. I thought you were scared.");
        talk("BUT guess what? I am NOT offering mercy.");
        talk("NAH. NO HOW. NOT TODAY.");
        talk("You know how many Act 1s people play???");
        talk("\" ~Oooooh,~ I'll hit that Elite with Neow's Blessing! \"");
        talk("That's what you all sound like. It SUCKS.");
        talk("But you know what? Now it's MY time.");
        talk("You think you're gonna win?");
        talk("NAH. NO HOW. I've got some new tricks up my sleeve.");
        talk("Cuz you know what I've been doing?");
        talk("Instead of sleeping?");
        talk("I've been studying the DARK ARTS.");
        talk("Yeah, that's right...");
        talk("I'm EVIL LAGAVULIN now.");
        talk("So get ready. I mean it, like really.");
        talk("...");
        talk("...");
        talk("You ready?");
        talk("Yeah, that's right. I only gave you THAT long.");
        talk("Remember in Act 1 where I gave you some turns?");
        talk("Yeaaah some SETUP turns. You want that?");
        talk("NAH. NO HOW. NOT TODAY.");
        talk("'Cuz I'm AWAKE. WIDE AWAKE. Yeah. I was READY.");
        talk("OH YEAH. And you know what?");
        applyToSelf(new MetallicizePower(this, calcAscensionSpecial(8)));
        talk("THAT'S RIGHT. I get Metallicize too.");
        talk("YEAH. I don't have to sleep to get THAT anymore.");
        talk("Ooooh, is SOMEONE scared? It's okay to cry. Heehehe.");
        talk("OK, any last words?");
        talk("...");
        talk("...");
        talk("...");
        talk("...");
        talk("All you adventurers are so quiet and aloof. Whatever.");
        talk("Let's do this!!!");
    }

    @Override
    protected void setUpMisc() {
        super.setUpMisc();
        this.type = EnemyType.ELITE;
    }

    @Override
    public void executeTurn() {
        switch (this.nextMove) {
            case STR_DOWN:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                applyToPlayer(new StrengthPower(player(), -1));
                addToBot(new AddCardToDeckAction(new StrengthDownCard()));
                break;
            case DEX_DOWN:
                hitPlayer(AbstractGameAction.AttackEffect.BLUNT_HEAVY);
                applyToPlayer(new DexterityPower(player(), -1));
                addToBot(new AddCardToDeckAction(new DexDownCard()));
                break;
        }
    }

    @Override
    protected void getMove(int i) {
        if (lastMove(STR_DOWN)) {
            setMoveShortcut(DEX_DOWN, "EVIL Lagavulin's Dark Magic Powers Revealed: PERMANENT Dex Reduction");
        } else {
            setMoveShortcut(STR_DOWN, "EVIL Lagavulin's Dark Magic Powers Revealed: PERMANENT Strength Reduction");
        }
    }
}