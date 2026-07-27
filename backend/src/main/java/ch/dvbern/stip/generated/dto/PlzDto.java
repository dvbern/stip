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

/**
 * PLZ und ort für Type ahead funktionalitaet
 **/

@JsonTypeName("plz")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PlzDto  implements Serializable {
  private @Valid String plz;
  private @Valid String ort;
  private @Valid String kantonskuerzel;

  protected PlzDto(PlzDtoBuilder<?, ?> b) {
    this.plz = b.plz;
    this.ort = b.ort;
    this.kantonskuerzel = b.kantonskuerzel;
  }

  public PlzDto() {
  }

  /**
   **/
  public PlzDto plz(String plz) {
    this.plz = plz;
    return this;
  }

  
  @JsonProperty("plz")
  @NotNull
  public String getPlz() {
    return plz;
  }

  @JsonProperty("plz")
  public void setPlz(String plz) {
    this.plz = plz;
  }

  /**
   **/
  public PlzDto ort(String ort) {
    this.ort = ort;
    return this;
  }

  
  @JsonProperty("ort")
  @NotNull
  public String getOrt() {
    return ort;
  }

  @JsonProperty("ort")
  public void setOrt(String ort) {
    this.ort = ort;
  }

  /**
   **/
  public PlzDto kantonskuerzel(String kantonskuerzel) {
    this.kantonskuerzel = kantonskuerzel;
    return this;
  }

  
  @JsonProperty("kantonskuerzel")
  @NotNull
  public String getKantonskuerzel() {
    return kantonskuerzel;
  }

  @JsonProperty("kantonskuerzel")
  public void setKantonskuerzel(String kantonskuerzel) {
    this.kantonskuerzel = kantonskuerzel;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PlzDto plz = (PlzDto) o;
    return Objects.equals(this.plz, plz.plz) &&
        Objects.equals(this.ort, plz.ort) &&
        Objects.equals(this.kantonskuerzel, plz.kantonskuerzel);
  }

  @Override
  public int hashCode() {
    return Objects.hash(plz, ort, kantonskuerzel);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PlzDto {\n");
    
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    kantonskuerzel: ").append(toIndentedString(kantonskuerzel)).append("\n");
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


  public static PlzDtoBuilder<?, ?> builder() {
    return new PlzDtoBuilderImpl();
  }

  private static final class PlzDtoBuilderImpl extends PlzDtoBuilder<PlzDto, PlzDtoBuilderImpl> {

    @Override
    protected PlzDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PlzDto build() {
      return new PlzDto(this);
    }
  }

  public static abstract class PlzDtoBuilder<C extends PlzDto, B extends PlzDtoBuilder<C, B>>  {
    private String plz;
    private String ort;
    private String kantonskuerzel;
    protected abstract B self();

    public abstract C build();

    public B plz(String plz) {
      this.plz = plz;
      return self();
    }
    public B ort(String ort) {
      this.ort = ort;
      return self();
    }
    public B kantonskuerzel(String kantonskuerzel) {
      this.kantonskuerzel = kantonskuerzel;
      return self();
    }
  }
}

