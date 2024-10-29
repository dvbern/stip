package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("SozialdienstAdmin")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstAdminDto  implements Serializable {
  private @Valid String keycloakId;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String eMail;

  /**
   **/
  public SozialdienstAdminDto keycloakId(String keycloakId) {
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
  public SozialdienstAdminDto eMail(String eMail) {
    this.eMail = eMail;
    return this;
  }


  @JsonProperty("eMail")
  @NotNull
  public String geteMail() {
    return eMail;
  }

  @JsonProperty("eMail")
  public void seteMail(String eMail) {
    this.eMail = eMail;
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
    return Objects.equals(this.keycloakId, sozialdienstAdmin.keycloakId) &&
        Objects.equals(this.vorname, sozialdienstAdmin.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdmin.nachname) &&
        Objects.equals(this.eMail, sozialdienstAdmin.eMail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keycloakId, vorname, nachname, eMail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminDto {\n");

    sb.append("    keycloakId: ").append(toIndentedString(keycloakId)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    eMail: ").append(toIndentedString(eMail)).append("\n");
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

