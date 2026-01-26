package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ApplyDemoDataResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class ApplyDemoDataResponseDto  implements Serializable {
  private @Valid UUID gesuchId;
  private @Valid UUID gesuchTrancheId;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid String gesuchStatus;

  /**
   **/
  public ApplyDemoDataResponseDto gesuchId(UUID gesuchId) {
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
  public ApplyDemoDataResponseDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty("gesuchTrancheId")
  @NotNull
  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty("gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gesuchStatus(String gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public String getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(String gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyDemoDataResponseDto applyDemoDataResponse = (ApplyDemoDataResponseDto) o;
    return Objects.equals(this.gesuchId, applyDemoDataResponse.gesuchId) &&
        Objects.equals(this.gesuchTrancheId, applyDemoDataResponse.gesuchTrancheId) &&
        Objects.equals(this.gueltigAb, applyDemoDataResponse.gueltigAb) &&
        Objects.equals(this.gueltigBis, applyDemoDataResponse.gueltigBis) &&
        Objects.equals(this.gesuchStatus, applyDemoDataResponse.gesuchStatus);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchId, gesuchTrancheId, gueltigAb, gueltigBis, gesuchStatus);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyDemoDataResponseDto {\n");
    
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
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

