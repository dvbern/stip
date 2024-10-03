package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("GsDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GsDashboardDto  implements Serializable {
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuch.type.Gesuchstatus gesuchStatus;
  private @Valid UUID id;
  private @Valid GesuchTrancheSlimDto offeneAenderung;
  private @Valid UUID gesuchTrancheWithMissingDocumentsId;

  /**
   **/
  public GsDashboardDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }


  @JsonProperty("gesuchsperiode")
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
  public GsDashboardDto gesuchTrancheWithMissingDocumentsId(UUID gesuchTrancheWithMissingDocumentsId) {
    this.gesuchTrancheWithMissingDocumentsId = gesuchTrancheWithMissingDocumentsId;
    return this;
  }


  @JsonProperty("gesuchTrancheWithMissingDocumentsId")
  public UUID getGesuchTrancheWithMissingDocumentsId() {
    return gesuchTrancheWithMissingDocumentsId;
  }

  @JsonProperty("gesuchTrancheWithMissingDocumentsId")
  public void setGesuchTrancheWithMissingDocumentsId(UUID gesuchTrancheWithMissingDocumentsId) {
    this.gesuchTrancheWithMissingDocumentsId = gesuchTrancheWithMissingDocumentsId;
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
        Objects.equals(this.gesuchTrancheWithMissingDocumentsId, gsDashboard.gesuchTrancheWithMissingDocumentsId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchsperiode, gesuchStatus, id, offeneAenderung, gesuchTrancheWithMissingDocumentsId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GsDashboardDto {\n");

    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    offeneAenderung: ").append(toIndentedString(offeneAenderung)).append("\n");
    sb.append("    gesuchTrancheWithMissingDocumentsId: ").append(toIndentedString(gesuchTrancheWithMissingDocumentsId)).append("\n");
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

