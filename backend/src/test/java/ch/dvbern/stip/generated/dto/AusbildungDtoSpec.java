/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import ch.dvbern.stip.generated.dto.AusbildungsPensumDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * AusbildungDtoSpec
 */
@JsonPropertyOrder({
  AusbildungDtoSpec.JSON_PROPERTY_AUSBILDUNGSGANG_ID,
  AusbildungDtoSpec.JSON_PROPERTY_FACHRICHTUNG,
  AusbildungDtoSpec.JSON_PROPERTY_AUSBILDUNG_NICHT_GEFUNDEN,
  AusbildungDtoSpec.JSON_PROPERTY_AUSBILDUNG_BEGIN,
  AusbildungDtoSpec.JSON_PROPERTY_AUSBILDUNG_END,
  AusbildungDtoSpec.JSON_PROPERTY_PENSUM,
  AusbildungDtoSpec.JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSLAND,
  AusbildungDtoSpec.JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSSTAETTE,
  AusbildungDtoSpec.JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSGANG
})
@JsonTypeName("Ausbildung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AusbildungDtoSpec {
  public static final String JSON_PROPERTY_AUSBILDUNGSGANG_ID = "ausbildungsgangId";
  private UUID ausbildungsgangId;

  public static final String JSON_PROPERTY_FACHRICHTUNG = "fachrichtung";
  private String fachrichtung;

  public static final String JSON_PROPERTY_AUSBILDUNG_NICHT_GEFUNDEN = "ausbildungNichtGefunden";
  private Boolean ausbildungNichtGefunden;

  public static final String JSON_PROPERTY_AUSBILDUNG_BEGIN = "ausbildungBegin";
  private String ausbildungBegin;

  public static final String JSON_PROPERTY_AUSBILDUNG_END = "ausbildungEnd";
  private String ausbildungEnd;

  public static final String JSON_PROPERTY_PENSUM = "pensum";
  private AusbildungsPensumDtoSpec pensum;

  public static final String JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSLAND = "alternativeAusbildungsland";
  private String alternativeAusbildungsland;

  public static final String JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSSTAETTE = "alternativeAusbildungsstaette";
  private String alternativeAusbildungsstaette;

  public static final String JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSGANG = "alternativeAusbildungsgang";
  private String alternativeAusbildungsgang;

  public AusbildungDtoSpec() {
  }

  public AusbildungDtoSpec ausbildungsgangId(UUID ausbildungsgangId) {
    
    this.ausbildungsgangId = ausbildungsgangId;
    return this;
  }

   /**
   * Get ausbildungsgangId
   * @return ausbildungsgangId
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSGANG_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UUID getAusbildungsgangId() {
    return ausbildungsgangId;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSGANG_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAusbildungsgangId(UUID ausbildungsgangId) {
    this.ausbildungsgangId = ausbildungsgangId;
  }


  public AusbildungDtoSpec fachrichtung(String fachrichtung) {
    
    this.fachrichtung = fachrichtung;
    return this;
  }

   /**
   * Get fachrichtung
   * @return fachrichtung
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_FACHRICHTUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getFachrichtung() {
    return fachrichtung;
  }


  @JsonProperty(JSON_PROPERTY_FACHRICHTUNG)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setFachrichtung(String fachrichtung) {
    this.fachrichtung = fachrichtung;
  }


  public AusbildungDtoSpec ausbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
    return this;
  }

   /**
   * Get ausbildungNichtGefunden
   * @return ausbildungNichtGefunden
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_NICHT_GEFUNDEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Boolean getAusbildungNichtGefunden() {
    return ausbildungNichtGefunden;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_NICHT_GEFUNDEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAusbildungNichtGefunden(Boolean ausbildungNichtGefunden) {
    this.ausbildungNichtGefunden = ausbildungNichtGefunden;
  }


  public AusbildungDtoSpec ausbildungBegin(String ausbildungBegin) {
    
    this.ausbildungBegin = ausbildungBegin;
    return this;
  }

   /**
   * Datum im Format mm.YYYY
   * @return ausbildungBegin
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_BEGIN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getAusbildungBegin() {
    return ausbildungBegin;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_BEGIN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbildungBegin(String ausbildungBegin) {
    this.ausbildungBegin = ausbildungBegin;
  }


  public AusbildungDtoSpec ausbildungEnd(String ausbildungEnd) {
    
    this.ausbildungEnd = ausbildungEnd;
    return this;
  }

   /**
   * Datum im Format mm.YYYY
   * @return ausbildungEnd
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_END)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getAusbildungEnd() {
    return ausbildungEnd;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNG_END)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbildungEnd(String ausbildungEnd) {
    this.ausbildungEnd = ausbildungEnd;
  }


  public AusbildungDtoSpec pensum(AusbildungsPensumDtoSpec pensum) {
    
    this.pensum = pensum;
    return this;
  }

   /**
   * Get pensum
   * @return pensum
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PENSUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public AusbildungsPensumDtoSpec getPensum() {
    return pensum;
  }


  @JsonProperty(JSON_PROPERTY_PENSUM)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPensum(AusbildungsPensumDtoSpec pensum) {
    this.pensum = pensum;
  }


  public AusbildungDtoSpec alternativeAusbildungsland(String alternativeAusbildungsland) {
    
    this.alternativeAusbildungsland = alternativeAusbildungsland;
    return this;
  }

   /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   * @return alternativeAusbildungsland
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSLAND)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAlternativeAusbildungsland() {
    return alternativeAusbildungsland;
  }


  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSLAND)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAlternativeAusbildungsland(String alternativeAusbildungsland) {
    this.alternativeAusbildungsland = alternativeAusbildungsland;
  }


  public AusbildungDtoSpec alternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
    return this;
  }

   /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   * @return alternativeAusbildungsstaette
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSSTAETTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAlternativeAusbildungsstaette() {
    return alternativeAusbildungsstaette;
  }


  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSSTAETTE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setAlternativeAusbildungsstaette(String alternativeAusbildungsstaette) {
    this.alternativeAusbildungsstaette = alternativeAusbildungsstaette;
  }


  public AusbildungDtoSpec alternativeAusbildungsgang(String alternativeAusbildungsgang) {
    
    this.alternativeAusbildungsgang = alternativeAusbildungsgang;
    return this;
  }

   /**
   * Required wenn andere ausbildungNichtGefunden &#x3D; true
   * @return alternativeAusbildungsgang
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSGANG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getAlternativeAusbildungsgang() {
    return alternativeAusbildungsgang;
  }


  @JsonProperty(JSON_PROPERTY_ALTERNATIVE_AUSBILDUNGSGANG)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
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
    AusbildungDtoSpec ausbildung = (AusbildungDtoSpec) o;
    return Objects.equals(this.ausbildungsgangId, ausbildung.ausbildungsgangId) &&
        Objects.equals(this.fachrichtung, ausbildung.fachrichtung) &&
        Objects.equals(this.ausbildungNichtGefunden, ausbildung.ausbildungNichtGefunden) &&
        Objects.equals(this.ausbildungBegin, ausbildung.ausbildungBegin) &&
        Objects.equals(this.ausbildungEnd, ausbildung.ausbildungEnd) &&
        Objects.equals(this.pensum, ausbildung.pensum) &&
        Objects.equals(this.alternativeAusbildungsland, ausbildung.alternativeAusbildungsland) &&
        Objects.equals(this.alternativeAusbildungsstaette, ausbildung.alternativeAusbildungsstaette) &&
        Objects.equals(this.alternativeAusbildungsgang, ausbildung.alternativeAusbildungsgang);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildungsgangId, fachrichtung, ausbildungNichtGefunden, ausbildungBegin, ausbildungEnd, pensum, alternativeAusbildungsland, alternativeAusbildungsstaette, alternativeAusbildungsgang);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungDtoSpec {\n");
    sb.append("    ausbildungsgangId: ").append(toIndentedString(ausbildungsgangId)).append("\n");
    sb.append("    fachrichtung: ").append(toIndentedString(fachrichtung)).append("\n");
    sb.append("    ausbildungNichtGefunden: ").append(toIndentedString(ausbildungNichtGefunden)).append("\n");
    sb.append("    ausbildungBegin: ").append(toIndentedString(ausbildungBegin)).append("\n");
    sb.append("    ausbildungEnd: ").append(toIndentedString(ausbildungEnd)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
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
