package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * Familien Budget daten fuer und von der Berechnung
 **/

@JsonTypeName("FamilienBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class FamilienBudgetresultatDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String sozialversicherungsnummer;
  private @Valid LocalDate geburtsdatum;
  private @Valid Integer steuerjahr;
  private @Valid String veranlagungscode;
  private @Valid Integer total;
  private @Valid Integer einnahmenMinusKosten;
  private @Valid Integer anzahlPersonenImHaushalt;
  private @Valid Integer anzahlKinderInAusbildung;
  private @Valid Integer einnahmeUeberschuss;
  private @Valid Integer proKopfTeilungKinderInAusbildung;
  private @Valid Integer anrechenbareElterlicheLeistung;
  private @Valid Integer halbierungsReduktion;
  private @Valid Integer fehlbetrag;
  private @Valid Integer proKopfTeilung;
  private @Valid Integer ungedeckterAnteilLebenshaltungskosten;
  private @Valid FamilienBudgetresultatEinnahmenDto einnahmen;
  private @Valid FamilienBudgetresultatKostenDto kosten;
  private @Valid String vornamePartner;
  private @Valid String nachnamePartner;

  /**
   **/
  public FamilienBudgetresultatDto steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
    return this;
  }

  
  @JsonProperty("steuerdatenTyp")
  @NotNull
  public ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp getSteuerdatenTyp() {
    return steuerdatenTyp;
  }

  @JsonProperty("steuerdatenTyp")
  public void setSteuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
  }

  /**
   **/
  public FamilienBudgetresultatDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public FamilienBudgetresultatDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public FamilienBudgetresultatDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummer")
  @NotNull
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   * dd.MM.yyyy
   **/
  public FamilienBudgetresultatDto geburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public FamilienBudgetresultatDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty("steuerjahr")
  @NotNull
  public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty("steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   **/
  public FamilienBudgetresultatDto veranlagungscode(String veranlagungscode) {
    this.veranlagungscode = veranlagungscode;
    return this;
  }

  
  @JsonProperty("veranlagungscode")
  @NotNull
  public String getVeranlagungscode() {
    return veranlagungscode;
  }

  @JsonProperty("veranlagungscode")
  public void setVeranlagungscode(String veranlagungscode) {
    this.veranlagungscode = veranlagungscode;
  }

  /**
   **/
  public FamilienBudgetresultatDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty("total")
  @NotNull
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public FamilienBudgetresultatDto einnahmenMinusKosten(Integer einnahmenMinusKosten) {
    this.einnahmenMinusKosten = einnahmenMinusKosten;
    return this;
  }

  
  @JsonProperty("einnahmenMinusKosten")
  @NotNull
  public Integer getEinnahmenMinusKosten() {
    return einnahmenMinusKosten;
  }

  @JsonProperty("einnahmenMinusKosten")
  public void setEinnahmenMinusKosten(Integer einnahmenMinusKosten) {
    this.einnahmenMinusKosten = einnahmenMinusKosten;
  }

  /**
   **/
  public FamilienBudgetresultatDto anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
    return this;
  }

  
  @JsonProperty("anzahlPersonenImHaushalt")
  @NotNull
  public Integer getAnzahlPersonenImHaushalt() {
    return anzahlPersonenImHaushalt;
  }

  @JsonProperty("anzahlPersonenImHaushalt")
  public void setAnzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
    this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
  }

  /**
   **/
  public FamilienBudgetresultatDto anzahlKinderInAusbildung(Integer anzahlKinderInAusbildung) {
    this.anzahlKinderInAusbildung = anzahlKinderInAusbildung;
    return this;
  }

  
  @JsonProperty("anzahlKinderInAusbildung")
  @NotNull
  public Integer getAnzahlKinderInAusbildung() {
    return anzahlKinderInAusbildung;
  }

  @JsonProperty("anzahlKinderInAusbildung")
  public void setAnzahlKinderInAusbildung(Integer anzahlKinderInAusbildung) {
    this.anzahlKinderInAusbildung = anzahlKinderInAusbildung;
  }

  /**
   **/
  public FamilienBudgetresultatDto einnahmeUeberschuss(Integer einnahmeUeberschuss) {
    this.einnahmeUeberschuss = einnahmeUeberschuss;
    return this;
  }

  
  @JsonProperty("einnahmeUeberschuss")
  @NotNull
  public Integer getEinnahmeUeberschuss() {
    return einnahmeUeberschuss;
  }

  @JsonProperty("einnahmeUeberschuss")
  public void setEinnahmeUeberschuss(Integer einnahmeUeberschuss) {
    this.einnahmeUeberschuss = einnahmeUeberschuss;
  }

  /**
   **/
  public FamilienBudgetresultatDto proKopfTeilungKinderInAusbildung(Integer proKopfTeilungKinderInAusbildung) {
    this.proKopfTeilungKinderInAusbildung = proKopfTeilungKinderInAusbildung;
    return this;
  }

  
  @JsonProperty("proKopfTeilungKinderInAusbildung")
  @NotNull
  public Integer getProKopfTeilungKinderInAusbildung() {
    return proKopfTeilungKinderInAusbildung;
  }

  @JsonProperty("proKopfTeilungKinderInAusbildung")
  public void setProKopfTeilungKinderInAusbildung(Integer proKopfTeilungKinderInAusbildung) {
    this.proKopfTeilungKinderInAusbildung = proKopfTeilungKinderInAusbildung;
  }

  /**
   **/
  public FamilienBudgetresultatDto anrechenbareElterlicheLeistung(Integer anrechenbareElterlicheLeistung) {
    this.anrechenbareElterlicheLeistung = anrechenbareElterlicheLeistung;
    return this;
  }

  
  @JsonProperty("anrechenbareElterlicheLeistung")
  @NotNull
  public Integer getAnrechenbareElterlicheLeistung() {
    return anrechenbareElterlicheLeistung;
  }

  @JsonProperty("anrechenbareElterlicheLeistung")
  public void setAnrechenbareElterlicheLeistung(Integer anrechenbareElterlicheLeistung) {
    this.anrechenbareElterlicheLeistung = anrechenbareElterlicheLeistung;
  }

  /**
   **/
  public FamilienBudgetresultatDto halbierungsReduktion(Integer halbierungsReduktion) {
    this.halbierungsReduktion = halbierungsReduktion;
    return this;
  }

  
  @JsonProperty("halbierungsReduktion")
  @NotNull
  public Integer getHalbierungsReduktion() {
    return halbierungsReduktion;
  }

  @JsonProperty("halbierungsReduktion")
  public void setHalbierungsReduktion(Integer halbierungsReduktion) {
    this.halbierungsReduktion = halbierungsReduktion;
  }

  /**
   **/
  public FamilienBudgetresultatDto fehlbetrag(Integer fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
    return this;
  }

  
  @JsonProperty("fehlbetrag")
  @NotNull
  public Integer getFehlbetrag() {
    return fehlbetrag;
  }

  @JsonProperty("fehlbetrag")
  public void setFehlbetrag(Integer fehlbetrag) {
    this.fehlbetrag = fehlbetrag;
  }

  /**
   **/
  public FamilienBudgetresultatDto proKopfTeilung(Integer proKopfTeilung) {
    this.proKopfTeilung = proKopfTeilung;
    return this;
  }

  
  @JsonProperty("proKopfTeilung")
  @NotNull
  public Integer getProKopfTeilung() {
    return proKopfTeilung;
  }

  @JsonProperty("proKopfTeilung")
  public void setProKopfTeilung(Integer proKopfTeilung) {
    this.proKopfTeilung = proKopfTeilung;
  }

  /**
   **/
  public FamilienBudgetresultatDto ungedeckterAnteilLebenshaltungskosten(Integer ungedeckterAnteilLebenshaltungskosten) {
    this.ungedeckterAnteilLebenshaltungskosten = ungedeckterAnteilLebenshaltungskosten;
    return this;
  }

  
  @JsonProperty("ungedeckterAnteilLebenshaltungskosten")
  @NotNull
  public Integer getUngedeckterAnteilLebenshaltungskosten() {
    return ungedeckterAnteilLebenshaltungskosten;
  }

  @JsonProperty("ungedeckterAnteilLebenshaltungskosten")
  public void setUngedeckterAnteilLebenshaltungskosten(Integer ungedeckterAnteilLebenshaltungskosten) {
    this.ungedeckterAnteilLebenshaltungskosten = ungedeckterAnteilLebenshaltungskosten;
  }

  /**
   **/
  public FamilienBudgetresultatDto einnahmen(FamilienBudgetresultatEinnahmenDto einnahmen) {
    this.einnahmen = einnahmen;
    return this;
  }

  
  @JsonProperty("einnahmen")
  @NotNull
  public FamilienBudgetresultatEinnahmenDto getEinnahmen() {
    return einnahmen;
  }

  @JsonProperty("einnahmen")
  public void setEinnahmen(FamilienBudgetresultatEinnahmenDto einnahmen) {
    this.einnahmen = einnahmen;
  }

  /**
   **/
  public FamilienBudgetresultatDto kosten(FamilienBudgetresultatKostenDto kosten) {
    this.kosten = kosten;
    return this;
  }

  
  @JsonProperty("kosten")
  @NotNull
  public FamilienBudgetresultatKostenDto getKosten() {
    return kosten;
  }

  @JsonProperty("kosten")
  public void setKosten(FamilienBudgetresultatKostenDto kosten) {
    this.kosten = kosten;
  }

  /**
   **/
  public FamilienBudgetresultatDto vornamePartner(String vornamePartner) {
    this.vornamePartner = vornamePartner;
    return this;
  }

  
  @JsonProperty("vornamePartner")
  public String getVornamePartner() {
    return vornamePartner;
  }

  @JsonProperty("vornamePartner")
  public void setVornamePartner(String vornamePartner) {
    this.vornamePartner = vornamePartner;
  }

  /**
   **/
  public FamilienBudgetresultatDto nachnamePartner(String nachnamePartner) {
    this.nachnamePartner = nachnamePartner;
    return this;
  }

  
  @JsonProperty("nachnamePartner")
  public String getNachnamePartner() {
    return nachnamePartner;
  }

  @JsonProperty("nachnamePartner")
  public void setNachnamePartner(String nachnamePartner) {
    this.nachnamePartner = nachnamePartner;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FamilienBudgetresultatDto familienBudgetresultat = (FamilienBudgetresultatDto) o;
    return Objects.equals(this.steuerdatenTyp, familienBudgetresultat.steuerdatenTyp) &&
        Objects.equals(this.vorname, familienBudgetresultat.vorname) &&
        Objects.equals(this.nachname, familienBudgetresultat.nachname) &&
        Objects.equals(this.sozialversicherungsnummer, familienBudgetresultat.sozialversicherungsnummer) &&
        Objects.equals(this.geburtsdatum, familienBudgetresultat.geburtsdatum) &&
        Objects.equals(this.steuerjahr, familienBudgetresultat.steuerjahr) &&
        Objects.equals(this.veranlagungscode, familienBudgetresultat.veranlagungscode) &&
        Objects.equals(this.total, familienBudgetresultat.total) &&
        Objects.equals(this.einnahmenMinusKosten, familienBudgetresultat.einnahmenMinusKosten) &&
        Objects.equals(this.anzahlPersonenImHaushalt, familienBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.anzahlKinderInAusbildung, familienBudgetresultat.anzahlKinderInAusbildung) &&
        Objects.equals(this.einnahmeUeberschuss, familienBudgetresultat.einnahmeUeberschuss) &&
        Objects.equals(this.proKopfTeilungKinderInAusbildung, familienBudgetresultat.proKopfTeilungKinderInAusbildung) &&
        Objects.equals(this.anrechenbareElterlicheLeistung, familienBudgetresultat.anrechenbareElterlicheLeistung) &&
        Objects.equals(this.halbierungsReduktion, familienBudgetresultat.halbierungsReduktion) &&
        Objects.equals(this.fehlbetrag, familienBudgetresultat.fehlbetrag) &&
        Objects.equals(this.proKopfTeilung, familienBudgetresultat.proKopfTeilung) &&
        Objects.equals(this.ungedeckterAnteilLebenshaltungskosten, familienBudgetresultat.ungedeckterAnteilLebenshaltungskosten) &&
        Objects.equals(this.einnahmen, familienBudgetresultat.einnahmen) &&
        Objects.equals(this.kosten, familienBudgetresultat.kosten) &&
        Objects.equals(this.vornamePartner, familienBudgetresultat.vornamePartner) &&
        Objects.equals(this.nachnamePartner, familienBudgetresultat.nachnamePartner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdatenTyp, vorname, nachname, sozialversicherungsnummer, geburtsdatum, steuerjahr, veranlagungscode, total, einnahmenMinusKosten, anzahlPersonenImHaushalt, anzahlKinderInAusbildung, einnahmeUeberschuss, proKopfTeilungKinderInAusbildung, anrechenbareElterlicheLeistung, halbierungsReduktion, fehlbetrag, proKopfTeilung, ungedeckterAnteilLebenshaltungskosten, einnahmen, kosten, vornamePartner, nachnamePartner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatDto {\n");
    
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    veranlagungscode: ").append(toIndentedString(veranlagungscode)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    einnahmenMinusKosten: ").append(toIndentedString(einnahmenMinusKosten)).append("\n");
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
    sb.append("    anzahlKinderInAusbildung: ").append(toIndentedString(anzahlKinderInAusbildung)).append("\n");
    sb.append("    einnahmeUeberschuss: ").append(toIndentedString(einnahmeUeberschuss)).append("\n");
    sb.append("    proKopfTeilungKinderInAusbildung: ").append(toIndentedString(proKopfTeilungKinderInAusbildung)).append("\n");
    sb.append("    anrechenbareElterlicheLeistung: ").append(toIndentedString(anrechenbareElterlicheLeistung)).append("\n");
    sb.append("    halbierungsReduktion: ").append(toIndentedString(halbierungsReduktion)).append("\n");
    sb.append("    fehlbetrag: ").append(toIndentedString(fehlbetrag)).append("\n");
    sb.append("    proKopfTeilung: ").append(toIndentedString(proKopfTeilung)).append("\n");
    sb.append("    ungedeckterAnteilLebenshaltungskosten: ").append(toIndentedString(ungedeckterAnteilLebenshaltungskosten)).append("\n");
    sb.append("    einnahmen: ").append(toIndentedString(einnahmen)).append("\n");
    sb.append("    kosten: ").append(toIndentedString(kosten)).append("\n");
    sb.append("    vornamePartner: ").append(toIndentedString(vornamePartner)).append("\n");
    sb.append("    nachnamePartner: ").append(toIndentedString(nachnamePartner)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


}

