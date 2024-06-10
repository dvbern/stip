package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
@JsonTypeName("antragssteller")
public class AntragsstellerV1 {
      boolean tertiaerstufe;
      int einkommen;
      int einkommenPartner;
      int vermoegen;
      int alimente;
      int rente;
      int kinderAusbildungszulagen;
      int ergaenzungsleistungen;
      int leistungenEO;
      int gemeindeInstitutionen;
      int alter;
      int grundbedarf;
      int wohnkosten;
      int medizinischeGrundversorgung;
      int ausbildungskosten;
      int steuern;
      int steuernKonkubinatspartner;
      int fahrkosten;
      int fahrkostenPartner;
      int verpflegung;
      int verpflegungPartner;
      int fremdbetreuung;
      int anteilFamilienbudget;
      boolean lehre;
      boolean eigenerHaushalt;
      boolean abgeschlosseneErstausbildung;

      public static AntragsstellerV1 fromGesuchFormular(final GesuchFormular gesuchFormular) {
          final var personInAusbildung = gesuchFormular.getPersonInAusbildung();
          final var partner = gesuchFormular.getPartner();
          final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();

          return new AntragsstellerV1Builder()
              .tertiaerstufe(false)
              .einkommen(einnahmenKosten.getNettoerwerbseinkommen())
              .einkommenPartner(partner.getJahreseinkommen() != null ? partner.getJahreseinkommen() : 0)
              .vermoegen(personInAusbildung.getVermoegenVorjahr() != null ? personInAusbildung.getVermoegenVorjahr() : 0)
              .alimente(einnahmenKosten.getAlimente() != null ? einnahmenKosten.getAlimente() : 0)
              .rente(einnahmenKosten.getRenten() != null ? einnahmenKosten.getRenten() : 0)
              .kinderAusbildungszulagen(einnahmenKosten.getZulagen() != null ? einnahmenKosten.getZulagen() : 0)
              .ergaenzungsleistungen(einnahmenKosten.getErgaenzungsleistungen() != null ? einnahmenKosten.getErgaenzungsleistungen() : 0)
              .leistungenEO(einnahmenKosten.getEoLeistungen() != null ? einnahmenKosten.getEoLeistungen() : 0)
              .gemeindeInstitutionen(0)
              .alter((int) ChronoUnit.YEARS.between(LocalDate.now(), personInAusbildung.getGeburtsdatum()))
              .grundbedarf(0)
              .wohnkosten(einnahmenKosten.getWohnkosten() != null ? einnahmenKosten.getWohnkosten() : 0)
              .medizinischeGrundversorgung(0)
              .ausbildungskosten(einnahmenKosten.getAusbildungskostenTertiaerstufe() != null ? einnahmenKosten.getAusbildungskostenTertiaerstufe() : 0)
              .steuern(0)
              .steuernKonkubinatspartner(0)
              .fahrkosten(einnahmenKosten.getFahrkosten())
              .fahrkostenPartner(partner.getFahrkosten() != null ? partner.getFahrkosten() : 0)
              .verpflegung(0)
              .verpflegungPartner(partner.getVerpflegungskosten() != null ? partner.getVerpflegungskosten() : 0)
              .fremdbetreuung(0)
              .anteilFamilienbudget(0)
              .lehre(true)
              .eigenerHaushalt(personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT)
              .abgeschlosseneErstausbildung(true)
              .build();
      }

      public static AntragsstellerV1 withDefaults() {
          return new AntragsstellerV1Builder()
              .tertiaerstufe(false)
              .einkommen(0)
              .einkommenPartner(0)
              .vermoegen(0)
              .alimente(0)
              .rente(0)
              .kinderAusbildungszulagen(0)
              .ergaenzungsleistungen(0)
              .leistungenEO(0)
              .gemeindeInstitutionen(0)
              .alter(0)
              .grundbedarf(0)
              .wohnkosten(0)
              .medizinischeGrundversorgung(0)
              .ausbildungskosten(0)
              .steuern(0)
              .steuernKonkubinatspartner(0)
              .fahrkosten(0)
              .fahrkostenPartner(0)
              .verpflegung(0)
              .verpflegungPartner(0)
              .fremdbetreuung(0)
              .anteilFamilienbudget(0)
              .lehre(false)
              .eigenerHaushalt(false)
              .abgeschlosseneErstausbildung(false)
              .build();
      }
}
