package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("SozialdienstAdminCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstAdminCreateDto  implements Serializable {
  private @Valid String keycloakId;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String email;

  /**
   **/
  public SozialdienstAdminCreateDto keycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
    return this;
  }


  @JsonProperty("keycloakId")
  @NotNull
  public String getKeycloakId() {
    return keycloakId;
  }

  @JsonProperty("keycloakId")
  public void setKeycloakId(String keycloakId) {
    this.keycloakId = keycloakId;
  }

  /**
   **/
  public SozialdienstAdminCreateDto vorname(String vorname) {
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
  public SozialdienstAdminCreateDto nachname(String nachname) {
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
  public SozialdienstAdminCreateDto email(String email) {
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
    SozialdienstAdminCreateDto sozialdienstAdminCreate = (SozialdienstAdminCreateDto) o;
    return Objects.equals(this.keycloakId, sozialdienstAdminCreate.keycloakId) &&
        Objects.equals(this.vorname, sozialdienstAdminCreate.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdminCreate.nachname) &&
        Objects.equals(this.email, sozialdienstAdminCreate.email);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keycloakId, vorname, nachname, email);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminCreateDto {\n");

    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
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

