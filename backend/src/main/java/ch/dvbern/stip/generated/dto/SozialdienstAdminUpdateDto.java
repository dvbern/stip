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
  private @Valid String eMail;

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
  public SozialdienstAdminUpdateDto eMail(String eMail) {
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
    SozialdienstAdminUpdateDto sozialdienstAdminUpdate = (SozialdienstAdminUpdateDto) o;
    return Objects.equals(this.vorname, sozialdienstAdminUpdate.vorname) &&
        Objects.equals(this.nachname, sozialdienstAdminUpdate.nachname) &&
        Objects.equals(this.eMail, sozialdienstAdminUpdate.eMail);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, eMail);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstAdminUpdateDto {\n");

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

