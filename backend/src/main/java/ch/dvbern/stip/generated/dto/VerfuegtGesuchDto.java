package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
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



@JsonTypeName("VerfuegtGesuch")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class VerfuegtGesuchDto  implements Serializable {
  private LocalDate timestamp;
  private @Valid List<@Valid GesuchTrancheSlimDto> tranchen = new ArrayList<>();
  private UUID berechnungId;

  protected VerfuegtGesuchDto(VerfuegtGesuchDtoBuilder<?, ?> b) {
    this.timestamp = b.timestamp;
    this.tranchen = b.tranchen;
    this.berechnungId = b.berechnungId;
  }

  public VerfuegtGesuchDto() {
  }

  /**
   **/
  public VerfuegtGesuchDto timestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  
  @JsonProperty(required = true, value = "timestamp")
  @NotNull public LocalDate getTimestamp() {
    return timestamp;
  }

  @JsonProperty(required = true, value = "timestamp")
  public void setTimestamp(LocalDate timestamp) {
    this.timestamp = timestamp;
  }

  /**
   **/
  public VerfuegtGesuchDto tranchen(List<@Valid GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
    return this;
  }

  
  @JsonProperty(required = true, value = "tranchen")
  @NotNull @Valid public List<@Valid GesuchTrancheSlimDto> getTranchen() {
    return tranchen;
  }

  @JsonProperty(required = true, value = "tranchen")
  public void setTranchen(List<@Valid GesuchTrancheSlimDto> tranchen) {
    this.tranchen = tranchen;
  }

  public VerfuegtGesuchDto addTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (this.tranchen == null) {
      this.tranchen = new ArrayList<>();
    }

    this.tranchen.add(tranchenItem);
    return this;
  }

  public VerfuegtGesuchDto removeTranchenItem(GesuchTrancheSlimDto tranchenItem) {
    if (tranchenItem != null && this.tranchen != null) {
      this.tranchen.remove(tranchenItem);
    }

    return this;
  }
  /**
   **/
  public VerfuegtGesuchDto berechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
    return this;
  }

  
  @JsonProperty(required = true, value = "berechnungId")
  @NotNull public UUID getBerechnungId() {
    return berechnungId;
  }

  @JsonProperty(required = true, value = "berechnungId")
  public void setBerechnungId(UUID berechnungId) {
    this.berechnungId = berechnungId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegtGesuchDto verfuegtGesuch = (VerfuegtGesuchDto) o;
    return Objects.equals(this.timestamp, verfuegtGesuch.timestamp) &&
        Objects.equals(this.tranchen, verfuegtGesuch.tranchen) &&
        Objects.equals(this.berechnungId, verfuegtGesuch.berechnungId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, tranchen, berechnungId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegtGesuchDto {\n");
    
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
    sb.append("    tranchen: ").append(toIndentedString(tranchen)).append("\n");
    sb.append("    berechnungId: ").append(toIndentedString(berechnungId)).append("\n");
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


  public static VerfuegtGesuchDtoBuilder<?, ?> builder() {
    return new VerfuegtGesuchDtoBuilderImpl();
  }

  private static final class VerfuegtGesuchDtoBuilderImpl extends VerfuegtGesuchDtoBuilder<VerfuegtGesuchDto, VerfuegtGesuchDtoBuilderImpl> {

    @Override
    protected VerfuegtGesuchDtoBuilderImpl self() {
      return this;
    }

    @Override
    public VerfuegtGesuchDto build() {
      return new VerfuegtGesuchDto(this);
    }
  }

  public static abstract class VerfuegtGesuchDtoBuilder<C extends VerfuegtGesuchDto, B extends VerfuegtGesuchDtoBuilder<C, B>>  {
    private LocalDate timestamp;
    private List<GesuchTrancheSlimDto> tranchen = new ArrayList<>();
    private UUID berechnungId;
    protected abstract B self();

    public abstract C build();

    public B timestamp(LocalDate timestamp) {
      this.timestamp = timestamp;
      return self();
    }
    public B tranchen(List<GesuchTrancheSlimDto> tranchen) {
      this.tranchen = tranchen;
      return self();
    }
    public B berechnungId(UUID berechnungId) {
      this.berechnungId = berechnungId;
      return self();
    }
  }
}
