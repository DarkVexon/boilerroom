package code.cardmods;

import basemod.abstracts.AbstractCardModifier;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;

import static code.BoilerRoomMod.makeID;
import static thePackmaster.SpireAnniversary5Mod.makeID;

@AbstractCardModifier.SaveIgnore
public class IsSpecterModifier extends AbstractCardModifier {
    public static final String ID = makeID("IsGhostModifier");
    private static UIStrings uiStrings = CardCrawlGame.languagePack.getUIString(ID);

    public AbstractCard ghost;

    public IsSpecterModifier(AbstractCard source) {
        this.ghost = source;
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new IsSpecterModifier(ghost.makeStatEquivalentCopy()); // Might need to do stat equiv copy of the ghost too
    }
}
