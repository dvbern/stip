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



@JsonTypeName("WelcomeMail")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class WelcomeMailDto  implements Serializable {
  private @Valid String name;
  private @Valid String vorname;
  private @Valid String email;
  private @Valid String redirectUri;

  protected WelcomeMailDto(WelcomeMailDtoBuilder<?, ?> b) {
    this.name = b.name;
    this.vorname = b.vorname;
    this.email = b.email;
    this.redirectUri = b.redirectUri;
  }

  public WelcomeMailDto() {
  }

  /**
   **/
  public WelcomeMailDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public WelcomeMailDto vorname(String vorname) {
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
  public WelcomeMailDto email(String email) {
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
  public WelcomeMailDto redirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  
  @JsonProperty("redirectUri")
  @NotNull
  public String getRedirectUri() {
    return redirectUri;
  }

  @JsonProperty("redirectUri")
  public void setRedirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    WelcomeMailDto welcomeMail = (WelcomeMailDto) o;
    return Objects.equals(this.name, welcomeMail.name) &&
        Objects.equals(this.vorname, welcomeMail.vorname) &&
        Objects.equals(this.email, welcomeMail.email) &&
        Objects.equals(this.redirectUri, welcomeMail.redirectUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, vorname, email, redirectUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class WelcomeMailDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    redirectUri: ").append(toIndentedString(redirectUri)).append("\n");
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


  public static WelcomeMailDtoBuilder<?, ?> builder() {
    return new WelcomeMailDtoBuilderImpl();
  }

  private static final class WelcomeMailDtoBuilderImpl extends WelcomeMailDtoBuilder<WelcomeMailDto, WelcomeMailDtoBuilderImpl> {

    @Override
    protected WelcomeMailDtoBuilderImpl self() {
      return this;
    }

    @Override
    public WelcomeMailDto build() {
      return new WelcomeMailDto(this);
    }
  }

  public static abstract class WelcomeMailDtoBuilder<C extends WelcomeMailDto, B extends WelcomeMailDtoBuilder<C, B>>  {
    private String name;
    private String vorname;
    private String email;
    private String redirectUri;
    protected abstract B self();

    public abstract C build();

    public B name(String name) {
      this.name = name;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B email(String email) {
      this.email = email;
      return self();
    }
    public B redirectUri(String redirectUri) {
      this.redirectUri = redirectUri;
      return self();
    }
  }
}

