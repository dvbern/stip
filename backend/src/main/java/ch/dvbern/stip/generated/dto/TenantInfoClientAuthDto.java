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



@JsonTypeName("TenantInfo_clientAuth")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class TenantInfoClientAuthDto  implements Serializable {
  private @Valid String authServerUrl;
  private @Valid String clientId;

  /**
   **/
  public TenantInfoClientAuthDto authServerUrl(String authServerUrl) {
    this.authServerUrl = authServerUrl;
    return this;
  }

  
  @JsonProperty("authServerUrl")
  public String getAuthServerUrl() {
    return authServerUrl;
  }

  @JsonProperty("authServerUrl")
  public void setAuthServerUrl(String authServerUrl) {
    this.authServerUrl = authServerUrl;
  }

  /**
   **/
  public TenantInfoClientAuthDto clientId(String clientId) {
    this.clientId = clientId;
    return this;
  }

  
  @JsonProperty("clientId")
  public String getClientId() {
    return clientId;
  }

  @JsonProperty("clientId")
  public void setClientId(String clientId) {
    this.clientId = clientId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantInfoClientAuthDto tenantInfoClientAuth = (TenantInfoClientAuthDto) o;
    return Objects.equals(this.authServerUrl, tenantInfoClientAuth.authServerUrl) &&
        Objects.equals(this.clientId, tenantInfoClientAuth.clientId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(authServerUrl, clientId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantInfoClientAuthDto {\n");
    
    sb.append("    authServerUrl: ").append(toIndentedString(authServerUrl)).append("\n");
    sb.append("    clientId: ").append(toIndentedString(clientId)).append("\n");
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

