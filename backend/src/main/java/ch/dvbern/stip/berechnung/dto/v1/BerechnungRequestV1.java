package ch.dvbern.stip.berechnung.dto.v1;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

import ch.dvbern.stip.api.common.entity.AbstractFamilieEntity;
import ch.dvbern.stip.api.common.type.Ausbildungssituation;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.entity.GesuchTranche;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
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
        final List<Integer> personenImHaushaltList = List.of(
            personenImHaushalt.getPersonenImHaushalt1(),
            personenImHaushalt.getPersonenImHaushalt2()
        );

        final List<ElternteilV1> elternteilerequests = new ArrayList<>(
            List.of(
                ElternteilV1.builderWithDefaults().build(),
                ElternteilV1.builderWithDefaults().build()
            )
        );

        final List<Eltern> elternteile = gesuchFormular.getElterns().stream().toList();
        ListIterator<Steuerdaten> steuerdatenListIterator = gesuchFormular.getSteuerdaten().stream().sorted(
            Comparator.comparing(Steuerdaten::getSteuerdatenTyp)
        ).toList().listIterator();

        List<AbstractFamilieEntity> kinderDerElternInHaushalten = new ArrayList<>(
            gesuchFormular.getGeschwisters().stream()
                .filter(
                    geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT
                ).map(
                    geschwister -> (AbstractFamilieEntity) geschwister)
                .toList()
        );

        if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
            kinderDerElternInHaushalten.add(gesuchFormular.getPersonInAusbildung());
        }

        while (steuerdatenListIterator.hasNext()) {
            final int currentIdx = steuerdatenListIterator.nextIndex();
            elternteilerequests.set(
                currentIdx,
                ElternteilV1.buildFromDependants(
                    gesuch.getGesuchsperiode(),
                    elternteile,
                    steuerdatenListIterator.next(),
                    personenImHaushaltList.get(currentIdx),
                    kinderDerElternInHaushalten,
                    (int) gesuchFormular.getGeschwisters().stream().filter(
                        geschwister -> geschwister.getAusbildungssituation() != Ausbildungssituation.KEINE
                    ).count(),
                    elternTyp
                )
            );
        }

        final var antragssteller = AntragsstellerV1.buildFromDependants(gesuchFormular);

        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode()),
            new InputFamilienbudgetV1(elternteilerequests.get(0)),
            new InputFamilienbudgetV1(elternteilerequests.get(1)),
            new InputPersoenlichesbudgetV1(antragssteller)
        );
    }

    public static int getGrundbedarf(final Gesuchsperiode gesuchsperiode, final int anzahlPersonenImHaushalt) {
        int grundbedarf = 0;
        switch (anzahlPersonenImHaushalt) {
        case 1:
            grundbedarf = gesuchsperiode.getPerson1();
//            if (wohntInWG) {
//                grundbedarf -= gesuchsperiode.getW
//            }
            break;
        case 2:
            grundbedarf = gesuchsperiode.getPersonen2();
            break;
        case 3:
            grundbedarf = gesuchsperiode.getPersonen3();
            break;
        case 4:
            grundbedarf = gesuchsperiode.getPersonen4();
            break;
        case 5:
            grundbedarf = gesuchsperiode.getPersonen5();
            break;
        case 6:
            grundbedarf = gesuchsperiode.getPersonen6();
            break;
        case 7:
            grundbedarf = gesuchsperiode.getPersonen7();
            break;
        default:
            grundbedarf = gesuchsperiode.getPersonen7() + (anzahlPersonenImHaushalt - 7) * gesuchsperiode.getProWeiterePerson();
            break;
        }
        return grundbedarf;
    }

    public static int getEffektiveWohnkosten(
        final int eingegebeneWohnkosten,
        final Gesuchsperiode gesuchsperiode,
        int anzahlPersonenImHaushalt
    ) {
        int maxWohnkosten = gesuchsperiode.getWohnkostenFam5pluspers();
        switch (anzahlPersonenImHaushalt) {
        case 1:
            maxWohnkosten = gesuchsperiode.getWohnkostenFam1pers();
            break;
        case 2:
            maxWohnkosten = gesuchsperiode.getWohnkostenFam2pers();
            break;
        case 3:
            maxWohnkosten = gesuchsperiode.getWohnkostenFam3pers();
            break;
        case 4:
            maxWohnkosten = gesuchsperiode.getWohnkostenFam4pers();
            break;
        default:
            break;
        }
        return Integer.min(eingegebeneWohnkosten, maxWohnkosten);
    }

    public static int getMedizinischeGrundversorgung(final int alter, final Gesuchsperiode gesuchsperiode) {
        int medizinischeGrundversorgung = gesuchsperiode.getErwachsene2699();
        if (alter <= 18) {
            medizinischeGrundversorgung = gesuchsperiode.getKinder0018();
        } else if (alter <= 25) {
            medizinischeGrundversorgung = gesuchsperiode.getJugendlicheErwachsene1925();
        }
        return medizinischeGrundversorgung;
    }
}
