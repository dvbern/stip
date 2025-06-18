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

package ch.dvbern.stip.api.generator.api;

import java.util.UUID;

import ch.dvbern.stip.api.generator.api.model.gesuch.DarlehenDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.EinnahmenKostenUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.ElternUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.FamiliensituationUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.GeschwisterUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.KindUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.LebenslaufItemUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PartnerUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.PersonInAusbildungUpdateDtoSpecModel;
import ch.dvbern.stip.api.generator.api.model.gesuch.SteuererklaerungUpdateTabsDtoSpecModel;
import ch.dvbern.stip.api.util.TestConstants;
import ch.dvbern.stip.api.util.TestUtil;
import ch.dvbern.stip.generated.dto.ElternTypDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchUpdateDtoSpec;
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import ch.dvbern.stip.generated.dto.ZivilstandDtoSpec;

public class GesuchTestSpecGenerator {
    public static GesuchTrancheUpdateDtoSpec gesuchTrancheDtoSpec() {
        var model = new GesuchTrancheUpdateDtoSpec();
        model.setId(UUID.randomUUID());
        return model;
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecPersonInAusbildung() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    PersonInAusbildungUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPersonInAusbildung()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecFamiliensituation() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    FamiliensituationUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecFamiliensituation()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecPartner() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    PartnerUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecPartner()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecGeschwister() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    GeschwisterUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecGeschwisters()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecLebenslauf() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    LebenslaufItemUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecLebenslauf()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecElterns() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    ElternUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecElterns()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSteuererklaerungTabs() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    SteuererklaerungUpdateTabsDtoSpecModel.gesuchFormularUpdateDtoSpecSteuererklaerung()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecEinnahmenKosten() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    EinnahmenKostenUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecEinnahmenKosten()
                );
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecKinder() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith()
                .setGesuchFormular(
                    KindUpdateDtoSpecModel.gesuchFormularUpdateDtoSpecKinder()
                );
        });
    }

    private static GesuchFormularUpdateDtoSpec gesuchFormularUpdateDtoSpecFull() {
        return TestUtil.createUpdateDtoSpec(GesuchFormularUpdateDtoSpec::new, (model) -> {
            model.setPersonInAusbildung(PersonInAusbildungUpdateDtoSpecModel.personInAusbildungUpdateDtoSpec());
            model.getPersonInAusbildung().setZivilstand(ZivilstandDtoSpec.VERHEIRATET);
            // model.setAusbildung(AusbildungUpdateDtoSpecModel.ausbildungUpdateDtoSpec());
            model.setLebenslaufItems(LebenslaufItemUpdateDtoSpecModel.lebenslaufItemUpdateDtoSpecs());
            model.setFamiliensituation(FamiliensituationUpdateDtoSpecModel.familiensituationUpdateDtoSpec());
            model.setElterns(ElternUpdateDtoSpecModel.elternUpdateDtoSpecs(2));
            model.getElterns().get(0).setElternTyp(ElternTypDtoSpec.VATER);
            model.getElterns().get(0).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_VATER);
            model.getElterns().get(1).setElternTyp(ElternTypDtoSpec.MUTTER);
            model.getElterns().get(1).setSozialversicherungsnummer(TestConstants.AHV_NUMMER_VALID_MUTTER);
            model.setSteuererklaerung(
                SteuererklaerungUpdateTabsDtoSpecModel.steuererklaerungDtoSpecs(SteuerdatenTypDtoSpec.FAMILIE)
            );
            model.setPartner(PartnerUpdateDtoSpecModel.partnerUpdateDtoSpec());
            model.setKinds(KindUpdateDtoSpecModel.kindUpdateDtoSpecs());
            model.setEinnahmenKosten(EinnahmenKostenUpdateDtoSpecModel.einnahmenKostenUpdateDtoSpec());
            model.setDarlehen(DarlehenDtoSpecModel.darlehenDtoSpec());
        });
    }

    public static GesuchUpdateDtoSpec gesuchUpdateDtoSpecFull() {
        return TestUtil.createUpdateDtoSpec(GesuchUpdateDtoSpec::new, (model) -> {
            model.setGesuchTrancheToWorkWith(gesuchTrancheDtoSpec());
            model.getGesuchTrancheToWorkWith().setId(UUID.randomUUID());
            model.getGesuchTrancheToWorkWith().setGesuchFormular(gesuchFormularUpdateDtoSpecFull());
        });
    }
}
