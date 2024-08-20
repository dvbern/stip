package ch.dvbern.stip.api.gesuch.entity;

import java.util.Set;

import ch.dvbern.stip.api.eltern.entity.Eltern;
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
            return true;
        }

        final var mutterValid = isElternTeilRequiredAndVorhanden(ElternTyp.MUTTER, gesuchFormular, constraintValidatorContext);
        final var vaterValid = isElternTeilRequiredAndVorhanden(ElternTyp.VATER, gesuchFormular, constraintValidatorContext);

        return mutterValid && vaterValid;
    }

    private boolean isElternTeilRequiredAndVorhanden(
        final ElternTyp elternTyp,
        final GesuchFormular gesuchFormular,
        final ConstraintValidatorContext constraintValidatorContext
    ) {
        var isValid = true;
        if (isElternTeilRequired(elternTyp, gesuchFormular.getFamiliensituation())) {
            if (!isElternTeilVorhanden(elternTyp, gesuchFormular.getElterns())) {
                isValid = GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
        } else {
            if (isElternTeilVorhanden(elternTyp, gesuchFormular.getElterns())) {
                isValid = GesuchValidatorUtil.addProperty(constraintValidatorContext, property);
            }
        }

        return isValid;
    }

    private boolean isElternTeilVorhanden(final ElternTyp elternTyp, final Set<Eltern> eltern) {
        return eltern.stream().anyMatch(elternTeil -> elternTeil.getElternTyp() == elternTyp);
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
