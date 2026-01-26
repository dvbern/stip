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



@JsonTypeName("DemoAusbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DemoAusbildungDto  implements Serializable {
  private @Valid String ausbildungsstaette;
  private @Valid String ausbildungsgang;
  private @Valid String berufsbezeichnungFachrichtung;
  private @Valid String plz;
  private @Valid String ort;
  private @Valid Boolean isAusbildungAusland;
  private @Valid LocalDate ausbildungBeginn;
  private @Valid LocalDate ausbildungEnd;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;
  private @Valid Boolean ausbildungNichtGefunden;

  /**
   **/
  public DemoAusbildungDto ausbildungsstaette(String ausbildungsstaette) {
    this.ausbildungsstaette = ausbildungsstaette;
    return this;
  }

  
  @JsonProperty("ausbildungsstaette")
  @NotNull
  public String getAusbildungsstaette() {
    return ausbildungsstaette;
  }

  @JsonProperty("ausbildungsstaette")
  public void setAusbildungsstaette(String ausbildungsstaette) {
    this.ausbildungsstaette = ausbildungsstaette;
  }

  /**
   **/
  public DemoAusbildungDto ausbildungsgang(String ausbildungsgang) {
    this.ausbildungsgang = ausbildungsgang;
    return this;
  }

  
  @JsonProperty("ausbildungsgang")
  @NotNull
  public String getAusbildungsgang() {
    return ausbildungsgang;
  }

  @JsonProperty("ausbildungsgang")
  public void setAusbildungsgang(String ausbildungsgang) {
    this.ausbildungsgang = ausbildungsgang;
  }

  /**
   **/
  public DemoAusbildungDto berufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
    return this;
  }

  
  @JsonProperty("berufsbezeichnungFachrichtung")
  @NotNull
  public String getBerufsbezeichnungFachrichtung() {
    return berufsbezeichnungFachrichtung;
  }

  @JsonProperty("berufsbezeichnungFachrichtung")
  public void setBerufsbezeichnungFachrichtung(String berufsbezeichnungFachrichtung) {
    this.berufsbezeichnungFachrichtung = berufsbezeichnungFachrichtung;
  }

  /**
   **/
  public DemoAusbildungDto plz(String plz) {
    this.plz = plz;
    return this;
  }

  
  @JsonProperty("plz")
  @NotNull
  public String getPlz() {
    return plz;
  }

  @JsonProperty("plz")
  public void setPlz(String plz) {
    this.plz = plz;
  }

  /**
   **/
  public DemoAusbildungDto ort(String ort) {
    this.ort = ort;
    return this;
  }

  
  @JsonProperty("ort")
  @NotNull
  public String getOrt() {
    return ort;
  }

  @JsonProperty("ort")
  public void setOrt(String ort) {
    this.ort = ort;
  }

  /**
   **/
  public DemoAusbildungDto isAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
    return this;
  }

  
  @JsonProperty("isAusbildungAusland")
  @NotNull
  public Boolean getIsAusbildungAusland() {
    return isAusbildungAusland;
  }

  @JsonProperty("isAusbildungAusland")
  public void setIsAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
  }

  /**
   **/
  public DemoAusbildungDto ausbildungBeginn(LocalDate ausbildungBeginn) {
    this.ausbildungBeginn = ausbildungBeginn;
    return this;
  }

  
  @JsonProperty("ausbildungBeginn")
  @NotNull
  public LocalDate getAusbildungBeginn() {
    return ausbildungBeginn;
  }

  @JsonProperty("ausbildungBeginn")
  public void setAusbildungBeginn(LocalDate ausbildungBeginn) {
    this.ausbildungBeginn = ausbildungBeginn;
  }

  /**
   **/
  public DemoAusbildungDto ausbildungEnd(LocalDate ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
    return this;
  }

  
  @JsonProperty("ausbildungEnd")
  @NotNull
  public LocalDate getAusbildungEnd() {
    return ausbildungEnd;
  }

  @JsonProperty("ausbildungEnd")
  public void setAusbildungEnd(LocalDate ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
  }

  /**
   **/
  public DemoAusbildungDto pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
  }

  /**
   **/
  public DemoAusbildungDto ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
    return this;
  }

  
  @JsonProperty("ausbildungNichtGefunden")
  public Boolean getAusbildungNichtGefunden() {
    return ausbildungNichtGefunden;
  }

  @JsonProperty("ausbildungNichtGefunden")
  public void setAusbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoAusbildungDto demoAusbildung = (DemoAusbildungDto) o;
    return Objects.equals(this.ausbildungsstaette, demoAusbildung.ausbildungsstaette) &&
        Objects.equals(this.ausbildungsgang, demoAusbildung.ausbildungsgang) &&
        Objects.equals(this.berufsbezeichnungFachrichtung, demoAusbildung.berufsbezeichnungFachrichtung) &&
        Objects.equals(this.plz, demoAusbildung.plz) &&
        Objects.equals(this.ort, demoAusbildung.ort) &&
        Objects.equals(this.isAusbildungAusland, demoAusbildung.isAusbildungAusland) &&
        Objects.equals(this.ausbildungBeginn, demoAusbildung.ausbildungBeginn) &&
        Objects.equals(this.ausbildungEnd, demoAusbildung.ausbildungEnd) &&
        Objects.equals(this.pensum, demoAusbildung.pensum) &&
        Objects.equals(this.ausbildungNichtGefunden, demoAusbildung.ausbildungNichtGefunden);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildungsstaette, ausbildungsgang, berufsbezeichnungFachrichtung, plz, ort, isAusbildungAusland, ausbildungBeginn, ausbildungEnd, pensum, ausbildungNichtGefunden);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoAusbildungDto {\n");
    
    sb.append("    ausbildungsstaette: ").append(toIndentedString(ausbildungsstaette)).append("\n");
    sb.append("    ausbildungsgang: ").append(toIndentedString(ausbildungsgang)).append("\n");
    sb.append("    berufsbezeichnungFachrichtung: ").append(toIndentedString(berufsbezeichnungFachrichtung)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    isAusbildungAusland: ").append(toIndentedString(isAusbildungAusland)).append("\n");
    sb.append("    ausbildungBeginn: ").append(toIndentedString(ausbildungBeginn)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
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

