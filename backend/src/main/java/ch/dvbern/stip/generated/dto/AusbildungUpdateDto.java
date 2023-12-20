package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsPensumDto;
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



@JsonTypeName("AusbildungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungUpdateDto  implements Serializable {
  private @Valid String fachrichtung;
  private @Valid String ausbildungBegin;
  private @Valid String ausbildungEnd;
  private @Valid AusbildungsPensumDto pensum;
  private @Valid UUID ausbildungsgangId;
  private @Valid String alternativeAusbildungsgang;
  private @Valid Boolean ausbildungNichtGefunden;
  private @Valid String alternativeAusbildungsstaette;

  /**
   **/
  public AusbildungUpdateDto fachrichtung(String fachrichtung) {
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
  public AusbildungUpdateDto ausbildungBegin(String ausbildungBegin) {
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
  public AusbildungUpdateDto ausbildungEnd(String ausbildungEnd) {
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
  public AusbildungUpdateDto pensum(AusbildungsPensumDto pensum) {
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
  public AusbildungUpdateDto ausbildungsgangId(UUID ausbildungsgangId) {
    this.ausbildungsgangId = ausbildungsgangId;
    return this;
  }

  
  @JsonProperty("ausbildungsgangId")
  public UUID getAusbildungsgangId() {
    return ausbildungsgangId;
  }

  @JsonProperty("ausbildungsgangId")
  public void setAusbildungsgangId(UUID ausbildungsgangId) {
    this.ausbildungsgangId = ausbildungsgangId;
  }

  /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true 
   **/
  public AusbildungUpdateDto alternativeAusbildungsgang(String alternativeAusbildungsgang) {
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

  /**
   **/
  public AusbildungUpdateDto ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
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
  public AusbildungUpdateDto alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungUpdateDto ausbildungUpdate = (AusbildungUpdateDto) o;
    return Objects.equals(this.fachrichtung, ausbildungUpdate.fachrichtung) &&
        Objects.equals(this.ausbildungBegin, ausbildungUpdate.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildungUpdate.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildungUpdate.pensum) &&
        Objects.equals(this.ausbildungsgangId, ausbildungUpdate.ausbildungsgangId) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildungUpdate.alternativeAusbildungsgang) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildungUpdate.ausbildungNichtGefunden) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildungUpdate.alternativeAusbildungsstaette);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fachrichtung, ausbildungBegin, ausbildungEnd, pensum, ausbildungsgangId, alternativeAusbildungsgang, ausbildungNichtGefunden, alternativeAusbildungsstaette);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUpdateDto {\n");
    
    sb.append("    fachrichtung: ").append(toIndentedString(fachrichtung)).append("\n");
    sb.append("    ausbildungBegin: ").append(toIndentedString(ausbildungBegin)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
    sb.append("    ausbildungsgangId: ").append(toIndentedString(ausbildungsgangId)).append("\n");
    sb.append("    alternativeAusbildungsgang: ").append(toIndentedString(alternativeAusbildungsgang)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
    sb.append("    alternativeAusbildungsstaette: ").append(toIndentedString(alternativeAusbildungsstaette)).append("\n");
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

