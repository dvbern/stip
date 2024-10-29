package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.ausbildung.entity.Ausbildung;
import ch.dvbern.stip.api.ausbildung.util.AusbildungDiffUtil;
import ch.dvbern.stip.api.common.service.DateMapper;
import ch.dvbern.stip.api.common.service.DateToMonthYear;
import ch.dvbern.stip.api.common.service.EntityUpdateMapper;
import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.service.MonthYearToBeginOfMonth;
import ch.dvbern.stip.api.common.service.MonthYearToEndOfMonth;
import ch.dvbern.stip.api.fall.service.FallMapper;
import ch.dvbern.stip.generated.dto.AusbildungDto;
import ch.dvbern.stip.generated.dto.AusbildungUpdateDto;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(config = MappingConfig.class, uses = { FallMapper.class, AusbildungsgangMapper.class })
public abstract class AusbildungMapper extends EntityUpdateMapper<AusbildungUpdateDto, Ausbildung> {
//    @Mapping(source = "ausbildungsgang.id", target = "ausbildungsgang.id")
//    @Mapping(
//        source = "ausbildungBegin",
//        target = "ausbildungBegin",
//        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
//    )
//    @Mapping(
//        source = "ausbildungEnd",
//        target = "ausbildungEnd",
//        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
//    )
//    public abstract Ausbildung toEntity(AusbildungDto ausbildungDto);

    @Mapping(source = "ausbildungsgang.id", target = "ausbildungsgang.id")
    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, DateToMonthYear.class }
    )
    public abstract AusbildungDto toDto(Ausbildung ausbildung);

    @Mapping(source = "ausbildungsgangId", target = "ausbildungsgang.id")
    @Mapping(
        source = "ausbildungBegin",
        target = "ausbildungBegin",
        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
    )
    @Mapping(
        source = "ausbildungEnd",
        target = "ausbildungEnd",
        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
    )
    @Mapping(source = "fallId", target = "fall.id")
    public abstract Ausbildung toNewEntity(AusbildungUpdateDto ausbildungDto);

//    @Mapping(source = "ausbildungsgangId",
//        target = "ausbildungsgang",
//        qualifiedBy = { EntityReferenceMapper.class, EntityIdReference.class })
//    @Mapping(
//        source = "ausbildungBegin",
//        target = "ausbildungBegin",
//        qualifiedBy = { DateMapper.class, MonthYearToBeginOfMonth.class }
//    )
//    @Mapping(
//        source = "ausbildungEnd",
//        target = "ausbildungEnd",
//        qualifiedBy = { DateMapper.class, MonthYearToEndOfMonth.class }
//    )
//    public abstract Ausbildung partialUpdate(AusbildungUpdateDto ausbildungDto, @MappingTarget Ausbildung ausbildung);

    @Override
    @BeforeMapping
    protected void resetDependentDataBeforeUpdate(
        final AusbildungUpdateDto newFormular,
        final @MappingTarget Ausbildung targetFormular
    ) {
        resetFieldIf(
            () -> AusbildungDiffUtil.hasIsAusbildungAuslandChanged(targetFormular, newFormular),
            "Clear Ausbildungsort because IsAusbildungAusland has changed",
            () -> {
                if (newFormular != null && Boolean.TRUE.equals(newFormular.getIsAusbildungAusland())) {
                    newFormular.setAusbildungsort(null);
                }
            }
        );
    }
}
