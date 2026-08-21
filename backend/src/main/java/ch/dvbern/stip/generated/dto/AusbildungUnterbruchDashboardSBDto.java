package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungUnterbruchAntragSBDto;
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



@JsonTypeName("AusbildungUnterbruchDashboardSB")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungUnterbruchDashboardSBDto  implements Serializable {
  private @Valid Boolean canCreateAusbildungUnterbruch;
  private @Valid List<AusbildungUnterbruchAntragSBDto> ausbildungUnterbruchs = new ArrayList<>();

  protected AusbildungUnterbruchDashboardSBDto(AusbildungUnterbruchDashboardSBDtoBuilder<?, ?> b) {
    this.canCreateAusbildungUnterbruch = b.canCreateAusbildungUnterbruch;
    this.ausbildungUnterbruchs = b.ausbildungUnterbruchs;
  }

  public AusbildungUnterbruchDashboardSBDto() {
  }

  /**
   **/
  public AusbildungUnterbruchDashboardSBDto canCreateAusbildungUnterbruch(Boolean canCreateAusbildungUnterbruch) {
    this.canCreateAusbildungUnterbruch = canCreateAusbildungUnterbruch;
    return this;
  }

  
  @JsonProperty("canCreateAusbildungUnterbruch")
  @NotNull
  public Boolean getCanCreateAusbildungUnterbruch() {
    return canCreateAusbildungUnterbruch;
  }

  @JsonProperty("canCreateAusbildungUnterbruch")
  public void setCanCreateAusbildungUnterbruch(Boolean canCreateAusbildungUnterbruch) {
    this.canCreateAusbildungUnterbruch = canCreateAusbildungUnterbruch;
  }

  /**
   **/
  public AusbildungUnterbruchDashboardSBDto ausbildungUnterbruchs(List<AusbildungUnterbruchAntragSBDto> ausbildungUnterbruchs) {
    this.ausbildungUnterbruchs = ausbildungUnterbruchs;
    return this;
  }

  
  @JsonProperty("ausbildungUnterbruchs")
  @NotNull
  public List<AusbildungUnterbruchAntragSBDto> getAusbildungUnterbruchs() {
    return ausbildungUnterbruchs;
  }

  @JsonProperty("ausbildungUnterbruchs")
  public void setAusbildungUnterbruchs(List<AusbildungUnterbruchAntragSBDto> ausbildungUnterbruchs) {
    this.ausbildungUnterbruchs = ausbildungUnterbruchs;
  }

  public AusbildungUnterbruchDashboardSBDto addAusbildungUnterbruchsItem(AusbildungUnterbruchAntragSBDto ausbildungUnterbruchsItem) {
    if (this.ausbildungUnterbruchs == null) {
      this.ausbildungUnterbruchs = new ArrayList<>();
    }

    this.ausbildungUnterbruchs.add(ausbildungUnterbruchsItem);
    return this;
  }

  public AusbildungUnterbruchDashboardSBDto removeAusbildungUnterbruchsItem(AusbildungUnterbruchAntragSBDto ausbildungUnterbruchsItem) {
    if (ausbildungUnterbruchsItem != null && this.ausbildungUnterbruchs != null) {
      this.ausbildungUnterbruchs.remove(ausbildungUnterbruchsItem);
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
    AusbildungUnterbruchDashboardSBDto ausbildungUnterbruchDashboardSB = (AusbildungUnterbruchDashboardSBDto) o;
    return Objects.equals(this.canCreateAusbildungUnterbruch, ausbildungUnterbruchDashboardSB.canCreateAusbildungUnterbruch) &&
        Objects.equals(this.ausbildungUnterbruchs, ausbildungUnterbruchDashboardSB.ausbildungUnterbruchs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canCreateAusbildungUnterbruch, ausbildungUnterbruchs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUnterbruchDashboardSBDto {\n");
    
    sb.append("    canCreateAusbildungUnterbruch: ").append(toIndentedString(canCreateAusbildungUnterbruch)).append("\n");
    sb.append("    ausbildungUnterbruchs: ").append(toIndentedString(ausbildungUnterbruchs)).append("\n");
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


  public static AusbildungUnterbruchDashboardSBDtoBuilder<?, ?> builder() {
    return new AusbildungUnterbruchDashboardSBDtoBuilderImpl();
  }

  private static final class AusbildungUnterbruchDashboardSBDtoBuilderImpl extends AusbildungUnterbruchDashboardSBDtoBuilder<AusbildungUnterbruchDashboardSBDto, AusbildungUnterbruchDashboardSBDtoBuilderImpl> {

    @Override
    protected AusbildungUnterbruchDashboardSBDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungUnterbruchDashboardSBDto build() {
      return new AusbildungUnterbruchDashboardSBDto(this);
    }
  }

  public static abstract class AusbildungUnterbruchDashboardSBDtoBuilder<C extends AusbildungUnterbruchDashboardSBDto, B extends AusbildungUnterbruchDashboardSBDtoBuilder<C, B>>  {
    private Boolean canCreateAusbildungUnterbruch;
    private List<AusbildungUnterbruchAntragSBDto> ausbildungUnterbruchs = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B canCreateAusbildungUnterbruch(Boolean canCreateAusbildungUnterbruch) {
      this.canCreateAusbildungUnterbruch = canCreateAusbildungUnterbruch;
      return self();
    }
    public B ausbildungUnterbruchs(List<AusbildungUnterbruchAntragSBDto> ausbildungUnterbruchs) {
      this.ausbildungUnterbruchs = ausbildungUnterbruchs;
      return self();
    }
  }
}

