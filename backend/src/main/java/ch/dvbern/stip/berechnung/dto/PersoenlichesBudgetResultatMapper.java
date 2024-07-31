package ch.dvbern.stip.berechnung.dto;

import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;

public interface PersoenlichesBudgetResultatMapper {
    PersoenlichesBudgetresultatDto mapFromRequest(
        final DmnRequest request,
        final int einnahmenPersoenlichesBudget,
        final int ausgabenPersoenlichesBudget,
        final int persoenlichesbudgetBerechnet
    );
}
