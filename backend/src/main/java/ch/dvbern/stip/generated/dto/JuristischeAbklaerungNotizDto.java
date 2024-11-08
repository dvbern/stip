package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchNotizTypDto;
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



@JsonTypeName("JuristischeAbklaerungNotiz")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class JuristischeAbklaerungNotizDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String antwort;
  private @Valid UUID gesuchId;
  private @Valid String betreff;
  private @Valid String text;
  private @Valid GesuchNotizTypDto notizTyp;
  private @Valid String userErstellt;
  private @Valid LocalDate timestampErstellt;
  private @Valid String userMutiert;
  private @Valid LocalDate timestampMutiert;

  /**
   **/
  public JuristischeAbklaerungNotizDto id(UUID id) {
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
  public JuristischeAbklaerungNotizDto antwort(String antwort) {
    this.antwort = antwort;
    return this;
  }

  
  @JsonProperty("antwort")
  @NotNull
  public String getAntwort() {
    return antwort;
  }

  @JsonProperty("antwort")
  public void setAntwort(String antwort) {
    this.antwort = antwort;
  }

  /**
   **/
  public JuristischeAbklaerungNotizDto gesuchId(UUID gesuchId) {
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
  public JuristischeAbklaerungNotizDto betreff(String betreff) {
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
  public JuristischeAbklaerungNotizDto text(String text) {
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
  public JuristischeAbklaerungNotizDto notizTyp(GesuchNotizTypDto notizTyp) {
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
  public JuristischeAbklaerungNotizDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public JuristischeAbklaerungNotizDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public JuristischeAbklaerungNotizDto userMutiert(String userMutiert) {
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
  public JuristischeAbklaerungNotizDto timestampMutiert(LocalDate timestampMutiert) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    JuristischeAbklaerungNotizDto juristischeAbklaerungNotiz = (JuristischeAbklaerungNotizDto) o;
    return Objects.equals(this.id, juristischeAbklaerungNotiz.id) &&
        Objects.equals(this.antwort, juristischeAbklaerungNotiz.antwort) &&
        Objects.equals(this.gesuchId, juristischeAbklaerungNotiz.gesuchId) &&
        Objects.equals(this.betreff, juristischeAbklaerungNotiz.betreff) &&
        Objects.equals(this.text, juristischeAbklaerungNotiz.text) &&
        Objects.equals(this.notizTyp, juristischeAbklaerungNotiz.notizTyp) &&
        Objects.equals(this.userErstellt, juristischeAbklaerungNotiz.userErstellt) &&
        Objects.equals(this.timestampErstellt, juristischeAbklaerungNotiz.timestampErstellt) &&
        Objects.equals(this.userMutiert, juristischeAbklaerungNotiz.userMutiert) &&
        Objects.equals(this.timestampMutiert, juristischeAbklaerungNotiz.timestampMutiert);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, antwort, gesuchId, betreff, text, notizTyp, userErstellt, timestampErstellt, userMutiert, timestampMutiert);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class JuristischeAbklaerungNotizDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    antwort: ").append(toIndentedString(antwort)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    notizTyp: ").append(toIndentedString(notizTyp)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    userMutiert: ").append(toIndentedString(userMutiert)).append("\n");
    sb.append("    timestampMutiert: ").append(toIndentedString(timestampMutiert)).append("\n");
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

