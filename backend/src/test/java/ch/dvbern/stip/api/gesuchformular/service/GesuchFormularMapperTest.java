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

package ch.dvbern.stip.api.gesuchformular.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapperImpl;
import ch.dvbern.stip.api.ausbildung.service.AusbildungsgangMapperImpl;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapperImpl;
import ch.dvbern.stip.api.auszahlung.type.Kontoinhaber;
import ch.dvbern.stip.api.common.service.EntityReferenceMapperImpl;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.dokument.entity.GesuchDokument;
import ch.dvbern.stip.api.dokument.service.GesuchDokumentService;
import ch.dvbern.stip.api.dokument.type.DokumentTyp;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapperImpl;
import ch.dvbern.stip.api.eltern.service.ElternMapperImpl;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapperImpl;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.generator.entities.GesuchGenerator;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapperImpl;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuchformular.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchformular.util.GesuchFormularCalculationUtil;
import ch.dvbern.stip.api.gesuchtranche.entity.GesuchTranche;
import ch.dvbern.stip.api.kind.entity.Kind;
import ch.dvbern.stip.api.kind.service.KindMapperImpl;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapperImpl;
import ch.dvbern.stip.api.partner.entity.Partner;
import ch.dvbern.stip.api.partner.service.PartnerMapperImpl;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapperImpl;
import ch.dvbern.stip.api.personinausbildung.type.Zivilstand;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapperImpl;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.AuszahlungUpdateDto;
import ch.dvbern.stip.generated.dto.EinnahmenKostenUpdateDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.LebenslaufItemUpdateDto;
import ch.dvbern.stip.generated.dto.PartnerUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import ch.dvbern.stip.generated.dto.SteuerdatenUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import static io.smallrye.common.constraint.Assert.assertFalse;
import static io.smallrye.common.constraint.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class GesuchFormularMapperTest {
    @Test
    void resetElternRemovesVaterTest() {
        // Arrange
        final var elterns = new ArrayList<ElternUpdateDto>();
        final var vater = new ElternUpdateDto();
        vater.setElternTyp(ElternTyp.VATER);
        final var mutter = new ElternUpdateDto();
        mutter.setElternTyp(ElternTyp.MUTTER);
        elterns.add(vater);
        elterns.add(mutter);

        final var familiensituation = new FamiliensituationUpdateDto();
        familiensituation.setElternVerheiratetZusammen(true);

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setElterns(elterns);
        updateFormular.setFamiliensituation(familiensituation);

        var target = new GesuchFormular()
            .setFamiliensituation(null);

        // Act & Assert
        final var mapper = createMapper();
        // Partial update is needed here to "initialise" the target
        target = mapper.partialUpdate(updateFormular, target);
        mapper.resetEltern(updateFormular, target);

        // Without werZahltAlimente set, nothing should be cleared
        assertThat(updateFormular.getElterns().size(), is(2));

        // Setting it so the VATER pays alimony, and then clearing on update should remove the father from the Gesuch
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.VATER);
        mapper.resetEltern(updateFormular, target);

        assertThat(updateFormular.getElterns().size(), is(1));
        assertThat(updateFormular.getElterns().stream().toList().get(0).getElternTyp(), is(ElternTyp.MUTTER));
    }

    @Test
    void resetEinnahmenKostenRemovesWohnkostenTest() {
        final var targetPia = new PersonInAusbildung();
        targetPia.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        final var target = new GesuchFormular()
            .setPersonInAusbildung(targetPia)
            .setEinnahmenKosten(new EinnahmenKosten().setWohnkosten(1));

        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setWohnsitz(Wohnsitz.MUTTER_VATER);

        final var updateEinnahmenKosten = new EinnahmenKostenUpdateDto();
        updateEinnahmenKosten.setWohnkosten(1);

        final var update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(updatePia);
        update.setEinnahmenKosten(updateEinnahmenKosten);

        final var mapper = createMapper();
        mapper.resetEinnahmenKosten(update, target);

        assertThat(update.getEinnahmenKosten().getWohnkosten(), is(nullValue()));
    }

    @Test
    void resetEinnahmenKostenRemovesAlimenteTest() {
        // Arrange
        final var updateFamsit = new FamiliensituationUpdateDto();
        updateFamsit.setElternVerheiratetZusammen(false);
        updateFamsit.setGerichtlicheAlimentenregelung(true);

        final var updateEinnahmenKosten = new EinnahmenKostenUpdateDto();
        updateEinnahmenKosten.setAlimente(1);

        final var update = new GesuchFormularUpdateDto();
        update.setFamiliensituation(updateFamsit);
        update.setEinnahmenKosten(updateEinnahmenKosten);

        final var mapper = createMapper();
        final var target = new GesuchFormular();

        // Initialise target
        mapper.partialUpdate(update, target);

        updateFamsit.setGerichtlicheAlimentenregelung(false);

        // Act
        mapper.resetEinnahmenKosten(update, target);

        // Assert
        assertThat(update.getEinnahmenKosten().getAlimente(), is(nullValue()));
    }

    @Test
    void resetEinnahmenKostenRemovesAuswaertigeMittagessenTest() {
        // Arrange
        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);

        final var updateEinnahmenKosten = new EinnahmenKostenUpdateDto();
        updateEinnahmenKosten.setAuswaertigeMittagessenProWoche(1);

        final var update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(updatePia);
        update.setEinnahmenKosten(updateEinnahmenKosten);

        final var mapper = createMapper();
        final var target = new GesuchFormular();

        // Initialise target
        mapper.partialUpdate(update, target);

        updatePia.setWohnsitz(Wohnsitz.FAMILIE);

        // Act
        mapper.resetEinnahmenKosten(update, target);

        // Assert
        assertThat(update.getEinnahmenKosten().getAuswaertigeMittagessenProWoche(), is(nullValue()));
    }

    @Test
    void resetEinnahmenKostenClearsBetreuungskostenKinderTest() {
        // Arrange
        final var target = new GesuchFormular()
            .setKinds(new HashSet<>() {
                {
                    add(new Kind());
                }
            });

        final var updateEinnahmenKosten = new EinnahmenKostenUpdateDto();
        updateEinnahmenKosten.setBetreuungskostenKinder(1);

        final var update = new GesuchFormularUpdateDto();
        update.setEinnahmenKosten(updateEinnahmenKosten);
        update.setKinds(new ArrayList<>());

        final var mapper = createMapper();

        // Act
        mapper.partialUpdate(update, target);

        // Assert
        assertThat(target.getEinnahmenKosten().getBetreuungskostenKinder(), is(nullValue()));
    }

    @Test
    void resetDependentDataRemovesWgWohnendTest() {
        final var targetPia = new PersonInAusbildung();
        targetPia.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        final var target = new GesuchFormular()
            .setPersonInAusbildung(targetPia)
            .setEinnahmenKosten(new EinnahmenKosten().setWgWohnend(true));

        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setWohnsitz(Wohnsitz.MUTTER_VATER);

        final var updateEinnahmenKosten = new EinnahmenKostenUpdateDto();
        updateEinnahmenKosten.setWgWohnend(true);

        final var update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(updatePia);
        update.setEinnahmenKosten(updateEinnahmenKosten);

        final var mapper = createMapper();

        mapper.resetEinnahmenKosten(update, target);

        mapper.resetDependentDataBeforeUpdate(update, target);
        assertThat(update.getEinnahmenKosten().getWgWohnend(), is(nullValue()));
    }

    @Test
    void resetLebenslaufItemsClearsLebenslaufItems() {
        // Arrange
        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setGeburtsdatum(LocalDate.now().minusYears(20));

        final var dateFormatter = DateTimeFormatter.ofPattern("MM.yyyy");
        final var updateLebenslaufItem = new LebenslaufItemUpdateDto();
        updateLebenslaufItem.setVon(LocalDate.now().minusMonths(1).format(dateFormatter));
        updateLebenslaufItem.setBis(LocalDate.now().plusMonths(1).format(dateFormatter));

        final var updateLebenslaufItems = new ArrayList<LebenslaufItemUpdateDto>();
        updateLebenslaufItems.add(updateLebenslaufItem);

        final var update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(updatePia);
        update.setLebenslaufItems(updateLebenslaufItems);

        final var mapper = createMapper();
        final var target = new GesuchFormular();

        // Initialise target
        mapper.partialUpdate(update, target);

        updatePia.setGeburtsdatum(LocalDate.now().minusYears(10));

        // Act
        mapper.resetLebenslaufItems(update, target);

        // Assert
        assertThat(update.getLebenslaufItems().size(), is(0));
    }

    @Test
    void resetPartnerClearsPartnerTest() {
        // Arrange
        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setZivilstand(Zivilstand.KONKUBINAT);

        final var updatePartner = new PartnerUpdateDto();

        final var update = new GesuchFormularUpdateDto();
        update.setPersonInAusbildung(updatePia);
        update.setPartner(updatePartner);

        final var mapper = createMapper();
        final var target = new GesuchFormular();

        // Initialise target
        mapper.partialUpdate(update, target);

        updatePia.setZivilstand(Zivilstand.LEDIG);

        // Act
        mapper.resetPartner(update, target);

        // Assert
        assertThat(update.getPartner(), is(nullValue()));
    }

    @Test
    void calculateSteuernKantonGemeindeTest() {
        GesuchFormular gesuchFormular =
            new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(0))
                .setPartner(new Partner().setJahreseinkommen(0));
        final var ausbildung = new Ausbildung();
        ausbildung.setAusbildungBegin(LocalDate.now().minusYears(1));
        ausbildung.setAusbildungEnd(LocalDate.now().plusYears(1));

        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        GesuchFormularMapper mapper = createMapper();
        GesuchFormularDto gesuchFormularDto = mapper.toDto(gesuchFormular);
        assertThat(gesuchFormularDto.getEinnahmenKosten().getSteuernKantonGemeinde(), is(0));

        // total einkommen >= 20 000 : steuern = 10%
        gesuchFormular = new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(20000))
            .setPartner(new Partner().setJahreseinkommen(0));
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        gesuchFormularDto = mapper.toDto(gesuchFormular);
        assertThat(gesuchFormularDto.getEinnahmenKosten().getSteuernKantonGemeinde(), is((int) ((20000) * 0.1)));

        gesuchFormular = new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(20000))
            .setPartner(new Partner().setJahreseinkommen(1));
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        gesuchFormularDto = mapper.toDto(gesuchFormular);
        assertThat(gesuchFormularDto.getEinnahmenKosten().getSteuernKantonGemeinde(), is((int) ((20000 + 1) * 0.1)));
        // sonst: steuern = 0
        gesuchFormular = new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(19999))
            .setPartner(new Partner().setJahreseinkommen(0));
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        gesuchFormularDto = mapper.toDto(gesuchFormular);
        assertThat(gesuchFormularDto.getEinnahmenKosten().getSteuernKantonGemeinde(), is(0));

        // handle null inputs
        gesuchFormular = new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten().setNettoerwerbseinkommen(null))
            .setPartner(new Partner().setJahreseinkommen(null));
        gesuchFormular.setTranche(new GesuchTranche().setGesuch(new Gesuch().setAusbildung(ausbildung)));
        gesuchFormularDto = mapper.toDto(gesuchFormular);
        assertThat(gesuchFormularDto.getEinnahmenKosten().getSteuernKantonGemeinde(), is(0));
    }

    @Test
    void setCorrectVermoegenValueGT18Test() {
        // neues gesuch
        Gesuch gesuch = GesuchGenerator.initGesuch();
        GesuchTranche tranche = gesuch.getGesuchTranchen().get(0);
        LocalDate geburtsDatum = LocalDate.now().minusYears(20);
        tranche.setGesuchFormular(
            new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten())
                .setPersonInAusbildung((PersonInAusbildung) new PersonInAusbildung().setGeburtsdatum(geburtsDatum))
                .setTranche(tranche)
        );
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(geburtsDatum);
        GesuchFormularMapper gesuchFormularMapper = createMapper();
        GesuchFormularDto gesuchFormularDto = gesuchFormularMapper.toDto(tranche.getGesuchFormular());
        assertTrue(GesuchFormularCalculationUtil.wasGSOlderThan18(tranche.getGesuchFormular()));
        GesuchFormular formular = gesuchFormularMapper.toEntity(gesuchFormularDto);
        assertFalse(formular.getEinnahmenKosten().getVermoegen() == null);
        assertTrue(formular.getEinnahmenKosten().getVermoegen() >= 0);

        assertTrue(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() != null);
    }

    @Test
    void setCorrectVermoegenNonZeroValueGT18Test() {
        final int vermoegen = 10000;
        // neues gesuch
        Gesuch gesuch = GesuchGenerator.initGesuch();
        GesuchTranche tranche = gesuch.getGesuchTranchen().get(0);
        LocalDate geburtsDatum = LocalDate.now().minusYears(20);
        tranche.setGesuchFormular(
            new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten())
                .setPersonInAusbildung((PersonInAusbildung) new PersonInAusbildung().setGeburtsdatum(geburtsDatum))
                .setTranche(tranche)
        );
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(geburtsDatum);
        tranche.getGesuchFormular().getEinnahmenKosten().setVermoegen(vermoegen);
        GesuchFormularMapper gesuchFormularMapper = createMapper();
        GesuchFormularDto gesuchFormularDto = gesuchFormularMapper.toDto(tranche.getGesuchFormular());
        assertTrue(GesuchFormularCalculationUtil.wasGSOlderThan18(tranche.getGesuchFormular()));
        GesuchFormular formular = gesuchFormularMapper.toEntity(gesuchFormularDto);
        assertFalse(formular.getEinnahmenKosten().getVermoegen() == null);
        assertTrue(formular.getEinnahmenKosten().getVermoegen() == vermoegen);

        assertTrue(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() != null);
    }

    @Test
    void setCorrectVermoegenValueLT18Test() {
        // neues gesuch
        Gesuch gesuch = GesuchGenerator.initGesuch();
        GesuchTranche tranche = gesuch.getGesuchTranchen().get(0);
        LocalDate geburtsDatum = LocalDate.now().minusYears(2);
        tranche.setGesuchFormular(
            new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten())
                .setPersonInAusbildung((PersonInAusbildung) new PersonInAusbildung().setGeburtsdatum(geburtsDatum))
                .setTranche(tranche)
        );
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(geburtsDatum);
        GesuchFormularMapper gesuchFormularMapper = createMapper();
        GesuchFormularDto gesuchFormularDto = gesuchFormularMapper.toDto(tranche.getGesuchFormular());
        assertFalse(GesuchFormularCalculationUtil.wasGSOlderThan18(tranche.getGesuchFormular()));
        GesuchFormular formular = gesuchFormularMapper.toEntity(gesuchFormularDto);
        assertTrue(formular.getEinnahmenKosten().getVermoegen() == null);

        assertTrue(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() != null);
    }

    @Test
    void setCorrectVermoegenNonZeroValueLT18Test() {
        final int vermoegen = 10000;
        // neues gesuch
        Gesuch gesuch = GesuchGenerator.initGesuch();
        GesuchTranche tranche = gesuch.getGesuchTranchen().get(0);
        LocalDate geburtsDatum = LocalDate.now().minusYears(16);
        tranche.setGesuchFormular(
            new GesuchFormular().setEinnahmenKosten(new EinnahmenKosten())
                .setPersonInAusbildung((PersonInAusbildung) new PersonInAusbildung().setGeburtsdatum(geburtsDatum))
                .setTranche(tranche)
        );
        tranche.getGesuchFormular().getPersonInAusbildung().setGeburtsdatum(geburtsDatum);
        tranche.getGesuchFormular().getEinnahmenKosten().setVermoegen(vermoegen);
        GesuchFormularMapper gesuchFormularMapper = createMapper();
        GesuchFormularDto gesuchFormularDto = gesuchFormularMapper.toDto(tranche.getGesuchFormular());
        assertFalse(GesuchFormularCalculationUtil.wasGSOlderThan18(tranche.getGesuchFormular()));
        GesuchFormular formular = gesuchFormularMapper.toEntity(gesuchFormularDto);
        assertTrue(formular.getEinnahmenKosten().getVermoegen() == null);

        assertTrue(tranche.getGesuch().getGesuchsperiode().getGesuchsjahr().getTechnischesJahr() != null);
    }

    @Test
    void resetSteuerdatenDoesntClearTabTest() {
        // Arrange
        final var updateFamsit = new FamiliensituationUpdateDto();
        updateFamsit.setElternVerheiratetZusammen(true);

        final var updateTab = new SteuerdatenUpdateDto();
        updateTab.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        // This needs to be this verbose as List.of creates an immutable list
        // This also needs to be a mutable list since List.removeAll(...)
        // will throw an exception even if no items are to be removed
        final List<SteuerdatenUpdateDto> updateSteuerdaten = new ArrayList<>() {
            {
                add(updateTab);
            }
        };

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setFamiliensituation(updateFamsit);
        updateFormular.setSteuerdaten(updateSteuerdaten);

        final var mapper = createMapper();

        // Act
        final var targetFormular =
            mapper.partialUpdate(
                updateFormular,
                new GesuchFormular().setFamiliensituation(new Familiensituation()).setTranche(new GesuchTranche())
            );

        // Assert
        assertThat(targetFormular.getSteuerdaten().size(), is(1));
        assertThat(
            targetFormular.getSteuerdaten().stream().findFirst().get().getSteuerdatenTyp(),
            is(SteuerdatenTyp.FAMILIE)
        );
    }

    @Test
    void resetSteuerdatenClearsFamilieTabTest() {
        // Arrange
        final var updateFamsit = new FamiliensituationUpdateDto();
        updateFamsit.setElternVerheiratetZusammen(true);

        final var updateTab = new SteuerdatenUpdateDto();
        updateTab.setSteuerdatenTyp(SteuerdatenTyp.FAMILIE);
        // This needs to be this verbose as List.of creates an immutable list
        final List<SteuerdatenUpdateDto> updateSteuerdaten = new ArrayList<>() {
            {
                add(updateTab);
            }
        };

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setFamiliensituation(updateFamsit);
        updateFormular.setSteuerdaten(updateSteuerdaten);

        final var mapper = createMapper();

        // Init target
        var targetFormular =
            mapper.partialUpdate(
                updateFormular,
                new GesuchFormular().setFamiliensituation(new Familiensituation()).setTranche(new GesuchTranche())
            );

        // Rearrange
        updateFamsit.setElternVerheiratetZusammen(false);
        updateFamsit.setGerichtlicheAlimentenregelung(true);
        updateFamsit.setWerZahltAlimente(Elternschaftsteilung.VATER);

        // Act
        targetFormular = mapper.partialUpdate(updateFormular, targetFormular);

        // Assert
        assertThat(targetFormular.getSteuerdaten().size(), is(0));
    }

    @Test
    void resetSteuerdatenAfterUpdateClearsSaeuleValues() {
        // Arrange
        final var formular = new GesuchFormular()
            .setSteuerdaten(
                Set.of(
                    new Steuerdaten()
                        .setIsArbeitsverhaeltnisSelbstaendig(true)
                        .setSaeule2(1000)
                        .setSaeule3a(1000)
                )
            );

        final var mapper = createMapper();

        // Act
        mapper.resetSteuerdatenAfterUpdate(formular);

        // Assert
        assertThat(formular.getSteuerdaten().stream().findFirst().get().getSaeule2(), is(1000));
        assertThat(formular.getSteuerdaten().stream().findFirst().get().getSaeule3a(), is(1000));

        // Rearrange
        formular.getSteuerdaten().stream().findFirst().get().setIsArbeitsverhaeltnisSelbstaendig(false);

        // Act
        mapper.resetSteuerdatenAfterUpdate(formular);

        // Assert
        assertThat(formular.getSteuerdaten().stream().findFirst().get().getSaeule2(), is(nullValue()));
        assertThat(formular.getSteuerdaten().stream().findFirst().get().getSaeule3a(), is(nullValue()));
    }

    @Test
    void dontSetAuszahlungAdresseOnUpdate() {
        // Arrange
        final var piaAdresse = new AdresseDto();
        piaAdresse.setId(UUID.randomUUID());

        final var pia = new PersonInAusbildungUpdateDto();
        pia.setAdresse(piaAdresse);

        final var auszahlungAdresse = new AdresseDto();
        auszahlungAdresse.setId(UUID.randomUUID());

        final var auszahlung = new AuszahlungUpdateDto();
        auszahlung.setKontoinhaber(Kontoinhaber.ANDERE);
        auszahlung.setAdresse(auszahlungAdresse);

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setPersonInAusbildung(pia);
        updateFormular.setAuszahlung(auszahlung);

        final var mapper = createMapper();

        // Act
        mapper.setAuszahlungAdresseBeforeUpdate(updateFormular, null);

        // Assert
        assertThat(auszahlung.getAdresse().getId(), is(auszahlungAdresse.getId()));
    }

    @Test
    void setAuszahlungPiaAdresseOnUpdateTest() {
        // Arrange
        final var piaAdresse = new AdresseDto();
        piaAdresse.setId(UUID.randomUUID());

        final var pia = new PersonInAusbildungUpdateDto();
        pia.setAdresse(piaAdresse);

        final var auszahlungAdresse = new AdresseDto();
        auszahlungAdresse.setId(UUID.randomUUID());

        final var auszahlung = new AuszahlungUpdateDto();
        auszahlung.setKontoinhaber(Kontoinhaber.GESUCHSTELLER);
        auszahlung.setAdresse(auszahlungAdresse);

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setPersonInAusbildung(pia);
        updateFormular.setAuszahlung(auszahlung);

        final var mapper = createMapper();

        // Act
        mapper.setAuszahlungAdresseBeforeUpdate(updateFormular, null);

        // Assert
        assertThat(auszahlung.getAdresse().getId(), is(piaAdresse.getId()));
    }

    @ParameterizedTest
    @CsvSource(
        {
            "MUTTER",
            "VATER"
        }
    )
    void setAuszahlungElternteilAdresseOnUpdateTest(final ElternTyp toTestFor) {
        // Arrange
        final var elternAdresse = new AdresseDto();
        elternAdresse.setId(UUID.randomUUID());

        final var elternTeil = new ElternUpdateDto();
        elternTeil.setElternTyp(toTestFor);
        elternTeil.setAdresse(elternAdresse);

        final var auszahlungAdresse = new AdresseDto();
        auszahlungAdresse.setId(UUID.randomUUID());

        final var auszahlung = new AuszahlungUpdateDto();
        auszahlung.setAdresse(auszahlungAdresse);

        if (toTestFor == ElternTyp.VATER) {
            auszahlung.setKontoinhaber(Kontoinhaber.VATER);
        } else if (toTestFor == ElternTyp.MUTTER) {
            auszahlung.setKontoinhaber(Kontoinhaber.MUTTER);
        }

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setElterns(List.of(elternTeil));
        updateFormular.setAuszahlung(auszahlung);

        final var mapper = createMapper();

        // Act
        mapper.setAuszahlungAdresseBeforeUpdate(updateFormular, null);

        // Assert
        assertThat(auszahlung.getAdresse().getId(), is(elternAdresse.getId()));
    }

    @Test
    void removeGesuchDokumenteIfFamsitChanges() {
        // Arrange
        final var famsit = new FamiliensituationUpdateDto();
        famsit.setElternVerheiratetZusammen(true);
        final var updateDto = new GesuchFormularUpdateDto();
        updateDto.setFamiliensituation(famsit);

        final var gesuchDokument =
            new GesuchDokument().setDokumentTyp(DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE);
        final var gesuchDokumente = new ArrayList<GesuchDokument>();
        gesuchDokumente.add(gesuchDokument);

        final var target = new GesuchFormular();
        target.setTranche(new GesuchTranche().setGesuchDokuments(gesuchDokumente));

        final var calledWithCorrectType = new AtomicBoolean(false);
        final var mockedService = Mockito.mock(GesuchDokumentService.class);
        Mockito.doAnswer((Answer<Void>) invocation -> {
            final var param = invocation.getArgument(0, GesuchDokument.class);
            if (param.getDokumentTyp() == DokumentTyp.ELTERN_MIETVERTRAG_HYPOTEKARZINSABRECHNUNG_FAMILIE) {
                calledWithCorrectType.set(true);
            }

            return null;
        }).when(mockedService).removeGesuchDokument(Mockito.any(GesuchDokument.class));

        final var mapper = createMapper();
        mapper.partialUpdate(updateDto, target);
        mapper.gesuchDokumentService = mockedService;

        updateDto.getFamiliensituation().setElternVerheiratetZusammen(false);

        // Act
        mapper.partialUpdate(updateDto, target);

        // Assert
        assertThat(calledWithCorrectType.get(), is(true));
    }

    GesuchFormularMapper createMapper() {
        final var mapper = (GesuchFormularMapper) new GesuchFormularMapperImpl(
            new PersonInAusbildungMapperImpl(),
            new FamiliensituationMapperImpl(),
            new AusbildungMapperImpl(
                new AusbildungsgangMapperImpl(new EntityReferenceMapperImpl())
            ),
            new LebenslaufItemMapperImpl(),
            new PartnerMapperImpl(),
            new AuszahlungMapperImpl(),
            new GeschwisterMapperImpl(),
            new ElternMapperImpl(),
            new KindMapperImpl(),
            new EinnahmenKostenMapperImpl(),
            new SteuerdatenMapperImpl()
        );

        // Remove this once/ if SteuerdatenTabBerechnungsService is a DMN service and mock it
        mapper.steuerdatenTabBerechnungsService = new SteuerdatenTabBerechnungsService();
        mapper.familiensituationMapper = new FamiliensituationMapperImpl();
        mapper.gesuchDokumentService = Mockito.mock(GesuchDokumentService.class);

        return mapper;
    }
}
