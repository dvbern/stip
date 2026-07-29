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



@JsonTypeName("SozialdienstAdmin")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class SozialdienstAdminDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String email;

  protected SozialdienstAdminDto(SozialdienstAdminDtoBuilder<?, ?> b) {
    this.vorname = b.vorname;
    this.nachname = b.nachname;
    this.email = b.email;
  }

  public SozialdienstAdminDto() {
  }

  /**
   **/
  public SozialdienstAdminDto vorname(String vorname) {
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
  public SozialdienstAdminDto nachname(String nachname) {
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
  public SozialdienstAdminDto email(String email) {
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
    SozialdienstAdminDto sozialdienstAdmin = (SozialdienstAdminDto) o;
    return Objects.equals(this.vorname, sozialdienstAdmin.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdmin.nachname) &&
        Objects.equals(this.email, sozialdienstAdmin.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminDto {\n");
    
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


  public static SozialdienstAdminDtoBuilder<?, ?> builder() {
    return new SozialdienstAdminDtoBuilderImpl();
  }

  private static final class SozialdienstAdminDtoBuilderImpl extends SozialdienstAdminDtoBuilder<SozialdienstAdminDto, SozialdienstAdminDtoBuilderImpl> {

    @Override
    protected SozialdienstAdminDtoBuilderImpl self() {
      return this;
    }

    @Override
    public SozialdienstAdminDto build() {
      return new SozialdienstAdminDto(this);
    }
  }

  public static abstract class SozialdienstAdminDtoBuilder<C extends SozialdienstAdminDto, B extends SozialdienstAdminDtoBuilder<C, B>>  {
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

