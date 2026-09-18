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
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchNotizCreateDto  implements Serializable {
  private UUID gesuchId;
  private String betreff;
  private String text;
  private ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp;

  protected GesuchNotizCreateDto(GesuchNotizCreateDtoBuilder<?, ?> b) {
    this.gesuchId = b.gesuchId;
    this.betreff = b.betreff;
    this.text = b.text;
    this.notizTyp = b.notizTyp;
  }

  public GesuchNotizCreateDto() {
  }

  /**
   **/
  public GesuchNotizCreateDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchId")
  @NotNull public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty(required = true, value = "gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public GesuchNotizCreateDto betreff(String betreff) {
    this.betreff = betreff;
    return this;
  }

  
  @JsonProperty(required = true, value = "betreff")
  @NotNull public String getBetreff() {
    return betreff;
  }

  @JsonProperty(required = true, value = "betreff")
  public void setBetreff(String betreff) {
    this.betreff = betreff;
  }

  /**
   **/
  public GesuchNotizCreateDto text(String text) {
    this.text = text;
    return this;
  }

  
  @JsonProperty(required = true, value = "text")
  @NotNull public String getText() {
    return text;
  }

  @JsonProperty(required = true, value = "text")
  public void setText(String text) {
    this.text = text;
  }

  /**
   **/
  public GesuchNotizCreateDto notizTyp(ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp) {
    this.notizTyp = notizTyp;
    return this;
  }

  
  @JsonProperty(required = true, value = "notizTyp")
  @NotNull public ch.dvbern.stip.api.notiz.type.GesuchNotizTyp getNotizTyp() {
    return notizTyp;
  }

  @JsonProperty(required = true, value = "notizTyp")
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
    return o == null ? "null" : o.toString().replace("\n", "\n    ");
  }


  public static GesuchNotizCreateDtoBuilder<?, ?> builder() {
    return new GesuchNotizCreateDtoBuilderImpl();
  }

  private static final class GesuchNotizCreateDtoBuilderImpl extends GesuchNotizCreateDtoBuilder<GesuchNotizCreateDto, GesuchNotizCreateDtoBuilderImpl> {

    @Override
    protected GesuchNotizCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchNotizCreateDto build() {
      return new GesuchNotizCreateDto(this);
    }
  }

  public static abstract class GesuchNotizCreateDtoBuilder<C extends GesuchNotizCreateDto, B extends GesuchNotizCreateDtoBuilder<C, B>>  {
    private UUID gesuchId;
    private String betreff;
    private String text;
    private ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp;
    protected abstract B self();

    public abstract C build();

    public B gesuchId(UUID gesuchId) {
      this.gesuchId = gesuchId;
      return self();
    }
    public B betreff(String betreff) {
      this.betreff = betreff;
      return self();
    }
    public B text(String text) {
      this.text = text;
      return self();
    }
    public B notizTyp(ch.dvbern.stip.api.notiz.type.GesuchNotizTyp notizTyp) {
      this.notizTyp = notizTyp;
      return self();
    }
  }
}
