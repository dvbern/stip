/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.generator.entities.gesuchsperiode;

import java.time.LocalDate;

import ch.dvbern.stip.api.common.type.GueltigkeitStatus;
import ch.dvbern.stip.api.generator.AbstractTestBuilder;
import ch.dvbern.stip.api.gesuchsjahr.entity.Gesuchsjahr;
import ch.dvbern.stip.api.gesuchsjahr.entity.GesuchsjahrBuilder;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
import ch.dvbern.stip.api.gesuchsperioden.entity.GesuchsperiodeBuilder;

public final class GesuchsperiodeTestBuilder extends AbstractTestBuilder<Gesuchsperiode, GesuchsperiodeTestBuilder> {
    GesuchsperiodeTestBuilder(Gesuchsperiode entity, LocalDate referenceDate) {
        super(entity, referenceDate);
    }

    public static GesuchsperiodeTestBuilder standardDirectDepsFruehling(LocalDate referenceDate) {
        Gesuchsjahr gesuchsjahr = GesuchsjahrBuilder.gesuchsjahr()
            .bezeichnungDe("Gesuchsjahr " + referenceDate.getYear())
            .bezeichnungFr("Année de la demande " + referenceDate.getYear())
            .technischesJahr(referenceDate.getYear())
            .gueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .build();

        Gesuchsperiode gesuchsperiode = GesuchsperiodeBuilder.gesuchsperiode()
            .bezeichnungDe("Frühling " + referenceDate.getYear())
            .bezeichnungFr("Printemps " + referenceDate.getYear())
            .fiskaljahr(String.valueOf(referenceDate.getYear()))
            .gesuchsjahr(gesuchsjahr)
            .gesuchsperiodeStart(LocalDate.of(referenceDate.getYear(), 1, 1))
            .gesuchsperiodeStopp(LocalDate.of(referenceDate.getYear(), 12, 31))
            .aufschaltterminStart(LocalDate.of(referenceDate.getYear(), 1, 15))
            .einreichefristNormal(LocalDate.of(referenceDate.getYear(), 6, 30))
            .einreichefristReduziert(LocalDate.of(referenceDate.getYear(), 9, 30))
            .ausbKostenSekII(2000)
            .ausbKostenTertiaer(3000)
            .freibetragVermoegen(30000)
            .freibetragErwerbseinkommen(6000)
            .einkommensfreibetrag(6000)
            .elternbeteiligungssatz(50)
            .vermogenSatzAngerechnet(15)
            .integrationszulage(2400)
            .limiteEkFreibetragIntegrationszulage(13200)
            .stipLimiteMinimalstipendium(500)
            .person1(12072)
            .personen2(18468)
            .personen3(22452)
            .personen4(25836)
            .personen5(29220)
            .personen6(31668)
            .personen7(34116)
            .proWeiterePerson(2448)
            .kinder0017(1400)
            .jugendlicheErwachsene1824(4600)
            .erwachsene2599(5400)
            .wohnkostenFam1pers(13536)
            .wohnkostenFam2pers(16260)
            .wohnkostenFam3pers(16260)
            .wohnkostenFam4pers(19932)
            .wohnkostenFam5pluspers(25260)
            .wohnkostenPersoenlich1pers(10009)
            .wohnkostenPersoenlich2pers(13526)
            .wohnkostenPersoenlich3pers(16260)
            .wohnkostenPersoenlich4pers(19932)
            .wohnkostenPersoenlich5pluspers(25260)
            .gueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .preisProMahlzeit(10)
            .maxSaeule3a(7056)
            .anzahlWochenLehre(47)
            .anzahlWochenSchule(38)
            .vermoegensanteilInProzent(15)
            .reduzierungDesGrundbedarfs(2838)
            .limiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .zweiterAuszahlungsterminMonat(6)
            .zweiterAuszahlungsterminTag(1)
            .fristNachreichenDokumente(30)
            .fristUploadUnterschriftenblatt(1)
            .stichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate.of(referenceDate.getYear(), 12, 31))
            .build();

        return new GesuchsperiodeTestBuilder(gesuchsperiode, referenceDate);
    }

    public static GesuchsperiodeTestBuilder standardDirectDepsHerbst(LocalDate referenceDate) {
        Gesuchsjahr gesuchsjahr = GesuchsjahrBuilder.gesuchsjahr()
            .bezeichnungDe("Gesuchsjahr " + referenceDate.getYear())
            .bezeichnungFr("Année de la demande " + referenceDate.getYear())
            .technischesJahr(referenceDate.getYear())
            .gueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .build();

        Gesuchsperiode gesuchsperiode = GesuchsperiodeBuilder.gesuchsperiode()
            .bezeichnungDe("Frühling " + referenceDate.getYear())
            .bezeichnungFr("Printemps " + referenceDate.getYear())
            .fiskaljahr(String.valueOf(referenceDate.getYear()))
            .gesuchsjahr(gesuchsjahr)
            .gesuchsperiodeStart(LocalDate.of(referenceDate.getYear(), 1, 7))
            .gesuchsperiodeStopp(LocalDate.of(referenceDate.getYear() + 1, 6, 30))
            .aufschaltterminStart(LocalDate.of(referenceDate.getYear(), 7, 15))
            .einreichefristNormal(LocalDate.of(referenceDate.getYear(), 12, 31))
            .einreichefristReduziert(LocalDate.of(referenceDate.getYear() + 1, 3, 15))
            .ausbKostenSekII(2000)
            .ausbKostenTertiaer(3000)
            .freibetragVermoegen(30000)
            .freibetragErwerbseinkommen(6000)
            .einkommensfreibetrag(6000)
            .elternbeteiligungssatz(50)
            .vermogenSatzAngerechnet(15)
            .integrationszulage(2400)
            .limiteEkFreibetragIntegrationszulage(13200)
            .stipLimiteMinimalstipendium(500)
            .person1(12072)
            .personen2(18468)
            .personen3(22452)
            .personen4(25836)
            .personen5(29220)
            .personen6(31668)
            .personen7(34116)
            .proWeiterePerson(2448)
            .kinder0017(1400)
            .jugendlicheErwachsene1824(4600)
            .erwachsene2599(5400)
            .wohnkostenFam1pers(13536)
            .wohnkostenFam2pers(16260)
            .wohnkostenFam3pers(16260)
            .wohnkostenFam4pers(19932)
            .wohnkostenFam5pluspers(25260)
            .wohnkostenPersoenlich1pers(10009)
            .wohnkostenPersoenlich2pers(13526)
            .wohnkostenPersoenlich3pers(16260)
            .wohnkostenPersoenlich4pers(19932)
            .wohnkostenPersoenlich5pluspers(25260)
            .gueltigkeitStatus(GueltigkeitStatus.PUBLIZIERT)
            .preisProMahlzeit(10)
            .maxSaeule3a(7056)
            .anzahlWochenLehre(47)
            .anzahlWochenSchule(38)
            .vermoegensanteilInProzent(15)
            .reduzierungDesGrundbedarfs(2838)
            .limiteAlterAntragsstellerHalbierungElternbeitrag(25)
            .zweiterAuszahlungsterminMonat(6)
            .zweiterAuszahlungsterminTag(1)
            .fristNachreichenDokumente(30)
            .fristUploadUnterschriftenblatt(1)
            .stichtagVolljaehrigkeitMedizinischeGrundversorgung(LocalDate.of(referenceDate.getYear(), 12, 31))
            .build();

        return new GesuchsperiodeTestBuilder(gesuchsperiode, referenceDate);
    }
}
