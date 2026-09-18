package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("DemoPartner")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class DemoPartnerDto  implements Serializable {
  private String sozialversicherungsnummer;
  private String nachname;
  private String vorname;
  private String strasse;
  private String hausnummer;
  private String plz;
  private String ort;
  private String land;
  private String geburtsdatum;
  private Integer alter;
  private Boolean inAusbildung;
  private String coAdresse;
  private ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;

  protected DemoPartnerDto(DemoPartnerDtoBuilder<?, ?> b) {
    this.sozialversicherungsnummer = b.sozialversicherungsnummer;
    this.nachname = b.nachname;
    this.vorname = b.vorname;
    this.strasse = b.strasse;
    this.hausnummer = b.hausnummer;
    this.plz = b.plz;
    this.ort = b.ort;
    this.land = b.land;
    this.geburtsdatum = b.geburtsdatum;
    this.alter = b.alter;
    this.inAusbildung = b.inAusbildung;
    this.coAdresse = b.coAdresse;
    this.pensum = b.pensum;
  }

  public DemoPartnerDto() {
  }

  /**
   **/
  public DemoPartnerDto sozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "sozialversicherungsnummer")
  @NotNull public String getSozialversicherungsnummer() {
    return sozialversicherungsnummer;
  }

  @JsonProperty(required = true, value = "sozialversicherungsnummer")
  public void setSozialversicherungsnummer(String sozialversicherungsnummer) {
    this.sozialversicherungsnummer = sozialversicherungsnummer;
  }

  /**
   **/
  public DemoPartnerDto nachname(String nachname) {
    this.nachname = nachname;
    return this;
  }

  
  @JsonProperty(required = true, value = "nachname")
  @NotNull public String getNachname() {
    return nachname;
  }

  @JsonProperty(required = true, value = "nachname")
  public void setNachname(String nachname) {
    this.nachname = nachname;
  }

  /**
   **/
  public DemoPartnerDto vorname(String vorname) {
    this.vorname = vorname;
    return this;
  }

  
  @JsonProperty(required = true, value = "vorname")
  @NotNull public String getVorname() {
    return vorname;
  }

  @JsonProperty(required = true, value = "vorname")
  public void setVorname(String vorname) {
    this.vorname = vorname;
  }

  /**
   **/
  public DemoPartnerDto strasse(String strasse) {
    this.strasse = strasse;
    return this;
  }

  
  @JsonProperty(required = true, value = "strasse")
  @NotNull public String getStrasse() {
    return strasse;
  }

  @JsonProperty(required = true, value = "strasse")
  public void setStrasse(String strasse) {
    this.strasse = strasse;
  }

  /**
   **/
  public DemoPartnerDto hausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "hausnummer")
  @NotNull public String getHausnummer() {
    return hausnummer;
  }

  @JsonProperty(required = true, value = "hausnummer")
  public void setHausnummer(String hausnummer) {
    this.hausnummer = hausnummer;
  }

  /**
   **/
  public DemoPartnerDto plz(String plz) {
    this.plz = plz;
    return this;
  }

  
  @JsonProperty(required = true, value = "plz")
  @NotNull public String getPlz() {
    return plz;
  }

  @JsonProperty(required = true, value = "plz")
  public void setPlz(String plz) {
    this.plz = plz;
  }

  /**
   **/
  public DemoPartnerDto ort(String ort) {
    this.ort = ort;
    return this;
  }

  
  @JsonProperty(required = true, value = "ort")
  @NotNull public String getOrt() {
    return ort;
  }

  @JsonProperty(required = true, value = "ort")
  public void setOrt(String ort) {
    this.ort = ort;
  }

  /**
   **/
  public DemoPartnerDto land(String land) {
    this.land = land;
    return this;
  }

  
  @JsonProperty(required = true, value = "land")
  @NotNull public String getLand() {
    return land;
  }

  @JsonProperty(required = true, value = "land")
  public void setLand(String land) {
    this.land = land;
  }

  /**
   **/
  public DemoPartnerDto geburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
    return this;
  }

  
  @JsonProperty(required = true, value = "geburtsdatum")
  @NotNull  @Pattern(regexp="^\\d{2}.\\d{2}$")public String getGeburtsdatum() {
    return geburtsdatum;
  }

  @JsonProperty(required = true, value = "geburtsdatum")
  public void setGeburtsdatum(String geburtsdatum) {
    this.geburtsdatum = geburtsdatum;
  }

  /**
   **/
  public DemoPartnerDto alter(Integer alter) {
    this.alter = alter;
    return this;
  }

  
  @JsonProperty(required = true, value = "alter")
  @NotNull public Integer getAlter() {
    return alter;
  }

  @JsonProperty(required = true, value = "alter")
  public void setAlter(Integer alter) {
    this.alter = alter;
  }

  /**
   **/
  public DemoPartnerDto inAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
    return this;
  }

  
  @JsonProperty(required = true, value = "inAusbildung")
  @NotNull public Boolean getInAusbildung() {
    return inAusbildung;
  }

  @JsonProperty(required = true, value = "inAusbildung")
  public void setInAusbildung(Boolean inAusbildung) {
    this.inAusbildung = inAusbildung;
  }

  /**
   **/
  public DemoPartnerDto coAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
    return this;
  }

  
  @JsonProperty("coAdresse")
  public String getCoAdresse() {
    return coAdresse;
  }

  @JsonProperty("coAdresse")
  public void setCoAdresse(String coAdresse) {
    this.coAdresse = coAdresse;
  }

  /**
   **/
  public DemoPartnerDto pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
    return this;
  }

  
  @JsonProperty("pensum")
  public ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum getPensum() {
    return pensum;
  }

  @JsonProperty("pensum")
  public void setPensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
    this.pensum = pensum;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    DemoPartnerDto demoPartner = (DemoPartnerDto) o;
    return Objects.equals(this.sozialversicherungsnummer, demoPartner.sozialversicherungsnummer) &&
        Objects.equals(this.nachname, demoPartner.nachname) &&
        Objects.equals(this.vorname, demoPartner.vorname) &&
        Objects.equals(this.strasse, demoPartner.strasse) &&
        Objects.equals(this.hausnummer, demoPartner.hausnummer) &&
        Objects.equals(this.plz, demoPartner.plz) &&
        Objects.equals(this.ort, demoPartner.ort) &&
        Objects.equals(this.land, demoPartner.land) &&
        Objects.equals(this.geburtsdatum, demoPartner.geburtsdatum) &&
        Objects.equals(this.alter, demoPartner.alter) &&
        Objects.equals(this.inAusbildung, demoPartner.inAusbildung) &&
        Objects.equals(this.coAdresse, demoPartner.coAdresse) &&
        Objects.equals(this.pensum, demoPartner.pensum);
  }

  @Override
  public int hashCode() {
    return Objects.hash(sozialversicherungsnummer, nachname, vorname, strasse, hausnummer, plz, ort, land, geburtsdatum, alter, inAusbildung, coAdresse, pensum);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class DemoPartnerDto {\n");
    
    sb.append("    sozialversicherungsnummer: ").append(toIndentedString(sozialversicherungsnummer)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    strasse: ").append(toIndentedString(strasse)).append("\n");
    sb.append("    hausnummer: ").append(toIndentedString(hausnummer)).append("\n");
    sb.append("    plz: ").append(toIndentedString(plz)).append("\n");
    sb.append("    ort: ").append(toIndentedString(ort)).append("\n");
    sb.append("    land: ").append(toIndentedString(land)).append("\n");
    sb.append("    geburtsdatum: ").append(toIndentedString(geburtsdatum)).append("\n");
    sb.append("    alter: ").append(toIndentedString(alter)).append("\n");
    sb.append("    inAusbildung: ").append(toIndentedString(inAusbildung)).append("\n");
    sb.append("    coAdresse: ").append(toIndentedString(coAdresse)).append("\n");
    sb.append("    pensum: ").append(toIndentedString(pensum)).append("\n");
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


  public static DemoPartnerDtoBuilder<?, ?> builder() {
    return new DemoPartnerDtoBuilderImpl();
  }

  private static final class DemoPartnerDtoBuilderImpl extends DemoPartnerDtoBuilder<DemoPartnerDto, DemoPartnerDtoBuilderImpl> {

    @Override
    protected DemoPartnerDtoBuilderImpl self() {
      return this;
    }

    @Override
    public DemoPartnerDto build() {
      return new DemoPartnerDto(this);
    }
  }

  public static abstract class DemoPartnerDtoBuilder<C extends DemoPartnerDto, B extends DemoPartnerDtoBuilder<C, B>>  {
    private String sozialversicherungsnummer;
    private String nachname;
    private String vorname;
    private String strasse;
    private String hausnummer;
    private String plz;
    private String ort;
    private String land;
    private String geburtsdatum;
    private Integer alter;
    private Boolean inAusbildung;
    private String coAdresse;
    private ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum;
    protected abstract B self();

    public abstract C build();

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
    public B strasse(String strasse) {
      this.strasse = strasse;
      return self();
    }
    public B hausnummer(String hausnummer) {
      this.hausnummer = hausnummer;
      return self();
    }
    public B plz(String plz) {
      this.plz = plz;
      return self();
    }
    public B ort(String ort) {
      this.ort = ort;
      return self();
    }
    public B land(String land) {
      this.land = land;
      return self();
    }
    public B geburtsdatum(String geburtsdatum) {
      this.geburtsdatum = geburtsdatum;
      return self();
    }
    public B alter(Integer alter) {
      this.alter = alter;
      return self();
    }
    public B inAusbildung(Boolean inAusbildung) {
      this.inAusbildung = inAusbildung;
      return self();
    }
    public B coAdresse(String coAdresse) {
      this.coAdresse = coAdresse;
      return self();
    }
    public B pensum(ch.dvbern.stip.api.ausbildung.type.AusbildungsPensum pensum) {
      this.pensum = pensum;
      return self();
    }
  }
}
