package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ZahlungsverbindungSlim")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ZahlungsverbindungSlimDto  implements Serializable {
  private @Valid AdresseDto adresse;
  private @Valid String vorname;
  private @Valid String nachname;
  private @Valid String institution;

  /**
   **/
  public ZahlungsverbindungSlimDto adresse(AdresseDto adresse) {
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

  /**
   **/
  public ZahlungsverbindungSlimDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public ZahlungsverbindungSlimDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public ZahlungsverbindungSlimDto institution(String institution) {
    this.institution = institution;
    return this;
  }

  
  @JsonProperty("institution")
  public String getInstitution() {
    return institution;
  }

  @JsonProperty("institution")
  public void setInstitution(String institution) {
    this.institution = institution;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ZahlungsverbindungSlimDto zahlungsverbindungSlim = (ZahlungsverbindungSlimDto) o;
    return Objects.equals(this.adresse, zahlungsverbindungSlim.adresse) &&
        Objects.equals(this.vorname, zahlungsverbindungSlim.vorname) &&
        Objects.equals(this.nachname, zahlungsverbindungSlim.nachname) &&
        Objects.equals(this.institution, zahlungsverbindungSlim.institution);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adresse, vorname, nachname, institution);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ZahlungsverbindungSlimDto {\n");
    
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    institution: ").append(toIndentedString(institution)).append("\n");
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

