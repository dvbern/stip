package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DeploymentConfig")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DeploymentConfigDto  implements Serializable {
  private @Valid String environment;
  private @Valid String version;
  private @Valid List<String> allowedMimeTypes;

  /**
   **/
  public DeploymentConfigDto environment(String environment) {
    this.environment = environment;
    return this;
  }

  
  @JsonProperty("environment")
  public String getEnvironment() {
    return environment;
  }

  @JsonProperty("environment")
  public void setEnvironment(String environment) {
    this.environment = environment;
  }

  /**
   **/
  public DeploymentConfigDto version(String version) {
    this.version = version;
    return this;
  }

  
  @JsonProperty("version")
  public String getVersion() {
    return version;
  }

  @JsonProperty("version")
  public void setVersion(String version) {
    this.version = version;
  }

  /**
   **/
  public DeploymentConfigDto allowedMimeTypes(List<String> allowedMimeTypes) {
    this.allowedMimeTypes = allowedMimeTypes;
    return this;
  }

  
  @JsonProperty("allowedMimeTypes")
  public List<String> getAllowedMimeTypes() {
    return allowedMimeTypes;
  }

  @JsonProperty("allowedMimeTypes")
  public void setAllowedMimeTypes(List<String> allowedMimeTypes) {
    this.allowedMimeTypes = allowedMimeTypes;
  }

  public DeploymentConfigDto addAllowedMimeTypesItem(String allowedMimeTypesItem) {
    if (this.allowedMimeTypes == null) {
      this.allowedMimeTypes = new ArrayList<>();
    }

    this.allowedMimeTypes.add(allowedMimeTypesItem);
    return this;
  }

  public DeploymentConfigDto removeAllowedMimeTypesItem(String allowedMimeTypesItem) {
    if (allowedMimeTypesItem != null && this.allowedMimeTypes != null) {
      this.allowedMimeTypes.remove(allowedMimeTypesItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DeploymentConfigDto deploymentConfig = (DeploymentConfigDto) o;
    return Objects.equals(this.environment, deploymentConfig.environment) &&
        Objects.equals(this.version, deploymentConfig.version) &&
        Objects.equals(this.allowedMimeTypes, deploymentConfig.allowedMimeTypes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(environment, version, allowedMimeTypes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DeploymentConfigDto {\n");
    
    sb.append("    environment: ").append(toIndentedString(environment)).append("\n");
    sb.append("    version: ").append(toIndentedString(version)).append("\n");
    sb.append("    allowedMimeTypes: ").append(toIndentedString(allowedMimeTypes)).append("\n");
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

