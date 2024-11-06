package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;



@JsonTypeName("Bildungskategorie")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BildungskategorieDto  implements Serializable {
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe bildungsstufe;
  private @Valid Integer bfs;
  private @Valid UUID id;

  /**
   **/
  public BildungskategorieDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }


  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public BildungskategorieDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }


  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public BildungskategorieDto bildungsstufe(ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe bildungsstufe) {
    this.bildungsstufe = bildungsstufe;
    return this;
  }


  @JsonProperty("bildungsstufe")
  @NotNull
  public ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe getBildungsstufe() {
    return bildungsstufe;
  }

  @JsonProperty("bildungsstufe")
  public void setBildungsstufe(ch.dvbern.stip.api.bildungskategorie.type.Bildungsstufe bildungsstufe) {
    this.bildungsstufe = bildungsstufe;
  }

  /**
   **/
  public BildungskategorieDto bfs(Integer bfs) {
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
  public BildungskategorieDto id(UUID id) {
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
    BildungskategorieDto bildungskategorie = (BildungskategorieDto) o;
    return Objects.equals(this.bezeichnungDe, bildungskategorie.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, bildungskategorie.bezeichnungFr) &&
        Objects.equals(this.bildungsstufe, bildungskategorie.bildungsstufe) &&
        Objects.equals(this.bfs, bildungskategorie.bfs) &&
        Objects.equals(this.id, bildungskategorie.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bezeichnungDe, bezeichnungFr, bildungsstufe, bfs, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BildungskategorieDto {\n");

    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
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

