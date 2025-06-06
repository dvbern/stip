/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import ch.dvbern.stip.generated.dto.FallWithDelegierungDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * PaginatedSozDashboardDtoSpec
 */
@JsonPropertyOrder({
  PaginatedSozDashboardDtoSpec.JSON_PROPERTY_ENTRIES,
  PaginatedSozDashboardDtoSpec.JSON_PROPERTY_PAGE,
  PaginatedSozDashboardDtoSpec.JSON_PROPERTY_PAGE_SIZE,
  PaginatedSozDashboardDtoSpec.JSON_PROPERTY_TOTAL_ENTRIES
})
@JsonTypeName("PaginatedSozDashboard")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class PaginatedSozDashboardDtoSpec {
  public static final String JSON_PROPERTY_ENTRIES = "entries";
  private List<FallWithDelegierungDtoSpec> entries;

  public static final String JSON_PROPERTY_PAGE = "page";
  private Integer page;

  public static final String JSON_PROPERTY_PAGE_SIZE = "pageSize";
  private Integer pageSize;

  public static final String JSON_PROPERTY_TOTAL_ENTRIES = "totalEntries";
  private Integer totalEntries;

  public PaginatedSozDashboardDtoSpec() {
  }

  public PaginatedSozDashboardDtoSpec entries(List<FallWithDelegierungDtoSpec> entries) {
    
    this.entries = entries;
    return this;
  }

  public PaginatedSozDashboardDtoSpec addEntriesItem(FallWithDelegierungDtoSpec entriesItem) {
    if (this.entries == null) {
      this.entries = new ArrayList<>();
    }
    this.entries.add(entriesItem);
    return this;
  }

   /**
   * Get entries
   * @return entries
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public List<FallWithDelegierungDtoSpec> getEntries() {
    return entries;
  }


  @JsonProperty(JSON_PROPERTY_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setEntries(List<FallWithDelegierungDtoSpec> entries) {
    this.entries = entries;
  }


  public PaginatedSozDashboardDtoSpec page(Integer page) {
    
    this.page = page;
    return this;
  }

   /**
   * Get page
   * @return page
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getPage() {
    return page;
  }


  @JsonProperty(JSON_PROPERTY_PAGE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPage(Integer page) {
    this.page = page;
  }


  public PaginatedSozDashboardDtoSpec pageSize(Integer pageSize) {
    
    this.pageSize = pageSize;
    return this;
  }

   /**
   * Get pageSize
   * @return pageSize
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_PAGE_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getPageSize() {
    return pageSize;
  }


  @JsonProperty(JSON_PROPERTY_PAGE_SIZE)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setPageSize(Integer pageSize) {
    this.pageSize = pageSize;
  }


  public PaginatedSozDashboardDtoSpec totalEntries(Integer totalEntries) {
    
    this.totalEntries = totalEntries;
    return this;
  }

   /**
   * Get totalEntries
   * @return totalEntries
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_TOTAL_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public Integer getTotalEntries() {
    return totalEntries;
  }


  @JsonProperty(JSON_PROPERTY_TOTAL_ENTRIES)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
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
    PaginatedSozDashboardDtoSpec paginatedSozDashboard = (PaginatedSozDashboardDtoSpec) o;
    return Objects.equals(this.entries, paginatedSozDashboard.entries) &&
        Objects.equals(this.page, paginatedSozDashboard.page) &&
        Objects.equals(this.pageSize, paginatedSozDashboard.pageSize) &&
        Objects.equals(this.totalEntries, paginatedSozDashboard.totalEntries);
  }

  @Override
  public int hashCode() {
    return Objects.hash(entries, page, pageSize, totalEntries);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PaginatedSozDashboardDtoSpec {\n");
    sb.append("    entries: ").append(toIndentedString(entries)).append("\n");
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

