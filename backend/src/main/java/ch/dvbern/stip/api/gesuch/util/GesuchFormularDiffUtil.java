package ch.dvbern.stip.api.gesuch.util;

import java.util.Objects;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.common.util.NullDiffUtil;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class GesuchFormularDiffUtil {


    /**
     * Checks if the Alimente Aufteilung has changed to both parents in the GesuchFormular.
     *
     * @param toUpdate the original GesuchFormular object
     * @param update   the updated GesuchFormular object
     * @return true if the Alimente Aufteilung has changed to both parents, false otherwise
     */
    public boolean hasAlimenteAufteilungChangedToBoth(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (toUpdate.getFamiliensituation() == null || update.getFamiliensituation() == null) {
            return false;
        }

        return toUpdate.getFamiliensituation().getWerZahltAlimente() != Elternschaftsteilung.GEMEINSAM &&
                update.getFamiliensituation().getWerZahltAlimente() == Elternschaftsteilung.GEMEINSAM;
    }


    /**
     * Checks if the Geburtsdatum (date of birth) of the PersonInAusbildung (person in education) in the GesuchFormular (application form) has changed.
     *
     * @param toUpdate the original GesuchFormular object
     * @param update   the updated GesuchFormular object
     * @return true if the Geburtsdatum of the PersonInAusbildung has changed, false otherwise
     */
    public static boolean hasGeburtsdatumOfPersonInAusbildungChanged(
            GesuchFormular toUpdate,
            GesuchFormularUpdateDto update) {
        if (toUpdate.getPersonInAusbildung() == null
                || toUpdate.getPersonInAusbildung().getGeburtsdatum() == null
                || update.getPersonInAusbildung() == null) {
            return false;
        }

        return !toUpdate.getPersonInAusbildung()
                .getGeburtsdatum()
                .equals(update.getPersonInAusbildung().getGeburtsdatum());
    }

    /**
     * Checks if the Zivilstand (marital status) of a GesuchFormular has changed to one person.
     *
     * @param toUpdate the original GesuchFormular object
     * @param update   the updated GesuchFormular object
     * @return true if the Zivilstand has changed to one person, false otherwise
     */
    public static boolean hasZivilstandChangedToOnePerson(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (toUpdate.getPersonInAusbildung() == null
                || toUpdate.getPersonInAusbildung().getZivilstand() == null
                || update.getPersonInAusbildung() == null
                || update.getPersonInAusbildung().getZivilstand() == null) {
            return false;
        }

        return toUpdate.getPersonInAusbildung().getZivilstand().hasPartnerschaft() &&
                !update.getPersonInAusbildung().getZivilstand().hasPartnerschaft();
    }

    /**
     * Checks if the update to the "Eigener Haushalt" (own household) status has been made in the GesuchFormularUpdateDto object.
     *
     * @param update the GesuchFormularUpdateDto object to check for the "Eigener Haushalt" update
     * @return true if the update to "Eigener Haushalt" has been made, false otherwise
     */
    public static boolean isUpdateToEigenerHaushalt(GesuchFormularUpdateDto update) {
        if (update.getPersonInAusbildung() == null) {
            return false;
        }

        return update.getPersonInAusbildung().getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT;
    }



    /**
     * Checks if the Elternteil (parent) is marked as verstorben (deceased) or unknown in the given GesuchFormularUpdateDto object.
     *
     * @param update the GesuchFormularUpdateDto object to check for the Elternteil status
     * @return true if the Elternteil is marked as verstorben or unknown, false otherwise
     */
    public boolean isElternteilVerstorbenOrUnbekannt(GesuchFormularUpdateDto update) {
        return update.getFamiliensituation() != null
                && Boolean.TRUE.equals(update.getFamiliensituation().getElternteilUnbekanntVerstorben());
    }

    /**
     * Checks if the "Gerichtliche Alimentenregelung" flag has changed in the "Familiensituation" of the given updated "GesuchFormular".
     *
     * @param toUpdate the original GesuchFormular object
     * @param update   the updated GesuchFormular object
     * @return true if the "Gerichtliche Alimentenregelung" flag has changed, false otherwise
     */
    public boolean hasGerichtlicheAlimenteregelungChanged(GesuchFormular toUpdate, GesuchFormularUpdateDto update) {
        if (update.getFamiliensituation() == null) {
            return false;
        }

        if (toUpdate.getFamiliensituation() == null) {
            return update.getFamiliensituation().getGerichtlicheAlimentenregelung() != null;
        }

        if (toUpdate.getFamiliensituation().getGerichtlicheAlimentenregelung() == null) {
            return update.getFamiliensituation().getGerichtlicheAlimentenregelung() != null;
        }

        return !toUpdate.getFamiliensituation().getGerichtlicheAlimentenregelung()
                .equals(update.getFamiliensituation().getGerichtlicheAlimentenregelung());
    }

    public boolean hasWerZahltAlimenteChanged(GesuchFormularUpdateDto newFormular, GesuchFormular toUpdate) {
        if (NullDiffUtil.hasNullChanged(newFormular.getFamiliensituation(), toUpdate.getFamiliensituation())) {
            return true;
        }

        if (newFormular.getFamiliensituation() == null || toUpdate.getFamiliensituation() == null) {
            return false;
        }

		return newFormular.getFamiliensituation().getWerZahltAlimente() != toUpdate.getFamiliensituation()
            .getWerZahltAlimente();
	}

    public boolean hasElternteilVerstorbenOrUnbekanntChanged(
        GesuchFormularUpdateDto newFormular,
        GesuchFormular toUpdate
    ) {
        if (NullDiffUtil.hasNullChanged(newFormular.getFamiliensituation(), toUpdate.getFamiliensituation())) {
            return true;
        }

        if (newFormular.getFamiliensituation() == null ||
            toUpdate.getFamiliensituation() == null) {
            return false;
        }

        // Boxed !=
        return !Objects.equals(newFormular.getFamiliensituation().getElternteilUnbekanntVerstorben(),
            toUpdate.getFamiliensituation().getElternteilUnbekanntVerstorben());
    }

    public boolean hasWohnsitzChanged(
        GesuchFormularUpdateDto newFormular,
        GesuchFormular toUpdate
    ) {
        if (NullDiffUtil.hasNullChanged(newFormular.getPersonInAusbildung(), toUpdate.getPersonInAusbildung())) {
            return true;
        }

        if (newFormular.getPersonInAusbildung() == null ||
            toUpdate.getPersonInAusbildung() == null) {
            return false;
        }

        return newFormular.getPersonInAusbildung().getWohnsitz() != toUpdate.getPersonInAusbildung().getWohnsitz();
    }
}
