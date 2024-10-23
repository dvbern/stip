package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;


@JsonTypeName("SozialdienstCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstCreateDto  implements Serializable {
  private @Valid String name;
  private @Valid String iban;
  private @Valid AdresseDto adresse;
  private @Valid SozialdienstAdminCreateDto admin;

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
  public SozialdienstCreateDto admin(SozialdienstAdminCreateDto admin) {
    this.admin = admin;
    return this;
  }


  @JsonProperty("admin")
  @NotNull
  public SozialdienstAdminCreateDto getAdmin() {
    return admin;
  }

  @JsonProperty("admin")
  public void setAdmin(SozialdienstAdminCreateDto admin) {
    this.admin = admin;
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
        Objects.equals(this.admin, sozialdienstCreate.admin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, iban, adresse, admin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstCreateDto {\n");

    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    admin: ").append(toIndentedString(admin)).append("\n");
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

