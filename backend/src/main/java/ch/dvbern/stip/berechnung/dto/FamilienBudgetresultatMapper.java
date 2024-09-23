package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;

public interface FamilienBudgetresultatMapper {
    FamilienBudgetresultatDto mapFromRequest(
        final DmnRequest request,
        final SteuerdatenTyp steuerdatenTyp,
        final int budgetToUse,
        final int einnahmenFamilienbudget,
        final int ausgabenFamilienbudget,
        final int familienbudgetBerechnet
    );
}
