package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchStateInfoDto;
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



@JsonTypeName("GesuchInfo")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class GesuchInfoDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String fallNummer;
  private @Valid String fallId;
  private @Valid String gesuchNummer;
  private @Valid LocalDate startDate;
  private @Valid LocalDate endDate;
  private @Valid GesuchStateInfoDto state;
  private @Valid String piaVorname;
  private @Valid String piaNachname;

  protected GesuchInfoDto(GesuchInfoDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.fallNummer = b.fallNummer;
    this.fallId = b.fallId;
    this.gesuchNummer = b.gesuchNummer;
    this.startDate = b.startDate;
    this.endDate = b.endDate;
    this.state = b.state;
    this.piaVorname = b.piaVorname;
    this.piaNachname = b.piaNachname;
  }

  public GesuchInfoDto() {
  }

  /**
   **/
  public GesuchInfoDto id(UUID id) {
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
  public GesuchInfoDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty("fallNummer")
  @NotNull
  public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty("fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public GesuchInfoDto fallId(String fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty("fallId")
  @NotNull
  public String getFallId() {
    return fallId;
  }

  @JsonProperty("fallId")
  public void setFallId(String fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public GesuchInfoDto gesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty("gesuchNummer")
  @NotNull
  public String getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty("gesuchNummer")
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public GesuchInfoDto startDate(LocalDate startDate) {
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
  public GesuchInfoDto endDate(LocalDate endDate) {
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
  public GesuchInfoDto state(GesuchStateInfoDto state) {
    this.state = state;
    return this;
  }

  
  @JsonProperty("state")
  @NotNull
  public GesuchStateInfoDto getState() {
    return state;
  }

  @JsonProperty("state")
  public void setState(GesuchStateInfoDto state) {
    this.state = state;
  }

  /**
   **/
  public GesuchInfoDto piaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
    return this;
  }

  
  @JsonProperty("piaVorname")
  public String getPiaVorname() {
    return piaVorname;
  }

  @JsonProperty("piaVorname")
  public void setPiaVorname(String piaVorname) {
    this.piaVorname = piaVorname;
  }

  /**
   **/
  public GesuchInfoDto piaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
    return this;
  }

  
  @JsonProperty("piaNachname")
  public String getPiaNachname() {
    return piaNachname;
  }

  @JsonProperty("piaNachname")
  public void setPiaNachname(String piaNachname) {
    this.piaNachname = piaNachname;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchInfoDto gesuchInfo = (GesuchInfoDto) o;
    return Objects.equals(this.id, gesuchInfo.id) &&
        Objects.equals(this.fallNummer, gesuchInfo.fallNummer) &&
        Objects.equals(this.fallId, gesuchInfo.fallId) &&
        Objects.equals(this.gesuchNummer, gesuchInfo.gesuchNummer) &&
        Objects.equals(this.startDate, gesuchInfo.startDate) &&
        Objects.equals(this.endDate, gesuchInfo.endDate) &&
        Objects.equals(this.state, gesuchInfo.state) &&
        Objects.equals(this.piaVorname, gesuchInfo.piaVorname) &&
        Objects.equals(this.piaNachname, gesuchInfo.piaNachname);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, fallNummer, fallId, gesuchNummer, startDate, endDate, state, piaVorname, piaNachname);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchInfoDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    startDate: ").append(toIndentedString(startDate)).append("\n");
    sb.append("    endDate: ").append(toIndentedString(endDate)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("    piaVorname: ").append(toIndentedString(piaVorname)).append("\n");
    sb.append("    piaNachname: ").append(toIndentedString(piaNachname)).append("\n");
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


  public static GesuchInfoDtoBuilder<?, ?> builder() {
    return new GesuchInfoDtoBuilderImpl();
  }

  private static final class GesuchInfoDtoBuilderImpl extends GesuchInfoDtoBuilder<GesuchInfoDto, GesuchInfoDtoBuilderImpl> {

    @Override
    protected GesuchInfoDtoBuilderImpl self() {
      return this;
    }

    @Override
    public GesuchInfoDto build() {
      return new GesuchInfoDto(this);
    }
  }

  public static abstract class GesuchInfoDtoBuilder<C extends GesuchInfoDto, B extends GesuchInfoDtoBuilder<C, B>>  {
    private UUID id;
    private String fallNummer;
    private String fallId;
    private String gesuchNummer;
    private LocalDate startDate;
    private LocalDate endDate;
    private GesuchStateInfoDto state;
    private String piaVorname;
    private String piaNachname;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B fallId(String fallId) {
      this.fallId = fallId;
      return self();
    }
    public B gesuchNummer(String gesuchNummer) {
      this.gesuchNummer = gesuchNummer;
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
    public B state(GesuchStateInfoDto state) {
      this.state = state;
      return self();
    }
    public B piaVorname(String piaVorname) {
      this.piaVorname = piaVorname;
      return self();
    }
    public B piaNachname(String piaNachname) {
      this.piaNachname = piaNachname;
      return self();
    }
  }
}

