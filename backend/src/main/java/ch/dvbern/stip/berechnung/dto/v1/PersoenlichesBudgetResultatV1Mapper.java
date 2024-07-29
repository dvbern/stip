package ch.dvbern.stip.berechnung.dto.v1;

import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.PersoenlichesBudgetResultatMapper;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import jakarta.inject.Singleton;

@Singleton
@DmnModelVersion(major = 1, minor = 0)
public class PersoenlichesBudgetResultatV1Mapper implements PersoenlichesBudgetResultatMapper {
    @Override
    public PersoenlichesBudgetresultatDto mapFromRequest(
        final DmnRequest request,
        final int einnahmenPersoenlichesBudget,
        final int ausgabenPersoenlichesBudget,
        final int persoenlichesbudgetBerechnet
    ) {
        final BerechnungRequestV1 berechnungsRequest = (BerechnungRequestV1) request;
        final var antragssteller = berechnungsRequest.getInputPersoenlichesBudget().antragssteller;

        return new PersoenlichesBudgetresultatDto()
            .eigenerHaushalt(antragssteller.isEigenerHaushalt())
            .einkommen(antragssteller.getEinkommen())
            .leistungenEO(antragssteller.getLeistungenEO())
            .rente(antragssteller.getRente())
            .kinderAusbildungszulagen(antragssteller.getKinderAusbildungszulagen())
            .ergaenzungsleistungen(antragssteller.getErgaenzungsleistungen())
            .gemeindeInstitutionen(antragssteller.getGemeindeInstitutionen())
            .vermoegen(antragssteller.getVermoegen())
            .anteilFamilienbudget(antragssteller.getAnteilFamilienbudget())
            .einkommenPartner(antragssteller.getEinkommenPartner())
            .einnahmenPersoenlichesBudget(einnahmenPersoenlichesBudget)
            .grundbedarf(antragssteller.getGrundbedarf())
            .wohnkosten(antragssteller.getWohnkosten())
            .medizinischeGrundversorgung(antragssteller.getMedizinischeGrundversorgung())
            .steuernKantonGemeinde(antragssteller.getSteuern())
            .fahrkosten(antragssteller.getFahrkosten())
            .fahrkostenPartner(antragssteller.getFahrkostenPartner())
            .verpflegung(antragssteller.getVerpflegung())
            .verpflegungPartner(antragssteller.getVerpflegungPartner())
            .fremdbetreuung(antragssteller.getFremdbetreuung())
            .ausbildungskosten(antragssteller.getAusbildungskosten())
            .ausgabenPersoenlichesBudget(ausgabenPersoenlichesBudget)
            .persoenlichesbudgetBerechnet(persoenlichesbudgetBerechnet);
    }
}
