package ch.dvbern.stip.api.partner.entity;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraintValidator
    implements ConstraintValidator<AusbildungMitEinkommenOderErwerbstaetigRequiredFieldsConstraint, Partner> {
    @Override
    public boolean isValid(Partner partner, ConstraintValidatorContext constraintValidatorContext) {
        if (partner.isAusbildungMitEinkommenOderErwerbstaetig()) {
            return partner.getFahrkosten() != null
                && partner.getJahreseinkommen() != null
                && partner.getVerpflegungskosten() != null;
        }
        return true;
    }
}
