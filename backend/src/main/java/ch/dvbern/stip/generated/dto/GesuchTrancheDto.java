package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchFormularDto;
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



@JsonTypeName("GesuchTranche")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchTrancheDto  implements Serializable {
  private @Valid UUID id;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status;
  private @Valid ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ;
  private @Valid String comment;
  private @Valid GesuchFormularDto gesuchFormular;

  /**
   **/
  public GesuchTrancheDto id(UUID id) {
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
  public GesuchTrancheDto gueltigAb(LocalDate gueltigAb) {
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
  public GesuchTrancheDto gueltigBis(LocalDate gueltigBis) {
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
  public GesuchTrancheDto status(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status) {
    this.status = status;
    return this;
  }

  
  @JsonProperty("status")
  @NotNull
  public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus getStatus() {
    return status;
  }

  @JsonProperty("status")
  public void setStatus(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheStatus status) {
    this.status = status;
  }

  /**
   **/
  public GesuchTrancheDto typ(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
    return this;
  }

  
  @JsonProperty("typ")
  @NotNull
  public ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp getTyp() {
    return typ;
  }

  @JsonProperty("typ")
  public void setTyp(ch.dvbern.stip.api.gesuchtranche.type.GesuchTrancheTyp typ) {
    this.typ = typ;
  }

  /**
   **/
  public GesuchTrancheDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @JsonProperty("comment")
  public String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   **/
  public GesuchTrancheDto gesuchFormular(GesuchFormularDto gesuchFormular) {
    this.gesuchFormular = gesuchFormular;
    return this;
  }

  
  @JsonProperty("gesuchFormular")
  public GesuchFormularDto getGesuchFormular() {
    return gesuchFormular;
  }

  @JsonProperty("gesuchFormular")
  public void setGesuchFormular(GesuchFormularDto gesuchFormular) {
    this.gesuchFormular = gesuchFormular;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheDto gesuchTranche = (GesuchTrancheDto) o;
    return Objects.equals(this.id, gesuchTranche.id) &&
        Objects.equals(this.gueltigAb, gesuchTranche.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchTranche.gueltigBis) &&
        Objects.equals(this.status, gesuchTranche.status) &&
        Objects.equals(this.typ, gesuchTranche.typ) &&
        Objects.equals(this.comment, gesuchTranche.comment) &&
        Objects.equals(this.gesuchFormular, gesuchTranche.gesuchFormular);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigAb, gueltigBis, status, typ, comment, gesuchFormular);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    gesuchFormular: ").append(toIndentedString(gesuchFormular)).append("\n");
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

