package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheUpdateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchUpdateDto  implements Serializable {
  private GesuchTrancheUpdateDto gesuchTrancheToWorkWith;
  private String nachfristDokumente;

  protected GesuchUpdateDto(GesuchUpdateDtoBuilder<?, ?> b) {
    this.gesuchTrancheToWorkWith = b.gesuchTrancheToWorkWith;
    this.nachfristDokumente = b.nachfristDokumente;
  }

  public GesuchUpdateDto() {
  }

  /**
   **/
  public GesuchUpdateDto gesuchTrancheToWorkWith(GesuchTrancheUpdateDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchTrancheToWorkWith")
  @NotNull @Valid public GesuchTrancheUpdateDto getGesuchTrancheToWorkWith() {
    return gesuchTrancheToWorkWith;
  }

  @JsonProperty(required = true, value = "gesuchTrancheToWorkWith")
  public void setGesuchTrancheToWorkWith(GesuchTrancheUpdateDto gesuchTrancheToWorkWith) {
    this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
  }

  /**
   **/
  public GesuchUpdateDto nachfristDokumente(String nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
    return this;
  }

  
  @JsonProperty("nachfristDokumente")
  public String getNachfristDokumente() {
    return nachfristDokumente;
  }

  @JsonProperty("nachfristDokumente")
  public void setNachfristDokumente(String nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchUpdateDto gesuchUpdate = (GesuchUpdateDto) o;
    return Objects.equals(this.gesuchTrancheToWorkWith, gesuchUpdate.gesuchTrancheToWorkWith) &&
        Objects.equals(this.nachfristDokumente, gesuchUpdate.nachfristDokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchTrancheToWorkWith, nachfristDokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchUpdateDto {\n");
    
    sb.append("    gesuchTrancheToWorkWith: ").append(toIndentedString(gesuchTrancheToWorkWith)).append("\n");
    sb.append("    nachfristDokumente: ").append(toIndentedString(nachfristDokumente)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static GesuchUpdateDtoBuilder<?, ?> builder() {
    return new GesuchUpdateDtoBuilderImpl();
  }

  private static final class GesuchUpdateDtoBuilderImpl extends GesuchUpdateDtoBuilder<GesuchUpdateDto, GesuchUpdateDtoBuilderImpl> {

    @Override
    protected GesuchUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchUpdateDto build() {
      return new GesuchUpdateDto(this);
    }
  }

  public static abstract class GesuchUpdateDtoBuilder<C extends GesuchUpdateDto, B extends GesuchUpdateDtoBuilder<C, B>>  {
    private GesuchTrancheUpdateDto gesuchTrancheToWorkWith;
    private String nachfristDokumente;
    protected abstract B self();

    public abstract C build();

    public B gesuchTrancheToWorkWith(GesuchTrancheUpdateDto gesuchTrancheToWorkWith) {
      this.gesuchTrancheToWorkWith = gesuchTrancheToWorkWith;
      return self();
    }
    public B nachfristDokumente(String nachfristDokumente) {
      this.nachfristDokumente = nachfristDokumente;
      return self();
    }
  }
}
