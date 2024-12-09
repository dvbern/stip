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



@JsonTypeName("GesuchNotizCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchNotizCreateDto  implements Serializable {
  private @Valid UUID gesuchId;
  private @Valid String betreff;
  private @Valid String text;
  private @Valid ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp;

  /**
   **/
  public GesuchNotizCreateDto gesuchId(UUID gesuchId) {
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
  public GesuchNotizCreateDto betreff(String betreff) {
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
  public GesuchNotizCreateDto text(String text) {
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
  public GesuchNotizCreateDto notizTyp(ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp) {
    this.notizTyp = notizTyp;
    return this;
  }

  
  @JsonProperty("notizTyp")
  @NotNull
  public ch.dvbern.stip.api.notiz.type.GesuchNotizTyp getNotizTyp() {
    return notizTyp;
  }

  @JsonProperty("notizTyp")
  public void setNotizTyp(ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp) {
    this.notizTyp = notizTyp;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchNotizCreateDto gesuchNotizCreate = (GesuchNotizCreateDto) o;
    return Objects.equals(this.gesuchId, gesuchNotizCreate.gesuchId) &&
        Objects.equals(this.betreff, gesuchNotizCreate.betreff) &&
        Objects.equals(this.text, gesuchNotizCreate.text) &&
        Objects.equals(this.notizTyp, gesuchNotizCreate.notizTyp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchId, betreff, text, notizTyp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchNotizCreateDto {\n");
    
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    notizTyp: ").append(toIndentedString(notizTyp)).append("\n");
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

