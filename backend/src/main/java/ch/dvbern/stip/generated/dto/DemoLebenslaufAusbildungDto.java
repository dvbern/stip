package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("DemoLebenslaufAusbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoLebenslaufAusbildungDto  implements Serializable {
  private @Valid String abschluss;
  private @Valid LocalDate von;
  private @Valid LocalDate bis;
  private @Valid ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz;
  private @Valid Boolean ausbildungAbgeschlossen;
  private @Valid String berufsbezeichnungFachrichtung;

  /**
   **/
  public DemoLebenslaufAusbildungDto abschluss(String abschluss) {
    this.abschluss = abschluss;
    return this;
  }

  
  @JsonProperty("abschluss")
  @NotNull
  public String getAbschluss() {
    return abschluss;
  }

  @JsonProperty("abschluss")
  public void setAbschluss(String abschluss) {
    this.abschluss = abschluss;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto von(LocalDate von) {
    this.von = von;
    return this;
  }

  
  @JsonProperty("von")
  @NotNull
  public LocalDate getVon() {
    return von;
  }

  @JsonProperty("von")
  public void setVon(LocalDate von) {
    this.von = von;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto bis(LocalDate bis) {
    this.bis = bis;
    return this;
  }

  
  @JsonProperty("bis")
  @NotNull
  public LocalDate getBis() {
    return bis;
  }

  @JsonProperty("bis")
  public void setBis(LocalDate bis) {
    this.bis = bis;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto wohnsitz(ch.dvbern.stip.api.lebenslauf.type.WohnsitzKanton wohnsitz) {
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
  public DemoLebenslaufAusbildungDto ausbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
    return this;
  }

  
  @JsonProperty("ausbildungAbgeschlossen")
  @NotNull
  public Boolean getAusbildungAbgeschlossen() {
    return ausbildungAbgeschlossen;
  }

  @JsonProperty("ausbildungAbgeschlossen")
  public void setAusbildungAbgeschlossen(Boolean ausbildungAbgeschlossen) {
    this.ausbildungAbgeschlossen = ausbildungAbgeschlossen;
  }

  /**
   **/
  public DemoLebenslaufAusbildungDto berufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
    return this;
  }

  
  @JsonProperty("berufsbezeichnungFachrichtung")
  public String getBerufsbezeichnungFachrichtung() {
    return berufsbezeichnungFachrichtung;
  }

  @JsonProperty("berufsbezeichnungFachrichtung")
  public void setBerufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoLebenslaufAusbildungDto demoLebenslaufAusbildung = (DemoLebenslaufAusbildungDto) o;
    return Objects.equals(this.abschluss, demoLebenslaufAusbildung.abschluss) &&
        Objects.equals(this.von, demoLebenslaufAusbildung.von) &&
        Objects.equals(this.bis, demoLebenslaufAusbildung.bis) &&
        Objects.equals(this.wohnsitz, demoLebenslaufAusbildung.wohnsitz) &&
        Objects.equals(this.ausbildungAbgeschlossen, demoLebenslaufAusbildung.ausbildungAbgeschlossen) &&
        Objects.equals(this.berufsbezeichnungFachrichtung, demoLebenslaufAusbildung.berufsbezeichnungFachrichtung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abschluss, von, bis, wohnsitz, ausbildungAbgeschlossen, berufsbezeichnungFachrichtung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoLebenslaufAusbildungDto {\n");
    
    sb.append("    abschluss: ").append(toIndentedString(abschluss)).append("\n");
    sb.append("    von: ").append(toIndentedString(von)).append("\n");
    sb.append("    bis: ").append(toIndentedString(bis)).append("\n");
    sb.append("    wohnsitz: ").append(toIndentedString(wohnsitz)).append("\n");
    sb.append("    ausbildungAbgeschlossen: ").append(toIndentedString(ausbildungAbgeschlossen)).append("\n");
    sb.append("    berufsbezeichnungFachrichtung: ").append(toIndentedString(berufsbezeichnungFachrichtung)).append("\n");
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

