package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("Sozialdienst")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String name;
  private @Valid AdresseDto adresse;
  private @Valid String iban;
  private @Valid SozialdienstAdminDto sozialdienstAdmin;

  /**
   **/
  public SozialdienstDto id(UUID id) {
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
  public SozialdienstDto name(String name) {
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
  public SozialdienstDto adresse(AdresseDto adresse) {
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
  public SozialdienstDto iban(String iban) {
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
  public SozialdienstDto sozialdienstAdmin(SozialdienstAdminDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
    return this;
  }


  @JsonProperty("sozialdienstAdmin")
  public SozialdienstAdminDto getSozialdienstAdmin() {
    return sozialdienstAdmin;
  }

  @JsonProperty("sozialdienstAdmin")
  public void setSozialdienstAdmin(SozialdienstAdminDto sozialdienstAdmin) {
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
    SozialdienstDto sozialdienst = (SozialdienstDto) o;
    return Objects.equals(this.id, sozialdienst.id) &&
        Objects.equals(this.name, sozialdienst.name) &&
        Objects.equals(this.adresse, sozialdienst.adresse) &&
        Objects.equals(this.iban, sozialdienst.iban) &&
        Objects.equals(this.sozialdienstAdmin, sozialdienst.sozialdienstAdmin);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, adresse, iban, sozialdienstAdmin);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstDto {\n");

    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    adresse: ").append(toIndentedString(adresse)).append("\n");
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
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

