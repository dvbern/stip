package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SozialdienstSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DelegierungSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DelegierungSlimDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
  private @Valid SozialdienstSlimDto sozialdienst;

  protected DelegierungSlimDto(DelegierungSlimDtoBuilder<?, ?> b) {
    this.status = b.status;
    this.sozialdienst = b.sozialdienst;
  }

  public DelegierungSlimDto() {
  }

  /**
   **/
  public DelegierungSlimDto status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.delegieren.type.DelegierungStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
    this.status = status;
  }

  /**
   **/
  public DelegierungSlimDto sozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
    return this;
  }

  
  @JsonProperty("sozialdienst")
  @NotNull
  public SozialdienstSlimDto getSozialdienst() {
    return sozialdienst;
  }

  @JsonProperty("sozialdienst")
  public void setSozialdienst(SozialdienstSlimDto sozialdienst) {
    this.sozialdienst = sozialdienst;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungSlimDto delegierungSlim = (DelegierungSlimDto) o;
    return Objects.equals(this.status, delegierungSlim.status) &&
        Objects.equals(this.sozialdienst, delegierungSlim.sozialdienst);
  }

  @Override
  public int hashCode() {
    return Objects.hash(status, sozialdienst);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungSlimDto {\n");
    
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    sozialdienst: ").append(toIndentedString(sozialdienst)).append("\n");
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


  public static DelegierungSlimDtoBuilder<?, ?> builder() {
    return new DelegierungSlimDtoBuilderImpl();
  }

  private static final class DelegierungSlimDtoBuilderImpl extends DelegierungSlimDtoBuilder<DelegierungSlimDto, DelegierungSlimDtoBuilderImpl> {

    @Override
    protected DelegierungSlimDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DelegierungSlimDto build() {
      return new DelegierungSlimDto(this);
    }
  }

  public static abstract class DelegierungSlimDtoBuilder<C extends DelegierungSlimDto, B extends DelegierungSlimDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.delegieren.type.DelegierungStatus status;
    private SozialdienstSlimDto sozialdienst;
    protected abstract B self();

    public abstract C build();

    public B status(ch.dvbern.stip.api.delegieren.type.DelegierungStatus status) {
      this.status = status;
      return self();
    }
    public B sozialdienst(SozialdienstSlimDto sozialdienst) {
      this.sozialdienst = sozialdienst;
      return self();
    }
  }
}

