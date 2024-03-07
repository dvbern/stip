package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;

import ch.dvbern.stip.api.ausbildung.service.AusbildungMapperImpl;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapperImpl;
import ch.dvbern.stip.api.common.service.DateMapperImpl;
import ch.dvbern.stip.api.common.service.EntityReferenceMapperImpl;
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
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapperImpl;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class GesuchFormularMapperTest {
    @Test
    void mapperRemovesVater() {
        // Arrange
        final var elterns = new ArrayList<ElternUpdateDto>();
        final var vater = new ElternUpdateDto();
        vater.setElternTyp(ElternTyp.VATER);
        final var mutter = new ElternUpdateDto();
        mutter.setElternTyp(ElternTyp.MUTTER);
        elterns.add(vater);
        elterns.add(mutter);

        final var familiensituation = new FamiliensituationUpdateDto();

        final var gesuchFormular = new GesuchFormularUpdateDto();
        gesuchFormular.setElterns(elterns);
        gesuchFormular.setFamiliensituation(familiensituation);

        var target = new GesuchFormular()
            .setFamiliensituation(new Familiensituation());

        // Act & Assert
        final var mapper = createMapper();
        // Partial update is needed here to "initialise" the target
        target = mapper.partialUpdate(gesuchFormular, target);
        mapper.resetDependentDataAfterData(gesuchFormular, target);

        // Without werZahltAlimente set, nothing should be cleared
        assertThat(target.getElterns().size(), is(2));

        // Setting it so the VATER pays alimony, and then clearing on update should remove the father from the Gesuch
        familiensituation.setWerZahltAlimente(Elternschaftsteilung.VATER);
        mapper.resetDependentDataAfterData(gesuchFormular, target);

        assertThat(target.getElterns().size(), is(1));
        assertThat(target.getElterns().stream().toList().get(0).getElternTyp(), is(ElternTyp.MUTTER));
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
