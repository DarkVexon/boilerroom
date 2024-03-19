package boiler.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import static boiler.BoilerRoomMod.makeID;

public class WormCard extends AbstractEasyCard {
    public static String ID = makeID(WormCard.class.getSimpleName());

    public WormCard() {
        super(ID, 1, CardType.CURSE, CardRarity.CURSE, CardTarget.NONE, CardColor.CURSE);
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
        addToBot(new DrawCardAction(1));
    }
}
