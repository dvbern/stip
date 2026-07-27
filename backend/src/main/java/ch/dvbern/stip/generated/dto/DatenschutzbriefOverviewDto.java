package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
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



@JsonTypeName("DatenschutzbriefOverview")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DatenschutzbriefOverviewDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String userErstellt;
  private @Valid String sozialversicherungsnummer;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;
  private @Valid LocalDate timestampErstellt;
  private @Valid DokumentDto dokument;
  private @Valid UUID massendruckJobId;

  protected DatenschutzbriefOverviewDto(DatenschutzbriefOverviewDtoBuilder<?, ?> b) {
    this.id = b.id;
    this.userErstellt = b.userErstellt;
    this.sozialversicherungsnummer = b.sozialversicherungsnummer;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.elternTyp = b.elternTyp;
    this.timestampErstellt = b.timestampErstellt;
    this.dokument = b.dokument;
    this.massendruckJobId = b.massendruckJobId;
  }

  public DatenschutzbriefOverviewDto() {
  }

  /**
   **/
  public DatenschutzbriefOverviewDto id(UUID id) {
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
  public DatenschutzbriefOverviewDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  @NotNull
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty("sozialversicherungsnummer")
  @NotNull
  public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty("sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty("nachname")
  @NotNull
  public String getNachname() {
    return nachname;
  }

  @JsonProperty("nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty("vorname")
  @NotNull
  public String getVorname() {
    return vorname;
  }

  @JsonProperty("vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
    return this;
  }

  
  @JsonProperty("elternTyp")
  @NotNull
  public ch.dvbern.stip.api.eltern.type.ElternTyp getElternTyp() {
    return elternTyp;
  }

  @JsonProperty("elternTyp")
  public void setElternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
    this.elternTyp = elternTyp;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto timestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  public LocalDate getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(LocalDate timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto dokument(DokumentDto dokument) {
    this.dokument = dokument;
    return this;
  }

  
  @JsonProperty("dokument")
  public DokumentDto getDokument() {
    return dokument;
  }

  @JsonProperty("dokument")
  public void setDokument(DokumentDto dokument) {
    this.dokument = dokument;
  }

  /**
   **/
  public DatenschutzbriefOverviewDto massendruckJobId(UUID massendruckJobId) {
    this.massendruckJobId = massendruckJobId;
    return this;
  }

  
  @JsonProperty("massendruckJobId")
  public UUID getMassendruckJobId() {
    return massendruckJobId;
  }

  @JsonProperty("massendruckJobId")
  public void setMassendruckJobId(UUID massendruckJobId) {
    this.massendruckJobId = massendruckJobId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DatenschutzbriefOverviewDto datenschutzbriefOverview = (DatenschutzbriefOverviewDto) o;
    return Objects.equals(this.id, datenschutzbriefOverview.id) &&
        Objects.equals(this.userErstellt, datenschutzbriefOverview.userErstellt) &&
        Objects.equals(this.sozialversicherungsnummer, datenschutzbriefOverview.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, datenschutzbriefOverview.nachname) &&
        Objects.equals(this.vorname, datenschutzbriefOverview.vorname) &&
        Objects.equals(this.elternTyp, datenschutzbriefOverview.elternTyp) &&
        Objects.equals(this.timestampErstellt, datenschutzbriefOverview.timestampErstellt) &&
        Objects.equals(this.dokument, datenschutzbriefOverview.dokument) &&
        Objects.equals(this.massendruckJobId, datenschutzbriefOverview.massendruckJobId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, userErstellt, sozialversicherungsnummer, nachname, vorname, elternTyp, timestampErstellt, dokument, massendruckJobId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DatenschutzbriefOverviewDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    elternTyp: ").append(toIndentedString(elternTyp)).append("\n");
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    dokument: ").append(toIndentedString(dokument)).append("\n");
    sb.append("    massendruckJobId: ").append(toIndentedString(massendruckJobId)).append("\n");
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


  public static DatenschutzbriefOverviewDtoBuilder<?, ?> builder() {
    return new DatenschutzbriefOverviewDtoBuilderImpl();
  }

  private static final class DatenschutzbriefOverviewDtoBuilderImpl extends DatenschutzbriefOverviewDtoBuilder<DatenschutzbriefOverviewDto, DatenschutzbriefOverviewDtoBuilderImpl> {

    @Override
    protected DatenschutzbriefOverviewDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DatenschutzbriefOverviewDto build() {
      return new DatenschutzbriefOverviewDto(this);
    }
  }

  public static abstract class DatenschutzbriefOverviewDtoBuilder<C extends DatenschutzbriefOverviewDto, B extends DatenschutzbriefOverviewDtoBuilder<C, B>>  {
    private UUID id;
    private String userErstellt;
    private String sozialversicherungsnummer;
    private String nachname;
    private String vorname;
    private ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;
    private LocalDate timestampErstellt;
    private DokumentDto dokument;
    private UUID massendruckJobId;
    protected abstract B self();

    public abstract C build();

    public B id(UUID id) {
      this.id = id;
      return self();
    }
    public B userErstellt(String userErstellt) {
      this.userErstellt = userErstellt;
      return self();
    }
    public B sozialversicherungsnummer(String sozialversicherungsnummer) {
      this.sozialversicherungsnummer = sozialversicherungsnummer;
      return self();
    }
    public B nachname(String nachname) {
      this.nachname = nachname;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
      this.elternTyp = elternTyp;
      return self();
    }
    public B timestampErstellt(LocalDate timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B dokument(DokumentDto dokument) {
      this.dokument = dokument;
      return self();
    }
    public B massendruckJobId(UUID massendruckJobId) {
      this.massendruckJobId = massendruckJobId;
      return self();
    }
  }
}

