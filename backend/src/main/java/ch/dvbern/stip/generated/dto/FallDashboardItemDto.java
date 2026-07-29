package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.AusbildungDashboardItemDto;
import ch.dvbern.stip.generated.dto.DelegierungSlimDto;
import ch.dvbern.stip.generated.dto.FallDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
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



@JsonTypeName("FallDashboardItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FallDashboardItemDto  implements Serializable {
  private @Valid FallDto fall;
  private @Valid List<AusbildungDashboardItemDto> ausbildungDashboardItems = new ArrayList<>();
  private @Valid DelegierungSlimDto currentDelegierung;
  private @Valid LocalDate earliestActiveGesuchPeriodeStart;

  protected FallDashboardItemDto(FallDashboardItemDtoBuilder<?, ?> b) {
    this.fall = b.fall;
    this.ausbildungDashboardItems = b.ausbildungDashboardItems;
    this.currentDelegierung = b.currentDelegierung;
    this.earliestActiveGesuchPeriodeStart = b.earliestActiveGesuchPeriodeStart;
  }

  public FallDashboardItemDto() {
  }

  /**
   **/
  public FallDashboardItemDto fall(FallDto fall) {
    this.fall = fall;
    return this;
  }

  
  @JsonProperty("fall")
  @NotNull
  public FallDto getFall() {
    return fall;
  }

  @JsonProperty("fall")
  public void setFall(FallDto fall) {
    this.fall = fall;
  }

  /**
   **/
  public FallDashboardItemDto ausbildungDashboardItems(List<AusbildungDashboardItemDto> ausbildungDashboardItems) {
    this.ausbildungDashboardItems = ausbildungDashboardItems;
    return this;
  }

  
  @JsonProperty("ausbildungDashboardItems")
  @NotNull
  public List<AusbildungDashboardItemDto> getAusbildungDashboardItems() {
    return ausbildungDashboardItems;
  }

  @JsonProperty("ausbildungDashboardItems")
  public void setAusbildungDashboardItems(List<AusbildungDashboardItemDto> ausbildungDashboardItems) {
    this.ausbildungDashboardItems = ausbildungDashboardItems;
  }

  public FallDashboardItemDto addAusbildungDashboardItemsItem(AusbildungDashboardItemDto ausbildungDashboardItemsItem) {
    if (this.ausbildungDashboardItems == null) {
      this.ausbildungDashboardItems = new ArrayList<>();
    }

    this.ausbildungDashboardItems.add(ausbildungDashboardItemsItem);
    return this;
  }

  public FallDashboardItemDto removeAusbildungDashboardItemsItem(AusbildungDashboardItemDto ausbildungDashboardItemsItem) {
    if (ausbildungDashboardItemsItem != null && this.ausbildungDashboardItems != null) {
      this.ausbildungDashboardItems.remove(ausbildungDashboardItemsItem);
    }

    return this;
  }
  /**
   **/
  public FallDashboardItemDto currentDelegierung(DelegierungSlimDto currentDelegierung) {
    this.currentDelegierung = currentDelegierung;
    return this;
  }

  
  @JsonProperty("currentDelegierung")
  public DelegierungSlimDto getCurrentDelegierung() {
    return currentDelegierung;
  }

  @JsonProperty("currentDelegierung")
  public void setCurrentDelegierung(DelegierungSlimDto currentDelegierung) {
    this.currentDelegierung = currentDelegierung;
  }

  /**
   **/
  public FallDashboardItemDto earliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
    this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
    return this;
  }

  
  @JsonProperty("earliestActiveGesuchPeriodeStart")
  public LocalDate getEarliestActiveGesuchPeriodeStart() {
    return earliestActiveGesuchPeriodeStart;
  }

  @JsonProperty("earliestActiveGesuchPeriodeStart")
  public void setEarliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
    this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FallDashboardItemDto fallDashboardItem = (FallDashboardItemDto) o;
    return Objects.equals(this.fall, fallDashboardItem.fall) &&
        Objects.equals(this.ausbildungDashboardItems, fallDashboardItem.ausbildungDashboardItems) &&
        Objects.equals(this.currentDelegierung, fallDashboardItem.currentDelegierung) &&
        Objects.equals(this.earliestActiveGesuchPeriodeStart, fallDashboardItem.earliestActiveGesuchPeriodeStart);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fall, ausbildungDashboardItems, currentDelegierung, earliestActiveGesuchPeriodeStart);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FallDashboardItemDto {\n");
    
    sb.append("    fall: ").append(toIndentedString(fall)).append("\n");
    sb.append("    ausbildungDashboardItems: ").append(toIndentedString(ausbildungDashboardItems)).append("\n");
    sb.append("    currentDelegierung: ").append(toIndentedString(currentDelegierung)).append("\n");
    sb.append("    earliestActiveGesuchPeriodeStart: ").append(toIndentedString(earliestActiveGesuchPeriodeStart)).append("\n");
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


  public static FallDashboardItemDtoBuilder<?, ?> builder() {
    return new FallDashboardItemDtoBuilderImpl();
  }

  private static final class FallDashboardItemDtoBuilderImpl extends FallDashboardItemDtoBuilder<FallDashboardItemDto, FallDashboardItemDtoBuilderImpl> {

    @Override
    protected FallDashboardItemDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FallDashboardItemDto build() {
      return new FallDashboardItemDto(this);
    }
  }

  public static abstract class FallDashboardItemDtoBuilder<C extends FallDashboardItemDto, B extends FallDashboardItemDtoBuilder<C, B>>  {
    private FallDto fall;
    private List<AusbildungDashboardItemDto> ausbildungDashboardItems = new ArrayList<>();
    private DelegierungSlimDto currentDelegierung;
    private LocalDate earliestActiveGesuchPeriodeStart;
    protected abstract B self();

    public abstract C build();

    public B fall(FallDto fall) {
      this.fall = fall;
      return self();
    }
    public B ausbildungDashboardItems(List<AusbildungDashboardItemDto> ausbildungDashboardItems) {
      this.ausbildungDashboardItems = ausbildungDashboardItems;
      return self();
    }
    public B currentDelegierung(DelegierungSlimDto currentDelegierung) {
      this.currentDelegierung = currentDelegierung;
      return self();
    }
    public B earliestActiveGesuchPeriodeStart(LocalDate earliestActiveGesuchPeriodeStart) {
      this.earliestActiveGesuchPeriodeStart = earliestActiveGesuchPeriodeStart;
      return self();
    }
  }
}

