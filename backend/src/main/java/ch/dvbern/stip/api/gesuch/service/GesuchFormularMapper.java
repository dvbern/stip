package ch.dvbern.stip.api.gesuch.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMappingUtil;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.familiensituation.type.ElternAbwesenheitsGrund;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularDiffUtil;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenMapper;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import jakarta.inject.Inject;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MappingConfig.class,
    uses =
        {
            AdresseMapper.class,
            PersonInAusbildungMapper.class,
            FamiliensituationMapper.class,
            AusbildungMapper.class,
            LebenslaufItemMapper.class,
            PartnerMapper.class,
            AuszahlungMapper.class,
            GeschwisterMapper.class,
            ElternMapper.class,
            KindMapper.class,
            EinnahmenKostenMapper.class,
            SteuerdatenMapper.class
        })
public abstract class GesuchFormularMapper extends EntityUpdateMapper<GesuchFormularUpdateDto, GesuchFormular> {
    @Inject
    SteuerdatenTabBerechnungsService steuerdatenTabBerechnungsService;

    @Inject
    FamiliensituationMapper familiensituationMapper;

    public abstract GesuchFormular toEntity(GesuchFormularDto gesuchFormularDto);

    public abstract GesuchFormularDto toDto(GesuchFormular gesuchFormular);

    @AfterMapping
    protected void calculateSteuerdatenTabs(
        final GesuchFormular entity,
        final @MappingTarget GesuchFormularDto dto
    ) {
        if (entity.getFamiliensituation() == null) {
            return;
        }

        dto.setSteuerdatenTabs(steuerdatenTabBerechnungsService.calculateTabs(entity.getFamiliensituation()));
    }

    @AfterMapping
    public void setCalculatedPropertiesOnDto(
        GesuchFormular gesuchFormular,
        @MappingTarget GesuchFormularDto gesuchFormularDto
    ) {
        if (gesuchFormularDto.getEinnahmenKosten() != null) {
            final var ek = gesuchFormularDto.getEinnahmenKosten();
            ek.setVermoegen(EinnahmenKostenMappingUtil.calculateVermoegen(gesuchFormular));
            ek.setSteuernKantonGemeinde(EinnahmenKostenMappingUtil.calculateSteuern(gesuchFormular));
        }
    }

    /**
     * partial update mapper for the Gesuchssteller
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract GesuchFormular partialUpdate(
        GesuchFormularUpdateDto gesuchFormularUpdateDto,
        @MappingTarget GesuchFormular gesuchFormular);

    @BeforeMapping
    protected void setAuszahlungAdresseBeforeUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        if (newFormular.getAuszahlung() == null) {
            return;
        }

        switch (newFormular.getAuszahlung().getKontoinhaber()) {
        case GESUCHSTELLER -> setPiaAdresse(newFormular);
        case VATER, MUTTER -> setForEltern(newFormular);
        }
    }

    void setPiaAdresse(final GesuchFormularUpdateDto newFormular) {
        if (newFormular.getPersonInAusbildung() == null ||
            newFormular.getPersonInAusbildung().getAdresse().getId() == null) {
            return;
        }

        newFormular.getAuszahlung().setAdresse(newFormular.getPersonInAusbildung().getAdresse());
    }

    void setForEltern(final GesuchFormularUpdateDto newFormular) {
        final Function<ElternTyp, ElternUpdateDto> getElternteilOfTyp = elternTyp -> {
            if (newFormular.getElterns() == null || newFormular.getElterns().isEmpty()) {
                return null;
            }

            return newFormular.getElterns()
                .stream()
                .filter(elternTeil -> elternTeil.getElternTyp() == elternTyp)
                .findFirst()
                .orElse(null);
        };

        final Consumer<ElternUpdateDto> setAdresseOfElternteil = elternteil -> {
            AdresseDto adresse = null;
            if (elternteil != null) {
                adresse = elternteil.getAdresse();
            }

            newFormular.getAuszahlung().setAdresse(adresse);
        };

        switch (newFormular.getAuszahlung().getKontoinhaber()) {
        case VATER -> setAdresseOfElternteil.accept(getElternteilOfTyp.apply(ElternTyp.VATER));
        case MUTTER -> setAdresseOfElternteil.accept(getElternteilOfTyp.apply(ElternTyp.MUTTER));
        }
    }

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
        resetEinnahmenKosten(newFormular, targetFormular);
        resetEltern(newFormular, targetFormular);
        resetLebenslaufItems(newFormular, targetFormular);
        resetPartner(newFormular, targetFormular);
        resetSteuerdatenTabs(newFormular, targetFormular);
    }

    @AfterMapping
    protected void resetDependentDataAfterUpdate(
        final GesuchFormular newFormular
    ) {
        resetSteuerdatenAfterUpdate(newFormular);
    }

    void resetEinnahmenKosten(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.isUpdateToEigenerHaushalt(newFormular),
            "Clear AuswaertigeMittagessenProWochen because Wohnsitz changed to not EIGENER_HAUSHALT",
            () -> {
                if (newFormular.getEinnahmenKosten() == null) {
                    return;
                }

                newFormular.getEinnahmenKosten().setAuswaertigeMittagessenProWoche(null);
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasGerichtlicheAlimenteregelungChanged(targetFormular, newFormular),
            "Clear Alimente because GerichtlicheAlimenteregelung has changed",
            () -> {
                if (newFormular.getEinnahmenKosten() == null) {
                    return;
                }

                newFormular.getEinnahmenKosten().setAlimente(null);
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(newFormular, targetFormular),
            "Clear Renten because Elternteil VERSTORBEN or UNBEKANNT",
            () -> {
                if (newFormular.getEinnahmenKosten() == null) {
                    return;
                }

                newFormular.getEinnahmenKosten().setRenten(null);
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasWohnsitzChanged(newFormular, targetFormular),
            "Clear Wohnkosten because wohnsitz changed",
            () -> {
                if (newFormular.getPersonInAusbildung() == null) {
                    return;
                }

                if (newFormular.getPersonInAusbildung().getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT &&
                    newFormular.getEinnahmenKosten() != null) {
                    newFormular.getEinnahmenKosten().setWohnkosten(null);
                    newFormular.getEinnahmenKosten().setWgWohnend(null);
                }
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasKinderChanged(newFormular, targetFormular),
            "Clear Betreuungskosten eigener Kinder because no Kinder",
            () -> {
                final var ek = newFormular.getEinnahmenKosten();
                if (ek != null && newFormular.getKinds().isEmpty()) {
                    ek.setBetreuungskostenKinder(null);
                }
            }
        );
    }

    void resetEltern(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasWerZahltAlimenteChanged(newFormular, targetFormular),
            "Clear Elternteil because werZahltAlimente changed",
            () -> {
                final var famsit = newFormular.getFamiliensituation();
                if (famsit == null) {
                    return;
                }

                final var werZahlt = newFormular.getFamiliensituation().getWerZahltAlimente();
                if (werZahlt == null) {
                    return;
                }

                switch (werZahlt) {
                case VATER -> removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                case MUTTER -> removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                case GEMEINSAM -> {
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                }
                }
            }
        );

        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasElternteilVerstorbenOrUnbekanntChanged(newFormular, targetFormular),
            "Clear Eltern because ElternteilUnbekanntVerstorben changed",
            () -> {
                if (newFormular.getFamiliensituation() == null) {
                    return;
                }

                if (newFormular.getFamiliensituation().getMutterUnbekanntVerstorben() != null &&
                    newFormular.getFamiliensituation().getMutterUnbekanntVerstorben()
                        != ElternAbwesenheitsGrund.WEDER_NOCH) {
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.MUTTER);
                }

                if (newFormular.getFamiliensituation().getVaterUnbekanntVerstorben() != null &&
                    newFormular.getFamiliensituation().getVaterUnbekanntVerstorben()
                        != ElternAbwesenheitsGrund.WEDER_NOCH) {
                    removeElternOfTyp(newFormular.getElterns(), ElternTyp.VATER);
                }
            }
        );
    }

    void resetLebenslaufItems(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasGeburtsdatumOfPersonInAusbildungChanged(targetFormular, newFormular),
            "Clear LebenslaufItems because Geburtsdatum has changed",
            () -> newFormular.setLebenslaufItems(new ArrayList<>())
        );
    }

    void resetPartner(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        resetFieldIf(
            () -> GesuchFormularDiffUtil.hasZivilstandChangedToOnePerson(targetFormular, newFormular),
            "Clear Partner because Zivilstand changed to one person",
            () -> {
                targetFormular.setPartner(null);
                newFormular.setPartner(null);
            }
        );
    }

    void resetSteuerdatenTabs(
        final GesuchFormularUpdateDto newFormular,
        final GesuchFormular targetFormular
    ) {
        if (newFormular.getSteuerdaten() == null || newFormular.getSteuerdaten().isEmpty()) {
            return;
        }

        if (newFormular.getFamiliensituation() == null) {
            newFormular.getSteuerdaten().clear();
        } else {
            if (targetFormular.getFamiliensituation() == null) {
                return;
            }

            final var targetFamsit = familiensituationMapper.partialUpdate(
                newFormular.getFamiliensituation(),
                targetFormular.getFamiliensituation()
            );

            final var requiredTabs = new HashSet<>(
                steuerdatenTabBerechnungsService.calculateTabs(targetFamsit)
            );

            newFormular.getSteuerdaten().removeAll(
                newFormular.getSteuerdaten()
                    .stream()
                    .filter(newTab -> !requiredTabs.contains(newTab.getSteuerdatenTyp()))
                    .toList()
            );
        }
    }

    void resetSteuerdatenAfterUpdate(
        final GesuchFormular gesuchFormular
    ) {
        gesuchFormular.getSteuerdaten().forEach(steuerdaten -> {
            if (!steuerdaten.getIsArbeitsverhaeltnisSelbstaendig()) {
                steuerdaten.setSaeule2(null);
                steuerdaten.setSaeule3a(null);
            }
        });
    }

    void removeElternOfTyp(final List<ElternUpdateDto> eltern, final ElternTyp typ) {
        if (eltern == null) {
            return;
        }

        eltern.removeAll(eltern.stream().filter(x -> x.getElternTyp() == typ).toList());
    }
}
