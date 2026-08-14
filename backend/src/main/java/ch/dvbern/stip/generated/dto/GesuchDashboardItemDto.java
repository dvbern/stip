package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.FreiwilligDarlehenDto;
import ch.dvbern.stip.generated.dto.GesuchDashboardItemMissingDocumentsDto;
import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import ch.dvbern.stip.generated.dto.GesuchsperiodeDto;
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



@JsonTypeName("GesuchDashboardItem")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchDashboardItemDto  implements Serializable {
  private @Valid GesuchsperiodeDto gesuchsperiode;
  private @Valid ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
  private @Valid UUID id;
  private @Valid UUID currentTrancheId;
  private @Valid List<FreiwilligDarlehenDto> freiwilligeDarlehenList = new ArrayList<>();
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid Boolean canCreateAenderung;
  private @Valid Boolean canCreateDarlehen;
  private @Valid LocalDate nachfristDokumente;
  private @Valid GesuchTrancheSlimDto offeneAenderung;
  private @Valid GesuchDashboardItemMissingDocumentsDto missingDocuments;

  protected GesuchDashboardItemDto(GesuchDashboardItemDtoBuilder<?, ?> b) {
    this.gesuchsperiode = b.gesuchsperiode;
    this.gesuchStatus = b.gesuchStatus;
    this.id = b.id;
    this.currentTrancheId = b.currentTrancheId;
    this.freiwilligeDarlehenList = b.freiwilligeDarlehenList;
    this.startDate = b.startDate;
    this.endDate = b.endDate;
    this.canCreateAenderung = b.canCreateAenderung;
    this.canCreateDarlehen = b.canCreateDarlehen;
    this.nachfristDokumente = b.nachfristDokumente;
    this.offeneAenderung = b.offeneAenderung;
    this.missingDocuments = b.missingDocuments;
  }

  public GesuchDashboardItemDto() {
  }

  /**
   **/
  public GesuchDashboardItemDto gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
    return this;
  }

  
  @JsonProperty("gesuchsperiode")
  @NotNull
  public GesuchsperiodeDto getGesuchsperiode() {
    return gesuchsperiode;
  }

  @JsonProperty("gesuchsperiode")
  public void setGesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
    this.gesuchsperiode = gesuchsperiode;
  }

  /**
   **/
  public GesuchDashboardItemDto gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
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
  public GesuchDashboardItemDto id(UUID id) {
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
  public GesuchDashboardItemDto currentTrancheId(UUID currentTrancheId) {
    this.currentTrancheId = currentTrancheId;
    return this;
  }

  
  @JsonProperty("currentTrancheId")
  @NotNull
  public UUID getCurrentTrancheId() {
    return currentTrancheId;
  }

  @JsonProperty("currentTrancheId")
  public void setCurrentTrancheId(UUID currentTrancheId) {
    this.currentTrancheId = currentTrancheId;
  }

  /**
   **/
  public GesuchDashboardItemDto freiwilligeDarlehenList(List<FreiwilligDarlehenDto> freiwilligeDarlehenList) {
    this.freiwilligeDarlehenList = freiwilligeDarlehenList;
    return this;
  }

  
  @JsonProperty("freiwilligeDarlehenList")
  @NotNull
  public List<FreiwilligDarlehenDto> getFreiwilligeDarlehenList() {
    return freiwilligeDarlehenList;
  }

  @JsonProperty("freiwilligeDarlehenList")
  public void setFreiwilligeDarlehenList(List<FreiwilligDarlehenDto> freiwilligeDarlehenList) {
    this.freiwilligeDarlehenList = freiwilligeDarlehenList;
  }

  public GesuchDashboardItemDto addFreiwilligeDarlehenListItem(FreiwilligDarlehenDto freiwilligeDarlehenListItem) {
    if (this.freiwilligeDarlehenList == null) {
      this.freiwilligeDarlehenList = new ArrayList<>();
    }

    this.freiwilligeDarlehenList.add(freiwilligeDarlehenListItem);
    return this;
  }

  public GesuchDashboardItemDto removeFreiwilligeDarlehenListItem(FreiwilligDarlehenDto freiwilligeDarlehenListItem) {
    if (freiwilligeDarlehenListItem != null && this.freiwilligeDarlehenList != null) {
      this.freiwilligeDarlehenList.remove(freiwilligeDarlehenListItem);
    }

    return this;
  }
  /**
   **/
  public GesuchDashboardItemDto startDate(LocalDate startDate) {
    this.startDate = startDate;
    return this;
  }

  
  @JsonProperty("startDate")
  @NotNull
  public LocalDate getStartDate() {
    return startDate;
  }

  @JsonProperty("startDate")
  public void setStartDate(LocalDate startDate) {
    this.startDate = startDate;
  }

  /**
   **/
  public GesuchDashboardItemDto endDate(LocalDate endDate) {
    this.endDate = endDate;
    return this;
  }

  
  @JsonProperty("endDate")
  @NotNull
  public LocalDate getEndDate() {
    return endDate;
  }

  @JsonProperty("endDate")
  public void setEndDate(LocalDate endDate) {
    this.endDate = endDate;
  }

  /**
   **/
  public GesuchDashboardItemDto canCreateAenderung(Boolean canCreateAenderung) {
    this.canCreateAenderung = canCreateAenderung;
    return this;
  }

  
  @JsonProperty("canCreateAenderung")
  @NotNull
  public Boolean getCanCreateAenderung() {
    return canCreateAenderung;
  }

  @JsonProperty("canCreateAenderung")
  public void setCanCreateAenderung(Boolean canCreateAenderung) {
    this.canCreateAenderung = canCreateAenderung;
  }

  /**
   **/
  public GesuchDashboardItemDto canCreateDarlehen(Boolean canCreateDarlehen) {
    this.canCreateDarlehen = canCreateDarlehen;
    return this;
  }

  
  @JsonProperty("canCreateDarlehen")
  @NotNull
  public Boolean getCanCreateDarlehen() {
    return canCreateDarlehen;
  }

  @JsonProperty("canCreateDarlehen")
  public void setCanCreateDarlehen(Boolean canCreateDarlehen) {
    this.canCreateDarlehen = canCreateDarlehen;
  }

  /**
   **/
  public GesuchDashboardItemDto nachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
    return this;
  }

  
  @JsonProperty("nachfristDokumente")
  public LocalDate getNachfristDokumente() {
    return nachfristDokumente;
  }

  @JsonProperty("nachfristDokumente")
  public void setNachfristDokumente(LocalDate nachfristDokumente) {
    this.nachfristDokumente = nachfristDokumente;
  }

  /**
   **/
  public GesuchDashboardItemDto offeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
    return this;
  }

  
  @JsonProperty("offeneAenderung")
  public GesuchTrancheSlimDto getOffeneAenderung() {
    return offeneAenderung;
  }

  @JsonProperty("offeneAenderung")
  public void setOffeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
    this.offeneAenderung = offeneAenderung;
  }

  /**
   **/
  public GesuchDashboardItemDto missingDocuments(GesuchDashboardItemMissingDocumentsDto missingDocuments) {
    this.missingDocuments = missingDocuments;
    return this;
  }

  
  @JsonProperty("missingDocuments")
  public GesuchDashboardItemMissingDocumentsDto getMissingDocuments() {
    return missingDocuments;
  }

  @JsonProperty("missingDocuments")
  public void setMissingDocuments(GesuchDashboardItemMissingDocumentsDto missingDocuments) {
    this.missingDocuments = missingDocuments;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchDashboardItemDto gesuchDashboardItem = (GesuchDashboardItemDto) o;
    return Objects.equals(this.gesuchsperiode, gesuchDashboardItem.gesuchsperiode) &&
        Objects.equals(this.gesuchStatus, gesuchDashboardItem.gesuchStatus) &&
        Objects.equals(this.id, gesuchDashboardItem.id) &&
        Objects.equals(this.currentTrancheId, gesuchDashboardItem.currentTrancheId) &&
        Objects.equals(this.freiwilligeDarlehenList, gesuchDashboardItem.freiwilligeDarlehenList) &&
        Objects.equals(this.startDate, gesuchDashboardItem.startDate) &&
        Objects.equals(this.endDate, gesuchDashboardItem.endDate) &&
        Objects.equals(this.canCreateAenderung, gesuchDashboardItem.canCreateAenderung) &&
        Objects.equals(this.canCreateDarlehen, gesuchDashboardItem.canCreateDarlehen) &&
        Objects.equals(this.nachfristDokumente, gesuchDashboardItem.nachfristDokumente) &&
        Objects.equals(this.offeneAenderung, gesuchDashboardItem.offeneAenderung) &&
        Objects.equals(this.missingDocuments, gesuchDashboardItem.missingDocuments);
  }

  @Override
  public int hashCode() {
    return Objects.hash(gesuchsperiode, gesuchStatus, id, currentTrancheId, freiwilligeDarlehenList, startDate, endDate, canCreateAenderung, canCreateDarlehen, nachfristDokumente, offeneAenderung, missingDocuments);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchDashboardItemDto {\n");
    
    sb.append("    gesuchsperiode: ").append(toIndentedString(gesuchsperiode)).append("\n");
    sb.append("    gesuchStatus: ").append(toIndentedString(gesuchStatus)).append("\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    currentTrancheId: ").append(toIndentedString(currentTrancheId)).append("\n");
    sb.append("    freiwilligeDarlehenList: ").append(toIndentedString(freiwilligeDarlehenList)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    canCreateAenderung: ").append(toIndentedString(canCreateAenderung)).append("\n");
    sb.append("    canCreateDarlehen: ").append(toIndentedString(canCreateDarlehen)).append("\n");
    sb.append("    nachfristDokumente: ").append(toIndentedString(nachfristDokumente)).append("\n");
    sb.append("    offeneAenderung: ").append(toIndentedString(offeneAenderung)).append("\n");
    sb.append("    missingDocuments: ").append(toIndentedString(missingDocuments)).append("\n");
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


  public static GesuchDashboardItemDtoBuilder<?, ?> builder() {
    return new GesuchDashboardItemDtoBuilderImpl();
  }

  private static final class GesuchDashboardItemDtoBuilderImpl extends GesuchDashboardItemDtoBuilder<GesuchDashboardItemDto, GesuchDashboardItemDtoBuilderImpl> {

    @Override
    protected GesuchDashboardItemDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchDashboardItemDto build() {
      return new GesuchDashboardItemDto(this);
    }
  }

  public static abstract class GesuchDashboardItemDtoBuilder<C extends GesuchDashboardItemDto, B extends GesuchDashboardItemDtoBuilder<C, B>>  {
    private GesuchsperiodeDto gesuchsperiode;
    private ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus;
    private UUID id;
    private UUID currentTrancheId;
    private List<FreiwilligDarlehenDto> freiwilligeDarlehenList = new ArrayList<>();
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean canCreateAenderung;
    private Boolean canCreateDarlehen;
    private LocalDate nachfristDokumente;
    private GesuchTrancheSlimDto offeneAenderung;
    private GesuchDashboardItemMissingDocumentsDto missingDocuments;
    protected abstract B self();

    public abstract C build();

    public B gesuchsperiode(GesuchsperiodeDto gesuchsperiode) {
      this.gesuchsperiode = gesuchsperiode;
      return self();
    }
    public B gesuchStatus(ch.dvbern.stip.api.gesuchstatus.type.Gesuchstatus gesuchStatus) {
      this.gesuchStatus = gesuchStatus;
      return self();
    }
    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B currentTrancheId(UUID currentTrancheId) {
      this.currentTrancheId = currentTrancheId;
      return self();
    }
    public B freiwilligeDarlehenList(List<FreiwilligDarlehenDto> freiwilligeDarlehenList) {
      this.freiwilligeDarlehenList = freiwilligeDarlehenList;
      return self();
    }
    public B startDate(LocalDate startDate) {
      this.startDate = startDate;
      return self();
    }
    public B endDate(LocalDate endDate) {
      this.endDate = endDate;
      return self();
    }
    public B canCreateAenderung(Boolean canCreateAenderung) {
      this.canCreateAenderung = canCreateAenderung;
      return self();
    }
    public B canCreateDarlehen(Boolean canCreateDarlehen) {
      this.canCreateDarlehen = canCreateDarlehen;
      return self();
    }
    public B nachfristDokumente(LocalDate nachfristDokumente) {
      this.nachfristDokumente = nachfristDokumente;
      return self();
    }
    public B offeneAenderung(GesuchTrancheSlimDto offeneAenderung) {
      this.offeneAenderung = offeneAenderung;
      return self();
    }
    public B missingDocuments(GesuchDashboardItemMissingDocumentsDto missingDocuments) {
      this.missingDocuments = missingDocuments;
      return self();
    }
  }
}

