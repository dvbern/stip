package ch.dvbern.stip.berechnung.dto.v1;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import ch.dvbern.stip.berechnung.dto.DmnModelVersion;
import ch.dvbern.stip.berechnung.dto.DmnRequest;
import ch.dvbern.stip.berechnung.dto.FamilienBudgetresultatMapper;
import ch.dvbern.stip.berechnung.dto.v1.BerechnungRequestV1.InputFamilienbudgetV1;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatDto;
import jakarta.inject.Singleton;

@Singleton
@DmnModelVersion(major = 1, minor = 0)
public class FamilienBudgetresultatV1Mapper implements FamilienBudgetresultatMapper {
    @Override
    public FamilienBudgetresultatDto mapFromRequest(
        DmnRequest request,
        final SteuerdatenTyp steuerdatenTyp,
        final int budgetToUse,
        final int einnahmenFamilienbudget,
        final int ausgabenFamilienbudget,
        final int familienbudgetBerechnet
    ) {
        final BerechnungRequestV1 berechnungsRequest = (BerechnungRequestV1) request;
        InputFamilienbudgetV1 inputFamilienbudget = null;
        switch (budgetToUse) {
            case 0 -> inputFamilienbudget = berechnungsRequest.getInputFamilienbudget1();
            case 1 -> inputFamilienbudget = berechnungsRequest.getInputFamilienbudget2();
            default -> throw new IllegalArgumentException("Budget " + budgetToUse + " is not a possible choice");
        }
        final var elternteil = inputFamilienbudget.elternteil;

        int anrechenbaresVermoegen = elternteil.getSteuerbaresVermoegen();
        if (elternteil.isSelbststaendigErwerbend()) {
            anrechenbaresVermoegen = Integer.max(
                anrechenbaresVermoegen - berechnungsRequest.getStammdaten().getFreibetragVermoegen(), 0
            );
        }
        anrechenbaresVermoegen = BigDecimal.valueOf(
                anrechenbaresVermoegen * berechnungsRequest.getStammdaten().getVermoegensanteilInProzent() / 100.0
            ).setScale(0, RoundingMode.HALF_UP).intValue();

        return new FamilienBudgetresultatDto()
            .familienBudgetTyp(steuerdatenTyp)
            .selbststaendigErwerbend(elternteil.isSelbststaendigErwerbend())
            .anzahlPersonenImHaushalt(elternteil.getAnzahlPersonenImHaushalt())
            .anzahlGeschwisterInAusbildung(elternteil.getAnzahlGeschwisterInAusbildung())
            .totalEinkuenfte(elternteil.getTotalEinkuenfte())
            .ergaenzungsleistungen(elternteil.getErgaenzungsleistungen())
            .steuerbaresVermoegen(elternteil.getSteuerbaresVermoegen())
            .anrechenbaresVermoegen(anrechenbaresVermoegen)
            .saeule2(elternteil.getEinzahlungSaeule2())
            .saeule3a(
                Integer.max(
                    elternteil.getEinzahlungSaeule3a() - berechnungsRequest.getStammdaten().getMaxSaeule3a(),
                    0
                )
            )
            .eigenmietwert(elternteil.getEigenmietwert())
            .alimente(elternteil.getAlimente())
            .einnahmenFamilienbudget(einnahmenFamilienbudget)
            .grundbedarf(elternteil.getGrundbedarf())
            .effektiveWohnkosten(elternteil.getEffektiveWohnkosten())
            .medizinischeGrundversorgung(elternteil.getMedizinischeGrundversorgung())
            .integrationszulage(elternteil.getIntegrationszulage())
            .steuernBund(elternteil.getSteuernBund())
            .steuernKantonGemeinde(elternteil.getSteuernStaat())
            .fahrkostenPerson1(elternteil.getFahrkostenPerson1())
            .fahrkostenPerson2(elternteil.getFahrkostenPerson2())
            .essenskostenPerson1(elternteil.getEssenskostenPerson1())
            .essenskostenPerson2(elternteil.getEssenskostenPerson2())
            .ausgabenFamilienbudget(ausgabenFamilienbudget)
            .familienbudgetBerechnet(familienbudgetBerechnet);
    }
}
