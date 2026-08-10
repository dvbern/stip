package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.TenantFeatureDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("TenantFeatures")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class TenantFeaturesDto  implements Serializable {
  private @Valid TenantFeatureDto steuerdaten;

  protected TenantFeaturesDto(TenantFeaturesDtoBuilder<?, ?> b) {
    this.steuerdaten = b.steuerdaten;
  }

  public TenantFeaturesDto() {
  }

  /**
   **/
  public TenantFeaturesDto steuerdaten(TenantFeatureDto steuerdaten) {
    this.steuerdaten = steuerdaten;
    return this;
  }

  
  @JsonProperty("steuerdaten")
  public TenantFeatureDto getSteuerdaten() {
    return steuerdaten;
  }

  @JsonProperty("steuerdaten")
  public void setSteuerdaten(TenantFeatureDto steuerdaten) {
    this.steuerdaten = steuerdaten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    TenantFeaturesDto tenantFeatures = (TenantFeaturesDto) o;
    return Objects.equals(this.steuerdaten, tenantFeatures.steuerdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(steuerdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class TenantFeaturesDto {\n");
    
    sb.append("    steuerdaten: ").append(toIndentedString(steuerdaten)).append("\n");
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


  public static TenantFeaturesDtoBuilder<?, ?> builder() {
    return new TenantFeaturesDtoBuilderImpl();
  }

  private static final class TenantFeaturesDtoBuilderImpl extends TenantFeaturesDtoBuilder<TenantFeaturesDto, TenantFeaturesDtoBuilderImpl> {

    @Override
    protected TenantFeaturesDtoBuilderImpl self() {
      return this;
    }

    @Override
    public TenantFeaturesDto build() {
      return new TenantFeaturesDto(this);
    }
  }

  public static abstract class TenantFeaturesDtoBuilder<C extends TenantFeaturesDto, B extends TenantFeaturesDtoBuilder<C, B>>  {
    private TenantFeatureDto steuerdaten;
    protected abstract B self();

    public abstract C build();

    public B steuerdaten(TenantFeatureDto steuerdaten) {
      this.steuerdaten = steuerdaten;
      return self();
    }
  }
}

