package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("BeschwerdeEntscheid")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BeschwerdeEntscheidDto  implements Serializable {
  private @Valid String kommentar;
  private @Valid Boolean beschwerdeErfolgreich;
  private @Valid DokumentDto dokument;

  protected BeschwerdeEntscheidDto(BeschwerdeEntscheidDtoBuilder<?, ?> b) {
    this.kommentar = b.kommentar;
    this.beschwerdeErfolgreich = b.beschwerdeErfolgreich;
    this.dokument = b.dokument;
  }

  public BeschwerdeEntscheidDto() {
  }

  /**
   **/
  public BeschwerdeEntscheidDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  @NotNull
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }

  /**
   **/
  public BeschwerdeEntscheidDto beschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
    return this;
  }

  
  @JsonProperty("beschwerdeErfolgreich")
  @NotNull
  public Boolean getBeschwerdeErfolgreich() {
    return beschwerdeErfolgreich;
  }

  @JsonProperty("beschwerdeErfolgreich")
  public void setBeschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
    this.beschwerdeErfolgreich = beschwerdeErfolgreich;
  }

  /**
   **/
  public BeschwerdeEntscheidDto dokument(DokumentDto dokument) {
    this.dokument = dokument;
    return this;
  }

  
  @JsonProperty("dokument")
  @NotNull
  public DokumentDto getDokument() {
    return dokument;
  }

  @JsonProperty("dokument")
  public void setDokument(DokumentDto dokument) {
    this.dokument = dokument;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BeschwerdeEntscheidDto beschwerdeEntscheid = (BeschwerdeEntscheidDto) o;
    return Objects.equals(this.kommentar, beschwerdeEntscheid.kommentar) &&
        Objects.equals(this.beschwerdeErfolgreich, beschwerdeEntscheid.beschwerdeErfolgreich) &&
        Objects.equals(this.dokument, beschwerdeEntscheid.dokument);
  }

  @Override
  public int hashCode() {
    return Objects.hash(kommentar, beschwerdeErfolgreich, dokument);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BeschwerdeEntscheidDto {\n");
    
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
    sb.append("    beschwerdeErfolgreich: ").append(toIndentedString(beschwerdeErfolgreich)).append("\n");
    sb.append("    dokument: ").append(toIndentedString(dokument)).append("\n");
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


  public static BeschwerdeEntscheidDtoBuilder<?, ?> builder() {
    return new BeschwerdeEntscheidDtoBuilderImpl();
  }

  private static final class BeschwerdeEntscheidDtoBuilderImpl extends BeschwerdeEntscheidDtoBuilder<BeschwerdeEntscheidDto, BeschwerdeEntscheidDtoBuilderImpl> {

    @Override
    protected BeschwerdeEntscheidDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BeschwerdeEntscheidDto build() {
      return new BeschwerdeEntscheidDto(this);
    }
  }

  public static abstract class BeschwerdeEntscheidDtoBuilder<C extends BeschwerdeEntscheidDto, B extends BeschwerdeEntscheidDtoBuilder<C, B>>  {
    private String kommentar;
    private Boolean beschwerdeErfolgreich;
    private DokumentDto dokument;
    protected abstract B self();

    public abstract C build();

    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
    public B beschwerdeErfolgreich(Boolean beschwerdeErfolgreich) {
      this.beschwerdeErfolgreich = beschwerdeErfolgreich;
      return self();
    }
    public B dokument(DokumentDto dokument) {
      this.dokument = dokument;
      return self();
    }
  }
}

