package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.berechnung.dto.BerechnungRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

@Value
public class BerechnungRequestV1 implements BerechnungRequest {
    @JsonProperty("Stammdaten_V1")
    StammdatenV1 stammdaten;

    @JsonProperty("InputFamilienBudget_1_V1")
    InputFamilienBudget_V1 inputFamilienBudget_1_V1;

    @JsonProperty("InputFamilienBudget_2_V1")
    InputFamilienBudget_V1 inputFamilienBudget_2_V1;

    @AllArgsConstructor
    public static class InputFamilienBudget_V1 {
        @JsonProperty
        ElternteilV1 elternteil;
    }

    @JsonProperty("InputPersoenlichesbudget_V1")
    InputPersoenlichesbudget_V1 inputPersoenlichesbudget_V1;

    @AllArgsConstructor
    public static class InputPersoenlichesbudget_V1 {
        @JsonProperty
        AntragsstellerV1 antragssteller;
    }

    public static BerechnungRequestV1 createRequest(final Gesuch gesuch, final UUID tranchenId) {
        final var elterteil1 = ElternteilV1.withDefaults();
        final var elterteil2 = ElternteilV1.withDefaults();
        final var gesuchTranche = gesuch.getGesuchTrancheById(tranchenId);
        var antragssteller = AntragsstellerV1.withDefaults();
        if (gesuchTranche.isPresent()) {
            antragssteller = AntragsstellerV1.fromGesuchFormular(gesuchTranche.get().getGesuchFormular());
        }
        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode()),
            new InputFamilienBudget_V1(elterteil1),
            new InputFamilienBudget_V1(elterteil2),
            new InputPersoenlichesbudget_V1(antragssteller)
        );
    }
}
