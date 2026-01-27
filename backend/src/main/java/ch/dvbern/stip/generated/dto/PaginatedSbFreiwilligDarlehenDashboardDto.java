package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDashboardDto;
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



@JsonTypeName("PaginatedSbFreiwilligDarlehenDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PaginatedSbFreiwilligDarlehenDashboardDto  implements Serializable {
  private @Valid Integer page;
  private @Valid Integer pageSize;
  private @Valid Integer totalEntries;
  private @Valid List<FreiwilligDarlehenDashboardDto> entries;

  /**
   **/
  public PaginatedSbFreiwilligDarlehenDashboardDto page(Integer page) {
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
  public PaginatedSbFreiwilligDarlehenDashboardDto pageSize(Integer pageSize) {
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
  public PaginatedSbFreiwilligDarlehenDashboardDto totalEntries(Integer totalEntries) {
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
  public PaginatedSbFreiwilligDarlehenDashboardDto entries(List<FreiwilligDarlehenDashboardDto> entries) {
    this.entries = entries;
    return this;
  }

  
  @JsonProperty("entries")
  public List<FreiwilligDarlehenDashboardDto> getEntries() {
    return entries;
  }

  @JsonProperty("entries")
  public void setEntries(List<FreiwilligDarlehenDashboardDto> entries) {
    this.entries = entries;
  }

  public PaginatedSbFreiwilligDarlehenDashboardDto addEntriesItem(FreiwilligDarlehenDashboardDto entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }

    this.entries.add(entriesItem);
    return this;
  }

  public PaginatedSbFreiwilligDarlehenDashboardDto removeEntriesItem(FreiwilligDarlehenDashboardDto entriesItem) {
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
    PaginatedSbFreiwilligDarlehenDashboardDto paginatedSbFreiwilligDarlehenDashboard = (PaginatedSbFreiwilligDarlehenDashboardDto) o;
    return Objects.equals(this.page, paginatedSbFreiwilligDarlehenDashboard.page) &&
        Objects.equals(this.pageSize, paginatedSbFreiwilligDarlehenDashboard.pageSize) &&
        Objects.equals(this.totalEntries, paginatedSbFreiwilligDarlehenDashboard.totalEntries) &&
        Objects.equals(this.entries, paginatedSbFreiwilligDarlehenDashboard.entries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, totalEntries, entries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedSbFreiwilligDarlehenDashboardDto {\n");
    
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


}

