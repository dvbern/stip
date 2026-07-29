package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.VerfuegungDokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("VerfuegungFall")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class VerfuegungFallDto  implements Serializable {
  private @Valid String timestampErstellt;
  private @Valid String yearRange;
  private @Valid Integer totalbetragStipendium;
  private @Valid VerfuegungDokumentDto dokument;

  protected VerfuegungFallDto(VerfuegungFallDtoBuilder<?, ?> b) {
    this.timestampErstellt = b.timestampErstellt;
    this.yearRange = b.yearRange;
    this.totalbetragStipendium = b.totalbetragStipendium;
    this.dokument = b.dokument;
  }

  public VerfuegungFallDto() {
  }

  /**
   **/
  public VerfuegungFallDto timestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public String getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public VerfuegungFallDto yearRange(String yearRange) {
    this.yearRange = yearRange;
    return this;
  }

  
  @JsonProperty("yearRange")
  @NotNull
  public String getYearRange() {
    return yearRange;
  }

  @JsonProperty("yearRange")
  public void setYearRange(String yearRange) {
    this.yearRange = yearRange;
  }

  /**
   **/
  public VerfuegungFallDto totalbetragStipendium(Integer totalbetragStipendium) {
    this.totalbetragStipendium = totalbetragStipendium;
    return this;
  }

  
  @JsonProperty("totalbetragStipendium")
  @NotNull
  public Integer getTotalbetragStipendium() {
    return totalbetragStipendium;
  }

  @JsonProperty("totalbetragStipendium")
  public void setTotalbetragStipendium(Integer totalbetragStipendium) {
    this.totalbetragStipendium = totalbetragStipendium;
  }

  /**
   **/
  public VerfuegungFallDto dokument(VerfuegungDokumentDto dokument) {
    this.dokument = dokument;
    return this;
  }

  
  @JsonProperty("dokument")
  public VerfuegungDokumentDto getDokument() {
    return dokument;
  }

  @JsonProperty("dokument")
  public void setDokument(VerfuegungDokumentDto dokument) {
    this.dokument = dokument;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegungFallDto verfuegungFall = (VerfuegungFallDto) o;
    return Objects.equals(this.timestampErstellt, verfuegungFall.timestampErstellt) &&
        Objects.equals(this.yearRange, verfuegungFall.yearRange) &&
        Objects.equals(this.totalbetragStipendium, verfuegungFall.totalbetragStipendium) &&
        Objects.equals(this.dokument, verfuegungFall.dokument);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampErstellt, yearRange, totalbetragStipendium, dokument);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegungFallDto {\n");
    
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    yearRange: ").append(toIndentedString(yearRange)).append("\n");
    sb.append("    totalbetragStipendium: ").append(toIndentedString(totalbetragStipendium)).append("\n");
    sb.append("    dokument: ").append(toIndentedString(dokument)).append("\n");
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


  public static VerfuegungFallDtoBuilder<?, ?> builder() {
    return new VerfuegungFallDtoBuilderImpl();
  }

  private static final class VerfuegungFallDtoBuilderImpl extends VerfuegungFallDtoBuilder<VerfuegungFallDto, VerfuegungFallDtoBuilderImpl> {

    @Override
    protected VerfuegungFallDtoBuilderImpl self() {
      return this;
    }

    @Override
    public VerfuegungFallDto build() {
      return new VerfuegungFallDto(this);
    }
  }

  public static abstract class VerfuegungFallDtoBuilder<C extends VerfuegungFallDto, B extends VerfuegungFallDtoBuilder<C, B>>  {
    private String timestampErstellt;
    private String yearRange;
    private Integer totalbetragStipendium;
    private VerfuegungDokumentDto dokument;
    protected abstract B self();

    public abstract C build();

    public B timestampErstellt(String timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B yearRange(String yearRange) {
      this.yearRange = yearRange;
      return self();
    }
    public B totalbetragStipendium(Integer totalbetragStipendium) {
      this.totalbetragStipendium = totalbetragStipendium;
      return self();
    }
    public B dokument(VerfuegungDokumentDto dokument) {
      this.dokument = dokument;
      return self();
    }
  }
}

