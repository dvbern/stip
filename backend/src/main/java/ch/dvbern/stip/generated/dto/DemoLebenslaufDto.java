package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoLebenslaufAusbildungDto;
import ch.dvbern.stip.generated.dto.DemoLebenslaufTaetigkeitDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoLebenslauf")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoLebenslaufDto  implements Serializable {
  private @Valid List<DemoLebenslaufAusbildungDto> ausbildung = new ArrayList<>();
  private @Valid List<DemoLebenslaufTaetigkeitDto> taetigkeiten = new ArrayList<>();

  protected DemoLebenslaufDto(DemoLebenslaufDtoBuilder<?, ?> b) {
    this.ausbildung = b.ausbildung;
    this.taetigkeiten = b.taetigkeiten;
  }

  public DemoLebenslaufDto() {
  }

  /**
   **/
  public DemoLebenslaufDto ausbildung(List<DemoLebenslaufAusbildungDto> ausbildung) {
    this.ausbildung = ausbildung;
    return this;
  }

  
  @JsonProperty("ausbildung")
  @NotNull
  public List<DemoLebenslaufAusbildungDto> getAusbildung() {
    return ausbildung;
  }

  @JsonProperty("ausbildung")
  public void setAusbildung(List<DemoLebenslaufAusbildungDto> ausbildung) {
    this.ausbildung = ausbildung;
  }

  public DemoLebenslaufDto addAusbildungItem(DemoLebenslaufAusbildungDto ausbildungItem) {
    if (this.ausbildung == null) {
      this.ausbildung = new ArrayList<>();
    }

    this.ausbildung.add(ausbildungItem);
    return this;
  }

  public DemoLebenslaufDto removeAusbildungItem(DemoLebenslaufAusbildungDto ausbildungItem) {
    if (ausbildungItem != null && this.ausbildung != null) {
      this.ausbildung.remove(ausbildungItem);
    }

    return this;
  }
  /**
   **/
  public DemoLebenslaufDto taetigkeiten(List<DemoLebenslaufTaetigkeitDto> taetigkeiten) {
    this.taetigkeiten = taetigkeiten;
    return this;
  }

  
  @JsonProperty("taetigkeiten")
  @NotNull
  public List<DemoLebenslaufTaetigkeitDto> getTaetigkeiten() {
    return taetigkeiten;
  }

  @JsonProperty("taetigkeiten")
  public void setTaetigkeiten(List<DemoLebenslaufTaetigkeitDto> taetigkeiten) {
    this.taetigkeiten = taetigkeiten;
  }

  public DemoLebenslaufDto addTaetigkeitenItem(DemoLebenslaufTaetigkeitDto taetigkeitenItem) {
    if (this.taetigkeiten == null) {
      this.taetigkeiten = new ArrayList<>();
    }

    this.taetigkeiten.add(taetigkeitenItem);
    return this;
  }

  public DemoLebenslaufDto removeTaetigkeitenItem(DemoLebenslaufTaetigkeitDto taetigkeitenItem) {
    if (taetigkeitenItem != null && this.taetigkeiten != null) {
      this.taetigkeiten.remove(taetigkeitenItem);
    }

    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoLebenslaufDto demoLebenslauf = (DemoLebenslaufDto) o;
    return Objects.equals(this.ausbildung, demoLebenslauf.ausbildung) &&
        Objects.equals(this.taetigkeiten, demoLebenslauf.taetigkeiten);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ausbildung, taetigkeiten);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoLebenslaufDto {\n");
    
    sb.append("    ausbildung: ").append(toIndentedString(ausbildung)).append("\n");
    sb.append("    taetigkeiten: ").append(toIndentedString(taetigkeiten)).append("\n");
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


  public static DemoLebenslaufDtoBuilder<?, ?> builder() {
    return new DemoLebenslaufDtoBuilderImpl();
  }

  private static final class DemoLebenslaufDtoBuilderImpl extends DemoLebenslaufDtoBuilder<DemoLebenslaufDto, DemoLebenslaufDtoBuilderImpl> {

    @Override
    protected DemoLebenslaufDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoLebenslaufDto build() {
      return new DemoLebenslaufDto(this);
    }
  }

  public static abstract class DemoLebenslaufDtoBuilder<C extends DemoLebenslaufDto, B extends DemoLebenslaufDtoBuilder<C, B>>  {
    private List<DemoLebenslaufAusbildungDto> ausbildung = new ArrayList<>();
    private List<DemoLebenslaufTaetigkeitDto> taetigkeiten = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B ausbildung(List<DemoLebenslaufAusbildungDto> ausbildung) {
      this.ausbildung = ausbildung;
      return self();
    }
    public B taetigkeiten(List<DemoLebenslaufTaetigkeitDto> taetigkeiten) {
      this.taetigkeiten = taetigkeiten;
      return self();
    }
  }
}

