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
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * PLZ und ort für Type ahead funktionalitaet
 */
@JsonPropertyOrder({
  PlzDtoSpec.JSON_PROPERTY_PLZ,
  PlzDtoSpec.JSON_PROPERTY_ORT,
  PlzDtoSpec.JSON_PROPERTY_KANTONSKUERZEL
})
@JsonTypeName("plz")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class PlzDtoSpec {
  public static final String JSON_PROPERTY_PLZ = "plz";
  private String plz;

  public static final String JSON_PROPERTY_ORT = "ort";
  private String ort;

  public static final String JSON_PROPERTY_KANTONSKUERZEL = "kantonskuerzel";
  private String kantonskuerzel;

  public PlzDtoSpec() {
  }

  public PlzDtoSpec plz(String plz) {
    
    this.plz = plz;
    return this;
  }

   /**
   * 
   * @return plz
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_PLZ)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getPlz() {
    return plz;
  }


  @JsonProperty(JSON_PROPERTY_PLZ)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setPlz(String plz) {
    this.plz = plz;
  }


  public PlzDtoSpec ort(String ort) {
    
    this.ort = ort;
    return this;
  }

   /**
   * 
   * @return ort
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ORT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getOrt() {
    return ort;
  }


  @JsonProperty(JSON_PROPERTY_ORT)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setOrt(String ort) {
    this.ort = ort;
  }


  public PlzDtoSpec kantonskuerzel(String kantonskuerzel) {
    
    this.kantonskuerzel = kantonskuerzel;
    return this;
  }

   /**
   * 
   * @return kantonskuerzel
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_KANTONSKUERZEL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getKantonskuerzel() {
    return kantonskuerzel;
  }


  @JsonProperty(JSON_PROPERTY_KANTONSKUERZEL)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setKantonskuerzel(String kantonskuerzel) {
    this.kantonskuerzel = kantonskuerzel;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlzDtoSpec plz = (PlzDtoSpec) o;
    return Objects.equals(this.plz, plz.plz) &&
        Objects.equals(this.ort, plz.ort) &&
        Objects.equals(this.kantonskuerzel, plz.kantonskuerzel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plz, ort, kantonskuerzel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlzDtoSpec {\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    kantonskuerzel: ").append(toIndentedString(kantonskuerzel)).append("\n");
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
