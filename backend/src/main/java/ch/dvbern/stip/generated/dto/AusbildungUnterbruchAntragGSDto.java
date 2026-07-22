package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
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



@JsonTypeName("AusbildungUnterbruchAntragGS")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AusbildungUnterbruchAntragGSDto  implements Serializable {
  private @Valid UUID id;
  private @Valid List<DokumentDto> dokuments = new ArrayList<>();
  private @Valid Boolean canEdit;
  private @Valid LocalDate unterbruchLatestEndDate;
  private @Valid LocalDate unterbruchEarliestStartDate;

  protected AusbildungUnterbruchAntragGSDto(AusbildungUnterbruchAntragGSDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.dokuments = b.dokuments;
    this.canEdit = b.canEdit;
    this.unterbruchLatestEndDate = b.unterbruchLatestEndDate;
    this.unterbruchEarliestStartDate = b.unterbruchEarliestStartDate;
  }

  public AusbildungUnterbruchAntragGSDto() {
  }

  /**
   **/
  public AusbildungUnterbruchAntragGSDto id(UUID id) {
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
  public AusbildungUnterbruchAntragGSDto dokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
    return this;
  }

  
  @JsonProperty("dokuments")
  @NotNull
  public List<DokumentDto> getDokuments() {
    return dokuments;
  }

  @JsonProperty("dokuments")
  public void setDokuments(List<DokumentDto> dokuments) {
    this.dokuments = dokuments;
  }

  public AusbildungUnterbruchAntragGSDto addDokumentsItem(DokumentDto dokumentsItem) {
    if (this.dokuments == null) {
      this.dokuments = new ArrayList<>();
    }

    this.dokuments.add(dokumentsItem);
    return this;
  }

  public AusbildungUnterbruchAntragGSDto removeDokumentsItem(DokumentDto dokumentsItem) {
    if (dokumentsItem != null && this.dokuments != null) {
      this.dokuments.remove(dokumentsItem);
    }

    return this;
  }
  /**
   **/
  public AusbildungUnterbruchAntragGSDto canEdit(Boolean canEdit) {
    this.canEdit = canEdit;
    return this;
  }

  
  @JsonProperty("canEdit")
  @NotNull
  public Boolean getCanEdit() {
    return canEdit;
  }

  @JsonProperty("canEdit")
  public void setCanEdit(Boolean canEdit) {
    this.canEdit = canEdit;
  }

  /**
   **/
  public AusbildungUnterbruchAntragGSDto unterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
    return this;
  }

  
  @JsonProperty("unterbruchLatestEndDate")
  @NotNull
  public LocalDate getUnterbruchLatestEndDate() {
    return unterbruchLatestEndDate;
  }

  @JsonProperty("unterbruchLatestEndDate")
  public void setUnterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
    this.unterbruchLatestEndDate = unterbruchLatestEndDate;
  }

  /**
   **/
  public AusbildungUnterbruchAntragGSDto unterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
    return this;
  }

  
  @JsonProperty("unterbruchEarliestStartDate")
  @NotNull
  public LocalDate getUnterbruchEarliestStartDate() {
    return unterbruchEarliestStartDate;
  }

  @JsonProperty("unterbruchEarliestStartDate")
  public void setUnterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
    this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AusbildungUnterbruchAntragGSDto ausbildungUnterbruchAntragGS = (AusbildungUnterbruchAntragGSDto) o;
    return Objects.equals(this.id, ausbildungUnterbruchAntragGS.id) &&
        Objects.equals(this.dokuments, ausbildungUnterbruchAntragGS.dokuments) &&
        Objects.equals(this.canEdit, ausbildungUnterbruchAntragGS.canEdit) &&
        Objects.equals(this.unterbruchLatestEndDate, ausbildungUnterbruchAntragGS.unterbruchLatestEndDate) &&
        Objects.equals(this.unterbruchEarliestStartDate, ausbildungUnterbruchAntragGS.unterbruchEarliestStartDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dokuments, canEdit, unterbruchLatestEndDate, unterbruchEarliestStartDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AusbildungUnterbruchAntragGSDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dokuments: ").append(toIndentedString(dokuments)).append("\n");
    sb.append("    canEdit: ").append(toIndentedString(canEdit)).append("\n");
    sb.append("    unterbruchLatestEndDate: ").append(toIndentedString(unterbruchLatestEndDate)).append("\n");
    sb.append("    unterbruchEarliestStartDate: ").append(toIndentedString(unterbruchEarliestStartDate)).append("\n");
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


  public static AusbildungUnterbruchAntragGSDtoBuilder<?, ?> builder() {
    return new AusbildungUnterbruchAntragGSDtoBuilderImpl();
  }

  private static final class AusbildungUnterbruchAntragGSDtoBuilderImpl extends AusbildungUnterbruchAntragGSDtoBuilder<AusbildungUnterbruchAntragGSDto, AusbildungUnterbruchAntragGSDtoBuilderImpl> {

    @Override
    protected AusbildungUnterbruchAntragGSDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AusbildungUnterbruchAntragGSDto build() {
      return new AusbildungUnterbruchAntragGSDto(this);
    }
  }

  public static abstract class AusbildungUnterbruchAntragGSDtoBuilder<C extends AusbildungUnterbruchAntragGSDto, B extends AusbildungUnterbruchAntragGSDtoBuilder<C, B>>  {
    private UUID id;
    private List<DokumentDto> dokuments = new ArrayList<>();
    private Boolean canEdit;
    private LocalDate unterbruchLatestEndDate;
    private LocalDate unterbruchEarliestStartDate;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B dokuments(List<DokumentDto> dokuments) {
      this.dokuments = dokuments;
      return self();
    }
    public B canEdit(Boolean canEdit) {
      this.canEdit = canEdit;
      return self();
    }
    public B unterbruchLatestEndDate(LocalDate unterbruchLatestEndDate) {
      this.unterbruchLatestEndDate = unterbruchLatestEndDate;
      return self();
    }
    public B unterbruchEarliestStartDate(LocalDate unterbruchEarliestStartDate) {
      this.unterbruchEarliestStartDate = unterbruchEarliestStartDate;
      return self();
    }
  }
}

