package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("BenutzerUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BenutzerUpdateDto  implements Serializable {
  private @Valid String sozialversicherungsnummer;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;

  /**
   **/
  public BenutzerUpdateDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }


  @JsonProperty("sozialversicherungsnummer")
  @NotNull
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public BenutzerUpdateDto vorname(String vorname) {
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
  public BenutzerUpdateDto nachname(String nachname) {
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
  public BenutzerUpdateDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
    this.benutzereinstellungen = benutzereinstellungen;
    return this;
  }


  @JsonProperty("benutzereinstellungen")
  public BenutzereinstellungenUpdateDto getBenutzereinstellungen() {
    return benutzereinstellungen;
  }

  @JsonProperty("benutzereinstellungen")
  public void setBenutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
    this.benutzereinstellungen = benutzereinstellungen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BenutzerUpdateDto benutzerUpdate = (BenutzerUpdateDto) o;
    return Objects.equals(this.sozialversicherungsnummer, benutzerUpdate.sozialversicherungsnummer) &&
        Objects.equals(this.vorname, benutzerUpdate.vorname) &&
        Objects.equals(this.nachname, benutzerUpdate.nachname) &&
        Objects.equals(this.benutzereinstellungen, benutzerUpdate.benutzereinstellungen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialversicherungsnummer, vorname, nachname, benutzereinstellungen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzerUpdateDto {\n");

    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    benutzereinstellungen: ").append(toIndentedString(benutzereinstellungen)).append("\n");
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

