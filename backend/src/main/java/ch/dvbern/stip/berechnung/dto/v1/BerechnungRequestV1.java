package ch.dvbern.stip.berechnung.dto.v1;

import java.util.UUID;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
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

    private static int elternImHaushaltIfWiederverheiratet(final boolean isElterWiederverheiratet) {
        return isElterWiederverheiratet ? 2 : 1;
    }

    public static BerechnungRequestV1 createRequest(final Gesuch gesuch, final UUID tranchenId, final ElternTyp elternTyp) {
        final var elterteil1Builder = ElternteilV1.builderWithDefaults();
        final var elterteil2Builder = ElternteilV1.builderWithDefaults();

        final var gesuchTranche = gesuch.getGesuchTrancheById(tranchenId);

        var antragssteller = AntragsstellerV1.withDefaults();
        if (gesuchTranche.isPresent()) {
            final var gesuchFormular = gesuchTranche.get().getGesuchFormular();

            antragssteller = AntragsstellerV1.fromGesuchFormular(gesuchFormular);

            int personenImHaushalt1 = 0;
            int personenImHaushalt2 = 0;
            final var geschwisterOhneEigenerHaushalt = gesuchFormular.getGeschwisters().stream().filter(geschwister -> geschwister.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT).toList();
            final var familiensituation = gesuchFormular.getFamiliensituation();

            if (familiensituation.getElternVerheiratetZusammen()) {
                personenImHaushalt1 = 2;
                if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                    personenImHaushalt1 += 1;
                }
                personenImHaushalt1 += geschwisterOhneEigenerHaushalt.size();
            } else if (familiensituation.getGerichtlicheAlimentenregelung()) {
                if (familiensituation.getWerZahltAlimente() == Elternschaftsteilung.VATER) {
                    personenImHaushalt1 = elternImHaushaltIfWiederverheiratet(familiensituation.getMutterWiederverheiratet());

                } else if (familiensituation.getWerZahltAlimente() == Elternschaftsteilung.MUTTER) {
                    personenImHaushalt1 = elternImHaushaltIfWiederverheiratet(familiensituation.getVaterWiederverheiratet());
                }
                if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                    personenImHaushalt1 += 1;
                }
                personenImHaushalt1 += geschwisterOhneEigenerHaushalt.size();
            } else if (familiensituation.getElternteilUnbekanntVerstorben()) {
                if (familiensituation.getMutterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH && familiensituation.getVaterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH) {
                    personenImHaushalt1 = elternImHaushaltIfWiederverheiratet(familiensituation.getVaterWiederverheiratet());

                }
                if (familiensituation.getVaterUnbekanntVerstorben() != ElternAbwesenheitsGrund.WEDER_NOCH && familiensituation.getMutterUnbekanntVerstorben() == ElternAbwesenheitsGrund.WEDER_NOCH) {
                    personenImHaushalt1 = elternImHaushaltIfWiederverheiratet(familiensituation.getMutterWiederverheiratet());
                }
                if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                    personenImHaushalt1 += 1;
                }
                personenImHaushalt1 += geschwisterOhneEigenerHaushalt.size();
            } else {
                personenImHaushalt1 = elternImHaushaltIfWiederverheiratet(familiensituation.getVaterWiederverheiratet());
                personenImHaushalt2 = elternImHaushaltIfWiederverheiratet(familiensituation.getMutterWiederverheiratet());

                int noGeschwisterTeilzeit = (int) geschwisterOhneEigenerHaushalt.stream().filter(
                    geschwister -> geschwister.getWohnsitzAnteilMutter().intValue() > 0 &&
                                   geschwister.getWohnsitzAnteilVater().intValue() > 0
                ).count();
                personenImHaushalt1 += (int) geschwisterOhneEigenerHaushalt.stream().filter(geschwister -> geschwister.getWohnsitzAnteilVater().intValue() == 100).count();
                personenImHaushalt2 += (int) geschwisterOhneEigenerHaushalt.stream().filter(geschwister -> geschwister.getWohnsitzAnteilMutter().intValue() == 100).count();

                if (elternTyp == ElternTyp.VATER) {
                    if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT && gesuchFormular.getPersonInAusbildung().getWohnsitzAnteilVater().intValue() > 0) {
                        personenImHaushalt1++;
                    } else if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                        personenImHaushalt2++;
                    }

                    personenImHaushalt1 += noGeschwisterTeilzeit;
                } else if (elternTyp == ElternTyp.MUTTER) {
                    if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT && gesuchFormular.getPersonInAusbildung().getWohnsitzAnteilMutter().intValue() > 0) {
                        personenImHaushalt2++;
                    } else if (gesuchFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                        personenImHaushalt1++;
                    }
                    personenImHaushalt2 += noGeschwisterTeilzeit;
                }
            }
            elterteil1Builder.anzahlPersonenImHaushalt(personenImHaushalt1);
            elterteil2Builder.anzahlPersonenImHaushalt(personenImHaushalt2);

        }
        return new BerechnungRequestV1(
            StammdatenV1.fromGesuchsperiode(gesuch.getGesuchsperiode()),
            new InputFamilienBudget_V1(elterteil1Builder.build()),
            new InputFamilienBudget_V1(elterteil2Builder.build()),
            new InputPersoenlichesbudget_V1(antragssteller)
        );
    }
}
