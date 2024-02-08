package ch.dvbern.stip.api.gesuch.service;

import ch.dvbern.stip.api.adresse.service.AdresseMapper;
import ch.dvbern.stip.api.ausbildung.service.AusbildungMapper;
import ch.dvbern.stip.api.auszahlung.service.AuszahlungMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMapper;
import ch.dvbern.stip.api.eltern.entity.Eltern;
import ch.dvbern.stip.api.eltern.service.ElternMapper;
import ch.dvbern.stip.api.eltern.type.ElternTyp;
import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.familiensituation.service.FamiliensituationMapper;
import ch.dvbern.stip.api.geschwister.service.GeschwisterMapper;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.kind.service.KindMapper;
import ch.dvbern.stip.api.lebenslauf.service.LebenslaufItemMapper;
import ch.dvbern.stip.api.partner.service.PartnerMapper;
import ch.dvbern.stip.api.personinausbildung.service.PersonInAusbildungMapper;
import ch.dvbern.stip.generated.dto.ElternUpdateDto;
import ch.dvbern.stip.generated.dto.FamiliensituationUpdateDto;
import ch.dvbern.stip.generated.dto.GesuchFormularDto;
import ch.dvbern.stip.generated.dto.GesuchFormularUpdateDto;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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
public abstract class GesuchFormularMapper {
    public abstract GesuchFormular toEntity(GesuchFormularDto gesuchFormularDto);

    public abstract GesuchFormularDto toDto(GesuchFormular gesuchFormular);

    /**
     * partial update mapper for the Gesuchssteller
     *
     * @param gesuchFormularUpdateDto
     * @param gesuchFormular
     * @return
     */
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    public abstract GesuchFormular partialUpdate(GesuchFormularUpdateDto gesuchFormularUpdateDto, @MappingTarget GesuchFormular gesuchFormular);


    @BeforeMapping
    public void clearDataOnUpdate(
        GesuchFormularUpdateDto gesuchFormularUpdateDto,
        @MappingTarget GesuchFormular gesuchFormular) {
        clearParentOnAlimonyChange(gesuchFormularUpdateDto, gesuchFormular);
    }

    private void clearParentOnAlimonyChange(GesuchFormularUpdateDto updateDto, GesuchFormular entity) {
        if (entity.getFamiliensituation() == null || updateDto.getFamiliensituation() == null) {
            return;
        }

        if (entity.getFamiliensituation().getWerZahltAlimente() == updateDto.getFamiliensituation().getWerZahltAlimente()) {
            return;
        }

        final var werZahlt = updateDto.getFamiliensituation().getWerZahltAlimente();
        if (werZahlt == null) {
            return;
        }

        switch (werZahlt) {
            case VATER -> removeElternOfTyp(updateDto.getElterns(), ElternTyp.VATER);
            case MUTTER -> removeElternOfTyp(updateDto.getElterns(), ElternTyp.MUTTER);
            case GEMEINSAM -> {
                removeElternOfTyp(updateDto.getElterns(), ElternTyp.MUTTER);
                removeElternOfTyp(updateDto.getElterns(), ElternTyp.VATER);
            }
        }
    }

    void removeElternOfTyp(List<ElternUpdateDto> eltern, ElternTyp typ) {
        if (eltern == null) {
            return;
        }

        final var elternToRemove = new HashSet<ElternUpdateDto>();
        for (ElternUpdateDto elternUpdateDto : eltern) {
            if (elternUpdateDto.getElternTyp() == typ) {
                elternToRemove.add(elternUpdateDto);
            }
        }

        eltern.removeAll(elternToRemove);
    }
}
