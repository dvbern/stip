package ch.dvbern.stip.generated.dto;

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



@JsonTypeName("SozialdienstUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SozialdienstUpdateDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String name;
  private @Valid ZahlungsverbindungDto zahlungsverbindung;

  /**
   **/
  public SozialdienstUpdateDto id(UUID id) {
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
  public SozialdienstUpdateDto name(String name) {
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
  public SozialdienstUpdateDto zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SozialdienstUpdateDto sozialdienstUpdate = (SozialdienstUpdateDto) o;
    return Objects.equals(this.id, sozialdienstUpdate.id) &&
        Objects.equals(this.name, sozialdienstUpdate.name) &&
        Objects.equals(this.zahlungsverbindung, sozialdienstUpdate.zahlungsverbindung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name, zahlungsverbindung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SozialdienstUpdateDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
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

