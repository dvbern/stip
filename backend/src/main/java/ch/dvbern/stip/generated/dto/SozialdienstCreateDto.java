package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AdresseDto;
import ch.dvbern.stip.generated.dto.SozialdienstAdminCreateDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("SozialdienstCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstCreateDto  implements Serializable {
  private @Valid String name;
  private @Valid String iban;
  private @Valid AdresseDto adresse;
  private @Valid SozialdienstAdminCreateDto sozialdienstAdmin;

  /**
   **/
  public SozialdienstCreateDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty("name")
  @NotNull
  public String getName() {
    return name;
  }

  @JsonProperty("name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public SozialdienstCreateDto iban(String iban) {
    this.iban = iban;
    return this;
  }

  
  @JsonProperty("iban")
  @NotNull
  public String getIban() {
    return iban;
  }

  @JsonProperty("iban")
  public void setIban(String iban) {
    this.iban = iban;
  }

  /**
   **/
  public SozialdienstCreateDto adresse(AdresseDto adresse) {
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
  public SozialdienstCreateDto sozialdienstAdmin(SozialdienstAdminCreateDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
    return this;
  }

  
  @JsonProperty("sozialdienstAdmin")
  @NotNull
  public SozialdienstAdminCreateDto getSozialdienstAdmin() {
    return sozialdienstAdmin;
  }

  @JsonProperty("sozialdienstAdmin")
  public void setSozialdienstAdmin(SozialdienstAdminCreateDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstCreateDto sozialdienstCreate = (SozialdienstCreateDto) o;
    return Objects.equals(this.name, sozialdienstCreate.name) &&
        Objects.equals(this.iban, sozialdienstCreate.iban) &&
        Objects.equals(this.adresse, sozialdienstCreate.adresse) &&
        Objects.equals(this.sozialdienstAdmin, sozialdienstCreate.sozialdienstAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, iban, adresse, sozialdienstAdmin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstCreateDto {\n");
    
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    sozialdienstAdmin: ").append(toIndentedString(sozialdienstAdmin)).append("\n");
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

