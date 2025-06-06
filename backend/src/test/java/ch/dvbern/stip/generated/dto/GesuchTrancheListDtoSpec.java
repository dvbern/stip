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
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GesuchTrancheListDtoSpec
 */
@JsonPropertyOrder({
  GesuchTrancheListDtoSpec.JSON_PROPERTY_TRANCHEN,
  GesuchTrancheListDtoSpec.JSON_PROPERTY_INITIAL_TRANCHEN
})
@JsonTypeName("GesuchTrancheList")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class GesuchTrancheListDtoSpec {
  public static final String JSON_PROPERTY_TRANCHEN = "tranchen";
  private List<GesuchTrancheSlimDtoSpec> tranchen;

  public static final String JSON_PROPERTY_INITIAL_TRANCHEN = "initialTranchen";
  private List<GesuchTrancheSlimDtoSpec> initialTranchen;

  public GesuchTrancheListDtoSpec() {
  }

  public GesuchTrancheListDtoSpec tranchen(List<GesuchTrancheSlimDtoSpec> tranchen) {
    
    this.tranchen = tranchen;
    return this;
  }

  public GesuchTrancheListDtoSpec addTranchenItem(GesuchTrancheSlimDtoSpec tranchenItem) {
    if (this.tranchen == null) {
      this.tranchen = new ArrayList<>();
    }
    this.tranchen.add(tranchenItem);
    return this;
  }

   /**
   * Get tranchen
   * @return tranchen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TRANCHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GesuchTrancheSlimDtoSpec> getTranchen() {
    return tranchen;
  }


  @JsonProperty(JSON_PROPERTY_TRANCHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setTranchen(List<GesuchTrancheSlimDtoSpec> tranchen) {
    this.tranchen = tranchen;
  }


  public GesuchTrancheListDtoSpec initialTranchen(List<GesuchTrancheSlimDtoSpec> initialTranchen) {
    
    this.initialTranchen = initialTranchen;
    return this;
  }

  public GesuchTrancheListDtoSpec addInitialTranchenItem(GesuchTrancheSlimDtoSpec initialTranchenItem) {
    if (this.initialTranchen == null) {
      this.initialTranchen = new ArrayList<>();
    }
    this.initialTranchen.add(initialTranchenItem);
    return this;
  }

   /**
   * Get initialTranchen
   * @return initialTranchen
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_INITIAL_TRANCHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<GesuchTrancheSlimDtoSpec> getInitialTranchen() {
    return initialTranchen;
  }


  @JsonProperty(JSON_PROPERTY_INITIAL_TRANCHEN)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setInitialTranchen(List<GesuchTrancheSlimDtoSpec> initialTranchen) {
    this.initialTranchen = initialTranchen;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheListDtoSpec gesuchTrancheList = (GesuchTrancheListDtoSpec) o;
    return Objects.equals(this.tranchen, gesuchTrancheList.tranchen) &&
        Objects.equals(this.initialTranchen, gesuchTrancheList.initialTranchen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(tranchen, initialTranchen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheListDtoSpec {\n");
    sb.append("    tranchen: ").append(toIndentedString(tranchen)).append("\n");
    sb.append("    initialTranchen: ").append(toIndentedString(initialTranchen)).append("\n");
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

