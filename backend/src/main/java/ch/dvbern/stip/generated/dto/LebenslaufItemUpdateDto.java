package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("LebenslaufItemUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class LebenslaufItemUpdateDto  implements Serializable {
  private @Valid String von;
  private @Valid String bis;
  private @Valid ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz;
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt bildungsart;
  private @Valid Boolean ausbildungAbgeschlossen;
  private @Valid String berufsbezeichnung;
  private @Valid String fachrichtung;
  private @Valid String titelDesAbschlusses;
  private @Valid ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart;
  private @Valid String taetigkeitsBeschreibung;

  /**
   * Datum im Format mm.YYYY
   **/
  public LebenslaufItemUpdateDto von(String von) {
    this.von = von;
    return this;
  }

  
  @JsonProperty("von")
  @NotNull
  public String getVon() {
    return von;
  }

  @JsonProperty("von")
  public void setVon(String von) {
    this.von = von;
  }

  /**
   * Datum im Format mm.YYYY
   **/
  public LebenslaufItemUpdateDto bis(String bis) {
    this.bis = bis;
    return this;
  }

  
  @JsonProperty("bis")
  @NotNull
  public String getBis() {
    return bis;
  }

  @JsonProperty("bis")
  public void setBis(String bis) {
    this.bis = bis;
  }

  /**
   **/
  public LebenslaufItemUpdateDto wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
    return this;
  }

  
  @JsonProperty("wohnsitz")
  @NotNull
  public ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton getWohnsitz() {
    return wohnsitz;
  }

  @JsonProperty("wohnsitz")
  public void setWohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
    this.wohnsitz = wohnsitz;
  }

  /**
   **/
  public LebenslaufItemUpdateDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public LebenslaufItemUpdateDto bildungsart(ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt bildungsart) {
    this.bildungsart = bildungsart;
    return this;
  }

  
  @JsonProperty("bildungsart")
  public ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt getBildungsart() {
    return bildungsart;
  }

  @JsonProperty("bildungsart")
  public void setBildungsart(ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt bildungsart) {
    this.bildungsart = bildungsart;
  }

  /**
   **/
  public LebenslaufItemUpdateDto ausbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
    return this;
  }

  
  @JsonProperty("ausbildungAbgeschlossen")
  public Boolean getAusbildungAbgeschlossen() {
    return ausbildungAbgeschlossen;
  }

  @JsonProperty("ausbildungAbgeschlossen")
  public void setAusbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
  }

  /**
   * Requierd wenn bildungsart &#x3D; &#39;EIDGENOESSISCHES_BERUFSATTEST&#39; oder &#39;EIDGENOESSISCHES_FAEHIGKEITSZEUGNIS&#39;
   **/
  public LebenslaufItemUpdateDto berufsbezeichnung(String berufsbezeichnung) {
    this.berufsbezeichnung = berufsbezeichnung;
    return this;
  }

  
  @JsonProperty("berufsbezeichnung")
  public String getBerufsbezeichnung() {
    return berufsbezeichnung;
  }

  @JsonProperty("berufsbezeichnung")
  public void setBerufsbezeichnung(String berufsbezeichnung) {
    this.berufsbezeichnung = berufsbezeichnung;
  }

  /**
   * Required wenn bildungsart &#x3D; \&quot;BACHELOR_HOCHSCHULE_UNI\&quot; oder \&quot;BACHELOR_FACHHOCHSCHULE\&quot; oder \&quot;MASTER\&quot;
   **/
  public LebenslaufItemUpdateDto fachrichtung(String fachrichtung) {
    this.fachrichtung = fachrichtung;
    return this;
  }

  
  @JsonProperty("fachrichtung")
  public String getFachrichtung() {
    return fachrichtung;
  }

  @JsonProperty("fachrichtung")
  public void setFachrichtung(String fachrichtung) {
    this.fachrichtung = fachrichtung;
  }

  /**
   * Required wenn bildungsart &#x3D; \&quot;ANDERER_AUSBILDUNGSABSCHLUSS\&quot;
   **/
  public LebenslaufItemUpdateDto titelDesAbschlusses(String titelDesAbschlusses) {
    this.titelDesAbschlusses = titelDesAbschlusses;
    return this;
  }

  
  @JsonProperty("titelDesAbschlusses")
  public String getTitelDesAbschlusses() {
    return titelDesAbschlusses;
  }

  @JsonProperty("titelDesAbschlusses")
  public void setTitelDesAbschlusses(String titelDesAbschlusses) {
    this.titelDesAbschlusses = titelDesAbschlusses;
  }

  /**
   **/
  public LebenslaufItemUpdateDto taetigkeitsart(ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart) {
    this.taetigkeitsart = taetigkeitsart;
    return this;
  }

  
  @JsonProperty("taetigkeitsart")
  public ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart getTaetigkeitsart() {
    return taetigkeitsart;
  }

  @JsonProperty("taetigkeitsart")
  public void setTaetigkeitsart(ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart) {
    this.taetigkeitsart = taetigkeitsart;
  }

  /**
   * Required wenn taetigkeitsart !&#x3D; null
   **/
  public LebenslaufItemUpdateDto taetigkeitsBeschreibung(String taetigkeitsBeschreibung) {
    this.taetigkeitsBeschreibung = taetigkeitsBeschreibung;
    return this;
  }

  
  @JsonProperty("taetigkeitsBeschreibung")
  public String getTaetigkeitsBeschreibung() {
    return taetigkeitsBeschreibung;
  }

  @JsonProperty("taetigkeitsBeschreibung")
  public void setTaetigkeitsBeschreibung(String taetigkeitsBeschreibung) {
    this.taetigkeitsBeschreibung = taetigkeitsBeschreibung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LebenslaufItemUpdateDto lebenslaufItemUpdate = (LebenslaufItemUpdateDto) o;
    return Objects.equals(this.von, lebenslaufItemUpdate.von) &&
        Objects.equals(this.bis, lebenslaufItemUpdate.bis) &&
        Objects.equals(this.wohnsitz, lebenslaufItemUpdate.wohnsitz) &&
        Objects.equals(this.id, lebenslaufItemUpdate.id) &&
        Objects.equals(this.bildungsart, lebenslaufItemUpdate.bildungsart) &&
        Objects.equals(this.ausbildungAbgeschlossen, lebenslaufItemUpdate.ausbildungAbgeschlossen) &&
        Objects.equals(this.berufsbezeichnung, lebenslaufItemUpdate.berufsbezeichnung) &&
        Objects.equals(this.fachrichtung, lebenslaufItemUpdate.fachrichtung) &&
        Objects.equals(this.titelDesAbschlusses, lebenslaufItemUpdate.titelDesAbschlusses) &&
        Objects.equals(this.taetigkeitsart, lebenslaufItemUpdate.taetigkeitsart) &&
        Objects.equals(this.taetigkeitsBeschreibung, lebenslaufItemUpdate.taetigkeitsBeschreibung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(von, bis, wohnsitz, id, bildungsart, ausbildungAbgeschlossen, berufsbezeichnung, fachrichtung, titelDesAbschlusses, taetigkeitsart, taetigkeitsBeschreibung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LebenslaufItemUpdateDto {\n");
    
    sb.append("    von: ").append(toIndentedString(von)).append("\n");
    sb.append("    bis: ").append(toIndentedString(bis)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bildungsart: ").append(toIndentedString(bildungsart)).append("\n");
    sb.append("    ausbildungAbgeschlossen: ").append(toIndentedString(ausbildungAbgeschlossen)).append("\n");
    sb.append("    berufsbezeichnung: ").append(toIndentedString(berufsbezeichnung)).append("\n");
    sb.append("    fachrichtung: ").append(toIndentedString(fachrichtung)).append("\n");
    sb.append("    titelDesAbschlusses: ").append(toIndentedString(titelDesAbschlusses)).append("\n");
    sb.append("    taetigkeitsart: ").append(toIndentedString(taetigkeitsart)).append("\n");
    sb.append("    taetigkeitsBeschreibung: ").append(toIndentedString(taetigkeitsBeschreibung)).append("\n");
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

