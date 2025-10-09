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



@JsonTypeName("EinnahmenKostenUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class EinnahmenKostenUpdateDto  implements Serializable {
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
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid Integer verplegungskosten;
  private @Valid Integer betreuungskostenKinder;
  private @Valid String veranlagungsStatus;
  private @Valid Integer steuerjahr;
  private @Valid Integer vermoegen;
  private @Valid Integer steuernKantonGemeinde;

  /**
   **/
  public EinnahmenKostenUpdateDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
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
  public EinnahmenKostenUpdateDto fahrkosten(Integer fahrkosten) {
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
  public EinnahmenKostenUpdateDto arbeitspensumProzent(Integer arbeitspensumProzent) {
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
  public EinnahmenKostenUpdateDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
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
  public EinnahmenKostenUpdateDto einnahmenBGSA(Integer einnahmenBGSA) {
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
  public EinnahmenKostenUpdateDto taggelderAHVIV(Integer taggelderAHVIV) {
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
  public EinnahmenKostenUpdateDto andereEinnahmen(Integer andereEinnahmen) {
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
  public EinnahmenKostenUpdateDto zulagen(Integer zulagen) {
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
  public EinnahmenKostenUpdateDto renten(Integer renten) {
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
  public EinnahmenKostenUpdateDto eoLeistungen(Integer eoLeistungen) {
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
  public EinnahmenKostenUpdateDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
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
  public EinnahmenKostenUpdateDto beitraege(Integer beitraege) {
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
  public EinnahmenKostenUpdateDto ausbildungskosten(Integer ausbildungskosten) {
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
  public EinnahmenKostenUpdateDto wohnkosten(Integer wohnkosten) {
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
  public EinnahmenKostenUpdateDto wgWohnend(Boolean wgWohnend) {
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
   * Required nur wenn die Person keinen eigenen Haushalt führt
   **/
  public EinnahmenKostenUpdateDto auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
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
  public EinnahmenKostenUpdateDto verplegungskosten(Integer verplegungskosten) {
    this.verplegungskosten = verplegungskosten;
    return this;
  }

  
  @JsonProperty("verplegungskosten")
  public Integer getVerplegungskosten() {
    return verplegungskosten;
  }

  @JsonProperty("verplegungskosten")
  public void setVerplegungskosten(Integer verplegungskosten) {
    this.verplegungskosten = verplegungskosten;
  }

  /**
   * Betreuungskosten eigener Kinder
   **/
  public EinnahmenKostenUpdateDto betreuungskostenKinder(Integer betreuungskostenKinder) {
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
  public EinnahmenKostenUpdateDto veranlagungsStatus(String veranlagungsStatus) {
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
  public EinnahmenKostenUpdateDto steuerjahr(Integer steuerjahr) {
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
  public EinnahmenKostenUpdateDto vermoegen(Integer vermoegen) {
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
  public EinnahmenKostenUpdateDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
 @Min(0) @Max(2147483647)  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    EinnahmenKostenUpdateDto einnahmenKostenUpdate = (EinnahmenKostenUpdateDto) o;
    return Objects.equals(this.nettoerwerbseinkommen, einnahmenKostenUpdate.nettoerwerbseinkommen) &&
        Objects.equals(this.fahrkosten, einnahmenKostenUpdate.fahrkosten) &&
        Objects.equals(this.arbeitspensumProzent, einnahmenKostenUpdate.arbeitspensumProzent) &&
        Objects.equals(this.unterhaltsbeitraege, einnahmenKostenUpdate.unterhaltsbeitraege) &&
        Objects.equals(this.einnahmenBGSA, einnahmenKostenUpdate.einnahmenBGSA) &&
        Objects.equals(this.taggelderAHVIV, einnahmenKostenUpdate.taggelderAHVIV) &&
        Objects.equals(this.andereEinnahmen, einnahmenKostenUpdate.andereEinnahmen) &&
        Objects.equals(this.zulagen, einnahmenKostenUpdate.zulagen) &&
        Objects.equals(this.renten, einnahmenKostenUpdate.renten) &&
        Objects.equals(this.eoLeistungen, einnahmenKostenUpdate.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, einnahmenKostenUpdate.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, einnahmenKostenUpdate.beitraege) &&
        Objects.equals(this.ausbildungskosten, einnahmenKostenUpdate.ausbildungskosten) &&
        Objects.equals(this.wohnkosten, einnahmenKostenUpdate.wohnkosten) &&
        Objects.equals(this.wgWohnend, einnahmenKostenUpdate.wgWohnend) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, einnahmenKostenUpdate.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.verplegungskosten, einnahmenKostenUpdate.verplegungskosten) &&
        Objects.equals(this.betreuungskostenKinder, einnahmenKostenUpdate.betreuungskostenKinder) &&
        Objects.equals(this.veranlagungsStatus, einnahmenKostenUpdate.veranlagungsStatus) &&
        Objects.equals(this.steuerjahr, einnahmenKostenUpdate.steuerjahr) &&
        Objects.equals(this.vermoegen, einnahmenKostenUpdate.vermoegen) &&
        Objects.equals(this.steuernKantonGemeinde, einnahmenKostenUpdate.steuernKantonGemeinde);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, fahrkosten, arbeitspensumProzent, unterhaltsbeitraege, einnahmenBGSA, taggelderAHVIV, andereEinnahmen, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, ausbildungskosten, wohnkosten, wgWohnend, auswaertigeMittagessenProWoche, verplegungskosten, betreuungskostenKinder, veranlagungsStatus, steuerjahr, vermoegen, steuernKantonGemeinde);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class EinnahmenKostenUpdateDto {\n");
    
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
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    verplegungskosten: ").append(toIndentedString(verplegungskosten)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
    sb.append("    veranlagungsStatus: ").append(toIndentedString(veranlagungsStatus)).append("\n");
    sb.append("    steuerjahr: ").append(toIndentedString(steuerjahr)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
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

