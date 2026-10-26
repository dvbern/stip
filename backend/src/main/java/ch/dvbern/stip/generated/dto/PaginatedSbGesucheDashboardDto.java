package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.SachbearbeiterForFilterDto;
import ch.dvbern.stip.generated.dto.SbDashboardGesuchDto;
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



@JsonTypeName("PaginatedSbGesucheDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class PaginatedSbGesucheDashboardDto  implements Serializable {
  private Integer page;
  private Integer pageSize;
  private Integer totalEntries;
  private @Valid List<@Valid SbDashboardGesuchDto> entries = new ArrayList<>();
  private @Valid List<@Valid SachbearbeiterForFilterDto> sachbearbeiters = new ArrayList<>();

  protected PaginatedSbGesucheDashboardDto(PaginatedSbGesucheDashboardDtoBuilder<?, ?> b) {
    this.page = b.page;
    this.pageSize = b.pageSize;
    this.totalEntries = b.totalEntries;
    this.entries = b.entries;
    this.sachbearbeiters = b.sachbearbeiters;
  }

  public PaginatedSbGesucheDashboardDto() {
  }

  /**
   **/
  public PaginatedSbGesucheDashboardDto page(Integer page) {
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
  public PaginatedSbGesucheDashboardDto pageSize(Integer pageSize) {
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
  public PaginatedSbGesucheDashboardDto totalEntries(Integer totalEntries) {
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
  public PaginatedSbGesucheDashboardDto entries(List<@Valid SbDashboardGesuchDto> entries) {
    this.entries = entries;
    return this;
  }

  
  @JsonProperty("entries")
  @Valid public List<@Valid SbDashboardGesuchDto> getEntries() {
    return entries;
  }

  @JsonProperty("entries")
  public void setEntries(List<@Valid SbDashboardGesuchDto> entries) {
    this.entries = entries;
  }

  public PaginatedSbGesucheDashboardDto addEntriesItem(SbDashboardGesuchDto entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }

    this.entries.add(entriesItem);
    return this;
  }

  public PaginatedSbGesucheDashboardDto removeEntriesItem(SbDashboardGesuchDto entriesItem) {
    if (entriesItem != null && this.entries != null) {
      this.entries.remove(entriesItem);
    }

    return this;
  }
  /**
   **/
  public PaginatedSbGesucheDashboardDto sachbearbeiters(List<@Valid SachbearbeiterForFilterDto> sachbearbeiters) {
    this.sachbearbeiters = sachbearbeiters;
    return this;
  }

  
  @JsonProperty("sachbearbeiters")
  @Valid public List<@Valid SachbearbeiterForFilterDto> getSachbearbeiters() {
    return sachbearbeiters;
  }

  @JsonProperty("sachbearbeiters")
  public void setSachbearbeiters(List<@Valid SachbearbeiterForFilterDto> sachbearbeiters) {
    this.sachbearbeiters = sachbearbeiters;
  }

  public PaginatedSbGesucheDashboardDto addSachbearbeitersItem(SachbearbeiterForFilterDto sachbearbeitersItem) {
    if (this.sachbearbeiters == null) {
      this.sachbearbeiters = new ArrayList<>();
    }

    this.sachbearbeiters.add(sachbearbeitersItem);
    return this;
  }

  public PaginatedSbGesucheDashboardDto removeSachbearbeitersItem(SachbearbeiterForFilterDto sachbearbeitersItem) {
    if (sachbearbeitersItem != null && this.sachbearbeiters != null) {
      this.sachbearbeiters.remove(sachbearbeitersItem);
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
    PaginatedSbGesucheDashboardDto paginatedSbGesucheDashboard = (PaginatedSbGesucheDashboardDto) o;
    return Objects.equals(this.page, paginatedSbGesucheDashboard.page) &&
        Objects.equals(this.pageSize, paginatedSbGesucheDashboard.pageSize) &&
        Objects.equals(this.totalEntries, paginatedSbGesucheDashboard.totalEntries) &&
        Objects.equals(this.entries, paginatedSbGesucheDashboard.entries) &&
        Objects.equals(this.sachbearbeiters, paginatedSbGesucheDashboard.sachbearbeiters);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, totalEntries, entries, sachbearbeiters);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedSbGesucheDashboardDto {\n");
    
    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalEntries: ").append(toIndentedString(totalEntries)).append("\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
    sb.append("    sachbearbeiters: ").append(toIndentedString(sachbearbeiters)).append("\n");
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


  public static PaginatedSbGesucheDashboardDtoBuilder<?, ?> builder() {
    return new PaginatedSbGesucheDashboardDtoBuilderImpl();
  }

  private static final class PaginatedSbGesucheDashboardDtoBuilderImpl extends PaginatedSbGesucheDashboardDtoBuilder<PaginatedSbGesucheDashboardDto, PaginatedSbGesucheDashboardDtoBuilderImpl> {

    @Override
    protected PaginatedSbGesucheDashboardDtoBuilderImpl self() {
      return this;
    }

    @Override
    public PaginatedSbGesucheDashboardDto build() {
      return new PaginatedSbGesucheDashboardDto(this);
    }
  }

  public static abstract class PaginatedSbGesucheDashboardDtoBuilder<C extends PaginatedSbGesucheDashboardDto, B extends PaginatedSbGesucheDashboardDtoBuilder<C, B>>  {
    private Integer page;
    private Integer pageSize;
    private Integer totalEntries;
    private List<SbDashboardGesuchDto> entries = new ArrayList<>();
    private List<SachbearbeiterForFilterDto> sachbearbeiters = new ArrayList<>();
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
    public B entries(List<SbDashboardGesuchDto> entries) {
      this.entries = entries;
      return self();
    }
    public B sachbearbeiters(List<SachbearbeiterForFilterDto> sachbearbeiters) {
      this.sachbearbeiters = sachbearbeiters;
      return self();
    }
  }
}
