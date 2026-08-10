package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DemoDataTestBerechnungResultatDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.time.LocalDate;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("ApplyDemoDataResponse")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class ApplyDemoDataResponseDto  implements Serializable {
  private @Valid UUID gesuchId;
  private @Valid UUID gesuchTrancheId;
  private @Valid LocalDate gueltigAb;
  private @Valid LocalDate gueltigBis;
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid DemoDataTestBerechnungResultatDto berechnungResultat;

  protected ApplyDemoDataResponseDto(ApplyDemoDataResponseDtoBuilder<?, ?> b) {
    this.gesuchId = b.gesuchId;
    this.gesuchTrancheId = b.gesuchTrancheId;
    this.gueltigAb = b.gueltigAb;
    this.gueltigBis = b.gueltigBis;
    this.gesuchStatus = b.gesuchStatus;
    this.berechnungResultat = b.berechnungResultat;
  }

  public ApplyDemoDataResponseDto() {
  }

  /**
   **/
  public ApplyDemoDataResponseDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  @NotNull
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
    return this;
  }

  
  @JsonProperty("gesuchTrancheId")
  @NotNull
  public UUID getGesuchTrancheId() {
    return gesuchTrancheId;
  }

  @JsonProperty("gesuchTrancheId")
  public void setGesuchTrancheId(UUID gesuchTrancheId) {
    this.gesuchTrancheId = gesuchTrancheId;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
    return this;
  }

  
  @JsonProperty("gueltigAb")
  @NotNull
  public LocalDate getGueltigAb() {
    return gueltigAb;
  }

  @JsonProperty("gueltigAb")
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
    return this;
  }

  
  @JsonProperty("gueltigBis")
  @NotNull
  public LocalDate getGueltigBis() {
    return gueltigBis;
  }

  @JsonProperty("gueltigBis")
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }

  /**
   **/
  public ApplyDemoDataResponseDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
    return this;
  }

  
  @JsonProperty("gesuchStatus")
  @NotNull
  public ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus getGesuchStatus() {
    return gesuchStatus;
  }

  @JsonProperty("gesuchStatus")
  public void setGesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
    this.gesuchStatus = gesuchStatus;
  }

  /**
   **/
  public ApplyDemoDataResponseDto berechnungResultat(DemoDataTestBerechnungResultatDto berechnungResultat) {
    this.berechnungResultat = berechnungResultat;
    return this;
  }

  
  @JsonProperty("berechnungResultat")
  @NotNull
  public DemoDataTestBerechnungResultatDto getBerechnungResultat() {
    return berechnungResultat;
  }

  @JsonProperty("berechnungResultat")
  public void setBerechnungResultat(DemoDataTestBerechnungResultatDto berechnungResultat) {
    this.berechnungResultat = berechnungResultat;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ApplyDemoDataResponseDto applyDemoDataResponse = (ApplyDemoDataResponseDto) o;
    return Objects.equals(this.gesuchId, applyDemoDataResponse.gesuchId) &&
        Objects.equals(this.gesuchTrancheId, applyDemoDataResponse.gesuchTrancheId) &&
        Objects.equals(this.gueltigAb, applyDemoDataResponse.gueltigAb) &&
        Objects.equals(this.gueltigBis, applyDemoDataResponse.gueltigBis) &&
        Objects.equals(this.gesuchStatus, applyDemoDataResponse.gesuchStatus) &&
        Objects.equals(this.berechnungResultat, applyDemoDataResponse.berechnungResultat);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchId, gesuchTrancheId, gueltigAb, gueltigBis, gesuchStatus, berechnungResultat);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ApplyDemoDataResponseDto {\n");
    
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    berechnungResultat: ").append(toIndentedString(berechnungResultat)).append("\n");
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


  public static ApplyDemoDataResponseDtoBuilder<?, ?> builder() {
    return new ApplyDemoDataResponseDtoBuilderImpl();
  }

  private static final class ApplyDemoDataResponseDtoBuilderImpl extends ApplyDemoDataResponseDtoBuilder<ApplyDemoDataResponseDto, ApplyDemoDataResponseDtoBuilderImpl> {

    @Override
    protected ApplyDemoDataResponseDtoBuilderImpl self() {
      return this;
    }

    @Override
    public ApplyDemoDataResponseDto build() {
      return new ApplyDemoDataResponseDto(this);
    }
  }

  public static abstract class ApplyDemoDataResponseDtoBuilder<C extends ApplyDemoDataResponseDto, B extends ApplyDemoDataResponseDtoBuilder<C, B>>  {
    private UUID gesuchId;
    private UUID gesuchTrancheId;
    private LocalDate gueltigAb;
    private LocalDate gueltigBis;
    private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
    private DemoDataTestBerechnungResultatDto berechnungResultat;
    protected abstract B self();

    public abstract C build();

    public B gesuchId(UUID gesuchId) {
      this.gesuchId = gesuchId;
      return self();
    }
    public B gesuchTrancheId(UUID gesuchTrancheId) {
      this.gesuchTrancheId = gesuchTrancheId;
      return self();
    }
    public B gueltigAb(LocalDate gueltigAb) {
      this.gueltigAb = gueltigAb;
      return self();
    }
    public B gueltigBis(LocalDate gueltigBis) {
      this.gueltigBis = gueltigBis;
      return self();
    }
    public B gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
      this.gesuchStatus = gesuchStatus;
      return self();
    }
    public B berechnungResultat(DemoDataTestBerechnungResultatDto berechnungResultat) {
      this.berechnungResultat = berechnungResultat;
      return self();
    }
  }
}

