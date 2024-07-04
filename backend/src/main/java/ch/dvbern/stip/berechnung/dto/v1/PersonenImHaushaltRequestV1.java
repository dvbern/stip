package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
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
public class PersonenImHaushaltRequestV1 implements DmnRequest {
    @JsonProperty
    PersonenImHaushaltInputV1 personenImHaushaltInput;

    @AllArgsConstructor
    @Builder
    @Jacksonized
    public static class PersonenImHaushaltInputV1 {
        @JsonProperty
        FamiliensituationV1 familiensituation;

        @JsonProperty
        PersonInAusbildungV1 personInAusbildung;

        @JsonProperty
        String elternToPrioritize;

        @JsonProperty
        int geschwisterTeilzeit;

        @JsonProperty
        int geschwisterVaterVollzeit;

        @JsonProperty
        int geschwisterMutterVollzeit;
    }

    @Override
    @JsonIgnore
    public String getVersion() {
        return "v1.0";
    }

    public static PersonenImHaushaltRequestV1 createRequest(final GesuchFormular gesuchFormular, final ElternTyp elternToPrioritize) {
        final var geschwisterOhneEigenerHaushalt = gesuchFormular.getGeschwisters().stream().filter(geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT).toList();
        final int geschwisterTeilzeit = (int) geschwisterOhneEigenerHaushalt.stream()
            .filter(
                geschwister -> geschwister.getWohnsitzAnteilMutter().intValue() > 0 &&
                               geschwister.getWohnsitzAnteilVater().intValue()  > 0
            ).count();
        final int geschwisterVaterVollzeit = (int) geschwisterOhneEigenerHaushalt.stream().filter(geschwister -> geschwister.getWohnsitzAnteilVater().intValue() == 100).count();
        final int geschwisterMutterVollzeit = (int) geschwisterOhneEigenerHaushalt.stream().filter(geschwister -> geschwister.getWohnsitzAnteilMutter().intValue() == 100).count();
        return new PersonenImHaushaltRequestV1(
            new PersonenImHaushaltInputV1(
                FamiliensituationV1.fromFamiliensituation(gesuchFormular.getFamiliensituation()),
                PersonInAusbildungV1.fromPersonInAusbildung(gesuchFormular.getPersonInAusbildung()),
                elternToPrioritize.toString(),
                geschwisterTeilzeit,
                geschwisterVaterVollzeit,
                geschwisterMutterVollzeit
            )
        );
    }
}
