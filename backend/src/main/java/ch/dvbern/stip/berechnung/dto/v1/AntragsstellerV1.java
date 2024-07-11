package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Data
@Builder
@Jacksonized
@Value
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
          final var antragsstellerBuilder = new AntragsstellerV1Builder();
          antragsstellerBuilder
              .tertiaerstufe(false)
              .einkommen(einnahmenKosten.getNettoerwerbseinkommen())
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
              .verpflegung(0)
              .fremdbetreuung(0)
              .anteilFamilienbudget(0)
              .lehre(true)
              .eigenerHaushalt(personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT)
              .abgeschlosseneErstausbildung(true);

          if (partner != null) {
              antragsstellerBuilder
                  .einkommenPartner(partner.getJahreseinkommen() != null ? partner.getJahreseinkommen() : 0)
                  .fahrkostenPartner(partner.getFahrkosten() != null ? partner.getFahrkosten() : 0)
                  .verpflegungPartner(partner.getVerpflegungskosten() != null ? partner.getVerpflegungskosten() : 0);
          }

          return antragsstellerBuilder.build();
      }

      public static AntragsstellerV1 withDefaults() {
          return new AntragsstellerV1Builder().build();
      }
}
