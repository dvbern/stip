package ch.dvbern.stip.api.gesuch.entity;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class FamiliensituationElternEntityRequiredConstraintValidator
    implements ConstraintValidator<FamiliensituationElternEntityRequiredConstraint, GesuchFormular> {
    @Override
    public boolean isValid(
        GesuchFormular gesuchFormular,
        ConstraintValidatorContext constraintValidatorContext) {
        if (gesuchFormular.getFamiliensituation() == null) {
            return false;
        }
        if (isElternTeilRequired(ElternTyp.MUTTER, gesuchFormular.getFamiliensituation())) {
            if (gesuchFormular.getElterns()
                .stream()
                .filter(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER)
                .findAny()
                .isEmpty()) {
                return false;
            }
        } else if (gesuchFormular.getElterns()
            .stream()
            .anyMatch(eltern -> eltern.getElternTyp() == ElternTyp.MUTTER)) {
            return false;
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
