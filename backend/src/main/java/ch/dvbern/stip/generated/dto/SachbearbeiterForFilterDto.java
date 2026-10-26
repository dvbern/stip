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



@JsonTypeName("SachbearbeiterForFilter")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SachbearbeiterForFilterDto  implements Serializable {
  private UUID id;
  private String vorname;
  private String nachname;

  protected SachbearbeiterForFilterDto(SachbearbeiterForFilterDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.vorname = b.vorname;
    this.nachname = b.nachname;
  }

  public SachbearbeiterForFilterDto() {
  }

  /**
   **/
  public SachbearbeiterForFilterDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty(required = true, value = "id")
  @NotNull public UUID getId() {
    return id;
  }

  @JsonProperty(required = true, value = "id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public SachbearbeiterForFilterDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty(required = true, value = "vorname")
  @NotNull public String getVorname() {
    return vorname;
  }

  @JsonProperty(required = true, value = "vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public SachbearbeiterForFilterDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty(required = true, value = "nachname")
  @NotNull public String getNachname() {
    return nachname;
  }

  @JsonProperty(required = true, value = "nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterForFilterDto sachbearbeiterForFilter = (SachbearbeiterForFilterDto) o;
    return Objects.equals(this.id, sachbearbeiterForFilter.id) &&
        Objects.equals(this.vorname, sachbearbeiterForFilter.vorname) &&
        Objects.equals(this.nachname, sachbearbeiterForFilter.nachname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vorname, nachname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterForFilterDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
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


  public static SachbearbeiterForFilterDtoBuilder<?, ?> builder() {
    return new SachbearbeiterForFilterDtoBuilderImpl();
  }

  private static final class SachbearbeiterForFilterDtoBuilderImpl extends SachbearbeiterForFilterDtoBuilder<SachbearbeiterForFilterDto, SachbearbeiterForFilterDtoBuilderImpl> {

    @Override
    protected SachbearbeiterForFilterDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SachbearbeiterForFilterDto build() {
      return new SachbearbeiterForFilterDto(this);
    }
  }

  public static abstract class SachbearbeiterForFilterDtoBuilder<C extends SachbearbeiterForFilterDto, B extends SachbearbeiterForFilterDtoBuilder<C, B>>  {
    private UUID id;
    private String vorname;
    private String nachname;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
  }
}
