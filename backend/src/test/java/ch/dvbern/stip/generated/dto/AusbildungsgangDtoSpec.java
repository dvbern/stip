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
import ch.dvbern.stip.generated.dto.BildungskategorieDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * AusbildungsgangDtoSpec
 */
@JsonPropertyOrder({
  AusbildungsgangDtoSpec.JSON_PROPERTY_ID,
  AusbildungsgangDtoSpec.JSON_PROPERTY_BEZEICHNUNG_DE,
  AusbildungsgangDtoSpec.JSON_PROPERTY_BEZEICHNUNG_FR,
  AusbildungsgangDtoSpec.JSON_PROPERTY_BILDUNGSKATEGORIE,
  AusbildungsgangDtoSpec.JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID
})
@JsonTypeName("Ausbildungsgang")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class AusbildungsgangDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_BEZEICHNUNG_DE = "bezeichnungDe";
  private String bezeichnungDe;

  public static final String JSON_PROPERTY_BEZEICHNUNG_FR = "bezeichnungFr";
  private String bezeichnungFr;

  public static final String JSON_PROPERTY_BILDUNGSKATEGORIE = "bildungskategorie";
  private BildungskategorieDtoSpec bildungskategorie;

  public static final String JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID = "ausbildungsstaetteId";
  private UUID ausbildungsstaetteId;

  public AusbildungsgangDtoSpec() {
  }

  public AusbildungsgangDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setId(UUID id) {
    this.id = id;
  }


  public AusbildungsgangDtoSpec bezeichnungDe(String bezeichnungDe) {
    
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


  public AusbildungsgangDtoSpec bezeichnungFr(String bezeichnungFr) {
    
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


  public AusbildungsgangDtoSpec bildungskategorie(BildungskategorieDtoSpec bildungskategorie) {
    
    this.bildungskategorie = bildungskategorie;
    return this;
  }

   /**
   * Get bildungskategorie
   * @return bildungskategorie
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_BILDUNGSKATEGORIE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public BildungskategorieDtoSpec getBildungskategorie() {
    return bildungskategorie;
  }


  @JsonProperty(JSON_PROPERTY_BILDUNGSKATEGORIE)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setBildungskategorie(BildungskategorieDtoSpec bildungskategorie) {
    this.bildungskategorie = bildungskategorie;
  }


  public AusbildungsgangDtoSpec ausbildungsstaetteId(UUID ausbildungsstaetteId) {
    
    this.ausbildungsstaetteId = ausbildungsstaetteId;
    return this;
  }

   /**
   * Get ausbildungsstaetteId
   * @return ausbildungsstaetteId
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UUID getAusbildungsstaetteId() {
    return ausbildungsstaetteId;
  }


  @JsonProperty(JSON_PROPERTY_AUSBILDUNGSSTAETTE_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
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
    AusbildungsgangDtoSpec ausbildungsgang = (AusbildungsgangDtoSpec) o;
    return Objects.equals(this.id, ausbildungsgang.id) &&
        Objects.equals(this.bezeichnungDe, ausbildungsgang.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, ausbildungsgang.bezeichnungFr) &&
        Objects.equals(this.bildungskategorie, ausbildungsgang.bildungskategorie) &&
        Objects.equals(this.ausbildungsstaetteId, ausbildungsgang.ausbildungsstaetteId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, bezeichnungDe, bezeichnungFr, bildungskategorie, ausbildungsstaetteId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungsgangDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    bildungskategorie: ").append(toIndentedString(bildungskategorie)).append("\n");
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

