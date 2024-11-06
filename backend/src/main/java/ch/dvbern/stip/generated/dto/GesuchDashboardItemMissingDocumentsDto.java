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



@JsonTypeName("GesuchDashboardItem_missingDocuments")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchDashboardItemMissingDocumentsDto  implements Serializable {
  private @Valid UUID trancheId;
  private @Valid Integer count;

  /**
   **/
  public GesuchDashboardItemMissingDocumentsDto trancheId(UUID trancheId) {
    this.trancheId = trancheId;
    return this;
  }

  
  @JsonProperty("trancheId")
  @NotNull
  public UUID getTrancheId() {
    return trancheId;
  }

  @JsonProperty("trancheId")
  public void setTrancheId(UUID trancheId) {
    this.trancheId = trancheId;
  }

  /**
   **/
  public GesuchDashboardItemMissingDocumentsDto count(Integer count) {
    this.count = count;
    return this;
  }

  
  @JsonProperty("count")
  @NotNull
  public Integer getCount() {
    return count;
  }

  @JsonProperty("count")
  public void setCount(Integer count) {
    this.count = count;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDashboardItemMissingDocumentsDto gesuchDashboardItemMissingDocuments = (GesuchDashboardItemMissingDocumentsDto) o;
    return Objects.equals(this.trancheId, gesuchDashboardItemMissingDocuments.trancheId) &&
        Objects.equals(this.count, gesuchDashboardItemMissingDocuments.count);
  }

  @Override
  public int hashCode() {
    return Objects.hash(trancheId, count);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDashboardItemMissingDocumentsDto {\n");
    
    sb.append("    trancheId: ").append(toIndentedString(trancheId)).append("\n");
    sb.append("    count: ").append(toIndentedString(count)).append("\n");
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

