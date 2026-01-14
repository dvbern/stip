package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatEinnahmenDto;
import ch.dvbern.stip.generated.dto.PersoenlichesBudgetresultatKostenDto;
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
 * Persoenliche Budget daten fuer und von der Berechnung
 **/

@JsonTypeName("PersoenlichesBudgetresultat")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PersoenlichesBudgetresultatDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String sozialversicherungsnummer;
  private @Valid LocalDate geburtsdatum;
  private @Valid Integer total;
  private @Valid Integer einnahmenMinusKosten;
  private @Valid Integer fehlbetrag;
  private @Valid Integer proKopfTeilung;
  private @Valid Boolean eigenerHaushalt;
  private @Valid Integer budgetTranche;
  private @Valid Integer anzahlMonate;
  private @Valid Integer gesetzlichesDarlehen;
  private @Valid Integer gesetzlichesDarlehenStipendium;
  private @Valid Integer anzahlPersonenImHaushalt;
  private @Valid PersoenlichesBudgetresultatEinnahmenDto einnahmen;
  private @Valid PersoenlichesBudgetresultatKostenDto kosten;
  private @Valid String vornamePartner;
  private @Valid String nachnamePartner;

  /**
   **/
  public PersoenlichesBudgetresultatDto vorname(String vorname) {
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
  public PersoenlichesBudgetresultatDto nachname(String nachname) {
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
  public PersoenlichesBudgetresultatDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public PersoenlichesBudgetresultatDto geburtsdatum(LocalDate geburtsdatum) {
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
  public PersoenlichesBudgetresultatDto total(Integer total) {
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
  public PersoenlichesBudgetresultatDto einnahmenMinusKosten(Integer einnahmenMinusKosten) {
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
  public PersoenlichesBudgetresultatDto fehlbetrag(Integer fehlbetrag) {
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
  public PersoenlichesBudgetresultatDto proKopfTeilung(Integer proKopfTeilung) {
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
  public PersoenlichesBudgetresultatDto eigenerHaushalt(Boolean eigenerHaushalt) {
    this.eigenerHaushalt = eigenerHaushalt;
    return this;
  }

  
  @JsonProperty("eigenerHaushalt")
  @NotNull
  public Boolean getEigenerHaushalt() {
    return eigenerHaushalt;
  }

  @JsonProperty("eigenerHaushalt")
  public void setEigenerHaushalt(Boolean eigenerHaushalt) {
    this.eigenerHaushalt = eigenerHaushalt;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto budgetTranche(Integer budgetTranche) {
    this.budgetTranche = budgetTranche;
    return this;
  }

  
  @JsonProperty("budgetTranche")
  @NotNull
  public Integer getBudgetTranche() {
    return budgetTranche;
  }

  @JsonProperty("budgetTranche")
  public void setBudgetTranche(Integer budgetTranche) {
    this.budgetTranche = budgetTranche;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto anzahlMonate(Integer anzahlMonate) {
    this.anzahlMonate = anzahlMonate;
    return this;
  }

  
  @JsonProperty("anzahlMonate")
  @NotNull
  public Integer getAnzahlMonate() {
    return anzahlMonate;
  }

  @JsonProperty("anzahlMonate")
  public void setAnzahlMonate(Integer anzahlMonate) {
    this.anzahlMonate = anzahlMonate;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto gesetzlichesDarlehen(Integer gesetzlichesDarlehen) {
    this.gesetzlichesDarlehen = gesetzlichesDarlehen;
    return this;
  }

  
  @JsonProperty("gesetzlichesDarlehen")
  @NotNull
  public Integer getGesetzlichesDarlehen() {
    return gesetzlichesDarlehen;
  }

  @JsonProperty("gesetzlichesDarlehen")
  public void setGesetzlichesDarlehen(Integer gesetzlichesDarlehen) {
    this.gesetzlichesDarlehen = gesetzlichesDarlehen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto gesetzlichesDarlehenStipendium(Integer gesetzlichesDarlehenStipendium) {
    this.gesetzlichesDarlehenStipendium = gesetzlichesDarlehenStipendium;
    return this;
  }

  
  @JsonProperty("gesetzlichesDarlehenStipendium")
  @NotNull
  public Integer getGesetzlichesDarlehenStipendium() {
    return gesetzlichesDarlehenStipendium;
  }

  @JsonProperty("gesetzlichesDarlehenStipendium")
  public void setGesetzlichesDarlehenStipendium(Integer gesetzlichesDarlehenStipendium) {
    this.gesetzlichesDarlehenStipendium = gesetzlichesDarlehenStipendium;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto anzahlPersonenImHaushalt(Integer anzahlPersonenImHaushalt) {
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
  public PersoenlichesBudgetresultatDto einnahmen(PersoenlichesBudgetresultatEinnahmenDto einnahmen) {
    this.einnahmen = einnahmen;
    return this;
  }

  
  @JsonProperty("einnahmen")
  @NotNull
  public PersoenlichesBudgetresultatEinnahmenDto getEinnahmen() {
    return einnahmen;
  }

  @JsonProperty("einnahmen")
  public void setEinnahmen(PersoenlichesBudgetresultatEinnahmenDto einnahmen) {
    this.einnahmen = einnahmen;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto kosten(PersoenlichesBudgetresultatKostenDto kosten) {
    this.kosten = kosten;
    return this;
  }

  
  @JsonProperty("kosten")
  @NotNull
  public PersoenlichesBudgetresultatKostenDto getKosten() {
    return kosten;
  }

  @JsonProperty("kosten")
  public void setKosten(PersoenlichesBudgetresultatKostenDto kosten) {
    this.kosten = kosten;
  }

  /**
   **/
  public PersoenlichesBudgetresultatDto vornamePartner(String vornamePartner) {
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
  public PersoenlichesBudgetresultatDto nachnamePartner(String nachnamePartner) {
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
    PersoenlichesBudgetresultatDto persoenlichesBudgetresultat = (PersoenlichesBudgetresultatDto) o;
    return Objects.equals(this.vorname, persoenlichesBudgetresultat.vorname) &&
        Objects.equals(this.nachname, persoenlichesBudgetresultat.nachname) &&
        Objects.equals(this.sozialversicherungsnummer, persoenlichesBudgetresultat.sozialversicherungsnummer) &&
        Objects.equals(this.geburtsdatum, persoenlichesBudgetresultat.geburtsdatum) &&
        Objects.equals(this.total, persoenlichesBudgetresultat.total) &&
        Objects.equals(this.einnahmenMinusKosten, persoenlichesBudgetresultat.einnahmenMinusKosten) &&
        Objects.equals(this.fehlbetrag, persoenlichesBudgetresultat.fehlbetrag) &&
        Objects.equals(this.proKopfTeilung, persoenlichesBudgetresultat.proKopfTeilung) &&
        Objects.equals(this.eigenerHaushalt, persoenlichesBudgetresultat.eigenerHaushalt) &&
        Objects.equals(this.budgetTranche, persoenlichesBudgetresultat.budgetTranche) &&
        Objects.equals(this.anzahlMonate, persoenlichesBudgetresultat.anzahlMonate) &&
        Objects.equals(this.gesetzlichesDarlehen, persoenlichesBudgetresultat.gesetzlichesDarlehen) &&
        Objects.equals(this.gesetzlichesDarlehenStipendium, persoenlichesBudgetresultat.gesetzlichesDarlehenStipendium) &&
        Objects.equals(this.anzahlPersonenImHaushalt, persoenlichesBudgetresultat.anzahlPersonenImHaushalt) &&
        Objects.equals(this.einnahmen, persoenlichesBudgetresultat.einnahmen) &&
        Objects.equals(this.kosten, persoenlichesBudgetresultat.kosten) &&
        Objects.equals(this.vornamePartner, persoenlichesBudgetresultat.vornamePartner) &&
        Objects.equals(this.nachnamePartner, persoenlichesBudgetresultat.nachnamePartner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, sozialversicherungsnummer, geburtsdatum, total, einnahmenMinusKosten, fehlbetrag, proKopfTeilung, eigenerHaushalt, budgetTranche, anzahlMonate, gesetzlichesDarlehen, gesetzlichesDarlehenStipendium, anzahlPersonenImHaushalt, einnahmen, kosten, vornamePartner, nachnamePartner);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PersoenlichesBudgetresultatDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    einnahmenMinusKosten: ").append(toIndentedString(einnahmenMinusKosten)).append("\n");
    sb.append("    fehlbetrag: ").append(toIndentedString(fehlbetrag)).append("\n");
    sb.append("    proKopfTeilung: ").append(toIndentedString(proKopfTeilung)).append("\n");
    sb.append("    eigenerHaushalt: ").append(toIndentedString(eigenerHaushalt)).append("\n");
    sb.append("    budgetTranche: ").append(toIndentedString(budgetTranche)).append("\n");
    sb.append("    anzahlMonate: ").append(toIndentedString(anzahlMonate)).append("\n");
    sb.append("    gesetzlichesDarlehen: ").append(toIndentedString(gesetzlichesDarlehen)).append("\n");
    sb.append("    gesetzlichesDarlehenStipendium: ").append(toIndentedString(gesetzlichesDarlehenStipendium)).append("\n");
    sb.append("    anzahlPersonenImHaushalt: ").append(toIndentedString(anzahlPersonenImHaushalt)).append("\n");
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

