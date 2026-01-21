package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DarlehenBuchhaltungEntryDto;
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



@JsonTypeName("DarlehenBuchhaltungOverview")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class DarlehenBuchhaltungOverviewDto  implements Serializable {
  private @Valid Integer total;
  private @Valid Integer totalFreiwillig;
  private @Valid Integer totalGesetzlich;
  private @Valid List<DarlehenBuchhaltungEntryDto> darlehenBuchhaltungEntrys = new ArrayList<>();

  /**
   **/
  public DarlehenBuchhaltungOverviewDto total(Integer total) {
    this.total = total;
    return this;
  }

  
  @JsonProperty("total")
  @NotNull
  public Integer getTotal() {
    return total;
  }

  @JsonProperty("total")
  public void setTotal(Integer total) {
    this.total = total;
  }

  /**
   **/
  public DarlehenBuchhaltungOverviewDto totalFreiwillig(Integer totalFreiwillig) {
    this.totalFreiwillig = totalFreiwillig;
    return this;
  }

  
  @JsonProperty("totalFreiwillig")
  @NotNull
  public Integer getTotalFreiwillig() {
    return totalFreiwillig;
  }

  @JsonProperty("totalFreiwillig")
  public void setTotalFreiwillig(Integer totalFreiwillig) {
    this.totalFreiwillig = totalFreiwillig;
  }

  /**
   **/
  public DarlehenBuchhaltungOverviewDto totalGesetzlich(Integer totalGesetzlich) {
    this.totalGesetzlich = totalGesetzlich;
    return this;
  }

  
  @JsonProperty("totalGesetzlich")
  @NotNull
  public Integer getTotalGesetzlich() {
    return totalGesetzlich;
  }

  @JsonProperty("totalGesetzlich")
  public void setTotalGesetzlich(Integer totalGesetzlich) {
    this.totalGesetzlich = totalGesetzlich;
  }

  /**
   **/
  public DarlehenBuchhaltungOverviewDto darlehenBuchhaltungEntrys(List<DarlehenBuchhaltungEntryDto> darlehenBuchhaltungEntrys) {
    this.darlehenBuchhaltungEntrys = darlehenBuchhaltungEntrys;
    return this;
  }

  
  @JsonProperty("darlehenBuchhaltungEntrys")
  @NotNull
  public List<DarlehenBuchhaltungEntryDto> getDarlehenBuchhaltungEntrys() {
    return darlehenBuchhaltungEntrys;
  }

  @JsonProperty("darlehenBuchhaltungEntrys")
  public void setDarlehenBuchhaltungEntrys(List<DarlehenBuchhaltungEntryDto> darlehenBuchhaltungEntrys) {
    this.darlehenBuchhaltungEntrys = darlehenBuchhaltungEntrys;
  }

  public DarlehenBuchhaltungOverviewDto addDarlehenBuchhaltungEntrysItem(DarlehenBuchhaltungEntryDto darlehenBuchhaltungEntrysItem) {
    if (this.darlehenBuchhaltungEntrys == null) {
      this.darlehenBuchhaltungEntrys = new ArrayList<>();
    }

    this.darlehenBuchhaltungEntrys.add(darlehenBuchhaltungEntrysItem);
    return this;
  }

  public DarlehenBuchhaltungOverviewDto removeDarlehenBuchhaltungEntrysItem(DarlehenBuchhaltungEntryDto darlehenBuchhaltungEntrysItem) {
    if (darlehenBuchhaltungEntrysItem != null && this.darlehenBuchhaltungEntrys != null) {
      this.darlehenBuchhaltungEntrys.remove(darlehenBuchhaltungEntrysItem);
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
    DarlehenBuchhaltungOverviewDto darlehenBuchhaltungOverview = (DarlehenBuchhaltungOverviewDto) o;
    return Objects.equals(this.total, darlehenBuchhaltungOverview.total) &&
        Objects.equals(this.totalFreiwillig, darlehenBuchhaltungOverview.totalFreiwillig) &&
        Objects.equals(this.totalGesetzlich, darlehenBuchhaltungOverview.totalGesetzlich) &&
        Objects.equals(this.darlehenBuchhaltungEntrys, darlehenBuchhaltungOverview.darlehenBuchhaltungEntrys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(total, totalFreiwillig, totalGesetzlich, darlehenBuchhaltungEntrys);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenBuchhaltungOverviewDto {\n");
    
    sb.append("    total: ").append(toIndentedString(total)).append("\n");
    sb.append("    totalFreiwillig: ").append(toIndentedString(totalFreiwillig)).append("\n");
    sb.append("    totalGesetzlich: ").append(toIndentedString(totalGesetzlich)).append("\n");
    sb.append("    darlehenBuchhaltungEntrys: ").append(toIndentedString(darlehenBuchhaltungEntrys)).append("\n");
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

