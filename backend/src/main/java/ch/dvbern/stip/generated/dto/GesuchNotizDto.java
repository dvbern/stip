package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("GesuchNotiz")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchNotizDto  implements Serializable {
  private @Valid String betreff;
  private @Valid String text;
  private @Valid String userMutiert;
  private @Valid LocalDate timestampMutiert;
  private @Valid UUID id;

  /**
   **/
  public GesuchNotizDto betreff(String betreff) {
    this.betreff = betreff;
    return this;
  }


  @JsonProperty("betreff")
  @NotNull
  public String getBetreff() {
    return betreff;
  }

  @JsonProperty("betreff")
  public void setBetreff(String betreff) {
    this.betreff = betreff;
  }

  /**
   **/
  public GesuchNotizDto text(String text) {
    this.text = text;
    return this;
  }


  @JsonProperty("text")
  @NotNull
  public String getText() {
    return text;
  }

  @JsonProperty("text")
  public void setText(String text) {
    this.text = text;
  }

  /**
   **/
  public GesuchNotizDto userMutiert(String userMutiert) {
    this.userMutiert = userMutiert;
    return this;
  }


  @JsonProperty("userMutiert")
  @NotNull
  public String getUserMutiert() {
    return userMutiert;
  }

  @JsonProperty("userMutiert")
  public void setUserMutiert(String userMutiert) {
    this.userMutiert = userMutiert;
  }

  /**
   **/
  public GesuchNotizDto timestampMutiert(LocalDate timestampMutiert) {
    this.timestampMutiert = timestampMutiert;
    return this;
  }


  @JsonProperty("timestampMutiert")
  @NotNull
  public LocalDate getTimestampMutiert() {
    return timestampMutiert;
  }

  @JsonProperty("timestampMutiert")
  public void setTimestampMutiert(LocalDate timestampMutiert) {
    this.timestampMutiert = timestampMutiert;
  }

  /**
   **/
  public GesuchNotizDto id(UUID id) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchNotizDto gesuchNotiz = (GesuchNotizDto) o;
    return Objects.equals(this.betreff, gesuchNotiz.betreff) &&
        Objects.equals(this.text, gesuchNotiz.text) &&
        Objects.equals(this.userMutiert, gesuchNotiz.userMutiert) &&
        Objects.equals(this.timestampMutiert, gesuchNotiz.timestampMutiert) &&
        Objects.equals(this.id, gesuchNotiz.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(betreff, text, userMutiert, timestampMutiert, id);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchNotizDto {\n");

    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    userMutiert: ").append(toIndentedString(userMutiert)).append("\n");
    sb.append("    timestampMutiert: ").append(toIndentedString(timestampMutiert)).append("\n");
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

