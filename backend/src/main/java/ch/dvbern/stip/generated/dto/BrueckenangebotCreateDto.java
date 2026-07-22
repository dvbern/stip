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



@JsonTypeName("BrueckenangebotCreate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BrueckenangebotCreateDto  implements Serializable {
  private @Valid ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;

  protected BrueckenangebotCreateDto(BrueckenangebotCreateDtoBuilder<?, ?> b) {
    this.bildungsrichtung = b.bildungsrichtung;
    this.bezeichnungDe = b.bezeichnungDe;
    this.bezeichnungFr = b.bezeichnungFr;
  }

  public BrueckenangebotCreateDto() {
  }

  /**
   **/
  public BrueckenangebotCreateDto bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
    return this;
  }

  
  @JsonProperty("bildungsrichtung")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung getBildungsrichtung() {
    return bildungsrichtung;
  }

  @JsonProperty("bildungsrichtung")
  public void setBildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
  }

  /**
   **/
  public BrueckenangebotCreateDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public BrueckenangebotCreateDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BrueckenangebotCreateDto brueckenangebotCreate = (BrueckenangebotCreateDto) o;
    return Objects.equals(this.bildungsrichtung, brueckenangebotCreate.bildungsrichtung) &&
        Objects.equals(this.bezeichnungDe, brueckenangebotCreate.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, brueckenangebotCreate.bezeichnungFr);
  }

  @Override
  public int hashCode() {
    return Objects.hash(bildungsrichtung, bezeichnungDe, bezeichnungFr);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BrueckenangebotCreateDto {\n");
    
    sb.append("    bildungsrichtung: ").append(toIndentedString(bildungsrichtung)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
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


  public static BrueckenangebotCreateDtoBuilder<?, ?> builder() {
    return new BrueckenangebotCreateDtoBuilderImpl();
  }

  private static final class BrueckenangebotCreateDtoBuilderImpl extends BrueckenangebotCreateDtoBuilder<BrueckenangebotCreateDto, BrueckenangebotCreateDtoBuilderImpl> {

    @Override
    protected BrueckenangebotCreateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BrueckenangebotCreateDto build() {
      return new BrueckenangebotCreateDto(this);
    }
  }

  public static abstract class BrueckenangebotCreateDtoBuilder<C extends BrueckenangebotCreateDto, B extends BrueckenangebotCreateDtoBuilder<C, B>>  {
    private ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;
    private String bezeichnungDe;
    private String bezeichnungFr;
    protected abstract B self();

    public abstract C build();

    public B bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
      this.bildungsrichtung = bildungsrichtung;
      return self();
    }
    public B bezeichnungDe(String bezeichnungDe) {
      this.bezeichnungDe = bezeichnungDe;
      return self();
    }
    public B bezeichnungFr(String bezeichnungFr) {
      this.bezeichnungFr = bezeichnungFr;
      return self();
    }
  }
}

