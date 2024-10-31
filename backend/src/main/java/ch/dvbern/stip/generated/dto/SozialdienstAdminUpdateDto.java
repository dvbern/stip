package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("SozialdienstAdminUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstAdminUpdateDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String email;

  /**
   **/
  public SozialdienstAdminUpdateDto vorname(String vorname) {
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
  public SozialdienstAdminUpdateDto nachname(String nachname) {
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
  public SozialdienstAdminUpdateDto email(String email) {
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
    SozialdienstAdminUpdateDto sozialdienstAdminUpdate = (SozialdienstAdminUpdateDto) o;
    return Objects.equals(this.vorname, sozialdienstAdminUpdate.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdminUpdate.nachname) &&
        Objects.equals(this.email, sozialdienstAdminUpdate.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminUpdateDto {\n");

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


}

