package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchDokumentDto;
import ch.dvbern.stip.generated.dto.GesuchDokumentEntryDto;
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



@JsonTypeName("GesuchDokumentList")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchDokumentListDto  implements Serializable {
  private @Valid List<@Valid GesuchDokumentEntryDto> entrys = new ArrayList<>();
  private @Valid List<@Valid GesuchDokumentDto> dokuments = new ArrayList<>();

  protected GesuchDokumentListDto(GesuchDokumentListDtoBuilder<?, ?> b) {
    this.entrys = b.entrys;
    this.dokuments = b.dokuments;
  }

  public GesuchDokumentListDto() {
  }

  /**
   **/
  public GesuchDokumentListDto entrys(List<@Valid GesuchDokumentEntryDto> entrys) {
    this.entrys = entrys;
    return this;
  }

  
  @JsonProperty(required = true, value = "entrys")
  @NotNull @Valid public List<@Valid GesuchDokumentEntryDto> getEntrys() {
    return entrys;
  }

  @JsonProperty(required = true, value = "entrys")
  public void setEntrys(List<@Valid GesuchDokumentEntryDto> entrys) {
    this.entrys = entrys;
  }

  public GesuchDokumentListDto addEntrysItem(GesuchDokumentEntryDto entrysItem) {
    if (this.entrys == null) {
      this.entrys = new ArrayList<>();
    }

    this.entrys.add(entrysItem);
    return this;
  }

  public GesuchDokumentListDto removeEntrysItem(GesuchDokumentEntryDto entrysItem) {
    if (entrysItem != null && this.entrys != null) {
      this.entrys.remove(entrysItem);
    }

    return this;
  }
  /**
   **/
  public GesuchDokumentListDto dokuments(List<@Valid GesuchDokumentDto> dokuments) {
    this.dokuments = dokuments;
    return this;
  }

  
  @JsonProperty(required = true, value = "dokuments")
  @NotNull @Valid public List<@Valid GesuchDokumentDto> getDokuments() {
    return dokuments;
  }

  @JsonProperty(required = true, value = "dokuments")
  public void setDokuments(List<@Valid GesuchDokumentDto> dokuments) {
    this.dokuments = dokuments;
  }

  public GesuchDokumentListDto addDokumentsItem(GesuchDokumentDto dokumentsItem) {
    if (this.dokuments == null) {
      this.dokuments = new ArrayList<>();
    }

    this.dokuments.add(dokumentsItem);
    return this;
  }

  public GesuchDokumentListDto removeDokumentsItem(GesuchDokumentDto dokumentsItem) {
    if (dokumentsItem != null && this.dokuments != null) {
      this.dokuments.remove(dokumentsItem);
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
    GesuchDokumentListDto gesuchDokumentList = (GesuchDokumentListDto) o;
    return Objects.equals(this.entrys, gesuchDokumentList.entrys) &&
        Objects.equals(this.dokuments, gesuchDokumentList.dokuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entrys, dokuments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDokumentListDto {\n");
    
    sb.append("    entrys: ").append(toIndentedString(entrys)).append("\n");
    sb.append("    dokuments: ").append(toIndentedString(dokuments)).append("\n");
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


  public static GesuchDokumentListDtoBuilder<?, ?> builder() {
    return new GesuchDokumentListDtoBuilderImpl();
  }

  private static final class GesuchDokumentListDtoBuilderImpl extends GesuchDokumentListDtoBuilder<GesuchDokumentListDto, GesuchDokumentListDtoBuilderImpl> {

    @Override
    protected GesuchDokumentListDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchDokumentListDto build() {
      return new GesuchDokumentListDto(this);
    }
  }

  public static abstract class GesuchDokumentListDtoBuilder<C extends GesuchDokumentListDto, B extends GesuchDokumentListDtoBuilder<C, B>>  {
    private List<GesuchDokumentEntryDto> entrys = new ArrayList<>();
    private List<GesuchDokumentDto> dokuments = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B entrys(List<GesuchDokumentEntryDto> entrys) {
      this.entrys = entrys;
      return self();
    }
    public B dokuments(List<GesuchDokumentDto> dokuments) {
      this.dokuments = dokuments;
      return self();
    }
  }
}
