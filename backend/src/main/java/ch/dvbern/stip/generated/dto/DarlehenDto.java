package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Objects;

@JsonTypeName("Darlehen")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenDto  implements Serializable {
  private @Valid Boolean willDarlehen;
  private @Valid Integer betragDarlehen;
  private @Valid Integer betragBezogenKanton;
  private @Valid Integer schulden;
  private @Valid Integer anzahlBetreibungen;
  private @Valid Boolean grundNichtBerechtigt;
  private @Valid Boolean grundAusbildungZwoelfJahre;
  private @Valid Boolean grundHoheGebuehren;
  private @Valid Boolean grundAnschaffungenFuerAusbildung;
  private @Valid Boolean grundZweitausbildung;

  /**
   * Required wenn volljährig, sonst nicht moeglich!
   **/
  public DarlehenDto willDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
    return this;
  }


  @JsonProperty("willDarlehen")
  @NotNull
  public Boolean getWillDarlehen() {
    return willDarlehen;
  }

  @JsonProperty("willDarlehen")
  public void setWillDarlehen(Boolean willDarlehen) {
    this.willDarlehen = willDarlehen;
  }

  /**
   * Required wenn willDarlehen &#x3D; true
   **/
  public DarlehenDto betragDarlehen(Integer betragDarlehen) {
    this.betragDarlehen = betragDarlehen;
    return this;
  }


  @JsonProperty("betragDarlehen")
  public Integer getBetragDarlehen() {
    return betragDarlehen;
  }

  @JsonProperty("betragDarlehen")
  public void setBetragDarlehen(Integer betragDarlehen) {
    this.betragDarlehen = betragDarlehen;
  }

  /**
   * Required wenn willDarlehen &#x3D; true
   **/
  public DarlehenDto betragBezogenKanton(Integer betragBezogenKanton) {
    this.betragBezogenKanton = betragBezogenKanton;
    return this;
  }


  @JsonProperty("betragBezogenKanton")
  public Integer getBetragBezogenKanton() {
    return betragBezogenKanton;
  }

  @JsonProperty("betragBezogenKanton")
  public void setBetragBezogenKanton(Integer betragBezogenKanton) {
    this.betragBezogenKanton = betragBezogenKanton;
  }

  /**
   * Required wenn willDarlehen &#x3D; true
   **/
  public DarlehenDto schulden(Integer schulden) {
    this.schulden = schulden;
    return this;
  }


  @JsonProperty("schulden")
  public Integer getSchulden() {
    return schulden;
  }

  @JsonProperty("schulden")
  public void setSchulden(Integer schulden) {
    this.schulden = schulden;
  }

  /**
   * Required wenn willDarlehen &#x3D; true - mit Fileupload
   **/
  public DarlehenDto anzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
    return this;
  }


  @JsonProperty("anzahlBetreibungen")
  public Integer getAnzahlBetreibungen() {
    return anzahlBetreibungen;
  }

  @JsonProperty("anzahlBetreibungen")
  public void setAnzahlBetreibungen(Integer anzahlBetreibungen) {
    this.anzahlBetreibungen = anzahlBetreibungen;
  }

  /**
   * Keine Stipendienberechtigung, auf Grund der tatsächlichen Kosten können die Mittel der Eltern jedoch nicht einbezogen werden - Falls Ja mit Fileupload
   **/
  public DarlehenDto grundNichtBerechtigt(Boolean grundNichtBerechtigt) {
    this.grundNichtBerechtigt = grundNichtBerechtigt;
    return this;
  }


  @JsonProperty("grundNichtBerechtigt")
  public Boolean getGrundNichtBerechtigt() {
    return grundNichtBerechtigt;
  }

  @JsonProperty("grundNichtBerechtigt")
  public void setGrundNichtBerechtigt(Boolean grundNichtBerechtigt) {
    this.grundNichtBerechtigt = grundNichtBerechtigt;
  }

  /**
   * Keine Stipendienberechtigung wegen Überschreitens der Ausbildungsdauer von 12 Jahren.
   **/
  public DarlehenDto grundAusbildungZwoelfJahre(Boolean grundAusbildungZwoelfJahre) {
    this.grundAusbildungZwoelfJahre = grundAusbildungZwoelfJahre;
    return this;
  }


  @JsonProperty("grundAusbildungZwoelfJahre")
  public Boolean getGrundAusbildungZwoelfJahre() {
    return grundAusbildungZwoelfJahre;
  }

  @JsonProperty("grundAusbildungZwoelfJahre")
  public void setGrundAusbildungZwoelfJahre(Boolean grundAusbildungZwoelfJahre) {
    this.grundAusbildungZwoelfJahre = grundAusbildungZwoelfJahre;
  }

  /**
   * Falls ja mit Fileupload
   **/
  public DarlehenDto grundHoheGebuehren(Boolean grundHoheGebuehren) {
    this.grundHoheGebuehren = grundHoheGebuehren;
    return this;
  }


  @JsonProperty("grundHoheGebuehren")
  public Boolean getGrundHoheGebuehren() {
    return grundHoheGebuehren;
  }

  @JsonProperty("grundHoheGebuehren")
  public void setGrundHoheGebuehren(Boolean grundHoheGebuehren) {
    this.grundHoheGebuehren = grundHoheGebuehren;
  }

  /**
   * Falls ja mit Fileupload
   **/
  public DarlehenDto grundAnschaffungenFuerAusbildung(Boolean grundAnschaffungenFuerAusbildung) {
    this.grundAnschaffungenFuerAusbildung = grundAnschaffungenFuerAusbildung;
    return this;
  }


  @JsonProperty("grundAnschaffungenFuerAusbildung")
  public Boolean getGrundAnschaffungenFuerAusbildung() {
    return grundAnschaffungenFuerAusbildung;
  }

  @JsonProperty("grundAnschaffungenFuerAusbildung")
  public void setGrundAnschaffungenFuerAusbildung(Boolean grundAnschaffungenFuerAusbildung) {
    this.grundAnschaffungenFuerAusbildung = grundAnschaffungenFuerAusbildung;
  }

  /**
   **/
  public DarlehenDto grundZweitausbildung(Boolean grundZweitausbildung) {
    this.grundZweitausbildung = grundZweitausbildung;
    return this;
  }


  @JsonProperty("grundZweitausbildung")
  public Boolean getGrundZweitausbildung() {
    return grundZweitausbildung;
  }

  @JsonProperty("grundZweitausbildung")
  public void setGrundZweitausbildung(Boolean grundZweitausbildung) {
    this.grundZweitausbildung = grundZweitausbildung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarlehenDto darlehen = (DarlehenDto) o;
    return Objects.equals(this.willDarlehen, darlehen.willDarlehen) &&
        Objects.equals(this.betragDarlehen, darlehen.betragDarlehen) &&
        Objects.equals(this.betragBezogenKanton, darlehen.betragBezogenKanton) &&
        Objects.equals(this.schulden, darlehen.schulden) &&
        Objects.equals(this.anzahlBetreibungen, darlehen.anzahlBetreibungen) &&
        Objects.equals(this.grundNichtBerechtigt, darlehen.grundNichtBerechtigt) &&
        Objects.equals(this.grundAusbildungZwoelfJahre, darlehen.grundAusbildungZwoelfJahre) &&
        Objects.equals(this.grundHoheGebuehren, darlehen.grundHoheGebuehren) &&
        Objects.equals(this.grundAnschaffungenFuerAusbildung, darlehen.grundAnschaffungenFuerAusbildung) &&
        Objects.equals(this.grundZweitausbildung, darlehen.grundZweitausbildung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(willDarlehen, betragDarlehen, betragBezogenKanton, schulden, anzahlBetreibungen, grundNichtBerechtigt, grundAusbildungZwoelfJahre, grundHoheGebuehren, grundAnschaffungenFuerAusbildung, grundZweitausbildung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenDto {\n");

    sb.append("    willDarlehen: ").append(toIndentedString(willDarlehen)).append("\n");
    sb.append("    betragDarlehen: ").append(toIndentedString(betragDarlehen)).append("\n");
    sb.append("    betragBezogenKanton: ").append(toIndentedString(betragBezogenKanton)).append("\n");
    sb.append("    schulden: ").append(toIndentedString(schulden)).append("\n");
    sb.append("    anzahlBetreibungen: ").append(toIndentedString(anzahlBetreibungen)).append("\n");
    sb.append("    grundNichtBerechtigt: ").append(toIndentedString(grundNichtBerechtigt)).append("\n");
    sb.append("    grundAusbildungZwoelfJahre: ").append(toIndentedString(grundAusbildungZwoelfJahre)).append("\n");
    sb.append("    grundHoheGebuehren: ").append(toIndentedString(grundHoheGebuehren)).append("\n");
    sb.append("    grundAnschaffungenFuerAusbildung: ").append(toIndentedString(grundAnschaffungenFuerAusbildung)).append("\n");
    sb.append("    grundZweitausbildung: ").append(toIndentedString(grundZweitausbildung)).append("\n");
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

