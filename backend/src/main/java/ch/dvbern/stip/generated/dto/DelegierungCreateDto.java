package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DelegierungCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DelegierungCreateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.common.type.Anrede anrede;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid LocalDate geburtsdatum;
  private @Valid String email;
  private @Valid ch.dvbern.stip.api.personinausbildung.type.Sprache sprache;
  private @Valid AdresseDto adresse;

  /**
   **/
  public DelegierungCreateDto anrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
    return this;
  }

  
  @JsonProperty("anrede")
  @NotNull
  public ch.dvbern.stip.api.common.type.Anrede getAnrede() {
    return anrede;
  }

  @JsonProperty("anrede")
  public void setAnrede(ch.dvbern.stip.api.common.type.Anrede anrede) {
    this.anrede = anrede;
  }

  /**
   **/
  public DelegierungCreateDto nachname(String nachname) {
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
  public DelegierungCreateDto vorname(String vorname) {
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
  public DelegierungCreateDto geburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty("geburtsdatum")
  @NotNull
  public LocalDate getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty("geburtsdatum")
  public void setGeburtsdatum(LocalDate geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DelegierungCreateDto email(String email) {
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
  public DelegierungCreateDto sprache(ch.dvbern.stip.api.personinausbildung.type.Sprache sprache) {
    this.sprache = sprache;
    return this;
  }

  
  @JsonProperty("sprache")
  @NotNull
  public ch.dvbern.stip.api.personinausbildung.type.Sprache getSprache() {
    return sprache;
  }

  @JsonProperty("sprache")
  public void setSprache(ch.dvbern.stip.api.personinausbildung.type.Sprache sprache) {
    this.sprache = sprache;
  }

  /**
   **/
  public DelegierungCreateDto adresse(AdresseDto adresse) {
    this.adresse = adresse;
    return this;
  }

  
  @JsonProperty("adresse")
  @NotNull
  public AdresseDto getAdresse() {
    return adresse;
  }

  @JsonProperty("adresse")
  public void setAdresse(AdresseDto adresse) {
    this.adresse = adresse;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DelegierungCreateDto delegierungCreate = (DelegierungCreateDto) o;
    return Objects.equals(this.anrede, delegierungCreate.anrede) &&
        Objects.equals(this.nachname, delegierungCreate.nachname) &&
        Objects.equals(this.vorname, delegierungCreate.vorname) &&
        Objects.equals(this.geburtsdatum, delegierungCreate.geburtsdatum) &&
        Objects.equals(this.email, delegierungCreate.email) &&
        Objects.equals(this.sprache, delegierungCreate.sprache) &&
        Objects.equals(this.adresse, delegierungCreate.adresse);
  }

  @Override
  public int hashCode() {
    return Objects.hash(anrede, nachname, vorname, geburtsdatum, email, sprache, adresse);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DelegierungCreateDto {\n");
    
    sb.append("    anrede: ").append(toIndentedString(anrede)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    sprache: ").append(toIndentedString(sprache)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
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

