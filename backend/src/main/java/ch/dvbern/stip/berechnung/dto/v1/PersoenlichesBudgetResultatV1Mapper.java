package ch.dvbern.stip.berechnung.dto.v1;

import java.util.List;

import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.PersoenlichesBudgetResultatMapper;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatDto;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;

@Singleton
@Slf4j
@DmnModelVersion(major = 1, minor = 0)
public class PersoenlichesBudgetResultatV1Mapper implements PersoenlichesBudgetResultatMapper {
    @Override
    public PersoenlichesBudgetresultatDto mapFromRequest(
        final DmnRequest request,
        final int einnahmenPersoenlichesBudget,
        final int ausgabenPersoenlichesBudget,
        final int persoenlichesbudgetBerechnet,
        final List<FamilienBudgetresultatDto> familienBudgetresultatList
    ) {
        final BerechnungRequestV1 berechnungsRequest = (BerechnungRequestV1) request;
        final var antragssteller = berechnungsRequest.getInputPersoenlichesBudget().antragssteller;
        final var stammdaten = berechnungsRequest.getStammdaten();

        var einkommen = antragssteller.getEinkommen();
        if (antragssteller.isTertiaerstufe()) {
            einkommen -= stammdaten.getEinkommensfreibetrag();
            einkommen = Integer.max(einkommen, 0);
        }
        var verpflegung = 0;
        if (!antragssteller.isEigenerHaushalt()) {
            final var wochenProJahr =
                berechnungsRequest.getInputPersoenlichesBudget().antragssteller.isLehre()
                    ? stammdaten.getAnzahlWochenLehre()
                    : stammdaten.getAnzahlWochenSchule();
            verpflegung = antragssteller.getVerpflegung() * wochenProJahr * stammdaten.getPreisProMahlzeit();
        }

        return new PersoenlichesBudgetresultatDto()
            .anzahlPersonenImHaushalt(antragssteller.getAnzahlPersonenImHaushalt())
            .anteilLebenshaltungskosten(
                getAnteilLebenshaltungskosten(familienBudgetresultatList, antragssteller)
            )
            .eigenerHaushalt(antragssteller.isEigenerHaushalt())
            .einkommen(einkommen)
            .leistungenEO(antragssteller.getLeistungenEO())
            .rente(antragssteller.getRente())
            .kinderAusbildungszulagen(antragssteller.getKinderAusbildungszulagen())
            .ergaenzungsleistungen(antragssteller.getErgaenzungsleistungen())
            .gemeindeInstitutionen(antragssteller.getGemeindeInstitutionen())
            .vermoegen(antragssteller.getVermoegen())
            .anteilFamilienbudget(
                getAnteilFamilienBudget(familienBudgetresultatList, antragssteller)
            )
            .einkommenPartner(antragssteller.getEinkommenPartner())
            .einnahmenPersoenlichesBudget(einnahmenPersoenlichesBudget)
            .grundbedarf(antragssteller.getGrundbedarf())
            .wohnkosten(antragssteller.getWohnkosten())
            .medizinischeGrundversorgung(antragssteller.getMedizinischeGrundversorgung())
            .steuernKantonGemeinde(antragssteller.getSteuern())
            .fahrkosten(antragssteller.getFahrkosten())
            .fahrkostenPartner(antragssteller.getFahrkostenPartner())
            .verpflegung(verpflegung)
            .verpflegungPartner(antragssteller.getVerpflegungPartner())
            .fremdbetreuung(antragssteller.getFremdbetreuung())
            .ausbildungskosten(antragssteller.getAusbildungskosten())
            .ausgabenPersoenlichesBudget(ausgabenPersoenlichesBudget)
            .persoenlichesbudgetBerechnet(persoenlichesbudgetBerechnet);
    }

    private static int getAnteilFamilienBudget(
        List<FamilienBudgetresultatDto> familienBudgetresultatList,
        AntragsstellerV1 antragssteller
    ) {
        int anteilFamilienBudget = 0;
        for (final FamilienBudgetresultatDto familienBudgetresultat : familienBudgetresultatList) {
            final var familienBudget = familienBudgetresultat.getFamilienbudgetBerechnet();
            if (familienBudget <= 0) {
                continue;
            }
            if (antragssteller.getAlter() < 25 || !antragssteller.isAbgeschlosseneErstausbildung()) {
                anteilFamilienBudget += familienBudget / (familienBudgetresultat.getAnzahlGeschwisterInAusbildung() + 1);
            } else {
                anteilFamilienBudget += (familienBudget / (familienBudgetresultat.getAnzahlGeschwisterInAusbildung() + 1)) / 2;
            }
        }
        return anteilFamilienBudget;
    }

    private static int getAnteilLebenshaltungskosten(
        List<FamilienBudgetresultatDto> familienBudgetresultatList,
        AntragsstellerV1 antragssteller
    ) {
        int anteilLebenshaltungskosten = 0;
        for (final FamilienBudgetresultatDto familienBudgetresultat : familienBudgetresultatList) {
            final var familienBudget = familienBudgetresultat.getFamilienbudgetBerechnet();
            if (familienBudget >= 0 || antragssteller.isEigenerHaushalt()) {
                continue;
            }
            anteilLebenshaltungskosten += familienBudget / familienBudgetresultat.getAnzahlPersonenImHaushalt();
        }
        return anteilLebenshaltungskosten;
    }
}
