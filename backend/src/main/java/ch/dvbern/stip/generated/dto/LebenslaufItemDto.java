package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("LebenslaufItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class LebenslaufItemDto  implements Serializable {
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
  public LebenslaufItemDto von(String von) {
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
  public LebenslaufItemDto bis(String bis) {
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
  public LebenslaufItemDto wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
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
  public LebenslaufItemDto id(UUID id) {
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
  public LebenslaufItemDto bildungsart(ch.dvbern.stip.api.lebenslauf.type.LebenslaufAusbildungsArt bildungsart) {
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
  public LebenslaufItemDto ausbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
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
  public LebenslaufItemDto berufsbezeichnung(String berufsbezeichnung) {
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
  public LebenslaufItemDto fachrichtung(String fachrichtung) {
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
  public LebenslaufItemDto titelDesAbschlusses(String titelDesAbschlusses) {
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
  public LebenslaufItemDto taetigkeitsart(ch.dvbern.stip.api.lebenslauf.type.Taetigkeitsart taetigkeitsart) {
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
  public LebenslaufItemDto taetigkeitsBeschreibung(String taetigkeitsBeschreibung) {
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
    LebenslaufItemDto lebenslaufItem = (LebenslaufItemDto) o;
    return Objects.equals(this.von, lebenslaufItem.von) &&
        Objects.equals(this.bis, lebenslaufItem.bis) &&
        Objects.equals(this.wohnsitz, lebenslaufItem.wohnsitz) &&
        Objects.equals(this.id, lebenslaufItem.id) &&
        Objects.equals(this.bildungsart, lebenslaufItem.bildungsart) &&
        Objects.equals(this.ausbildungAbgeschlossen, lebenslaufItem.ausbildungAbgeschlossen) &&
        Objects.equals(this.berufsbezeichnung, lebenslaufItem.berufsbezeichnung) &&
        Objects.equals(this.fachrichtung, lebenslaufItem.fachrichtung) &&
        Objects.equals(this.titelDesAbschlusses, lebenslaufItem.titelDesAbschlusses) &&
        Objects.equals(this.taetigkeitsart, lebenslaufItem.taetigkeitsart) &&
        Objects.equals(this.taetigkeitsBeschreibung, lebenslaufItem.taetigkeitsBeschreibung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(von, bis, wohnsitz, id, bildungsart, ausbildungAbgeschlossen, berufsbezeichnung, fachrichtung, titelDesAbschlusses, taetigkeitsart, taetigkeitsBeschreibung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LebenslaufItemDto {\n");

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

