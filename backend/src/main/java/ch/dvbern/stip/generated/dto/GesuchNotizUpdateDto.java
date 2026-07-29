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



@JsonTypeName("GesuchNotizUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchNotizUpdateDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String betreff;
  private @Valid String text;
  private @Valid Boolean pendenzAbgeschlossen;

  protected GesuchNotizUpdateDto(GesuchNotizUpdateDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.betreff = b.betreff;
    this.text = b.text;
    this.pendenzAbgeschlossen = b.pendenzAbgeschlossen;
  }

  public GesuchNotizUpdateDto() {
  }

  /**
   **/
  public GesuchNotizUpdateDto id(UUID id) {
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
  public GesuchNotizUpdateDto pendenzAbgeschlossen(Boolean pendenzAbgeschlossen) {
    this.pendenzAbgeschlossen = pendenzAbgeschlossen;
    return this;
  }

  
  @JsonProperty("pendenzAbgeschlossen")
  public Boolean getPendenzAbgeschlossen() {
    return pendenzAbgeschlossen;
  }

  @JsonProperty("pendenzAbgeschlossen")
  public void setPendenzAbgeschlossen(Boolean pendenzAbgeschlossen) {
    this.pendenzAbgeschlossen = pendenzAbgeschlossen;
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
    return Objects.equals(this.id, gesuchNotizUpdate.id) &&
        Objects.equals(this.betreff, gesuchNotizUpdate.betreff) &&
        Objects.equals(this.text, gesuchNotizUpdate.text) &&
        Objects.equals(this.pendenzAbgeschlossen, gesuchNotizUpdate.pendenzAbgeschlossen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, betreff, text, pendenzAbgeschlossen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchNotizUpdateDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    betreff: ").append(toIndentedString(betreff)).append("\n");
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
    sb.append("    pendenzAbgeschlossen: ").append(toIndentedString(pendenzAbgeschlossen)).append("\n");
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


  public static GesuchNotizUpdateDtoBuilder<?, ?> builder() {
    return new GesuchNotizUpdateDtoBuilderImpl();
  }

  private static final class GesuchNotizUpdateDtoBuilderImpl extends GesuchNotizUpdateDtoBuilder<GesuchNotizUpdateDto, GesuchNotizUpdateDtoBuilderImpl> {

    @Override
    protected GesuchNotizUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchNotizUpdateDto build() {
      return new GesuchNotizUpdateDto(this);
    }
  }

  public static abstract class GesuchNotizUpdateDtoBuilder<C extends GesuchNotizUpdateDto, B extends GesuchNotizUpdateDtoBuilder<C, B>>  {
    private UUID id;
    private String betreff;
    private String text;
    private Boolean pendenzAbgeschlossen;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
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
    public B pendenzAbgeschlossen(Boolean pendenzAbgeschlossen) {
      this.pendenzAbgeschlossen = pendenzAbgeschlossen;
      return self();
    }
  }
}

