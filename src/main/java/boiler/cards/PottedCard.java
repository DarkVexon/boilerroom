package boiler.cards;

import boiler.actions.RepeatCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.colorless.Madness;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static boiler.BoilerRoomMod.makeID;

public class PottedCard extends AbstractEasyCard {
    public static String ID = makeID(PottedCard.class.getSimpleName());

    private AbstractCard held;

    public PottedCard() {
        super(ID, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    }

    public PottedCard(AbstractCard input) {
        super(ID, input == null ? 0 : input.cost, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        if (input == null) {
            this.held = new Madness();
            rawDescription = "Congratulations, you created a glitched Pot. NL Play Madness. NL Exhaust.";
            this.exhaust = true;
            initializeDescription();
        } else {
            this.held = input;
            rawDescription = "Play the " + input.cost + " cost card in this Pot.";
            if (input.type == CardType.POWER || input.exhaust) {
                this.exhaust = true;
                rawDescription = rawDescription + " NL Exhaust.";
            }
            initializeDescription();
        }
    }

    @Override
    public boolean canUpgrade() {
        return false;
    }

    @Override
    public void upp() {
    }

    @Override
    public void use(AbstractPlayer abstractPlayer, AbstractMonster abstractMonster) {
        addToBot(new RepeatCardAction(held));
    }

    @Override
    public AbstractCard makeCopy() {
        return new PottedCard(held);
    }
}
