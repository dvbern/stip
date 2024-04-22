package ch.dvbern.stip.api.ausbildung.service;

import ch.dvbern.stip.api.common.service.MappingConfig;
import ch.dvbern.stip.api.common.type.Ausbildungsstufe;
import ch.dvbern.stip.api.common.type.Bildungsart;
import ch.dvbern.stip.generated.dto.BildungsartWithStufeDto;
import ch.dvbern.stip.generated.dto.BildungsstufeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

@Mapper(config = MappingConfig.class)
public abstract class BildungsartMapper {

//	@Mapping(target = "ausbildungsstufe", source = ".", qualifiedByName = "getAusbildungsstufeFromDto")
//	@Mapping(target = "bfs", source = ".", qualifiedByName = "getBfs")
//	@ValueMappings({
//			@ValueMapping(source = "bildungsart.GYMNASIALE_MATURITAETSSCHULEN", target = "GYMNASIALE_MATURITAETSSCHULEN"),
//			@ValueMapping(source = "bildungsart.SCHULEN_FUER_ALLGEMEINBILDUNG", target = "SCHULEN_FUER_ALLGEMEINBILDUNG"),
//			@ValueMapping(source = "bildungsart.VOLLZEITBERUFSSCHULEN", target = "VOLLZEITBERUFSSCHULEN"),
//			@ValueMapping(source = "bildungsart.LEHREN_ANLEHREN", target = "LEHREN_ANLEHREN"),
//			@ValueMapping(source = "bildungsart.BERUFSMATURITAET_NACH_LEHRE", target = "BERUFSMATURITAET_NACH_LEHRE"),
//			@ValueMapping(source = "bildungsart.HOEHERE_BERUFSBILDUNG", target = "HOEHERE_BERUFSBILDUNG"),
//			@ValueMapping(source = "bildungsart.FACHHOCHSCHULEN", target = "FACHHOCHSCHULEN"),
//			@ValueMapping(source = "bildungsart.UNIVERSITAETEN_ETH", target = "UNIVERSITAETEN_ETH")
//	})
//	public abstract Bildungsart toEntity(BildungsartWithStufeDto bildungsartWithStufeDto);

//	@Mapping(target = "bildungsart", source = ".", qualifiedByName = "getAusbildungsstufeFromEntity")
//	public abstract BildungsartWithStufeDto toDto(Bildungsart bildungsart);

	@Named("getAusbildungsstufeFromDto")
	Ausbildungsstufe getAusbildungsstufeFromDto(BildungsartWithStufeDto bildungsartWithStufeDto) {
		if (bildungsartWithStufeDto.getBildungsart().name().equals(Ausbildungsstufe.TERTIAER.name())) {
			return Ausbildungsstufe.TERTIAER;
		} else {
			return Ausbildungsstufe.SEKUNDAR_2;
		}
	}

	@Named("getAusbildungsstufeFromEntity")
	BildungsstufeDto getAusbildungsstufeFromEntity(Bildungsart bildungsart) {
		if (bildungsart.getAusbildungsstufe().name().equals(Ausbildungsstufe.TERTIAER.name())) {
			return BildungsstufeDto.TERTIAER;
		} else {
			return BildungsstufeDto.SEKUNDAR;
		}
	}

	@Named("getBfs")
	int getBfs(BildungsartWithStufeDto bildungsartWithStufeDto) {
		return 2;
	}
}
