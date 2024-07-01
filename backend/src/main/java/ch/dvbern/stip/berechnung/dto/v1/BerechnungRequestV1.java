package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.service.PersonenImHaushaltService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
@JsonIgnoreProperties
public class BerechnungRequestV1 implements DmnRequest {
    @JsonProperty("Stammdaten_V1")
    StammdatenV1 stammdaten;

    @JsonProperty("InputFamilienbudget_1_V1")
    InputFamilienbudgetV1 inputFamilienbudget1;

    @JsonProperty("InputFamilienbudget_2_V1")
    InputFamilienbudgetV1 inputFamilienbudget2;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class InputFamilienbudgetV1 {
        @JsonProperty("elternteil")
        ElternteilV1 elternteil;
    }

    @JsonProperty("InputPersoenlichesbudget_V1")
    InputPersoenlichesbudgetV1 inputPersoenlichesBudget;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class InputPersoenlichesbudgetV1 {
        @JsonProperty("antragssteller")
        AntragsstellerV1 antragssteller;
    }

    @Override
    @JsonIgnore
    public String getVersion() {
        return "v1.0";
    }

    public static BerechnungRequestV1 createRequest(
        final Gesuch gesuch,
        final GesuchTranche gesuchTranche,
        final ElternTyp elternTyp,
        final PersonenImHaushaltService personenImHaushaltService
    ) {
        final var gesuchFormular = gesuchTranche.getGesuchFormular();
        final var personenImHaushaltRequest = personenImHaushaltService.getPersonenImHaushaltRequest(
            ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1Builder.class.getAnnotation(DmnModelVersion.class).major(),
            ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1Builder.class.getAnnotation(DmnModelVersion.class).minor(),
            gesuchFormular,
            elternTyp
        );
        final var personenImHaushalt = personenImHaushaltService.calculatePersonenImHaushalt(personenImHaushaltRequest);

        final var elternteil1Builder = ElternteilV1.builderWithDefaults();
        final var elternteil2Builder = ElternteilV1.builderWithDefaults();

        final var antragssteller = AntragsstellerV1.fromGesuchFormular(gesuchFormular);

        elternteil1Builder.anzahlPersonenImHaushalt(personenImHaushalt.getPersonenImHaushalt1());
        elternteil2Builder.anzahlPersonenImHaushalt(personenImHaushalt.getPersonenImHaushalt2());

        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode()),
            new InputFamilienbudgetV1(elternteil1Builder.build()),
            new InputFamilienbudgetV1(elternteil2Builder.build()),
            new InputPersoenlichesbudgetV1(antragssteller)
        );
    }
}
