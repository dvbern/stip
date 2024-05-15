package ch.dvbern.stip.generated.dto;

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

/**
 * 
 **/

@JsonTypeName("Bildungsart")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BildungsartDto  implements Serializable {
  private @Valid String beschreibung;
  private @Valid ch.dvbern.stip.api.bildungsart.type.Bildungsstufe bildungsstufe;
  private @Valid Integer bfs;
  private @Valid UUID id;

  /**
   **/
  public BildungsartDto beschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
    return this;
  }

  
  @JsonProperty("beschreibung")
  @NotNull
  public String getBeschreibung() {
    return beschreibung;
  }

  @JsonProperty("beschreibung")
  public void setBeschreibung(String beschreibung) {
    this.beschreibung = beschreibung;
  }

  /**
   **/
  public BildungsartDto bildungsstufe(ch.dvbern.stip.api.bildungsart.type.Bildungsstufe bildungsstufe) {
    this.bildungsstufe = bildungsstufe;
    return this;
  }

  
  @JsonProperty("bildungsstufe")
  @NotNull
  public ch.dvbern.stip.api.bildungsart.type.Bildungsstufe getBildungsstufe() {
    return bildungsstufe;
  }

  @JsonProperty("bildungsstufe")
  public void setBildungsstufe(ch.dvbern.stip.api.bildungsart.type.Bildungsstufe bildungsstufe) {
    this.bildungsstufe = bildungsstufe;
  }

  /**
   **/
  public BildungsartDto bfs(Integer bfs) {
    this.bfs = bfs;
    return this;
  }

  
  @JsonProperty("bfs")
  @NotNull
  public Integer getBfs() {
    return bfs;
  }

  @JsonProperty("bfs")
  public void setBfs(Integer bfs) {
    this.bfs = bfs;
  }

  /**
   **/
  public BildungsartDto id(UUID id) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BildungsartDto bildungsart = (BildungsartDto) o;
    return Objects.equals(this.beschreibung, bildungsart.beschreibung) &&
        Objects.equals(this.bildungsstufe, bildungsart.bildungsstufe) &&
        Objects.equals(this.bfs, bildungsart.bfs) &&
        Objects.equals(this.id, bildungsart.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(beschreibung, bildungsstufe, bfs, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BildungsartDto {\n");
    
    sb.append("    beschreibung: ").append(toIndentedString(beschreibung)).append("\n");
    sb.append("    bildungsstufe: ").append(toIndentedString(bildungsstufe)).append("\n");
    sb.append("    bfs: ").append(toIndentedString(bfs)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
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

