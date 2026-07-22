package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FamilienBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.FamilienBudgetresultatKostenDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FamilienBudgetresultatDto  implements Serializable {
  private @Valid List<String> haushaltNames = new ArrayList<>();
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
  private @Valid Integer teilzeitKinderProzente;
  private @Valid FamilienBudgetresultatEinnahmenDto einnahmen;
  private @Valid FamilienBudgetresultatKostenDto kosten;
  private @Valid String vornamePartner;
  private @Valid String nachnamePartner;
  private @Valid String sozialversicherungsnummerPartner;
  private @Valid LocalDate geburtsdatumPartner;

  protected FamilienBudgetresultatDto(FamilienBudgetresultatDtoBuilder<?, ?> b) {
    this.haushaltNames = b.haushaltNames;
    this.steuerdatenTyp = b.steuerdatenTyp;
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.sozialversicherungsnummer = b.sozialversicherungsnummer;
    this.geburtsdatum = b.geburtsdatum;
    this.steuerjahr = b.steuerjahr;
    this.veranlagungscode = b.veranlagungscode;
    this.total = b.total;
    this.einnahmenMinusKosten = b.einnahmenMinusKosten;
    this.anzahlPersonenImHaushalt = b.anzahlPersonenImHaushalt;
    this.anzahlKinderInAusbildung = b.anzahlKinderInAusbildung;
    this.einnahmeUeberschuss = b.einnahmeUeberschuss;
    this.proKopfTeilungKinderInAusbildung = b.proKopfTeilungKinderInAusbildung;
    this.anrechenbareElterlicheLeistung = b.anrechenbareElterlicheLeistung;
    this.halbierungsReduktion = b.halbierungsReduktion;
    this.fehlbetrag = b.fehlbetrag;
    this.proKopfTeilung = b.proKopfTeilung;
    this.ungedeckterAnteilLebenshaltungskosten = b.ungedeckterAnteilLebenshaltungskosten;
    this.teilzeitKinderProzente = b.teilzeitKinderProzente;
    this.einnahmen = b.einnahmen;
    this.kosten = b.kosten;
    this.vornamePartner = b.vornamePartner;
    this.nachnamePartner = b.nachnamePartner;
    this.sozialversicherungsnummerPartner = b.sozialversicherungsnummerPartner;
    this.geburtsdatumPartner = b.geburtsdatumPartner;
  }

  public FamilienBudgetresultatDto() {
  }

  /**
   **/
  public FamilienBudgetresultatDto haushaltNames(List<String> haushaltNames) {
    this.haushaltNames = haushaltNames;
    return this;
  }

  
  @JsonProperty("haushaltNames")
  @NotNull
  public List<String> getHaushaltNames() {
    return haushaltNames;
  }

  @JsonProperty("haushaltNames")
  public void setHaushaltNames(List<String> haushaltNames) {
    this.haushaltNames = haushaltNames;
  }

  public FamilienBudgetresultatDto addHaushaltNamesItem(String haushaltNamesItem) {
    if (this.haushaltNames == null) {
      this.haushaltNames = new ArrayList<>();
    }

    this.haushaltNames.add(haushaltNamesItem);
    return this;
  }

  public FamilienBudgetresultatDto removeHaushaltNamesItem(String haushaltNamesItem) {
    if (haushaltNamesItem != null && this.haushaltNames != null) {
      this.haushaltNames.remove(haushaltNamesItem);
    }

    return this;
  }
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
  public FamilienBudgetresultatDto teilzeitKinderProzente(Integer teilzeitKinderProzente) {
    this.teilzeitKinderProzente = teilzeitKinderProzente;
    return this;
  }

  
  @JsonProperty("teilzeitKinderProzente")
  @NotNull
  public Integer getTeilzeitKinderProzente() {
    return teilzeitKinderProzente;
  }

  @JsonProperty("teilzeitKinderProzente")
  public void setTeilzeitKinderProzente(Integer teilzeitKinderProzente) {
    this.teilzeitKinderProzente = teilzeitKinderProzente;
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

  /**
   **/
  public FamilienBudgetresultatDto sozialversicherungsnummerPartner(String sozialversicherungsnummerPartner) {
    this.sozialversicherungsnummerPartner = sozialversicherungsnummerPartner;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummerPartner")
  public String getSozialversicherungsnummerPartner() {
    return sozialversicherungsnummerPartner;
  }

  @JsonProperty("sozialversicherungsnummerPartner")
  public void setSozialversicherungsnummerPartner(String sozialversicherungsnummerPartner) {
    this.sozialversicherungsnummerPartner = sozialversicherungsnummerPartner;
  }

  /**
   * dd.MM.yyyy
   **/
  public FamilienBudgetresultatDto geburtsdatumPartner(LocalDate geburtsdatumPartner) {
    this.geburtsdatumPartner = geburtsdatumPartner;
    return this;
  }

  
  @JsonProperty("geburtsdatumPartner")
  public LocalDate getGeburtsdatumPartner() {
    return geburtsdatumPartner;
  }

  @JsonProperty("geburtsdatumPartner")
  public void setGeburtsdatumPartner(LocalDate geburtsdatumPartner) {
    this.geburtsdatumPartner = geburtsdatumPartner;
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
    return Objects.equals(this.haushaltNames, familienBudgetresultat.haushaltNames) &&
        Objects.equals(this.steuerdatenTyp, familienBudgetresultat.steuerdatenTyp) &&
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
        Objects.equals(this.teilzeitKinderProzente, familienBudgetresultat.teilzeitKinderProzente) &&
        Objects.equals(this.einnahmen, familienBudgetresultat.einnahmen) &&
        Objects.equals(this.kosten, familienBudgetresultat.kosten) &&
        Objects.equals(this.vornamePartner, familienBudgetresultat.vornamePartner) &&
        Objects.equals(this.nachnamePartner, familienBudgetresultat.nachnamePartner) &&
        Objects.equals(this.sozialversicherungsnummerPartner, familienBudgetresultat.sozialversicherungsnummerPartner) &&
        Objects.equals(this.geburtsdatumPartner, familienBudgetresultat.geburtsdatumPartner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(haushaltNames, steuerdatenTyp, vorname, nachname, sozialversicherungsnummer, geburtsdatum, steuerjahr, veranlagungscode, total, einnahmenMinusKosten, anzahlPersonenImHaushalt, anzahlKinderInAusbildung, einnahmeUeberschuss, proKopfTeilungKinderInAusbildung, anrechenbareElterlicheLeistung, halbierungsReduktion, fehlbetrag, proKopfTeilung, ungedeckterAnteilLebenshaltungskosten, teilzeitKinderProzente, einnahmen, kosten, vornamePartner, nachnamePartner, sozialversicherungsnummerPartner, geburtsdatumPartner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FamilienBudgetresultatDto {\n");
    
    sb.append("    haushaltNames: ").append(toIndentedString(haushaltNames)).append("\n");
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
    sb.append("    teilzeitKinderProzente: ").append(toIndentedString(teilzeitKinderProzente)).append("\n");
    sb.append("    einnahmen: ").append(toIndentedString(einnahmen)).append("\n");
    sb.append("    kosten: ").append(toIndentedString(kosten)).append("\n");
    sb.append("    vornamePartner: ").append(toIndentedString(vornamePartner)).append("\n");
    sb.append("    nachnamePartner: ").append(toIndentedString(nachnamePartner)).append("\n");
    sb.append("    sozialversicherungsnummerPartner: ").append(toIndentedString(sozialversicherungsnummerPartner)).append("\n");
    sb.append("    geburtsdatumPartner: ").append(toIndentedString(geburtsdatumPartner)).append("\n");
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


  public static FamilienBudgetresultatDtoBuilder<?, ?> builder() {
    return new FamilienBudgetresultatDtoBuilderImpl();
  }

  private static final class FamilienBudgetresultatDtoBuilderImpl extends FamilienBudgetresultatDtoBuilder<FamilienBudgetresultatDto, FamilienBudgetresultatDtoBuilderImpl> {

    @Override
    protected FamilienBudgetresultatDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FamilienBudgetresultatDto build() {
      return new FamilienBudgetresultatDto(this);
    }
  }

  public static abstract class FamilienBudgetresultatDtoBuilder<C extends FamilienBudgetresultatDto, B extends FamilienBudgetresultatDtoBuilder<C, B>>  {
    private List<String> haushaltNames = new ArrayList<>();
    private ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp;
    private String vorname;
    private String nachname;
    private String sozialversicherungsnummer;
    private LocalDate geburtsdatum;
    private Integer steuerjahr;
    private String veranlagungscode;
    private Integer total;
    private Integer einnahmenMinusKosten;
    private Integer anzahlPersonenImHaushalt;
    private Integer anzahlKinderInAusbildung;
    private Integer einnahmeUeberschuss;
    private Integer proKopfTeilungKinderInAusbildung;
    private Integer anrechenbareElterlicheLeistung;
    private Integer halbierungsReduktion;
    private Integer fehlbetrag;
    private Integer proKopfTeilung;
    private Integer ungedeckterAnteilLebenshaltungskosten;
    private Integer teilzeitKinderProzente;
    private FamilienBudgetresultatEinnahmenDto einnahmen;
    private FamilienBudgetresultatKostenDto kosten;
    private String vornamePartner;
    private String nachnamePartner;
    private String sozialversicherungsnummerPartner;
    private LocalDate geburtsdatumPartner;
    protected abstract B self();

    public abstract C build();

    public B haushaltNames(List<String> haushaltNames) {
      this.haushaltNames = haushaltNames;
      return self();
    }
    public B steuerdatenTyp(ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp steuerdatenTyp) {
      this.steuerdatenTyp = steuerdatenTyp;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B sozialversicherungsnummer(String sozialversicherungsnummer) {
      this.sozialversicherungsnummer = sozialversicherungsnummer;
      return self();
    }
    public B geburtsdatum(LocalDate geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B steuerjahr(Integer steuerjahr) {
      this.steuerjahr = steuerjahr;
      return self();
    }
    public B veranlagungscode(String veranlagungscode) {
      this.veranlagungscode = veranlagungscode;
      return self();
    }
    public B total(Integer total) {
      this.total = total;
      return self();
    }
    public B einnahmenMinusKosten(Integer einnahmenMinusKosten) {
      this.einnahmenMinusKosten = einnahmenMinusKosten;
      return self();
    }
    public B anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
      this.anzahlPersonenImHaushalt = anzahlPersonenImHaushalt;
      return self();
    }
    public B anzahlKinderInAusbildung(Integer anzahlKinderInAusbildung) {
      this.anzahlKinderInAusbildung = anzahlKinderInAusbildung;
      return self();
    }
    public B einnahmeUeberschuss(Integer einnahmeUeberschuss) {
      this.einnahmeUeberschuss = einnahmeUeberschuss;
      return self();
    }
    public B proKopfTeilungKinderInAusbildung(Integer proKopfTeilungKinderInAusbildung) {
      this.proKopfTeilungKinderInAusbildung = proKopfTeilungKinderInAusbildung;
      return self();
    }
    public B anrechenbareElterlicheLeistung(Integer anrechenbareElterlicheLeistung) {
      this.anrechenbareElterlicheLeistung = anrechenbareElterlicheLeistung;
      return self();
    }
    public B halbierungsReduktion(Integer halbierungsReduktion) {
      this.halbierungsReduktion = halbierungsReduktion;
      return self();
    }
    public B fehlbetrag(Integer fehlbetrag) {
      this.fehlbetrag = fehlbetrag;
      return self();
    }
    public B proKopfTeilung(Integer proKopfTeilung) {
      this.proKopfTeilung = proKopfTeilung;
      return self();
    }
    public B ungedeckterAnteilLebenshaltungskosten(Integer ungedeckterAnteilLebenshaltungskosten) {
      this.ungedeckterAnteilLebenshaltungskosten = ungedeckterAnteilLebenshaltungskosten;
      return self();
    }
    public B teilzeitKinderProzente(Integer teilzeitKinderProzente) {
      this.teilzeitKinderProzente = teilzeitKinderProzente;
      return self();
    }
    public B einnahmen(FamilienBudgetresultatEinnahmenDto einnahmen) {
      this.einnahmen = einnahmen;
      return self();
    }
    public B kosten(FamilienBudgetresultatKostenDto kosten) {
      this.kosten = kosten;
      return self();
    }
    public B vornamePartner(String vornamePartner) {
      this.vornamePartner = vornamePartner;
      return self();
    }
    public B nachnamePartner(String nachnamePartner) {
      this.nachnamePartner = nachnamePartner;
      return self();
    }
    public B sozialversicherungsnummerPartner(String sozialversicherungsnummerPartner) {
      this.sozialversicherungsnummerPartner = sozialversicherungsnummerPartner;
      return self();
    }
    public B geburtsdatumPartner(LocalDate geburtsdatumPartner) {
      this.geburtsdatumPartner = geburtsdatumPartner;
      return self();
    }
  }
}

