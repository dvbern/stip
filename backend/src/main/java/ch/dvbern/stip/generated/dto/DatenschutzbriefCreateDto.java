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



@JsonTypeName("DatenschutzbriefCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DatenschutzbriefCreateDto  implements Serializable {
  private @Valid UUID elternId;

  protected DatenschutzbriefCreateDto(DatenschutzbriefCreateDtoBuilder<?, ?> b) {
    this.elternId = b.elternId;
  }

  public DatenschutzbriefCreateDto() {
  }

  /**
   **/
  public DatenschutzbriefCreateDto elternId(UUID elternId) {
    this.elternId = elternId;
    return this;
  }

  
  @JsonProperty("elternId")
  @NotNull
  public UUID getElternId() {
    return elternId;
  }

  @JsonProperty("elternId")
  public void setElternId(UUID elternId) {
    this.elternId = elternId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatenschutzbriefCreateDto datenschutzbriefCreate = (DatenschutzbriefCreateDto) o;
    return Objects.equals(this.elternId, datenschutzbriefCreate.elternId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(elternId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatenschutzbriefCreateDto {\n");
    
    sb.append("    elternId: ").append(toIndentedString(elternId)).append("\n");
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


  public static DatenschutzbriefCreateDtoBuilder<?, ?> builder() {
    return new DatenschutzbriefCreateDtoBuilderImpl();
  }

  private static final class DatenschutzbriefCreateDtoBuilderImpl extends DatenschutzbriefCreateDtoBuilder<DatenschutzbriefCreateDto, DatenschutzbriefCreateDtoBuilderImpl> {

    @Override
    protected DatenschutzbriefCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DatenschutzbriefCreateDto build() {
      return new DatenschutzbriefCreateDto(this);
    }
  }

  public static abstract class DatenschutzbriefCreateDtoBuilder<C extends DatenschutzbriefCreateDto, B extends DatenschutzbriefCreateDtoBuilder<C, B>>  {
    private UUID elternId;
    protected abstract B self();

    public abstract C build();

    public B elternId(UUID elternId) {
      this.elternId = elternId;
      return self();
    }
  }
}

