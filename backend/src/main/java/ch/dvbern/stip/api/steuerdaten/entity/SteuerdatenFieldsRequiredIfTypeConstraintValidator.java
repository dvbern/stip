package ch.dvbern.stip.api.steuerdaten.entity;

import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class SteuerdatenFieldsRequiredIfTypeConstraintValidator
    implements ConstraintValidator<SteuerdatenFieldsRequiredIfTypeConstraint, Steuerdaten> {

    @Override
    public boolean isValid(Steuerdaten steuerdaten, ConstraintValidatorContext context) {
        if (steuerdaten == null) {
            return true;
        }

        if (steuerdaten.getSteuerdatenTyp() == SteuerdatenTyp.FAMILIE) {
            final var isValid = steuerdaten.getSozialhilfebeitraegePartner() != null &&
                steuerdaten.getErgaenzungsleistungenPartner() != null;

            if (!isValid) {
                return GesuchValidatorUtil.addProperty(context, "steuerdaten");
            }
        }

        return true;
    }
}
