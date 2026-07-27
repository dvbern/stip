package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.DokumentDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DarlehenBuchhaltungEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DarlehenBuchhaltungEntryDto  implements Serializable {
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie;
  private @Valid Integer betrag;
  private @Valid DokumentDto verfuegung;
  private @Valid String yearRange;
  private @Valid String userErstellt;
  private @Valid String kommentar;

  protected DarlehenBuchhaltungEntryDto(DarlehenBuchhaltungEntryDtoBuilder<?, ?> b) {
    this.timestampErstellt = b.timestampErstellt;
    this.kategorie = b.kategorie;
    this.betrag = b.betrag;
    this.verfuegung = b.verfuegung;
    this.yearRange = b.yearRange;
    this.userErstellt = b.userErstellt;
    this.kommentar = b.kommentar;
  }

  public DarlehenBuchhaltungEntryDto() {
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
    return this;
  }

  
  @JsonProperty("timestampErstellt")
  @NotNull
  public java.time.LocalDateTime getTimestampErstellt() {
    return timestampErstellt;
  }

  @JsonProperty("timestampErstellt")
  public void setTimestampErstellt(java.time.LocalDateTime timestampErstellt) {
    this.timestampErstellt = timestampErstellt;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto kategorie(ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie) {
    this.kategorie = kategorie;
    return this;
  }

  
  @JsonProperty("kategorie")
  @NotNull
  public ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie getKategorie() {
    return kategorie;
  }

  @JsonProperty("kategorie")
  public void setKategorie(ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie) {
    this.kategorie = kategorie;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto betrag(Integer betrag) {
    this.betrag = betrag;
    return this;
  }

  
  @JsonProperty("betrag")
  @NotNull
  public Integer getBetrag() {
    return betrag;
  }

  @JsonProperty("betrag")
  public void setBetrag(Integer betrag) {
    this.betrag = betrag;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto verfuegung(DokumentDto verfuegung) {
    this.verfuegung = verfuegung;
    return this;
  }

  
  @JsonProperty("verfuegung")
  public DokumentDto getVerfuegung() {
    return verfuegung;
  }

  @JsonProperty("verfuegung")
  public void setVerfuegung(DokumentDto verfuegung) {
    this.verfuegung = verfuegung;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto yearRange(String yearRange) {
    this.yearRange = yearRange;
    return this;
  }

  
  @JsonProperty("yearRange")
  public String getYearRange() {
    return yearRange;
  }

  @JsonProperty("yearRange")
  public void setYearRange(String yearRange) {
    this.yearRange = yearRange;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto userErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
    return this;
  }

  
  @JsonProperty("userErstellt")
  public String getUserErstellt() {
    return userErstellt;
  }

  @JsonProperty("userErstellt")
  public void setUserErstellt(String userErstellt) {
    this.userErstellt = userErstellt;
  }

  /**
   **/
  public DarlehenBuchhaltungEntryDto kommentar(String kommentar) {
    this.kommentar = kommentar;
    return this;
  }

  
  @JsonProperty("kommentar")
  public String getKommentar() {
    return kommentar;
  }

  @JsonProperty("kommentar")
  public void setKommentar(String kommentar) {
    this.kommentar = kommentar;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DarlehenBuchhaltungEntryDto darlehenBuchhaltungEntry = (DarlehenBuchhaltungEntryDto) o;
    return Objects.equals(this.timestampErstellt, darlehenBuchhaltungEntry.timestampErstellt) &&
        Objects.equals(this.kategorie, darlehenBuchhaltungEntry.kategorie) &&
        Objects.equals(this.betrag, darlehenBuchhaltungEntry.betrag) &&
        Objects.equals(this.verfuegung, darlehenBuchhaltungEntry.verfuegung) &&
        Objects.equals(this.yearRange, darlehenBuchhaltungEntry.yearRange) &&
        Objects.equals(this.userErstellt, darlehenBuchhaltungEntry.userErstellt) &&
        Objects.equals(this.kommentar, darlehenBuchhaltungEntry.kommentar);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampErstellt, kategorie, betrag, verfuegung, yearRange, userErstellt, kommentar);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DarlehenBuchhaltungEntryDto {\n");
    
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    kategorie: ").append(toIndentedString(kategorie)).append("\n");
    sb.append("    betrag: ").append(toIndentedString(betrag)).append("\n");
    sb.append("    verfuegung: ").append(toIndentedString(verfuegung)).append("\n");
    sb.append("    yearRange: ").append(toIndentedString(yearRange)).append("\n");
    sb.append("    userErstellt: ").append(toIndentedString(userErstellt)).append("\n");
    sb.append("    kommentar: ").append(toIndentedString(kommentar)).append("\n");
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


  public static DarlehenBuchhaltungEntryDtoBuilder<?, ?> builder() {
    return new DarlehenBuchhaltungEntryDtoBuilderImpl();
  }

  private static final class DarlehenBuchhaltungEntryDtoBuilderImpl extends DarlehenBuchhaltungEntryDtoBuilder<DarlehenBuchhaltungEntryDto, DarlehenBuchhaltungEntryDtoBuilderImpl> {

    @Override
    protected DarlehenBuchhaltungEntryDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DarlehenBuchhaltungEntryDto build() {
      return new DarlehenBuchhaltungEntryDto(this);
    }
  }

  public static abstract class DarlehenBuchhaltungEntryDtoBuilder<C extends DarlehenBuchhaltungEntryDto, B extends DarlehenBuchhaltungEntryDtoBuilder<C, B>>  {
    private java.time.LocalDateTime timestampErstellt;
    private ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie;
    private Integer betrag;
    private DokumentDto verfuegung;
    private String yearRange;
    private String userErstellt;
    private String kommentar;
    protected abstract B self();

    public abstract C build();

    public B timestampErstellt(java.time.LocalDateTime timestampErstellt) {
      this.timestampErstellt = timestampErstellt;
      return self();
    }
    public B kategorie(ch.dvbern.stip.api.darlehen.type.DarlehenBuchhaltungEntryKategorie kategorie) {
      this.kategorie = kategorie;
      return self();
    }
    public B betrag(Integer betrag) {
      this.betrag = betrag;
      return self();
    }
    public B verfuegung(DokumentDto verfuegung) {
      this.verfuegung = verfuegung;
      return self();
    }
    public B yearRange(String yearRange) {
      this.yearRange = yearRange;
      return self();
    }
    public B userErstellt(String userErstellt) {
      this.userErstellt = userErstellt;
      return self();
    }
    public B kommentar(String kommentar) {
      this.kommentar = kommentar;
      return self();
    }
  }
}

