package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonTypeName("GesuchNotizUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchNotizUpdateDto  implements Serializable {
  private @Valid String betreff;
  private @Valid String text;
  private @Valid UUID id;
  private @Valid String user;
  private @Valid String datum;

  /**
   **/
  public GesuchNotizUpdateDto betreff(String betreff) {
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
  public GesuchNotizUpdateDto text(String text) {
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
  public GesuchNotizUpdateDto id(UUID id) {
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
  public GesuchNotizUpdateDto user(String user) {
    this.user = user;
    return this;
  }


  @JsonProperty("user")
  public String getUser() {
    return user;
  }

  @JsonProperty("user")
  public void setUser(String user) {
    this.user = user;
  }

  /**
   **/
  public GesuchNotizUpdateDto datum(String datum) {
    this.datum = datum;
    return this;
  }


  @JsonProperty("datum")
  public String getDatum() {
    return datum;
  }

  @JsonProperty("datum")
  public void setDatum(String datum) {
    this.datum = datum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchNotizUpdateDto gesuchNotizUpdate = (GesuchNotizUpdateDto) o;
    return Objects.equals(this.betreff, gesuchNotizUpdate.betreff) &&
        Objects.equals(this.text, gesuchNotizUpdate.text) &&
        Objects.equals(this.id, gesuchNotizUpdate.id) &&
        Objects.equals(this.user, gesuchNotizUpdate.user) &&
        Objects.equals(this.datum, gesuchNotizUpdate.datum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(betreff, text, id, user, datum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchNotizUpdateDto {\n");

    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    user: ").append(toIndentedString(user)).append("\n");
    sb.append("    datum: ").append(toIndentedString(datum)).append("\n");
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

