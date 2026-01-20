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



@JsonTypeName("DemoEinnahmenKosten")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoEinnahmenKostenDto  implements Serializable {
  private @Valid Integer nettoerwerbseinkommen;
  private @Valid Integer unterhaltsbeitraege;
  private @Valid Integer zulagen;
  private @Valid Integer renten;
  private @Valid Integer eoLeistungen;
  private @Valid Integer ergaenzungsleistungen;
  private @Valid Integer beitraege;
  private @Valid Integer einnahmenBGSA;
  private @Valid Integer taggelderAHVIV;
  private @Valid Integer andereEinnahmen;
  private @Valid Integer fahrkosten;
  private @Valid Integer vermoegen;
  private @Valid Integer steuernKantonGemeinde;
  private @Valid Integer ausbildungskosten;
  private @Valid Integer betreuungskostenKinder;
  private @Valid Integer verpflegungskosten;
  private @Valid Integer auswaertigeMittagessenProWoche;
  private @Valid Integer wohnkosten;

  /**
   **/
  public DemoEinnahmenKostenDto nettoerwerbseinkommen(Integer nettoerwerbseinkommen) {
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
  public DemoEinnahmenKostenDto unterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
    return this;
  }

  
  @JsonProperty("unterhaltsbeitraege")
  @NotNull
  public Integer getUnterhaltsbeitraege() {
    return unterhaltsbeitraege;
  }

  @JsonProperty("unterhaltsbeitraege")
  public void setUnterhaltsbeitraege(Integer unterhaltsbeitraege) {
    this.unterhaltsbeitraege = unterhaltsbeitraege;
  }

  /**
   **/
  public DemoEinnahmenKostenDto zulagen(Integer zulagen) {
    this.zulagen = zulagen;
    return this;
  }

  
  @JsonProperty("zulagen")
  @NotNull
  public Integer getZulagen() {
    return zulagen;
  }

  @JsonProperty("zulagen")
  public void setZulagen(Integer zulagen) {
    this.zulagen = zulagen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto renten(Integer renten) {
    this.renten = renten;
    return this;
  }

  
  @JsonProperty("renten")
  @NotNull
  public Integer getRenten() {
    return renten;
  }

  @JsonProperty("renten")
  public void setRenten(Integer renten) {
    this.renten = renten;
  }

  /**
   **/
  public DemoEinnahmenKostenDto eoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
    return this;
  }

  
  @JsonProperty("eoLeistungen")
  @NotNull
  public Integer getEoLeistungen() {
    return eoLeistungen;
  }

  @JsonProperty("eoLeistungen")
  public void setEoLeistungen(Integer eoLeistungen) {
    this.eoLeistungen = eoLeistungen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto ergaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
    return this;
  }

  
  @JsonProperty("ergaenzungsleistungen")
  @NotNull
  public Integer getErgaenzungsleistungen() {
    return ergaenzungsleistungen;
  }

  @JsonProperty("ergaenzungsleistungen")
  public void setErgaenzungsleistungen(Integer ergaenzungsleistungen) {
    this.ergaenzungsleistungen = ergaenzungsleistungen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto beitraege(Integer beitraege) {
    this.beitraege = beitraege;
    return this;
  }

  
  @JsonProperty("beitraege")
  @NotNull
  public Integer getBeitraege() {
    return beitraege;
  }

  @JsonProperty("beitraege")
  public void setBeitraege(Integer beitraege) {
    this.beitraege = beitraege;
  }

  /**
   **/
  public DemoEinnahmenKostenDto einnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
    return this;
  }

  
  @JsonProperty("einnahmenBGSA")
  @NotNull
  public Integer getEinnahmenBGSA() {
    return einnahmenBGSA;
  }

  @JsonProperty("einnahmenBGSA")
  public void setEinnahmenBGSA(Integer einnahmenBGSA) {
    this.einnahmenBGSA = einnahmenBGSA;
  }

  /**
   **/
  public DemoEinnahmenKostenDto taggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
    return this;
  }

  
  @JsonProperty("taggelderAHVIV")
  @NotNull
  public Integer getTaggelderAHVIV() {
    return taggelderAHVIV;
  }

  @JsonProperty("taggelderAHVIV")
  public void setTaggelderAHVIV(Integer taggelderAHVIV) {
    this.taggelderAHVIV = taggelderAHVIV;
  }

  /**
   **/
  public DemoEinnahmenKostenDto andereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
    return this;
  }

  
  @JsonProperty("andereEinnahmen")
  @NotNull
  public Integer getAndereEinnahmen() {
    return andereEinnahmen;
  }

  @JsonProperty("andereEinnahmen")
  public void setAndereEinnahmen(Integer andereEinnahmen) {
    this.andereEinnahmen = andereEinnahmen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto fahrkosten(Integer fahrkosten) {
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
   **/
  public DemoEinnahmenKostenDto vermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
    return this;
  }

  
  @JsonProperty("vermoegen")
  @NotNull
  public Integer getVermoegen() {
    return vermoegen;
  }

  @JsonProperty("vermoegen")
  public void setVermoegen(Integer vermoegen) {
    this.vermoegen = vermoegen;
  }

  /**
   **/
  public DemoEinnahmenKostenDto steuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
    return this;
  }

  
  @JsonProperty("steuernKantonGemeinde")
  @NotNull
  public Integer getSteuernKantonGemeinde() {
    return steuernKantonGemeinde;
  }

  @JsonProperty("steuernKantonGemeinde")
  public void setSteuernKantonGemeinde(Integer steuernKantonGemeinde) {
    this.steuernKantonGemeinde = steuernKantonGemeinde;
  }

  /**
   **/
  public DemoEinnahmenKostenDto ausbildungskosten(Integer ausbildungskosten) {
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
  public DemoEinnahmenKostenDto betreuungskostenKinder(Integer betreuungskostenKinder) {
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
   **/
  public DemoEinnahmenKostenDto verpflegungskosten(Integer verpflegungskosten) {
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
   **/
  public DemoEinnahmenKostenDto auswaertigeMittagessenProWoche(Integer auswaertigeMittagessenProWoche) {
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
  public DemoEinnahmenKostenDto wohnkosten(Integer wohnkosten) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoEinnahmenKostenDto demoEinnahmenKosten = (DemoEinnahmenKostenDto) o;
    return Objects.equals(this.nettoerwerbseinkommen, demoEinnahmenKosten.nettoerwerbseinkommen) &&
        Objects.equals(this.unterhaltsbeitraege, demoEinnahmenKosten.unterhaltsbeitraege) &&
        Objects.equals(this.zulagen, demoEinnahmenKosten.zulagen) &&
        Objects.equals(this.renten, demoEinnahmenKosten.renten) &&
        Objects.equals(this.eoLeistungen, demoEinnahmenKosten.eoLeistungen) &&
        Objects.equals(this.ergaenzungsleistungen, demoEinnahmenKosten.ergaenzungsleistungen) &&
        Objects.equals(this.beitraege, demoEinnahmenKosten.beitraege) &&
        Objects.equals(this.einnahmenBGSA, demoEinnahmenKosten.einnahmenBGSA) &&
        Objects.equals(this.taggelderAHVIV, demoEinnahmenKosten.taggelderAHVIV) &&
        Objects.equals(this.andereEinnahmen, demoEinnahmenKosten.andereEinnahmen) &&
        Objects.equals(this.fahrkosten, demoEinnahmenKosten.fahrkosten) &&
        Objects.equals(this.vermoegen, demoEinnahmenKosten.vermoegen) &&
        Objects.equals(this.steuernKantonGemeinde, demoEinnahmenKosten.steuernKantonGemeinde) &&
        Objects.equals(this.ausbildungskosten, demoEinnahmenKosten.ausbildungskosten) &&
        Objects.equals(this.betreuungskostenKinder, demoEinnahmenKosten.betreuungskostenKinder) &&
        Objects.equals(this.verpflegungskosten, demoEinnahmenKosten.verpflegungskosten) &&
        Objects.equals(this.auswaertigeMittagessenProWoche, demoEinnahmenKosten.auswaertigeMittagessenProWoche) &&
        Objects.equals(this.wohnkosten, demoEinnahmenKosten.wohnkosten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nettoerwerbseinkommen, unterhaltsbeitraege, zulagen, renten, eoLeistungen, ergaenzungsleistungen, beitraege, einnahmenBGSA, taggelderAHVIV, andereEinnahmen, fahrkosten, vermoegen, steuernKantonGemeinde, ausbildungskosten, betreuungskostenKinder, verpflegungskosten, auswaertigeMittagessenProWoche, wohnkosten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoEinnahmenKostenDto {\n");
    
    sb.append("    nettoerwerbseinkommen: ").append(toIndentedString(nettoerwerbseinkommen)).append("\n");
    sb.append("    unterhaltsbeitraege: ").append(toIndentedString(unterhaltsbeitraege)).append("\n");
    sb.append("    zulagen: ").append(toIndentedString(zulagen)).append("\n");
    sb.append("    renten: ").append(toIndentedString(renten)).append("\n");
    sb.append("    eoLeistungen: ").append(toIndentedString(eoLeistungen)).append("\n");
    sb.append("    ergaenzungsleistungen: ").append(toIndentedString(ergaenzungsleistungen)).append("\n");
    sb.append("    beitraege: ").append(toIndentedString(beitraege)).append("\n");
    sb.append("    einnahmenBGSA: ").append(toIndentedString(einnahmenBGSA)).append("\n");
    sb.append("    taggelderAHVIV: ").append(toIndentedString(taggelderAHVIV)).append("\n");
    sb.append("    andereEinnahmen: ").append(toIndentedString(andereEinnahmen)).append("\n");
    sb.append("    fahrkosten: ").append(toIndentedString(fahrkosten)).append("\n");
    sb.append("    vermoegen: ").append(toIndentedString(vermoegen)).append("\n");
    sb.append("    steuernKantonGemeinde: ").append(toIndentedString(steuernKantonGemeinde)).append("\n");
    sb.append("    ausbildungskosten: ").append(toIndentedString(ausbildungskosten)).append("\n");
    sb.append("    betreuungskostenKinder: ").append(toIndentedString(betreuungskostenKinder)).append("\n");
    sb.append("    verpflegungskosten: ").append(toIndentedString(verpflegungskosten)).append("\n");
    sb.append("    auswaertigeMittagessenProWoche: ").append(toIndentedString(auswaertigeMittagessenProWoche)).append("\n");
    sb.append("    wohnkosten: ").append(toIndentedString(wohnkosten)).append("\n");
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

