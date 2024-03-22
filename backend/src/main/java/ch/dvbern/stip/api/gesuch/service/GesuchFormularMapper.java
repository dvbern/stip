package ch.dvbern.stip.api.gesuch.service;

import java.util.List;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuch.util.GesuchFormularDiffUtil;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
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
            EinnahmenKostenMapper.class
        })
public abstract class GesuchFormularMapper extends EntityUpdateMapper<GesuchFormularUpdateDto, GesuchFormular> {
    public abstract GesuchFormular toEntity(GesuchFormularDto gesuchFormularDto);

    public abstract GesuchFormularDto toDto(GesuchFormular gesuchFormular);

    /**
     * partial update mapper for the Gesuchssteller
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract GesuchFormular partialUpdate(
        GesuchFormularUpdateDto gesuchFormularUpdateDto,
        @MappingTarget GesuchFormular gesuchFormular);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final GesuchFormularUpdateDto newFormular,
        final @MappingTarget GesuchFormular targetFormular
    ) {
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
    }

    void removeElternOfTyp(List<ElternUpdateDto> eltern, ElternTyp typ) {
        eltern.removeAll(eltern.stream().filter(x -> x.getElternTyp() == typ).toList());
    }
}
