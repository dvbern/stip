package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * 1 or 2 Aenderungstranche in relation to another Tranche
 **/

@JsonTypeName("GesuchTrancheWithChanges")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchTrancheWithChangesDto  implements Serializable {
  private @Valid List<GesuchTrancheDto> changes;

  /**
   **/
  public GesuchTrancheWithChangesDto changes(List<GesuchTrancheDto> changes) {
    this.changes = changes;
    return this;
  }

  
  @JsonProperty("changes")
  public List<GesuchTrancheDto> getChanges() {
    return changes;
  }

  @JsonProperty("changes")
  public void setChanges(List<GesuchTrancheDto> changes) {
    this.changes = changes;
  }

  public GesuchTrancheWithChangesDto addChangesItem(GesuchTrancheDto changesItem) {
    if (this.changes == null) {
      this.changes = new ArrayList<>();
    }

    this.changes.add(changesItem);
    return this;
  }

  public GesuchTrancheWithChangesDto removeChangesItem(GesuchTrancheDto changesItem) {
    if (changesItem != null && this.changes != null) {
      this.changes.remove(changesItem);
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
    GesuchTrancheWithChangesDto gesuchTrancheWithChanges = (GesuchTrancheWithChangesDto) o;
    return Objects.equals(this.changes, gesuchTrancheWithChanges.changes);
  }

  @Override
  public int hashCode() {
    return Objects.hash(changes);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheWithChangesDto {\n");
    
    sb.append("    changes: ").append(toIndentedString(changes)).append("\n");
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

