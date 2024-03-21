package boiler.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.CombatPhase;
import basemod.abstracts.events.phases.TextPhase;
import boiler.monsters.Shedinja;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;

import static boiler.BoilerRoomMod.makeID;

public class BoilerShedinja extends PhasedEvent {
    public static final String ID = makeID("BoilerShedinja");

    public BoilerShedinja() {
        super(ID, "a lone bug in between the boilers", "boilerResources/images/event/event.png");
        registerPhase(0, new TextPhase("in the distance, you see a lone bug training between the boilers. as you approach it, it turns around and flies at you with a mighty battle cry").
                addOption(FontHelper.colorString("fight for your life", "r"), (i)->{
                    AbstractDungeon.getCurrRoom().eliteTrigger = true;
                    transitionKey("Fight");
                }));

        registerPhase("Fight", new CombatPhase(Shedinja.ID)
                .addRewards(true, (room)-> {
                    room.addRelicToRewards(AbstractDungeon.returnRandomRelicTier());
                    room.addGoldToRewards(100);
                })
                .setNextKey("Victory"));

        registerPhase("Victory", new TextPhase("you have successfully managed to not die").addOption("leave", (t)->this.openMap()));
        transitionKey(0);
    }
}