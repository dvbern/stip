package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@JsonTypeName("GesuchNotiz")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchNotizDto  implements Serializable {
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;
  private @Valid GesuchNotizTypDto notizTyp;
  private @Valid UUID id;
  private @Valid String betreff;
  private @Valid String text;
  private @Valid String userMutiert;
  private @Valid LocalDate timestampMutiert;
  private @Valid String antwort;

  /**
   **/
  public GesuchNotizDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }


  @JsonProperty("userErstellt")
  @NotNull
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public GesuchNotizDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }


  @JsonProperty("timestampErstellt")
  @NotNull
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public GesuchNotizDto notizTyp(GesuchNotizTypDto notizTyp) {
    this.notizTyp = notizTyp;
    return this;
  }


  @JsonProperty("notizTyp")
  @NotNull
  public GesuchNotizTypDto getNotizTyp() {
    return notizTyp;
  }

  @JsonProperty("notizTyp")
  public void setNotizTyp(GesuchNotizTypDto notizTyp) {
    this.notizTyp = notizTyp;
  }

  /**
   **/
  public GesuchNotizDto id(UUID id) {
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
  public LocalDate getTimestampMutiert() {
    return timestampMutiert;
  }

  @JsonProperty("timestampMutiert")
  public void setTimestampMutiert(LocalDate timestampMutiert) {
    this.timestampMutiert = timestampMutiert;
  }

  /**
   **/
  public GesuchNotizDto antwort(String antwort) {
    this.antwort = antwort;
    return this;
  }


  @JsonProperty("antwort")
  public String getAntwort() {
    return antwort;
  }

  @JsonProperty("antwort")
  public void setAntwort(String antwort) {
    this.antwort = antwort;
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
    return Objects.equals(this.userErstellt, gesuchNotiz.userErstellt) &&
        Objects.equals(this.timestampErstellt, gesuchNotiz.timestampErstellt) &&
        Objects.equals(this.notizTyp, gesuchNotiz.notizTyp) &&
        Objects.equals(this.id, gesuchNotiz.id) &&
        Objects.equals(this.betreff, gesuchNotiz.betreff) &&
        Objects.equals(this.text, gesuchNotiz.text) &&
        Objects.equals(this.userMutiert, gesuchNotiz.userMutiert) &&
        Objects.equals(this.timestampMutiert, gesuchNotiz.timestampMutiert) &&
        Objects.equals(this.antwort, gesuchNotiz.antwort);
  }

  @Override
  public int hashCode() {
    return Objects.hash(userErstellt, timestampErstellt, notizTyp, id, betreff, text, userMutiert, timestampMutiert, antwort);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchNotizDto {\n");

    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    notizTyp: ").append(toIndentedString(notizTyp)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    userMutiert: ").append(toIndentedString(userMutiert)).append("\n");
    sb.append("    timestampMutiert: ").append(toIndentedString(timestampMutiert)).append("\n");
    sb.append("    antwort: ").append(toIndentedString(antwort)).append("\n");
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

