package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BenutzereinstellungenUpdateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Sachbearbeiter")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SachbearbeiterDto  implements Serializable {
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String telefonnummer;
  private @Valid String email;
  private @Valid String funktionDe;
  private @Valid String funktionFr;
  private @Valid List<String> sachbearbeiterRollen = new ArrayList<>();
  private @Valid UUID id;
  private @Valid BenutzereinstellungenUpdateDto benutzereinstellungen;
  private @Valid String redirectUri;

  /**
   **/
  public SachbearbeiterDto vorname(String vorname) {
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
  public SachbearbeiterDto nachname(String nachname) {
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
  public SachbearbeiterDto telefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
    return this;
  }

  
  @JsonProperty("telefonnummer")
  @NotNull
  public String getTelefonnummer() {
    return telefonnummer;
  }

  @JsonProperty("telefonnummer")
  public void setTelefonnummer(String telefonnummer) {
    this.telefonnummer = telefonnummer;
  }

  /**
   **/
  public SachbearbeiterDto email(String email) {
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
  public SachbearbeiterDto funktionDe(String funktionDe) {
    this.funktionDe = funktionDe;
    return this;
  }

  
  @JsonProperty("funktionDe")
  @NotNull
  public String getFunktionDe() {
    return funktionDe;
  }

  @JsonProperty("funktionDe")
  public void setFunktionDe(String funktionDe) {
    this.funktionDe = funktionDe;
  }

  /**
   **/
  public SachbearbeiterDto funktionFr(String funktionFr) {
    this.funktionFr = funktionFr;
    return this;
  }

  
  @JsonProperty("funktionFr")
  @NotNull
  public String getFunktionFr() {
    return funktionFr;
  }

  @JsonProperty("funktionFr")
  public void setFunktionFr(String funktionFr) {
    this.funktionFr = funktionFr;
  }

  /**
   **/
  public SachbearbeiterDto sachbearbeiterRollen(List<String> sachbearbeiterRollen) {
    this.sachbearbeiterRollen = sachbearbeiterRollen;
    return this;
  }

  
  @JsonProperty("sachbearbeiterRollen")
  @NotNull
  public List<String> getSachbearbeiterRollen() {
    return sachbearbeiterRollen;
  }

  @JsonProperty("sachbearbeiterRollen")
  public void setSachbearbeiterRollen(List<String> sachbearbeiterRollen) {
    this.sachbearbeiterRollen = sachbearbeiterRollen;
  }

  public SachbearbeiterDto addSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (this.sachbearbeiterRollen == null) {
      this.sachbearbeiterRollen = new ArrayList<>();
    }

    this.sachbearbeiterRollen.add(sachbearbeiterRollenItem);
    return this;
  }

  public SachbearbeiterDto removeSachbearbeiterRollenItem(String sachbearbeiterRollenItem) {
    if (sachbearbeiterRollenItem != null && this.sachbearbeiterRollen != null) {
      this.sachbearbeiterRollen.remove(sachbearbeiterRollenItem);
    }

    return this;
  }
  /**
   **/
  public SachbearbeiterDto id(UUID id) {
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
  public SachbearbeiterDto benutzereinstellungen(BenutzereinstellungenUpdateDto benutzereinstellungen) {
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
  public SachbearbeiterDto redirectUri(String redirectUri) {
    this.redirectUri = redirectUri;
    return this;
  }

  
  @JsonProperty("redirectUri")
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
    SachbearbeiterDto sachbearbeiter = (SachbearbeiterDto) o;
    return Objects.equals(this.vorname, sachbearbeiter.vorname) &&
        Objects.equals(this.nachname, sachbearbeiter.nachname) &&
        Objects.equals(this.telefonnummer, sachbearbeiter.telefonnummer) &&
        Objects.equals(this.email, sachbearbeiter.email) &&
        Objects.equals(this.funktionDe, sachbearbeiter.funktionDe) &&
        Objects.equals(this.funktionFr, sachbearbeiter.funktionFr) &&
        Objects.equals(this.sachbearbeiterRollen, sachbearbeiter.sachbearbeiterRollen) &&
        Objects.equals(this.id, sachbearbeiter.id) &&
        Objects.equals(this.benutzereinstellungen, sachbearbeiter.benutzereinstellungen) &&
        Objects.equals(this.redirectUri, sachbearbeiter.redirectUri);
  }

  @Override
  public int hashCode() {
    return Objects.hash(vorname, nachname, telefonnummer, email, funktionDe, funktionFr, sachbearbeiterRollen, id, benutzereinstellungen, redirectUri);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterDto {\n");
    
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    telefonnummer: ").append(toIndentedString(telefonnummer)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    funktionDe: ").append(toIndentedString(funktionDe)).append("\n");
    sb.append("    funktionFr: ").append(toIndentedString(funktionFr)).append("\n");
    sb.append("    sachbearbeiterRollen: ").append(toIndentedString(sachbearbeiterRollen)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    benutzereinstellungen: ").append(toIndentedString(benutzereinstellungen)).append("\n");
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


}

