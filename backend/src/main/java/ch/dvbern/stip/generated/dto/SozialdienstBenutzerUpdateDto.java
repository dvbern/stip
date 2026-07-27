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



@JsonTypeName("SozialdienstBenutzerUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstBenutzerUpdateDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String vorname;
  private @Valid String nachname;

  protected SozialdienstBenutzerUpdateDto(SozialdienstBenutzerUpdateDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.vorname = b.vorname;
    this.nachname = b.nachname;
  }

  public SozialdienstBenutzerUpdateDto() {
  }

  /**
   **/
  public SozialdienstBenutzerUpdateDto id(UUID id) {
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
  public SozialdienstBenutzerUpdateDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public SozialdienstBenutzerUpdateDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
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
    SozialdienstBenutzerUpdateDto sozialdienstBenutzerUpdate = (SozialdienstBenutzerUpdateDto) o;
    return Objects.equals(this.id, sozialdienstBenutzerUpdate.id) &&
        Objects.equals(this.vorname, sozialdienstBenutzerUpdate.vorname) &&
        Objects.equals(this.nachname, sozialdienstBenutzerUpdate.nachname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, vorname, nachname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstBenutzerUpdateDto {\n");
    
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
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }


  public static SozialdienstBenutzerUpdateDtoBuilder<?, ?> builder() {
    return new SozialdienstBenutzerUpdateDtoBuilderImpl();
  }

  private static final class SozialdienstBenutzerUpdateDtoBuilderImpl extends SozialdienstBenutzerUpdateDtoBuilder<SozialdienstBenutzerUpdateDto, SozialdienstBenutzerUpdateDtoBuilderImpl> {

    @Override
    protected SozialdienstBenutzerUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstBenutzerUpdateDto build() {
      return new SozialdienstBenutzerUpdateDto(this);
    }
  }

  public static abstract class SozialdienstBenutzerUpdateDtoBuilder<C extends SozialdienstBenutzerUpdateDto, B extends SozialdienstBenutzerUpdateDtoBuilder<C, B>>  {
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

