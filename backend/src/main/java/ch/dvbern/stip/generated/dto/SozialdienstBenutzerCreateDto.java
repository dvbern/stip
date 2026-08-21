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



@JsonTypeName("SozialdienstBenutzerCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstBenutzerCreateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String email;

  protected SozialdienstBenutzerCreateDto(SozialdienstBenutzerCreateDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.email = b.email;
  }

  public SozialdienstBenutzerCreateDto() {
  }

  /**
   **/
  public SozialdienstBenutzerCreateDto vorname(String vorname) {
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
  public SozialdienstBenutzerCreateDto nachname(String nachname) {
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

  /**
   **/
  public SozialdienstBenutzerCreateDto email(String email) {
    this.email = email;
    return this;
  }

  
  @JsonProperty("email")
  @NotNull
  public String getEmail() {
    return email;
  }

  @JsonProperty("email")
  public void setEmail(String email) {
    this.email = email;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstBenutzerCreateDto sozialdienstBenutzerCreate = (SozialdienstBenutzerCreateDto) o;
    return Objects.equals(this.vorname, sozialdienstBenutzerCreate.vorname) &&
        Objects.equals(this.nachname, sozialdienstBenutzerCreate.nachname) &&
        Objects.equals(this.email, sozialdienstBenutzerCreate.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstBenutzerCreateDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
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


  public static SozialdienstBenutzerCreateDtoBuilder<?, ?> builder() {
    return new SozialdienstBenutzerCreateDtoBuilderImpl();
  }

  private static final class SozialdienstBenutzerCreateDtoBuilderImpl extends SozialdienstBenutzerCreateDtoBuilder<SozialdienstBenutzerCreateDto, SozialdienstBenutzerCreateDtoBuilderImpl> {

    @Override
    protected SozialdienstBenutzerCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstBenutzerCreateDto build() {
      return new SozialdienstBenutzerCreateDto(this);
    }
  }

  public static abstract class SozialdienstBenutzerCreateDtoBuilder<C extends SozialdienstBenutzerCreateDto, B extends SozialdienstBenutzerCreateDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
    private String email;
    protected abstract B self();

    public abstract C build();

    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B email(String email) {
      this.email = email;
      return self();
    }
  }
}

