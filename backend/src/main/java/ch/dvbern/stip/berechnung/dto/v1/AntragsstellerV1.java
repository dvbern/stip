package ch.dvbern.stip.berechnung.dto.v1;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe;
import ch.dvbern.stip.api.common.type.Wohnsitz;
import ch.dvbern.stip.api.einnahmen_kosten.entity.EinnahmenKosten;
import ch.dvbern.stip.api.einnahmen_kosten.service.EinnahmenKostenMappingUtil;
import ch.dvbern.stip.api.gesuch.entity.GesuchFormular;
import ch.dvbern.stip.api.gesuchsperioden.entity.Gesuchsperiode;
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

      public static AntragsstellerV1 buildFromDependants(
          final GesuchFormular gesuchFormular
      ) {
          final var personInAusbildung = gesuchFormular.getPersonInAusbildung();
          final var partner = gesuchFormular.getPartner();
          final var einnahmenKosten = gesuchFormular.getEinnahmenKosten();
          final var gesuchsperiode = gesuchFormular.getTranche().getGesuch().getGesuchsperiode();
          final var ausbildung = gesuchFormular.getAusbildung();

          final AntragsstellerV1Builder builder = new AntragsstellerV1Builder();
          builder.tertiaerstufe(false);
          builder.einkommen(einnahmenKosten.getNettoerwerbseinkommen());
          builder.vermoegen(Objects.requireNonNullElse(einnahmenKosten.getVermoegen(), 0));
          builder.alimente(Objects.requireNonNullElse(einnahmenKosten.getAlimente(), 0));
          builder.rente(Objects.requireNonNullElse(einnahmenKosten.getRenten(), 0));
          builder.kinderAusbildungszulagen(Objects.requireNonNullElse(einnahmenKosten.getZulagen(), 0));
          builder.ergaenzungsleistungen(Objects.requireNonNullElse(einnahmenKosten.getErgaenzungsleistungen(), 0));
          builder.leistungenEO(Objects.requireNonNullElse(einnahmenKosten.getEoLeistungen(), 0));
          // TODO: builder.gemeindeInstitutionen();
          int alter = (int) ChronoUnit.YEARS.between(personInAusbildung.getGeburtsdatum(), LocalDate.now());
          builder.alter(alter);

          int medizinischeGrundversorgung = 0;
          int anzahlPersonenImHaushalt = 0;
          if (personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT) {
              anzahlPersonenImHaushalt = 1;
              medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(alter, gesuchsperiode);
              if (partner != null) {
                  anzahlPersonenImHaushalt += 1;
                  medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                      (int) ChronoUnit.YEARS.between(partner.getGeburtsdatum(), LocalDate.now()),
                      gesuchsperiode
                  );
              }
              for (final var kind : gesuchFormular.getKinds()) {
                  if (kind.getWohnsitz() != Wohnsitz.EIGENER_HAUSHALT) {
                      anzahlPersonenImHaushalt += 1;
                      medizinischeGrundversorgung += BerechnungRequestV1.getMedizinischeGrundversorgung(
                          (int) ChronoUnit.YEARS.between(kind.getGeburtsdatum(), LocalDate.now()), gesuchsperiode
                      );
                  }
              }

              builder.grundbedarf(
                  BerechnungRequestV1.getGrundbedarf(gesuchsperiode, anzahlPersonenImHaushalt)
              );
              // TODO: adapt for ig wohhaft in WG einnahmenKosten.getWgWohnend();
          } else {
              builder.grundbedarf(0);
          }
          builder.medizinischeGrundversorgung(medizinischeGrundversorgung);

          builder.wohnkosten(0);
          if (einnahmenKosten.getWohnkosten() != null && anzahlPersonenImHaushalt > 0) {
              builder.wohnkosten(
                  BerechnungRequestV1.getEffektiveWohnkosten(einnahmenKosten.getWohnkosten(), gesuchsperiode, anzahlPersonenImHaushalt)
              );
          }

          builder.ausbildungskosten(
              getAusbildungskosten(
                  einnahmenKosten,
                  gesuchsperiode,
                  ausbildung.getAusbildungsgang().getBildungskategorie().getBildungsstufe()
              )
          );
          builder.steuern(
              EinnahmenKostenMappingUtil.calculateSteuern(gesuchFormular)
              // TODO: + einnahmenKosten.getSteuernStaat() + einnahmenKosten.getSteuernBund()
          );
          builder.fahrkosten(Objects.requireNonNullElse(einnahmenKosten.getFahrkosten(), 0));
          builder.verpflegung(Objects.requireNonNullElse(einnahmenKosten.getAuswaertigeMittagessenProWoche(), 0));
          builder.fremdbetreuung(Objects.requireNonNullElse(einnahmenKosten.getBetreuungskostenKinder(), 0));
          // TODO: builder.anteilFamilienbudget(Objects.requireNonNullElse());
          // TODO: builder.lehre(Objects.requireNonNullElse());
          builder.eigenerHaushalt(personInAusbildung.getWohnsitz() == Wohnsitz.EIGENER_HAUSHALT);
          // TODO: builder.abgeschlosseneErstausbildung(Objects.requireNonNullElse());

          if (partner != null) {
              builder.einkommenPartner(Objects.requireNonNullElse(partner.getJahreseinkommen(), 0));
              //TODO: builder.steuernKonkubinatspartner();
              builder.fahrkostenPartner(Objects.requireNonNullElse(partner.getFahrkosten(), 0));
              builder.verpflegungPartner(Objects.requireNonNullElse(partner.getVerpflegungskosten(), 0));
          }
          return builder.build();
      }

      private static int getAusbildungskosten(
          final EinnahmenKosten einnahmenKosten,
          final Gesuchsperiode gesuchsperiode,
          final Bildungsstufe bildungsstufe
      ) {
          return switch (bildungsstufe) {
              case SEKUNDAR_2 -> Integer.min(
                  Objects.requireNonNullElse(einnahmenKosten.getAusbildungskostenSekundarstufeZwei(), 0),
                  gesuchsperiode.getAusbKostenSekII()
              );
              case TERTIAER -> Integer.min(
                  Objects.requireNonNullElse(einnahmenKosten.getAusbildungskostenTertiaerstufe(), 0),
                  gesuchsperiode.getAusbKostenTertiaer()
              );
          };
      }


}
