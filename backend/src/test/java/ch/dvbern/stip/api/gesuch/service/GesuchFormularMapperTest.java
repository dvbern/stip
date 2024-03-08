package ch.dvbern.stip.api.gesuch.service;

import java.math.BigDecimal;
import java.util.ArrayList;

import ch.dvbern.stip.api.ausbildung.service.AusbildungMapperImpl;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapperImpl;
import ch.dvbern.stip.api.common.service.DateMapperImpl;
import ch.dvbern.stip.api.common.service.EntityReferenceMapperImpl;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapperImpl;
import ch.dvbern.stip.api.eltern.service.ElternMapperImpl;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapperImpl;
import ch.dvbern.stip.api.familiensituation.type.Elternschaftsteilung;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapperImpl;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.service.KindMapperImpl;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapperImpl;
import ch.dvbern.stip.api.partner.service.PartnerMapperImpl;
import ch.dvbern.stip.api.personinausbildung.entity.PersonInAusbildung;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapperImpl;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import ch.dvbern.stip.generated.dto.PersonInAusbildungUpdateDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

class GesuchFormularMapperTest {
    @Test
    void resetDependentDataRemovesRentenTest() {
        final var target = new GesuchFormular()
            .setFamiliensituation(
                new Familiensituation()
                    .setElternteilUnbekanntVerstorben(true)
            )
            .setEinnahmenKosten(
                new EinnahmenKosten()
                    .setRenten(new BigDecimal(1))
            );

        final var updateFormular = new GesuchFormularUpdateDto();
        final var updateFamsit = new FamiliensituationUpdateDto();
        updateFamsit.setElternteilUnbekanntVerstorben(false);
        updateFormular.setFamiliensituation(updateFamsit);

        final var mapper = createMapper();
        mapper.resetDependentDataAfterData(updateFormular, target);

        assertThat(target.getEinnahmenKosten().getRenten(), is(nullValue()));
    }

    @Test
    void resetDependentDataRemovesVater() {
        // Arrange
        final var elterns = new ArrayList<ElternUpdateDto>();
        final var vater = new ElternUpdateDto();
        vater.setElternTyp(ElternTyp.VATER);
        final var mutter = new ElternUpdateDto();
        mutter.setElternTyp(ElternTyp.MUTTER);
        elterns.add(vater);
        elterns.add(mutter);

        final var familiensituation = new FamiliensituationUpdateDto();

        final var updateFormular = new GesuchFormularUpdateDto();
        updateFormular.setElterns(elterns);
        updateFormular.setFamiliensituation(familiensituation);

        var target = new GesuchFormular()
            .setFamiliensituation(null);

        // Act & Assert
        final var mapper = createMapper();
        // Partial update is needed here to "initialise" the target
        target = mapper.partialUpdate(updateFormular, target);
        mapper.resetDependentDataAfterData(updateFormular, target);

        // Without werZahltAlimente set, nothing should be cleared
        assertThat(target.getElterns().size(), is(2));

        // Setting it so the VATER pays alimony, and then clearing on update should remove the father from the Gesuch
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.VATER);
        mapper.resetDependentDataAfterData(updateFormular, target);

        assertThat(target.getElterns().size(), is(1));
        assertThat(target.getElterns().stream().toList().get(0).getElternTyp(), is(ElternTyp.MUTTER));
    }

    @Test
    void resetDependentDataRemovesWohnkostenTest() {
        final var targetPia = new PersonInAusbildung();
        targetPia.setWohnsitz(Wohnsitz.EIGENER_HAUSHALT);
        final var target = new GesuchFormular()
            .setPersonInAusbildung(targetPia)
            .setEinnahmenKosten(new EinnahmenKosten().setWohnkosten(new BigDecimal(1)));

        final var update = new GesuchFormularUpdateDto();
        final var updatePia = new PersonInAusbildungUpdateDto();
        updatePia.setWohnsitz(Wohnsitz.MUTTER_VATER);
        update.setPersonInAusbildung(updatePia);

        final var mapper = createMapper();
        mapper.resetDependentDataAfterData(update, target);

        assertThat(target.getEinnahmenKosten().getWohnkosten(), is(nullValue()));
    }

    GesuchFormularMapper createMapper() {
        return new GesuchFormularMapperImpl(
            new PersonInAusbildungMapperImpl(),
            new FamiliensituationMapperImpl(),
            new AusbildungMapperImpl(new EntityReferenceMapperImpl(), new DateMapperImpl()),
            new LebenslaufItemMapperImpl(new DateMapperImpl()),
            new PartnerMapperImpl(),
            new AuszahlungMapperImpl(),
            new GeschwisterMapperImpl(),
            new ElternMapperImpl(),
            new KindMapperImpl(),
            new EinnahmenKostenMapperImpl()
        );
    }
}
