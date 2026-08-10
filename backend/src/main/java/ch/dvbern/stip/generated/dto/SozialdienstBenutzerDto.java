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



@JsonTypeName("SozialdienstBenutzer")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstBenutzerDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String email;
  private @Valid UUID id;
  private @Valid Boolean isAdmin;

  protected SozialdienstBenutzerDto(SozialdienstBenutzerDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.email = b.email;
    this.id = b.id;
    this.isAdmin = b.isAdmin;
  }

  public SozialdienstBenutzerDto() {
  }

  /**
   **/
  public SozialdienstBenutzerDto vorname(String vorname) {
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
  public SozialdienstBenutzerDto nachname(String nachname) {
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
  public SozialdienstBenutzerDto email(String email) {
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

  /**
   **/
  public SozialdienstBenutzerDto id(UUID id) {
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
  public SozialdienstBenutzerDto isAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
    return this;
  }

  
  @JsonProperty("isAdmin")
  public Boolean getIsAdmin() {
    return isAdmin;
  }

  @JsonProperty("isAdmin")
  public void setIsAdmin(Boolean isAdmin) {
    this.isAdmin = isAdmin;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstBenutzerDto sozialdienstBenutzer = (SozialdienstBenutzerDto) o;
    return Objects.equals(this.vorname, sozialdienstBenutzer.vorname) &&
        Objects.equals(this.nachname, sozialdienstBenutzer.nachname) &&
        Objects.equals(this.email, sozialdienstBenutzer.email) &&
        Objects.equals(this.id, sozialdienstBenutzer.id) &&
        Objects.equals(this.isAdmin, sozialdienstBenutzer.isAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, email, id, isAdmin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstBenutzerDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isAdmin: ").append(toIndentedString(isAdmin)).append("\n");
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


  public static SozialdienstBenutzerDtoBuilder<?, ?> builder() {
    return new SozialdienstBenutzerDtoBuilderImpl();
  }

  private static final class SozialdienstBenutzerDtoBuilderImpl extends SozialdienstBenutzerDtoBuilder<SozialdienstBenutzerDto, SozialdienstBenutzerDtoBuilderImpl> {

    @Override
    protected SozialdienstBenutzerDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstBenutzerDto build() {
      return new SozialdienstBenutzerDto(this);
    }
  }

  public static abstract class SozialdienstBenutzerDtoBuilder<C extends SozialdienstBenutzerDto, B extends SozialdienstBenutzerDtoBuilder<C, B>>  {
    private String vorname;
    private String nachname;
    private String email;
    private UUID id;
    private Boolean isAdmin;
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
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B isAdmin(Boolean isAdmin) {
      this.isAdmin = isAdmin;
      return self();
    }
  }
}

