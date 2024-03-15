package code.cards;

import code.actions.RepeatCardAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static code.BoilerRoomMod.makeID;

public class PottedCard extends AbstractEasyCard {
    public static String ID = makeID(PottedCard.class.getSimpleName());

    private AbstractCard held;

    public PottedCard() {
        super(ID, -2, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
    }

    public PottedCard(AbstractCard input) {
        super(ID, input.cost, CardType.SKILL, CardRarity.SPECIAL, CardTarget.NONE, CardColor.COLORLESS);
        this.held = input;
        rawDescription = "Play the " + input.cost + " cost card in this Pot.";
        initializeDescription();
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
