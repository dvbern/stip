package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsPensumDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("AusbildungBase")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungBaseDto  implements Serializable {
  private @Valid String fachrichtung;
  private @Valid String ausbildungBegin;
  private @Valid String ausbildungEnd;
  private @Valid AusbildungsPensumDto pensum;
  private @Valid Boolean ausbildungNichtGefunden;
  private @Valid String alternativeAusbildungsland;
  private @Valid String alternativeAusbildungsstaette;
  private @Valid String alternativeAusbildungsgang;

  /**
   **/
  public AusbildungBaseDto fachrichtung(String fachrichtung) {
    this.fachrichtung = fachrichtung;
    return this;
  }

  
  @JsonProperty("fachrichtung")
  @NotNull
  public String getFachrichtung() {
    return fachrichtung;
  }

  @JsonProperty("fachrichtung")
  public void setFachrichtung(String fachrichtung) {
    this.fachrichtung = fachrichtung;
  }

  /**
   * Datum im Format mm.YYYY
   **/
  public AusbildungBaseDto ausbildungBegin(String ausbildungBegin) {
    this.ausbildungBegin = ausbildungBegin;
    return this;
  }

  
  @JsonProperty("ausbildungBegin")
  @NotNull
  public String getAusbildungBegin() {
    return ausbildungBegin;
  }

  @JsonProperty("ausbildungBegin")
  public void setAusbildungBegin(String ausbildungBegin) {
    this.ausbildungBegin = ausbildungBegin;
  }

  /**
   * Datum im Format mm.YYYY
   **/
  public AusbildungBaseDto ausbildungEnd(String ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
    return this;
  }

  
  @JsonProperty("ausbildungEnd")
  @NotNull
  public String getAusbildungEnd() {
    return ausbildungEnd;
  }

  @JsonProperty("ausbildungEnd")
  public void setAusbildungEnd(String ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
  }

  /**
   **/
  public AusbildungBaseDto pensum(AusbildungsPensumDto pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  @NotNull
  public AusbildungsPensumDto getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(AusbildungsPensumDto pensum) {
    this.pensum = pensum;
  }

  /**
   **/
  public AusbildungBaseDto ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
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

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   **/
  public AusbildungBaseDto alternativeAusbildungsland(String alternativeAusbildungsland) {
    this.alternativeAusbildungsland = alternativeAusbildungsland;
    return this;
  }

  
  @JsonProperty("alternativeAusbildungsland")
  public String getAlternativeAusbildungsland() {
    return alternativeAusbildungsland;
  }

  @JsonProperty("alternativeAusbildungsland")
  public void setAlternativeAusbildungsland(String alternativeAusbildungsland) {
    this.alternativeAusbildungsland = alternativeAusbildungsland;
  }

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   **/
  public AusbildungBaseDto alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
    return this;
  }

  
  @JsonProperty("alternativeAusbildungsstaette")
  public String getAlternativeAusbildungsstaette() {
    return alternativeAusbildungsstaette;
  }

  @JsonProperty("alternativeAusbildungsstaette")
  public void setAlternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
  }

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   **/
  public AusbildungBaseDto alternativeAusbildungsgang(String alternativeAusbildungsgang) {
    this.alternativeAusbildungsgang = alternativeAusbildungsgang;
    return this;
  }

  
  @JsonProperty("alternativeAusbildungsgang")
  public String getAlternativeAusbildungsgang() {
    return alternativeAusbildungsgang;
  }

  @JsonProperty("alternativeAusbildungsgang")
  public void setAlternativeAusbildungsgang(String alternativeAusbildungsgang) {
    this.alternativeAusbildungsgang = alternativeAusbildungsgang;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungBaseDto ausbildungBase = (AusbildungBaseDto) o;
    return Objects.equals(this.fachrichtung, ausbildungBase.fachrichtung) &&
        Objects.equals(this.ausbildungBegin, ausbildungBase.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildungBase.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildungBase.pensum) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildungBase.ausbildungNichtGefunden) &&
        Objects.equals(this.alternativeAusbildungsland, ausbildungBase.alternativeAusbildungsland) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildungBase.alternativeAusbildungsstaette) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildungBase.alternativeAusbildungsgang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fachrichtung, ausbildungBegin, ausbildungEnd, pensum, ausbildungNichtGefunden, alternativeAusbildungsland, alternativeAusbildungsstaette, alternativeAusbildungsgang);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungBaseDto {\n");
    
    sb.append("    fachrichtung: ").append(toIndentedString(fachrichtung)).append("\n");
    sb.append("    ausbildungBegin: ").append(toIndentedString(ausbildungBegin)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
    sb.append("    alternativeAusbildungsland: ").append(toIndentedString(alternativeAusbildungsland)).append("\n");
    sb.append("    alternativeAusbildungsstaette: ").append(toIndentedString(alternativeAusbildungsstaette)).append("\n");
    sb.append("    alternativeAusbildungsgang: ").append(toIndentedString(alternativeAusbildungsgang)).append("\n");
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

