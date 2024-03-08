package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.util.GesuchValidatorUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationElternEntityRequiredConstraintValidator
    implements ConstraintValidator<FamiliensituationElternEntityRequiredConstraint, GesuchFormular> {
    private String property = "";

    @Override
    public void initialize(FamiliensituationElternEntityRequiredConstraint constraintAnnotation) {
        property = constraintAnnotation.property();
    }

    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getFamiliensituation() == null) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        if (isElternTeilRequired(ElternTyp.MUTTER, gesuchFormular.getFamiliensituation())) {
            if (gesuchFormular.getElterns()
                .stream()
                .filter(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER)
                .findAny()
                .isEmpty()) {
                return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
        } else if (gesuchFormular.getElterns()
            .stream()
            .anyMatch(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER)) {
            return GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
        }
        if (isElternTeilRequired(ElternTyp.VATER, gesuchFormular.getFamiliensituation())) {
            return gesuchFormular.getElterns()
                .stream()
                .anyMatch(eltern -> eltern.getElternTyp() == ElternTyp.VATER);
        } else {
            return gesuchFormular.getElterns()
                .stream()
                .filter(eltern -> eltern.getElternTyp() == ElternTyp.VATER)
                .findAny()
                .isEmpty();
        }
    }

    private boolean isElternTeilRequired(ElternTyp elternTyp, Familiensituation familiensituation) {
        boolean elternteilLebt = true;
        if (familiensituation.getElternteilUnbekanntVerstorben() != null
            && familiensituation.getElternteilUnbekanntVerstorben()) {
            elternteilLebt = elternTyp == ElternTyp.VATER ?
                familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH
                : familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH;
        }
        boolean elternteilKeineAlimente =
            (familiensituation.getWerZahltAlimente() != Elternschaftsteilung.VATER || elternTyp != ElternTyp.VATER)
                && (familiensituation.getWerZahltAlimente() != Elternschaftsteilung.MUTTER
                || elternTyp != ElternTyp.MUTTER)
                && familiensituation.getWerZahltAlimente() != Elternschaftsteilung.GEMEINSAM;
        return elternteilLebt && elternteilKeineAlimente;
    }
}
