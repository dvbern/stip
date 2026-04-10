package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
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



@JsonTypeName("SachbearbeiterGesuchDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class SachbearbeiterGesuchDokumentDto  implements Serializable {
  private @Valid String type;
  private @Valid String description;
  private @Valid UUID id;
  private @Valid UUID gesuchId;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();

  /**
   **/
  public SachbearbeiterGesuchDokumentDto type(String type) {
    this.type = type;
    return this;
  }

  
  @JsonProperty("type")
  @NotNull
  public String getType() {
    return type;
  }

  @JsonProperty("type")
  public void setType(String type) {
    this.type = type;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto description(String description) {
    this.description = description;
    return this;
  }

  
  @JsonProperty("description")
  @NotNull
  public String getDescription() {
    return description;
  }

  @JsonProperty("description")
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto id(UUID id) {
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
  public SachbearbeiterGesuchDokumentDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public SachbearbeiterGesuchDokumentDto dokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
    return this;
  }

  
  @JsonProperty("dokumente")
  @NotNull
  public List<DokumentDto> getDokumente() {
    return dokumente;
  }

  @JsonProperty("dokumente")
  public void setDokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
  }

  public SachbearbeiterGesuchDokumentDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public SachbearbeiterGesuchDokumentDto removeDokumenteItem(DokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SachbearbeiterGesuchDokumentDto sachbearbeiterGesuchDokument = (SachbearbeiterGesuchDokumentDto) o;
    return Objects.equals(this.type, sachbearbeiterGesuchDokument.type) &&
        Objects.equals(this.description, sachbearbeiterGesuchDokument.description) &&
        Objects.equals(this.id, sachbearbeiterGesuchDokument.id) &&
        Objects.equals(this.gesuchId, sachbearbeiterGesuchDokument.gesuchId) &&
        Objects.equals(this.dokumente, sachbearbeiterGesuchDokument.dokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(type, description, id, gesuchId, dokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class SachbearbeiterGesuchDokumentDto {\n");
    
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    description: ").append(toIndentedString(description)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
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

