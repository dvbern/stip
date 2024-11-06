package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
import ch.dvbern.stip.generated.dto.SachbearbeiterZuordnungStammdatenDto;
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



@JsonTypeName("Benutzer")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BenutzerDto  implements Serializable {
  private @Valid String sozialversicherungsnummer;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid UUID id;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten;

  /**
   **/
  public BenutzerDto sozialversicherungsnummer(String sozialversicherungsnummer) {
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
  public BenutzerDto vorname(String vorname) {
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
  public BenutzerDto nachname(String nachname) {
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
  public BenutzerDto id(UUID id) {
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
  public BenutzerDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
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

  /**
   **/
  public BenutzerDto sachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
    return this;
  }

  
  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public SachbearbeiterZuordnungStammdatenDto getSachbearbeiterZuordnungStammdaten() {
    return sachbearbeiterZuordnungStammdaten;
  }

  @JsonProperty("sachbearbeiterZuordnungStammdaten")
  public void setSachbearbeiterZuordnungStammdaten(SachbearbeiterZuordnungStammdatenDto sachbearbeiterZuordnungStammdaten) {
    this.sachbearbeiterZuordnungStammdaten = sachbearbeiterZuordnungStammdaten;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BenutzerDto benutzer = (BenutzerDto) o;
    return Objects.equals(this.sozialversicherungsnummer, benutzer.sozialversicherungsnummer) &&
        Objects.equals(this.vorname, benutzer.vorname) &&
        Objects.equals(this.nachname, benutzer.nachname) &&
        Objects.equals(this.id, benutzer.id) &&
        Objects.equals(this.benutzereinstellungen, benutzer.benutzereinstellungen) &&
        Objects.equals(this.sachbearbeiterZuordnungStammdaten, benutzer.sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialversicherungsnummer, vorname, nachname, id, benutzereinstellungen, sachbearbeiterZuordnungStammdaten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BenutzerDto {\n");
    
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    benutzereinstellungen: ").append(toIndentedString(benutzereinstellungen)).append("\n");
    sb.append("    sachbearbeiterZuordnungStammdaten: ").append(toIndentedString(sachbearbeiterZuordnungStammdaten)).append("\n");
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

