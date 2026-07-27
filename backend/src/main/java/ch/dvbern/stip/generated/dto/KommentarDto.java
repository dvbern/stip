package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Kommentar")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class KommentarDto  implements Serializable {
  private @Valid String text;

  protected KommentarDto(KommentarDtoBuilder<?, ?> b) {
    this.text = b.text;
  }

  public KommentarDto() {
  }

  /**
   **/
  public KommentarDto text(String text) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    KommentarDto kommentar = (KommentarDto) o;
    return Objects.equals(this.text, kommentar.text);
  }

  @Override
  public int hashCode() {
    return Objects.hash(text);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class KommentarDto {\n");
    
    sb.append("    text: ").append(toIndentedString(text)).append("\n");
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


  public static KommentarDtoBuilder<?, ?> builder() {
    return new KommentarDtoBuilderImpl();
  }

  private static final class KommentarDtoBuilderImpl extends KommentarDtoBuilder<KommentarDto, KommentarDtoBuilderImpl> {

    @Override
    protected KommentarDtoBuilderImpl self() {
      return this;
    }

    @Override
    public KommentarDto build() {
      return new KommentarDto(this);
    }
  }

  public static abstract class KommentarDtoBuilder<C extends KommentarDto, B extends KommentarDtoBuilder<C, B>>  {
    private String text;
    protected abstract B self();

    public abstract C build();

    public B text(String text) {
      this.text = text;
      return self();
    }
  }
}

