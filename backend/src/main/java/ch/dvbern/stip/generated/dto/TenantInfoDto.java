package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.TenantAuthConfigDto;
import ch.dvbern.stip.generated.dto.TenantFeaturesDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("TenantInfo")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class TenantInfoDto  implements Serializable {
  private @Valid TenantAuthConfigDto clientAuth;
  private @Valid TenantFeaturesDto features;
  private @Valid String identifier;

  protected TenantInfoDto(TenantInfoDtoBuilder<?, ?> b) {
    this.clientAuth = b.clientAuth;
    this.features = b.features;
    this.identifier = b.identifier;
  }

  public TenantInfoDto() {
  }

  /**
   **/
  public TenantInfoDto clientAuth(TenantAuthConfigDto clientAuth) {
    this.clientAuth = clientAuth;
    return this;
  }

  
  @JsonProperty("clientAuth")
  @NotNull
  public TenantAuthConfigDto getClientAuth() {
    return clientAuth;
  }

  @JsonProperty("clientAuth")
  public void setClientAuth(TenantAuthConfigDto clientAuth) {
    this.clientAuth = clientAuth;
  }

  /**
   **/
  public TenantInfoDto features(TenantFeaturesDto features) {
    this.features = features;
    return this;
  }

  
  @JsonProperty("features")
  @NotNull
  public TenantFeaturesDto getFeatures() {
    return features;
  }

  @JsonProperty("features")
  public void setFeatures(TenantFeaturesDto features) {
    this.features = features;
  }

  /**
   **/
  public TenantInfoDto identifier(String identifier) {
    this.identifier = identifier;
    return this;
  }

  
  @JsonProperty("identifier")
  @NotNull
  public String getIdentifier() {
    return identifier;
  }

  @JsonProperty("identifier")
  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantInfoDto tenantInfo = (TenantInfoDto) o;
    return Objects.equals(this.clientAuth, tenantInfo.clientAuth) &&
        Objects.equals(this.features, tenantInfo.features) &&
        Objects.equals(this.identifier, tenantInfo.identifier);
  }

  @Override
  public int hashCode() {
    return Objects.hash(clientAuth, features, identifier);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantInfoDto {\n");
    
    sb.append("    clientAuth: ").append(toIndentedString(clientAuth)).append("\n");
    sb.append("    features: ").append(toIndentedString(features)).append("\n");
    sb.append("    identifier: ").append(toIndentedString(identifier)).append("\n");
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


  public static TenantInfoDtoBuilder<?, ?> builder() {
    return new TenantInfoDtoBuilderImpl();
  }

  private static final class TenantInfoDtoBuilderImpl extends TenantInfoDtoBuilder<TenantInfoDto, TenantInfoDtoBuilderImpl> {

    @Override
    protected TenantInfoDtoBuilderImpl self() {
      return this;
    }

    @Override
    public TenantInfoDto build() {
      return new TenantInfoDto(this);
    }
  }

  public static abstract class TenantInfoDtoBuilder<C extends TenantInfoDto, B extends TenantInfoDtoBuilder<C, B>>  {
    private TenantAuthConfigDto clientAuth;
    private TenantFeaturesDto features;
    private String identifier;
    protected abstract B self();

    public abstract C build();

    public B clientAuth(TenantAuthConfigDto clientAuth) {
      this.clientAuth = clientAuth;
      return self();
    }
    public B features(TenantFeaturesDto features) {
      this.features = features;
      return self();
    }
    public B identifier(String identifier) {
      this.identifier = identifier;
      return self();
    }
  }
}

