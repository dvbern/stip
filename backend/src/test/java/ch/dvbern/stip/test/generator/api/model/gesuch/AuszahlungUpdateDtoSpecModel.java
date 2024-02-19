package ch.dvbern.stip.test.generator.api.model.gesuch;

import ch.dvbern.stip.generated.test.dto.AuszahlungUpdateDtoSpec;
import ch.dvbern.stip.generated.test.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.test.util.TestConstants;
import org.instancio.Instancio;
import org.instancio.Model;

import static ch.dvbern.stip.test.generator.api.model.gesuch.AdresseSpecModel.adresseSpecModel;
import static org.instancio.Select.field;

public final class AuszahlungUpdateDtoSpecModel {

    public static final Model<AuszahlungUpdateDtoSpec> auszahlungUpdateDtoSpecModel =
        Instancio.of(AuszahlungUpdateDtoSpec.class)
            .set(field(AuszahlungUpdateDtoSpec::getAdresse), Instancio.create(adresseSpecModel))
            .set(field(AuszahlungUpdateDtoSpec::getIban), TestConstants.IBAN_CH_NUMMER_VALID)
            .toModel();

    public static final Model<GesuchFormularUpdateDtoSpec> gesuchFormularUpdateDtoSpecAuszahlungModel =
        Instancio.of(
                GesuchFormularUpdateDtoSpec.class)
            .set(
                field(GesuchFormularUpdateDtoSpec::getAuszahlung),
                Instancio.create(auszahlungUpdateDtoSpecModel)
            )
            .ignore(field(GesuchFormularUpdateDtoSpec::getFamiliensituation))
            .ignore(field(GesuchFormularUpdateDtoSpec::getElterns))
            .ignore(field(GesuchFormularUpdateDtoSpec::getGeschwisters))
            .ignore(field(GesuchFormularUpdateDtoSpec::getLebenslaufItems))
            .ignore(field(GesuchFormularUpdateDtoSpec::getEinnahmenKosten))
            .ignore(field(GesuchFormularUpdateDtoSpec::getAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPersonInAusbildung))
            .ignore(field(GesuchFormularUpdateDtoSpec::getKinds))
            .ignore(field(GesuchFormularUpdateDtoSpec::getPartner))
            .toModel();
}
