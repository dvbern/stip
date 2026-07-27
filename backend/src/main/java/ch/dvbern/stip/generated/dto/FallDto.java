package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Fall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FallDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid String tenant;

  protected FallDto(FallDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.fallNummer = b.fallNummer;
    this.tenant = b.tenant;
  }

  public FallDto() {
  }

  /**
   **/
  public FallDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public FallDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  @NotNull
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public FallDto tenant(String tenant) {
    this.tenant = tenant;
    return this;
  }

  
  @JsonProperty("tenant")
  @NotNull
  public String getTenant() {
    return tenant;
  }

  @JsonProperty("tenant")
  public void setTenant(String tenant) {
    this.tenant = tenant;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallDto fall = (FallDto) o;
    return Objects.equals(this.id, fall.id) &&
        Objects.equals(this.fallNummer, fall.fallNummer) &&
        Objects.equals(this.tenant, fall.tenant);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, tenant);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    tenant: ").append(toIndentedString(tenant)).append("\n");
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


  public static FallDtoBuilder<?, ?> builder() {
    return new FallDtoBuilderImpl();
  }

  private static final class FallDtoBuilderImpl extends FallDtoBuilder<FallDto, FallDtoBuilderImpl> {

    @Override
    protected FallDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FallDto build() {
      return new FallDto(this);
    }
  }

  public static abstract class FallDtoBuilder<C extends FallDto, B extends FallDtoBuilder<C, B>>  {
    private UUID id;
    private String fallNummer;
    private String tenant;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B tenant(String tenant) {
      this.tenant = tenant;
      return self();
    }
  }
}

