package code.events;

import basemod.abstracts.events.PhasedEvent;
import basemod.abstracts.events.phases.TextPhase;

import static code.BoilerRoomMod.makeID;

public abstract class AbstractBoilerRoomEvent extends PhasedEvent {
    public AbstractBoilerRoomEvent(String letter) {
        super(makeID("FellInBoiler" + letter), "Fell In Boiler " + letter, "boilerResources/images/event/event.png");

        TextPhase.OptionInfo optionA = new TextPhase.OptionInfo(getAText());
        optionA.setOptionResult(integer -> {
            doA();
            transitionKey(1);
        });
        TextPhase.OptionInfo optionB = new TextPhase.OptionInfo(getBText());
        optionB.setOptionResult(integer -> {
            doB();
            transitionKey(2);
        });
        TextPhase.OptionInfo optionC = new TextPhase.OptionInfo(getCText());
        optionC.setOptionResult(integer -> {
            doC();
            transitionKey(3);
        });

        registerPhase(0, new TextPhase("You fell into boiler " + letter + ", oh no!!!! Ouch!!!")
                .addOption(optionA)
                .addOption(optionB)
                .addOption(optionC));
        registerPhase(1, new TextPhase(getADoneText()).addOption("Leave", (t) -> this.openMap()));
        registerPhase(2, new TextPhase(getBDoneText()).addOption("Leave", (t) -> this.openMap()));
        registerPhase(3, new TextPhase(getCDoneText()).addOption("Leave", (t) -> this.openMap()));
        transitionKey(0);
    }

    protected abstract String getADoneText();

    protected abstract String getBDoneText();

    protected abstract String getCDoneText();

    protected abstract void doA();

    protected abstract void doB();

    protected abstract void doC();

    protected abstract String getAText();

    protected abstract String getBText();

    protected abstract String getCText();
}
