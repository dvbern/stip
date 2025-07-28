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



@JsonTypeName("AusbildungBase")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AusbildungBaseDto  implements Serializable {
  private @Valid UUID fallId;
  private @Valid String ausbildungBegin;
  private @Valid String ausbildungEnd;
  private @Valid AusbildungsPensumDto pensum;
  private @Valid UUID id;
  private @Valid String fachrichtung;
  private @Valid Boolean ausbildungNichtGefunden;
  private @Valid Boolean besuchtBMS;
  private @Valid String alternativeAusbildungsstaette;
  private @Valid String alternativeAusbildungsgang;
  private @Valid String ausbildungsort;
  private @Valid Boolean isAusbildungAusland;

  /**
   **/
  public AusbildungBaseDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public UUID getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
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
  public AusbildungBaseDto id(UUID id) {
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
  public AusbildungBaseDto fachrichtung(String fachrichtung) {
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
   * Required bei Ausbildungskategorien 4 oder 5. Kann nur dann auf true gesetzt werden.
   **/
  public AusbildungBaseDto besuchtBMS(Boolean besuchtBMS) {
    this.besuchtBMS = besuchtBMS;
    return this;
  }

  
  @JsonProperty("besuchtBMS")
  public Boolean getBesuchtBMS() {
    return besuchtBMS;
  }

  @JsonProperty("besuchtBMS")
  public void setBesuchtBMS(Boolean besuchtBMS) {
    this.besuchtBMS = besuchtBMS;
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

  /**
   * Not required if isAusbildungAusland &#x3D; true
   **/
  public AusbildungBaseDto ausbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
    return this;
  }

  
  @JsonProperty("ausbildungsort")
  public String getAusbildungsort() {
    return ausbildungsort;
  }

  @JsonProperty("ausbildungsort")
  public void setAusbildungsort(String ausbildungsort) {
    this.ausbildungsort = ausbildungsort;
  }

  /**
   **/
  public AusbildungBaseDto isAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
    return this;
  }

  
  @JsonProperty("isAusbildungAusland")
  public Boolean getIsAusbildungAusland() {
    return isAusbildungAusland;
  }

  @JsonProperty("isAusbildungAusland")
  public void setIsAusbildungAusland(Boolean isAusbildungAusland) {
    this.isAusbildungAusland = isAusbildungAusland;
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
    return Objects.equals(this.fallId, ausbildungBase.fallId) &&
        Objects.equals(this.ausbildungBegin, ausbildungBase.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildungBase.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildungBase.pensum) &&
        Objects.equals(this.id, ausbildungBase.id) &&
        Objects.equals(this.fachrichtung, ausbildungBase.fachrichtung) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildungBase.ausbildungNichtGefunden) &&
        Objects.equals(this.besuchtBMS, ausbildungBase.besuchtBMS) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildungBase.alternativeAusbildungsstaette) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildungBase.alternativeAusbildungsgang) &&
        Objects.equals(this.ausbildungsort, ausbildungBase.ausbildungsort) &&
        Objects.equals(this.isAusbildungAusland, ausbildungBase.isAusbildungAusland);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, ausbildungBegin, ausbildungEnd, pensum, id, fachrichtung, ausbildungNichtGefunden, besuchtBMS, alternativeAusbildungsstaette, alternativeAusbildungsgang, ausbildungsort, isAusbildungAusland);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungBaseDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    ausbildungBegin: ").append(toIndentedString(ausbildungBegin)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fachrichtung: ").append(toIndentedString(fachrichtung)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
    sb.append("    besuchtBMS: ").append(toIndentedString(besuchtBMS)).append("\n");
    sb.append("    alternativeAusbildungsstaette: ").append(toIndentedString(alternativeAusbildungsstaette)).append("\n");
    sb.append("    alternativeAusbildungsgang: ").append(toIndentedString(alternativeAusbildungsgang)).append("\n");
    sb.append("    ausbildungsort: ").append(toIndentedString(ausbildungsort)).append("\n");
    sb.append("    isAusbildungAusland: ").append(toIndentedString(isAusbildungAusland)).append("\n");
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

