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



@JsonTypeName("TenantAuthConfig")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class TenantAuthConfigDto  implements Serializable {
  private @Valid String authServerUrl;
  private @Valid String realm;

  /**
   **/
  public TenantAuthConfigDto authServerUrl(String authServerUrl) {
    this.authServerUrl = authServerUrl;
    return this;
  }

  
  @JsonProperty("authServerUrl")
  @NotNull
  public String getAuthServerUrl() {
    return authServerUrl;
  }

  @JsonProperty("authServerUrl")
  public void setAuthServerUrl(String authServerUrl) {
    this.authServerUrl = authServerUrl;
  }

  /**
   **/
  public TenantAuthConfigDto realm(String realm) {
    this.realm = realm;
    return this;
  }

  
  @JsonProperty("realm")
  @NotNull
  public String getRealm() {
    return realm;
  }

  @JsonProperty("realm")
  public void setRealm(String realm) {
    this.realm = realm;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantAuthConfigDto tenantAuthConfig = (TenantAuthConfigDto) o;
    return Objects.equals(this.authServerUrl, tenantAuthConfig.authServerUrl) &&
        Objects.equals(this.realm, tenantAuthConfig.realm);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authServerUrl, realm);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantAuthConfigDto {\n");
    
    sb.append("    authServerUrl: ").append(toIndentedString(authServerUrl)).append("\n");
    sb.append("    realm: ").append(toIndentedString(realm)).append("\n");
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

