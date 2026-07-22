package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungsstaetteDto;
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



@JsonTypeName("PaginatedAusbildungsstaette")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PaginatedAusbildungsstaetteDto  implements Serializable {
  private @Valid Integer page;
  private @Valid Integer pageSize;
  private @Valid Integer totalEntries;
  private @Valid List<AusbildungsstaetteDto> entries;

  protected PaginatedAusbildungsstaetteDto(PaginatedAusbildungsstaetteDtoBuilder<?, ?> b) {
    this.page = b.page;
    this.pageSize = b.pageSize;
    this.totalEntries = b.totalEntries;
    this.entries = b.entries;
  }

  public PaginatedAusbildungsstaetteDto() {
  }

  /**
   **/
  public PaginatedAusbildungsstaetteDto page(Integer page) {
    this.page = page;
    return this;
  }

  
  @JsonProperty("page")
  public Integer getPage() {
    return page;
  }

  @JsonProperty("page")
  public void setPage(Integer page) {
    this.page = page;
  }

  /**
   **/
  public PaginatedAusbildungsstaetteDto pageSize(Integer pageSize) {
    this.pageSize = pageSize;
    return this;
  }

  
  @JsonProperty("pageSize")
  public Integer getPageSize() {
    return pageSize;
  }

  @JsonProperty("pageSize")
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }

  /**
   **/
  public PaginatedAusbildungsstaetteDto totalEntries(Integer totalEntries) {
    this.totalEntries = totalEntries;
    return this;
  }

  
  @JsonProperty("totalEntries")
  public Integer getTotalEntries() {
    return totalEntries;
  }

  @JsonProperty("totalEntries")
  public void setTotalEntries(Integer totalEntries) {
    this.totalEntries = totalEntries;
  }

  /**
   **/
  public PaginatedAusbildungsstaetteDto entries(List<AusbildungsstaetteDto> entries) {
    this.entries = entries;
    return this;
  }

  
  @JsonProperty("entries")
  public List<AusbildungsstaetteDto> getEntries() {
    return entries;
  }

  @JsonProperty("entries")
  public void setEntries(List<AusbildungsstaetteDto> entries) {
    this.entries = entries;
  }

  public PaginatedAusbildungsstaetteDto addEntriesItem(AusbildungsstaetteDto entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }

    this.entries.add(entriesItem);
    return this;
  }

  public PaginatedAusbildungsstaetteDto removeEntriesItem(AusbildungsstaetteDto entriesItem) {
    if (entriesItem != null && this.entries != null) {
      this.entries.remove(entriesItem);
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
    PaginatedAusbildungsstaetteDto paginatedAusbildungsstaette = (PaginatedAusbildungsstaetteDto) o;
    return Objects.equals(this.page, paginatedAusbildungsstaette.page) &&
        Objects.equals(this.pageSize, paginatedAusbildungsstaette.pageSize) &&
        Objects.equals(this.totalEntries, paginatedAusbildungsstaette.totalEntries) &&
        Objects.equals(this.entries, paginatedAusbildungsstaette.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, totalEntries, entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedAusbildungsstaetteDto {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalEntries: ").append(toIndentedString(totalEntries)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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


  public static PaginatedAusbildungsstaetteDtoBuilder<?, ?> builder() {
    return new PaginatedAusbildungsstaetteDtoBuilderImpl();
  }

  private static final class PaginatedAusbildungsstaetteDtoBuilderImpl extends PaginatedAusbildungsstaetteDtoBuilder<PaginatedAusbildungsstaetteDto, PaginatedAusbildungsstaetteDtoBuilderImpl> {

    @Override
    protected PaginatedAusbildungsstaetteDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PaginatedAusbildungsstaetteDto build() {
      return new PaginatedAusbildungsstaetteDto(this);
    }
  }

  public static abstract class PaginatedAusbildungsstaetteDtoBuilder<C extends PaginatedAusbildungsstaetteDto, B extends PaginatedAusbildungsstaetteDtoBuilder<C, B>>  {
    private Integer page;
    private Integer pageSize;
    private Integer totalEntries;
    private List<AusbildungsstaetteDto> entries;
    protected abstract B self();

    public abstract C build();

    public B page(Integer page) {
      this.page = page;
      return self();
    }
    public B pageSize(Integer pageSize) {
      this.pageSize = pageSize;
      return self();
    }
    public B totalEntries(Integer totalEntries) {
      this.totalEntries = totalEntries;
      return self();
    }
    public B entries(List<AusbildungsstaetteDto> entries) {
      this.entries = entries;
      return self();
    }
  }
}

