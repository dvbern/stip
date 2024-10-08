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
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * AusbildungsgangUpdateDtoSpec
 */
@JsonPropertyOrder({
  AusbildungsgangUpdateDtoSpec.JSON_PROPERTY_BEZEICHNUNG_DE,
  AusbildungsgangUpdateDtoSpec.JSON_PROPERTY_BEZEICHNUNG_FR,
  AusbildungsgangUpdateDtoSpec.JSON_PROPERTY_BILDUNGSKATEGORIE_ID,
  AusbildungsgangUpdateDtoSpec.JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID
})
@JsonTypeName("AusbildungsgangUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AusbildungsgangUpdateDtoSpec {
  public static final String JSON_PROPERTY_BEZEICHNUNG_DE = "bezeichnungDe";
  private String bezeichnungDe;

  public static final String JSON_PROPERTY_BEZEICHNUNG_FR = "bezeichnungFr";
  private String bezeichnungFr;

  public static final String JSON_PROPERTY_BILDUNGSKATEGORIE_ID = "bildungskategorieId";
  private UUID bildungskategorieId;

  public static final String JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID = "ausbildungsstaetteId";
  private UUID ausbildungsstaetteId;

  public AusbildungsgangUpdateDtoSpec() {
  }

  public AusbildungsgangUpdateDtoSpec bezeichnungDe(String bezeichnungDe) {
    
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

   /**
   * Get bezeichnungDe
   * @return bezeichnungDe
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBezeichnungDe() {
    return bezeichnungDe;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_DE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }


  public AusbildungsgangUpdateDtoSpec bezeichnungFr(String bezeichnungFr) {
    
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

   /**
   * Get bezeichnungFr
   * @return bezeichnungFr
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public String getBezeichnungFr() {
    return bezeichnungFr;
  }


  @JsonProperty(JSON_PROPERTY_BEZEICHNUNG_FR)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }


  public AusbildungsgangUpdateDtoSpec bildungskategorieId(UUID bildungskategorieId) {
    
    this.bildungskategorieId = bildungskategorieId;
    return this;
  }

   /**
   * Get bildungskategorieId
   * @return bildungskategorieId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BILDUNGSKATEGORIE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getBildungskategorieId() {
    return bildungskategorieId;
  }


  @JsonProperty(JSON_PROPERTY_BILDUNGSKATEGORIE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBildungskategorieId(UUID bildungskategorieId) {
    this.bildungskategorieId = bildungskategorieId;
  }


  public AusbildungsgangUpdateDtoSpec ausbildungsstaetteId(UUID ausbildungsstaetteId) {
    
    this.ausbildungsstaetteId = ausbildungsstaetteId;
    return this;
  }

   /**
   * Get ausbildungsstaetteId
   * @return ausbildungsstaetteId
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getAusbildungsstaetteId() {
    return ausbildungsstaetteId;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setAusbildungsstaetteId(UUID ausbildungsstaetteId) {
    this.ausbildungsstaetteId = ausbildungsstaetteId;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungsgangUpdateDtoSpec ausbildungsgangUpdate = (AusbildungsgangUpdateDtoSpec) o;
    return Objects.equals(this.bezeichnungDe, ausbildungsgangUpdate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgangUpdate.bezeichnungFr) &&
        Objects.equals(this.bildungskategorieId, ausbildungsgangUpdate.bildungskategorieId) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgangUpdate.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, bildungskategorieId, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangUpdateDtoSpec {\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    bildungskategorieId: ").append(toIndentedString(bildungskategorieId)).append("\n");
    sb.append("    ausbildungsstaetteId: ").append(toIndentedString(ausbildungsstaetteId)).append("\n");
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

