package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("TenantFeature")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class TenantFeatureDto  implements Serializable {
  private @Valid Boolean enabled;
  private @Valid ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType adapterType;

  /**
   **/
  public TenantFeatureDto enabled(Boolean enabled) {
    this.enabled = enabled;
    return this;
  }

  
  @JsonProperty("enabled")
  @NotNull
  public Boolean getEnabled() {
    return enabled;
  }

  @JsonProperty("enabled")
  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  /**
   **/
  public TenantFeatureDto adapterType(ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType adapterType) {
    this.adapterType = adapterType;
    return this;
  }

  
  @JsonProperty("adapterType")
  public ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType getAdapterType() {
    return adapterType;
  }

  @JsonProperty("adapterType")
  public void setAdapterType(ch.dvbern.stip.integration.steuerdaten.domain.model.SteuerdatenAdapterType adapterType) {
    this.adapterType = adapterType;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantFeatureDto tenantFeature = (TenantFeatureDto) o;
    return Objects.equals(this.enabled, tenantFeature.enabled) &&
        Objects.equals(this.adapterType, tenantFeature.adapterType);
  }

  @Override
  public int hashCode() {
    return Objects.hash(enabled, adapterType);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantFeatureDto {\n");
    
    sb.append("    enabled: ").append(toIndentedString(enabled)).append("\n");
    sb.append("    adapterType: ").append(toIndentedString(adapterType)).append("\n");
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

