package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("EinnahmenKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class EinnahmenKostenDto  implements Serializable {
  private @Valid Integer nettoerwerbseinkommen;
  private @Valid Integer fahrkosten;
  private @Valid Integer arbeitspensumProzent;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer taggelderAHVIV;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer zulagen;
  private @Valid Integer renten;
  private @Valid Integer eoLeistungen;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer beitraege;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer wohnkosten;
  private @Valid Boolean wgWohnend;
  private @Valid Integer wgAnzahlPersonen;
  private @Valid Boolean alternativeWohnformWohnend;
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid Integer verpflegungskosten;
  private @Valid Integer betreuungskostenKinder;
  private @Valid String veranlagungsStatus;
  private @Valid Integer steuerjahr;
  private @Valid Integer vermoegen;
  private @Valid Integer steuern;

  /**
   **/
  public EinnahmenKostenDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
    return this;
  }

  
  @JsonProperty("nettoerwerbseinkommen")
  @NotNull
  public Integer getNettoerwerbseinkommen() {
    return nettoerwerbseinkommen;
  }

  @JsonProperty("nettoerwerbseinkommen")
  public void setNettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
    this.nettoerwerbseinkommen = nettoerwerbseinkommen;
  }

  /**
   **/
  public EinnahmenKostenDto fahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
    return this;
  }

  
  @JsonProperty("fahrkosten")
  @NotNull
  public Integer getFahrkosten() {
    return fahrkosten;
  }

  @JsonProperty("fahrkosten")
  public void setFahrkosten(Integer fahrkosten) {
    this.fahrkosten = fahrkosten;
  }

  /**
   * Required nur wenn nettoerwerbseinkommen &gt; 0
   **/
  public EinnahmenKostenDto arbeitspensumProzent(Integer arbeitspensumProzent) {
    this.arbeitspensumProzent = arbeitspensumProzent;
    return this;
  }

  
  @JsonProperty("arbeitspensumProzent")
  public Integer getArbeitspensumProzent() {
    return arbeitspensumProzent;
  }

  @JsonProperty("arbeitspensumProzent")
  public void setArbeitspensumProzent(Integer arbeitspensumProzent) {
    this.arbeitspensumProzent = arbeitspensumProzent;
  }

  /**
   * Wird immer angezeigt, ist aber optional
   **/
  public EinnahmenKostenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenDto taggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  public Integer getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  /**
   * Mit Dokument wenn &gt; 0
   **/
  public EinnahmenKostenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   * Required nur wenn mind. ein Kind gibt
   **/
  public EinnahmenKostenDto zulagen(Integer zulagen) {
    this.zulagen = zulagen;
    return this;
  }

  
  @JsonProperty("zulagen")
  public Integer getZulagen() {
    return zulagen;
  }

  @JsonProperty("zulagen")
  public void setZulagen(Integer zulagen) {
    this.zulagen = zulagen;
  }

  /**
   * Required nur wenn mind. ein Elternteil gestorben ist
   **/
  public EinnahmenKostenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public EinnahmenKostenDto eoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  public Integer getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public EinnahmenKostenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public EinnahmenKostenDto beitraege(Integer beitraege) {
    this.beitraege = beitraege;
    return this;
  }

  
  @JsonProperty("beitraege")
  public Integer getBeitraege() {
    return beitraege;
  }

  @JsonProperty("beitraege")
  public void setBeitraege(Integer beitraege) {
    this.beitraege = beitraege;
  }

  /**
   * The cost for the education calculated over the whole year
   **/
  public EinnahmenKostenDto ausbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
    return this;
  }

  
  @JsonProperty("ausbildungskosten")
  public Integer getAusbildungskosten() {
    return ausbildungskosten;
  }

  @JsonProperty("ausbildungskosten")
  public void setAusbildungskosten(Integer ausbildungskosten) {
    this.ausbildungskosten = ausbildungskosten;
  }

  /**
   **/
  public EinnahmenKostenDto wohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
    return this;
  }

  
  @JsonProperty("wohnkosten")
  public Integer getWohnkosten() {
    return wohnkosten;
  }

  @JsonProperty("wohnkosten")
  public void setWohnkosten(Integer wohnkosten) {
    this.wohnkosten = wohnkosten;
  }

  /**
   * Required nur wenn Person eigener Haushalt hat
   **/
  public EinnahmenKostenDto wgWohnend(Boolean wgWohnend) {
    this.wgWohnend = wgWohnend;
    return this;
  }

  
  @JsonProperty("wgWohnend")
  public Boolean getWgWohnend() {
    return wgWohnend;
  }

  @JsonProperty("wgWohnend")
  public void setWgWohnend(Boolean wgWohnend) {
    this.wgWohnend = wgWohnend;
  }

  /**
   **/
  public EinnahmenKostenDto wgAnzahlPersonen(Integer wgAnzahlPersonen) {
    this.wgAnzahlPersonen = wgAnzahlPersonen;
    return this;
  }

  
  @JsonProperty("wgAnzahlPersonen")
  public Integer getWgAnzahlPersonen() {
    return wgAnzahlPersonen;
  }

  @JsonProperty("wgAnzahlPersonen")
  public void setWgAnzahlPersonen(Integer wgAnzahlPersonen) {
    this.wgAnzahlPersonen = wgAnzahlPersonen;
  }

  /**
   **/
  public EinnahmenKostenDto alternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
    this.alternativeWohnformWohnend = alternativeWohnformWohnend;
    return this;
  }

  
  @JsonProperty("alternativeWohnformWohnend")
  public Boolean getAlternativeWohnformWohnend() {
    return alternativeWohnformWohnend;
  }

  @JsonProperty("alternativeWohnformWohnend")
  public void setAlternativeWohnformWohnend(Boolean alternativeWohnformWohnend) {
    this.alternativeWohnformWohnend = alternativeWohnformWohnend;
  }

  /**
   * Required nur wenn die Person keinen eigenen Haushalt führt
   **/
  public EinnahmenKostenDto auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
    return this;
  }

  
  @JsonProperty("auswaertigeMittagessenProWoche")
  public Integer getAuswaertigeMittagessenProWoche() {
    return auswaertigeMittagessenProWoche;
  }

  @JsonProperty("auswaertigeMittagessenProWoche")
  public void setAuswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
    this.auswaertigeMittagessenProWoche = auswaertigeMittagessenProWoche;
  }

  /**
   **/
  public EinnahmenKostenDto verpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
    return this;
  }

  
  @JsonProperty("verpflegungskosten")
  public Integer getVerpflegungskosten() {
    return verpflegungskosten;
  }

  @JsonProperty("verpflegungskosten")
  public void setVerpflegungskosten(Integer verpflegungskosten) {
    this.verpflegungskosten = verpflegungskosten;
  }

  /**
   * Betreuungskosten eigener Kinder
   **/
  public EinnahmenKostenDto betreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
    return this;
  }

  
  @JsonProperty("betreuungskostenKinder")
  public Integer getBetreuungskostenKinder() {
    return betreuungskostenKinder;
  }

  @JsonProperty("betreuungskostenKinder")
  public void setBetreuungskostenKinder(Integer betreuungskostenKinder) {
    this.betreuungskostenKinder = betreuungskostenKinder;
  }

  /**
   * Veranlagungsstatus
   **/
  public EinnahmenKostenDto veranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
    return this;
  }

  
  @JsonProperty("veranlagungsStatus")
  public String getVeranlagungsStatus() {
    return veranlagungsStatus;
  }

  @JsonProperty("veranlagungsStatus")
  public void setVeranlagungsStatus(String veranlagungsStatus) {
    this.veranlagungsStatus = veranlagungsStatus;
  }

  /**
   * Aktuelles oder ein vergangenes Steuerjahr als 4-stellige Zahl. Default ist Vorjahr des Gesuchsjahrs
   * minimum: 0
   * maximum: 99999
   **/
  public EinnahmenKostenDto steuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
    return this;
  }

  
  @JsonProperty("steuerjahr")
 @Min(0) @Max(99999)  public Integer getSteuerjahr() {
    return steuerjahr;
  }

  @JsonProperty("steuerjahr")
  public void setSteuerjahr(Integer steuerjahr) {
    this.steuerjahr = steuerjahr;
  }

  /**
   * Ganze Zahl, ohne Kommastellen
   * minimum: 0
   * maximum: 2147483647
   **/
  public EinnahmenKostenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
 @Min(0) @Max(2147483647)  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   * transient and calculated readonly field
   * minimum: 0
   * maximum: 2147483647
   **/
  public EinnahmenKostenDto steuern(Integer steuern) {
    this.steuern = steuern;
    return this;
  }

  
  @JsonProperty("steuern")
 @Min(0) @Max(2147483647)  public Integer getSteuern() {
    return steuern;
  }

  @JsonProperty("steuern")
  public void setSteuern(Integer steuern) {
    this.steuern = steuern;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinnahmenKostenDto einnahmenKosten = (EinnahmenKostenDto) o;
    return Objects.equals(this.nettoerwerbseinkommen, einnahmenKosten.nettoerwerbseinkommen) &&
        Objects.equals(this.fahrkosten, einnahmenKosten.fahrkosten) &&
        Objects.equals(this.arbeitspensumProzent, einnahmenKosten.arbeitspensumProzent) &&
        Objects.equals(this.unterhaltsbeitraege, einnahmenKosten.unterhaltsbeitraege) &&
        Objects.equals(this.einnahmenBGSA, einnahmenKosten.einnahmenBGSA) &&
        Objects.equals(this.taggelderAHVIV, einnahmenKosten.taggelderAHVIV) &&
        Objects.equals(this.andereEinnahmen, einnahmenKosten.andereEinnahmen) &&
        Objects.equals(this.zulagen, einnahmenKosten.zulagen) &&
        Objects.equals(this.renten, einnahmenKosten.renten) &&
        Objects.equals(this.eoLeistungen, einnahmenKosten.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, einnahmenKosten.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, einnahmenKosten.beitraege) &&
        Objects.equals(this.ausbildungskosten, einnahmenKosten.ausbildungskosten) &&
        Objects.equals(this.wohnkosten, einnahmenKosten.wohnkosten) &&
        Objects.equals(this.wgWohnend, einnahmenKosten.wgWohnend) &&
        Objects.equals(this.wgAnzahlPersonen, einnahmenKosten.wgAnzahlPersonen) &&
        Objects.equals(this.alternativeWohnformWohnend, einnahmenKosten.alternativeWohnformWohnend) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, einnahmenKosten.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.verpflegungskosten, einnahmenKosten.verpflegungskosten) &&
        Objects.equals(this.betreuungskostenKinder, einnahmenKosten.betreuungskostenKinder) &&
        Objects.equals(this.veranlagungsStatus, einnahmenKosten.veranlagungsStatus) &&
        Objects.equals(this.steuerjahr, einnahmenKosten.steuerjahr) &&
        Objects.equals(this.vermoegen, einnahmenKosten.vermoegen) &&
        Objects.equals(this.steuern, einnahmenKosten.steuern);
  }

  @Override
  public int hashCode() {
<<<<<<< HEAD
    return Objects.hash(nettoerwerbseinkommen, fahrkosten, arbeitspensumProzent, unterhaltsbeitraege, einnahmenBGSA, taggelderAHVIV, andereEinnahmen, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskosten, wohnkosten, wgWohnend, wgAnzahlPersonen, alternativeWohnformWohnend, auswaertigeMittagessenProWoche, verpflegungskosten, betreuungskostenKinder, veranlagungsStatus, steuerjahr, vermoegen, steuern);
=======
    return Objects.hash(nettoerwerbseinkommen, fahrkosten, verdienstRealisiert, alimente, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskosten, wohnkosten, wgWohnend, wgAnzahlPersonen, alternativeWohnformWohnend, auswaertigeMittagessenProWoche, betreuungskostenKinder, veranlagungsStatus, steuerjahr, vermoegen, steuernKantonGemeinde);
>>>>>>> main
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinnahmenKostenDto {\n");
    
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    arbeitspensumProzent: ").append(toIndentedString(arbeitspensumProzent)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
    sb.append("    wgWohnend: ").append(toIndentedString(wgWohnend)).append("\n");
    sb.append("    wgAnzahlPersonen: ").append(toIndentedString(wgAnzahlPersonen)).append("\n");
    sb.append("    alternativeWohnformWohnend: ").append(toIndentedString(alternativeWohnformWohnend)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
    sb.append("    veranlagungsStatus: ").append(toIndentedString(veranlagungsStatus)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuern: ").append(toIndentedString(steuern)).append("\n");
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

