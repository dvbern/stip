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
import ch.dvbern.stip.generated.dto.SteuerdatenTypDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * SteuererklaerungUpdateDtoSpec
 */
@JsonPropertyOrder({
  SteuererklaerungUpdateDtoSpec.JSON_PROPERTY_ID,
  SteuererklaerungUpdateDtoSpec.JSON_PROPERTY_STEUERDATEN_TYP,
  SteuererklaerungUpdateDtoSpec.JSON_PROPERTY_STEUERERKLAERUNG_IN_BERN
})
@JsonTypeName("SteuererklaerungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class SteuererklaerungUpdateDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_STEUERDATEN_TYP = "steuerdatenTyp";
  private SteuerdatenTypDtoSpec steuerdatenTyp;

  public static final String JSON_PROPERTY_STEUERERKLAERUNG_IN_BERN = "steuererklaerungInBern";
  private Boolean steuererklaerungInBern;

  public SteuererklaerungUpdateDtoSpec() {
  }

  public SteuererklaerungUpdateDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setId(UUID id) {
    this.id = id;
  }


  public SteuererklaerungUpdateDtoSpec steuerdatenTyp(SteuerdatenTypDtoSpec steuerdatenTyp) {
    
    this.steuerdatenTyp = steuerdatenTyp;
    return this;
  }

   /**
   * Get steuerdatenTyp
   * @return steuerdatenTyp
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERDATEN_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public SteuerdatenTypDtoSpec getSteuerdatenTyp() {
    return steuerdatenTyp;
  }


  @JsonProperty(JSON_PROPERTY_STEUERDATEN_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuerdatenTyp(SteuerdatenTypDtoSpec steuerdatenTyp) {
    this.steuerdatenTyp = steuerdatenTyp;
  }


  public SteuererklaerungUpdateDtoSpec steuererklaerungInBern(Boolean steuererklaerungInBern) {
    
    this.steuererklaerungInBern = steuererklaerungInBern;
    return this;
  }

   /**
   * Get steuererklaerungInBern
   * @return steuererklaerungInBern
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STEUERERKLAERUNG_IN_BERN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public Boolean getSteuererklaerungInBern() {
    return steuererklaerungInBern;
  }


  @JsonProperty(JSON_PROPERTY_STEUERERKLAERUNG_IN_BERN)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setSteuererklaerungInBern(Boolean steuererklaerungInBern) {
    this.steuererklaerungInBern = steuererklaerungInBern;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SteuererklaerungUpdateDtoSpec steuererklaerungUpdate = (SteuererklaerungUpdateDtoSpec) o;
    return Objects.equals(this.id, steuererklaerungUpdate.id) &&
        Objects.equals(this.steuerdatenTyp, steuererklaerungUpdate.steuerdatenTyp) &&
        Objects.equals(this.steuererklaerungInBern, steuererklaerungUpdate.steuererklaerungInBern);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, steuerdatenTyp, steuererklaerungInBern);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SteuererklaerungUpdateDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    steuerdatenTyp: ").append(toIndentedString(steuerdatenTyp)).append("\n");
    sb.append("    steuererklaerungInBern: ").append(toIndentedString(steuererklaerungInBern)).append("\n");
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

