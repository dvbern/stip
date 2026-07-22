package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.VerfuegungDokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
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



@JsonTypeName("Verfuegung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class VerfuegungDto  implements Serializable {
  private @Valid UUID id;
  private @Valid List<VerfuegungDokumentDto> dokumente = new ArrayList<>();
  private @Valid String timestampErstellt;

  protected VerfuegungDto(VerfuegungDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.dokumente = b.dokumente;
    this.timestampErstellt = b.timestampErstellt;
  }

  public VerfuegungDto() {
  }

  /**
   **/
  public VerfuegungDto id(UUID id) {
    this.id = id;
    return this;
  }

  
  @JsonProperty("id")
  @NotNull
  public UUID getId() {
    return id;
  }

  @JsonProperty("id")
  public void setId(UUID id) {
    this.id = id;
  }

  /**
   **/
  public VerfuegungDto dokumente(List<VerfuegungDokumentDto> dokumente) {
    this.dokumente = dokumente;
    return this;
  }

  
  @JsonProperty("dokumente")
  @NotNull
  public List<VerfuegungDokumentDto> getDokumente() {
    return dokumente;
  }

  @JsonProperty("dokumente")
  public void setDokumente(List<VerfuegungDokumentDto> dokumente) {
    this.dokumente = dokumente;
  }

  public VerfuegungDto addDokumenteItem(VerfuegungDokumentDto dokumenteItem) {
    if (this.dokumente == null) {
      this.dokumente = new ArrayList<>();
    }

    this.dokumente.add(dokumenteItem);
    return this;
  }

  public VerfuegungDto removeDokumenteItem(VerfuegungDokumentDto dokumenteItem) {
    if (dokumenteItem != null && this.dokumente != null) {
      this.dokumente.remove(dokumenteItem);
    }

    return this;
  }
  /**
   **/
  public VerfuegungDto timestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public String getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(String timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    VerfuegungDto verfuegung = (VerfuegungDto) o;
    return Objects.equals(this.id, verfuegung.id) &&
        Objects.equals(this.dokumente, verfuegung.dokumente) &&
        Objects.equals(this.timestampErstellt, verfuegung.timestampErstellt);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokumente, timestampErstellt);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class VerfuegungDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokumente: ").append(toIndentedString(dokumente)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
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


  public static VerfuegungDtoBuilder<?, ?> builder() {
    return new VerfuegungDtoBuilderImpl();
  }

  private static final class VerfuegungDtoBuilderImpl extends VerfuegungDtoBuilder<VerfuegungDto, VerfuegungDtoBuilderImpl> {

    @Override
    protected VerfuegungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public VerfuegungDto build() {
      return new VerfuegungDto(this);
    }
  }

  public static abstract class VerfuegungDtoBuilder<C extends VerfuegungDto, B extends VerfuegungDtoBuilder<C, B>>  {
    private UUID id;
    private List<VerfuegungDokumentDto> dokumente = new ArrayList<>();
    private String timestampErstellt;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B dokumente(List<VerfuegungDokumentDto> dokumente) {
      this.dokumente = dokumente;
      return self();
    }
    public B timestampErstellt(String timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
  }
}

