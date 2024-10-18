package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
import ch.dvbern.stip.generated.dto.GsDashboardMissingDocumentsDto;
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



@JsonTypeName("GsDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GsDashboardDto  implements Serializable {
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus;
  private @Valid UUID id;
  private @Valid GesuchTrancheSlimDto offeneAenderung;
  private @Valid GsDashboardMissingDocumentsDto missingDocuments;

  /**
   **/
  public GsDashboardDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }

  
  @JsonProperty("gesuchsperiode")
  @NotNull
  public GesuchsperiodeDto getGesuchsperiode() {
    return gesuchsperiode;
  }

  @JsonProperty("gesuchsperiode")
  public void setGesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
  }

  /**
   **/
  public GsDashboardDto gesuchStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public ch.dvbern.stip.api.gesuch.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public GsDashboardDto id(UUID id) {
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
  public GsDashboardDto offeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
    return this;
  }

  
  @JsonProperty("offeneAenderung")
  public GesuchTrancheSlimDto getOffeneAenderung() {
    return offeneAenderung;
  }

  @JsonProperty("offeneAenderung")
  public void setOffeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
  }

  /**
   **/
  public GsDashboardDto missingDocuments(GsDashboardMissingDocumentsDto missingDocuments) {
    this.missingDocuments = missingDocuments;
    return this;
  }

  
  @JsonProperty("missingDocuments")
  public GsDashboardMissingDocumentsDto getMissingDocuments() {
    return missingDocuments;
  }

  @JsonProperty("missingDocuments")
  public void setMissingDocuments(GsDashboardMissingDocumentsDto missingDocuments) {
    this.missingDocuments = missingDocuments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GsDashboardDto gsDashboard = (GsDashboardDto) o;
    return Objects.equals(this.gesuchsperiode, gsDashboard.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gsDashboard.gesuchStatus) &&
        Objects.equals(this.id, gsDashboard.id) &&
        Objects.equals(this.offeneAenderung, gsDashboard.offeneAenderung) &&
        Objects.equals(this.missingDocuments, gsDashboard.missingDocuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchsperiode, gesuchStatus, id, offeneAenderung, missingDocuments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GsDashboardDto {\n");
    
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    offeneAenderung: ").append(toIndentedString(offeneAenderung)).append("\n");
    sb.append("    missingDocuments: ").append(toIndentedString(missingDocuments)).append("\n");
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

