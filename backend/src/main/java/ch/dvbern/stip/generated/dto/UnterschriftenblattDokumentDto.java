package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("UnterschriftenblattDokument")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class UnterschriftenblattDokumentDto  implements Serializable {
  private @Valid UUID id;
  private @Valid ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp dokumentTyp;
  private @Valid List<DokumentDto> dokumente = new ArrayList<>();

  protected UnterschriftenblattDokumentDto(UnterschriftenblattDokumentDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.dokumentTyp = b.dokumentTyp;
    this.dokumente = b.dokumente;
  }

  public UnterschriftenblattDokumentDto() {
  }

  /**
   **/
  public UnterschriftenblattDokumentDto id(UUID id) {
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
  public UnterschriftenblattDokumentDto dokumentTyp(ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
    return this;
  }

  
  @JsonProperty("dokumentTyp")
  @NotNull
  public ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp getDokumentTyp() {
    return dokumentTyp;
  }

  @JsonProperty("dokumentTyp")
  public void setDokumentTyp(ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp dokumentTyp) {
    this.dokumentTyp = dokumentTyp;
  }

  /**
   **/
  public UnterschriftenblattDokumentDto dokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
    return this;
  }

  
  @JsonProperty("dokumente")
  @NotNull
  public List<DokumentDto> getDokumente() {
    return dokumente;
  }

  @JsonProperty("dokumente")
  public void setDokumente(List<DokumentDto> dokumente) {
    this.dokumente = dokumente;
  }

  public UnterschriftenblattDokumentDto addDokumenteItem(DokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public UnterschriftenblattDokumentDto removeDokumenteItem(DokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
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
    UnterschriftenblattDokumentDto unterschriftenblattDokument = (UnterschriftenblattDokumentDto) o;
    return Objects.equals(this.id, unterschriftenblattDokument.id) &&
        Objects.equals(this.dokumentTyp, unterschriftenblattDokument.dokumentTyp) &&
        Objects.equals(this.dokumente, unterschriftenblattDokument.dokumente);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokumentTyp, dokumente);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class UnterschriftenblattDokumentDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokumentTyp: ").append(toIndentedString(dokumentTyp)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
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


  public static UnterschriftenblattDokumentDtoBuilder<?, ?> builder() {
    return new UnterschriftenblattDokumentDtoBuilderImpl();
  }

  private static final class UnterschriftenblattDokumentDtoBuilderImpl extends UnterschriftenblattDokumentDtoBuilder<UnterschriftenblattDokumentDto, UnterschriftenblattDokumentDtoBuilderImpl> {

    @Override
    protected UnterschriftenblattDokumentDtoBuilderImpl self() {
      return this;
    }

    @Override
    public UnterschriftenblattDokumentDto build() {
      return new UnterschriftenblattDokumentDto(this);
    }
  }

  public static abstract class UnterschriftenblattDokumentDtoBuilder<C extends UnterschriftenblattDokumentDto, B extends UnterschriftenblattDokumentDtoBuilder<C, B>>  {
    private UUID id;
    private ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp dokumentTyp;
    private List<DokumentDto> dokumente = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B dokumentTyp(ch.dvbern.stip.api.unterschriftenblatt.type.UnterschriftenblattDokumentTyp dokumentTyp) {
      this.dokumentTyp = dokumentTyp;
      return self();
    }
    public B dokumente(List<DokumentDto> dokumente) {
      this.dokumente = dokumente;
      return self();
    }
  }
}

