package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.BuchhaltungEntryDto;
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



@JsonTypeName("BuchhaltungOverview")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class BuchhaltungOverviewDto  implements Serializable {
  private @Valid Boolean canRetryAuszahlung;
  private @Valid List<BuchhaltungEntryDto> buchhaltungEntrys = new ArrayList<>();

  protected BuchhaltungOverviewDto(BuchhaltungOverviewDtoBuilder<?, ?> b) {
    this.canRetryAuszahlung = b.canRetryAuszahlung;
    this.buchhaltungEntrys = b.buchhaltungEntrys;
  }

  public BuchhaltungOverviewDto() {
  }

  /**
   **/
  public BuchhaltungOverviewDto canRetryAuszahlung(Boolean canRetryAuszahlung) {
    this.canRetryAuszahlung = canRetryAuszahlung;
    return this;
  }

  
  @JsonProperty("canRetryAuszahlung")
  @NotNull
  public Boolean getCanRetryAuszahlung() {
    return canRetryAuszahlung;
  }

  @JsonProperty("canRetryAuszahlung")
  public void setCanRetryAuszahlung(Boolean canRetryAuszahlung) {
    this.canRetryAuszahlung = canRetryAuszahlung;
  }

  /**
   **/
  public BuchhaltungOverviewDto buchhaltungEntrys(List<BuchhaltungEntryDto> buchhaltungEntrys) {
    this.buchhaltungEntrys = buchhaltungEntrys;
    return this;
  }

  
  @JsonProperty("buchhaltungEntrys")
  @NotNull
  public List<BuchhaltungEntryDto> getBuchhaltungEntrys() {
    return buchhaltungEntrys;
  }

  @JsonProperty("buchhaltungEntrys")
  public void setBuchhaltungEntrys(List<BuchhaltungEntryDto> buchhaltungEntrys) {
    this.buchhaltungEntrys = buchhaltungEntrys;
  }

  public BuchhaltungOverviewDto addBuchhaltungEntrysItem(BuchhaltungEntryDto buchhaltungEntrysItem) {
    if (this.buchhaltungEntrys == null) {
      this.buchhaltungEntrys = new ArrayList<>();
    }

    this.buchhaltungEntrys.add(buchhaltungEntrysItem);
    return this;
  }

  public BuchhaltungOverviewDto removeBuchhaltungEntrysItem(BuchhaltungEntryDto buchhaltungEntrysItem) {
    if (buchhaltungEntrysItem != null && this.buchhaltungEntrys != null) {
      this.buchhaltungEntrys.remove(buchhaltungEntrysItem);
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
    BuchhaltungOverviewDto buchhaltungOverview = (BuchhaltungOverviewDto) o;
    return Objects.equals(this.canRetryAuszahlung, buchhaltungOverview.canRetryAuszahlung) &&
        Objects.equals(this.buchhaltungEntrys, buchhaltungOverview.buchhaltungEntrys);
  }

  @Override
  public int hashCode() {
    return Objects.hash(canRetryAuszahlung, buchhaltungEntrys);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuchhaltungOverviewDto {\n");
    
    sb.append("    canRetryAuszahlung: ").append(toIndentedString(canRetryAuszahlung)).append("\n");
    sb.append("    buchhaltungEntrys: ").append(toIndentedString(buchhaltungEntrys)).append("\n");
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


  public static BuchhaltungOverviewDtoBuilder<?, ?> builder() {
    return new BuchhaltungOverviewDtoBuilderImpl();
  }

  private static final class BuchhaltungOverviewDtoBuilderImpl extends BuchhaltungOverviewDtoBuilder<BuchhaltungOverviewDto, BuchhaltungOverviewDtoBuilderImpl> {

    @Override
    protected BuchhaltungOverviewDtoBuilderImpl self() {
      return this;
    }

    @Override
    public BuchhaltungOverviewDto build() {
      return new BuchhaltungOverviewDto(this);
    }
  }

  public static abstract class BuchhaltungOverviewDtoBuilder<C extends BuchhaltungOverviewDto, B extends BuchhaltungOverviewDtoBuilder<C, B>>  {
    private Boolean canRetryAuszahlung;
    private List<BuchhaltungEntryDto> buchhaltungEntrys = new ArrayList<>();
    protected abstract B self();

    public abstract C build();

    public B canRetryAuszahlung(Boolean canRetryAuszahlung) {
      this.canRetryAuszahlung = canRetryAuszahlung;
      return self();
    }
    public B buchhaltungEntrys(List<BuchhaltungEntryDto> buchhaltungEntrys) {
      this.buchhaltungEntrys = buchhaltungEntrys;
      return self();
    }
  }
}

