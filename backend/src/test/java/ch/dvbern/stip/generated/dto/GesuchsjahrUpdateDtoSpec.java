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
 * GesuchsjahrUpdateDtoSpec
 */
@JsonPropertyOrder({
  GesuchsjahrUpdateDtoSpec.JSON_PROPERTY_BEZEICHNUNG_DE,
  GesuchsjahrUpdateDtoSpec.JSON_PROPERTY_BEZEICHNUNG_FR,
  GesuchsjahrUpdateDtoSpec.JSON_PROPERTY_TECHNISCHES_JAHR
})
@JsonTypeName("GesuchsjahrUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class GesuchsjahrUpdateDtoSpec {
  public static final String JSON_PROPERTY_BEZEICHNUNG_DE = "bezeichnungDe";
  private String bezeichnungDe;

  public static final String JSON_PROPERTY_BEZEICHNUNG_FR = "bezeichnungFr";
  private String bezeichnungFr;

  public static final String JSON_PROPERTY_TECHNISCHES_JAHR = "technischesJahr";
  private Integer technischesJahr;

  public GesuchsjahrUpdateDtoSpec() {
  }

  public GesuchsjahrUpdateDtoSpec bezeichnungDe(String bezeichnungDe) {
    
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

   /**
   * Get bezeichnungDe
   * @return bezeichnungDe
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getBezeichnungDe() {
    return bezeichnungDe;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }


  public GesuchsjahrUpdateDtoSpec bezeichnungFr(String bezeichnungFr) {
    
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

   /**
   * Get bezeichnungFr
   * @return bezeichnungFr
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getBezeichnungFr() {
    return bezeichnungFr;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }


  public GesuchsjahrUpdateDtoSpec technischesJahr(Integer technischesJahr) {
    
    this.technischesJahr = technischesJahr;
    return this;
  }

   /**
   * Get technischesJahr
   * @return technischesJahr
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TECHNISCHES_JAHR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getTechnischesJahr() {
    return technischesJahr;
  }


  @JsonProperty(JSON_PROPERTY_TECHNISCHES_JAHR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTechnischesJahr(Integer technischesJahr) {
    this.technischesJahr = technischesJahr;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchsjahrUpdateDtoSpec gesuchsjahrUpdate = (GesuchsjahrUpdateDtoSpec) o;
    return Objects.equals(this.bezeichnungDe, gesuchsjahrUpdate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, gesuchsjahrUpdate.bezeichnungFr) &&
        Objects.equals(this.technischesJahr, gesuchsjahrUpdate.technischesJahr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, technischesJahr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchsjahrUpdateDtoSpec {\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    technischesJahr: ").append(toIndentedString(technischesJahr)).append("\n");
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
