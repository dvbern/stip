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
  private @Valid String keykloakId;
  private @Valid String vorname;
  private @Valid String nachname;

  /**
   **/
  public SozialdienstAdminDto keykloakId(String keykloakId) {
    this.keykloakId = keykloakId;
    return this;
  }


  @JsonProperty("keykloakId")
  @NotNull
  public String getKeykloakId() {
    return keykloakId;
  }

  @JsonProperty("keykloakId")
  public void setKeykloakId(String keykloakId) {
    this.keykloakId = keykloakId;
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstAdminDto sozialdienstAdmin = (SozialdienstAdminDto) o;
    return Objects.equals(this.keykloakId, sozialdienstAdmin.keykloakId) &&
        Objects.equals(this.vorname, sozialdienstAdmin.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdmin.nachname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(keykloakId, vorname, nachname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminDto {\n");

    sb.append("    keykloakId: ").append(toIndentedString(keykloakId)).append("\n");
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


}

