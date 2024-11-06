package ch.dvbern.stip.generated.dto;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import jakarta.validation.Valid;



@JsonTypeName("PaginatedResult")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class PaginatedResultDto  implements Serializable {
  private @Valid Integer page;
  private @Valid Integer pageSize;
  private @Valid Integer totalEntries;

  /**
   **/
  public PaginatedResultDto page(Integer page) {
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
  public PaginatedResultDto pageSize(Integer pageSize) {
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
  public PaginatedResultDto totalEntries(Integer totalEntries) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PaginatedResultDto paginatedResult = (PaginatedResultDto) o;
    return Objects.equals(this.page, paginatedResult.page) &&
        Objects.equals(this.pageSize, paginatedResult.pageSize) &&
        Objects.equals(this.totalEntries, paginatedResult.totalEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(page, pageSize, totalEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedResultDto {\n");

    sb.append("    page: ").append(toIndentedString(page)).append("\n");
    sb.append("    pageSize: ").append(toIndentedString(pageSize)).append("\n");
    sb.append("    totalEntries: ").append(toIndentedString(totalEntries)).append("\n");
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

