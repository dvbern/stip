package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SozialdienstBenutzerDto;
import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
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



@JsonTypeName("Sozialdienst")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String name;
  private @Valid SozialdienstBenutzerDto sozialdienstAdmin;
  private @Valid ZahlungsverbindungDto zahlungsverbindung;
  private @Valid Boolean aktiv;

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
  public SozialdienstDto sozialdienstAdmin(SozialdienstBenutzerDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
    return this;
  }

  
  @JsonProperty("sozialdienstAdmin")
  @NotNull
  public SozialdienstBenutzerDto getSozialdienstAdmin() {
    return sozialdienstAdmin;
  }

  @JsonProperty("sozialdienstAdmin")
  public void setSozialdienstAdmin(SozialdienstBenutzerDto sozialdienstAdmin) {
    this.sozialdienstAdmin = sozialdienstAdmin;
  }

  /**
   **/
  public SozialdienstDto zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
    return this;
  }

  
  @JsonProperty("zahlungsverbindung")
  @NotNull
  public ZahlungsverbindungDto getZahlungsverbindung() {
    return zahlungsverbindung;
  }

  @JsonProperty("zahlungsverbindung")
  public void setZahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
  }

  /**
   **/
  public SozialdienstDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
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
        Objects.equals(this.sozialdienstAdmin, sozialdienst.sozialdienstAdmin) &&
        Objects.equals(this.zahlungsverbindung, sozialdienst.zahlungsverbindung) &&
        Objects.equals(this.aktiv, sozialdienst.aktiv);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, sozialdienstAdmin, zahlungsverbindung, aktiv);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    sozialdienstAdmin: ").append(toIndentedString(sozialdienstAdmin)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
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

