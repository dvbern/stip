package ch.dvbern.stip.generated.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.UUID;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("Abschluss")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class AbschlussDto  implements Serializable {
  private @Valid UUID id;
  private @Valid String abschluss;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie;
  private @Valid ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung;
  private @Valid Integer bfsKategorie;
  private @Valid Boolean berufsbefaehigenderAbschluss;
  private @Valid ch.dvbern.stip.api.ausbildung.type.FerienTyp ferien;
  private @Valid String bezeichnungDe;
  private @Valid String bezeichnungFr;
  private @Valid Boolean aktiv;
  private @Valid ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage;

  /**
   **/
  public AbschlussDto id(UUID id) {
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
  public AbschlussDto abschluss(String abschluss) {
    this.abschluss = abschluss;
    return this;
  }

  
  @JsonProperty("abschluss")
  @NotNull
  public String getAbschluss() {
    return abschluss;
  }

  @JsonProperty("abschluss")
  public void setAbschluss(String abschluss) {
    this.abschluss = abschluss;
  }

  /**
   **/
  public AbschlussDto ausbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
    return this;
  }

  
  @JsonProperty("ausbildungskategorie")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie getAusbildungskategorie() {
    return ausbildungskategorie;
  }

  @JsonProperty("ausbildungskategorie")
  public void setAusbildungskategorie(ch.dvbern.stip.api.ausbildung.type.Ausbildungskategorie ausbildungskategorie) {
    this.ausbildungskategorie = ausbildungskategorie;
  }

  /**
   **/
  public AbschlussDto bildungskategorie(ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie) {
    this.bildungskategorie = bildungskategorie;
    return this;
  }

  
  @JsonProperty("bildungskategorie")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Bildungskategorie getBildungskategorie() {
    return bildungskategorie;
  }

  @JsonProperty("bildungskategorie")
  public void setBildungskategorie(ch.dvbern.stip.api.ausbildung.type.Bildungskategorie bildungskategorie) {
    this.bildungskategorie = bildungskategorie;
  }

  /**
   **/
  public AbschlussDto bildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
    return this;
  }

  
  @JsonProperty("bildungsrichtung")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung getBildungsrichtung() {
    return bildungsrichtung;
  }

  @JsonProperty("bildungsrichtung")
  public void setBildungsrichtung(ch.dvbern.stip.api.ausbildung.type.Bildungsrichtung bildungsrichtung) {
    this.bildungsrichtung = bildungsrichtung;
  }

  /**
   **/
  public AbschlussDto bfsKategorie(Integer bfsKategorie) {
    this.bfsKategorie = bfsKategorie;
    return this;
  }

  
  @JsonProperty("bfsKategorie")
  @NotNull
  public Integer getBfsKategorie() {
    return bfsKategorie;
  }

  @JsonProperty("bfsKategorie")
  public void setBfsKategorie(Integer bfsKategorie) {
    this.bfsKategorie = bfsKategorie;
  }

  /**
   **/
  public AbschlussDto berufsbefaehigenderAbschluss(Boolean berufsbefaehigenderAbschluss) {
    this.berufsbefaehigenderAbschluss = berufsbefaehigenderAbschluss;
    return this;
  }

  
  @JsonProperty("berufsbefaehigenderAbschluss")
  @NotNull
  public Boolean getBerufsbefaehigenderAbschluss() {
    return berufsbefaehigenderAbschluss;
  }

  @JsonProperty("berufsbefaehigenderAbschluss")
  public void setBerufsbefaehigenderAbschluss(Boolean berufsbefaehigenderAbschluss) {
    this.berufsbefaehigenderAbschluss = berufsbefaehigenderAbschluss;
  }

  /**
   **/
  public AbschlussDto ferien(ch.dvbern.stip.api.ausbildung.type.FerienTyp ferien) {
    this.ferien = ferien;
    return this;
  }

  
  @JsonProperty("ferien")
  @NotNull
  public ch.dvbern.stip.api.ausbildung.type.FerienTyp getFerien() {
    return ferien;
  }

  @JsonProperty("ferien")
  public void setFerien(ch.dvbern.stip.api.ausbildung.type.FerienTyp ferien) {
    this.ferien = ferien;
  }

  /**
   **/
  public AbschlussDto bezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
    return this;
  }

  
  @JsonProperty("bezeichnungDe")
  @NotNull
  public String getBezeichnungDe() {
    return bezeichnungDe;
  }

  @JsonProperty("bezeichnungDe")
  public void setBezeichnungDe(String bezeichnungDe) {
    this.bezeichnungDe = bezeichnungDe;
  }

  /**
   **/
  public AbschlussDto bezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
    return this;
  }

  
  @JsonProperty("bezeichnungFr")
  @NotNull
  public String getBezeichnungFr() {
    return bezeichnungFr;
  }

  @JsonProperty("bezeichnungFr")
  public void setBezeichnungFr(String bezeichnungFr) {
    this.bezeichnungFr = bezeichnungFr;
  }

  /**
   **/
  public AbschlussDto aktiv(Boolean aktiv) {
    this.aktiv = aktiv;
    return this;
  }

  
  @JsonProperty("aktiv")
  @NotNull
  public Boolean getAktiv() {
    return aktiv;
  }

  @JsonProperty("aktiv")
  public void setAktiv(Boolean aktiv) {
    this.aktiv = aktiv;
  }

  /**
   **/
  public AbschlussDto zusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
    return this;
  }

  
  @JsonProperty("zusatzfrage")
  public ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage getZusatzfrage() {
    return zusatzfrage;
  }

  @JsonProperty("zusatzfrage")
  public void setZusatzfrage(ch.dvbern.stip.api.ausbildung.type.AbschlussZusatzfrage zusatzfrage) {
    this.zusatzfrage = zusatzfrage;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AbschlussDto abschluss = (AbschlussDto) o;
    return Objects.equals(this.id, abschluss.id) &&
        Objects.equals(this.abschluss, abschluss.abschluss) &&
        Objects.equals(this.ausbildungskategorie, abschluss.ausbildungskategorie) &&
        Objects.equals(this.bildungskategorie, abschluss.bildungskategorie) &&
        Objects.equals(this.bildungsrichtung, abschluss.bildungsrichtung) &&
        Objects.equals(this.bfsKategorie, abschluss.bfsKategorie) &&
        Objects.equals(this.berufsbefaehigenderAbschluss, abschluss.berufsbefaehigenderAbschluss) &&
        Objects.equals(this.ferien, abschluss.ferien) &&
        Objects.equals(this.bezeichnungDe, abschluss.bezeichnungDe) &&
        Objects.equals(this.bezeichnungFr, abschluss.bezeichnungFr) &&
        Objects.equals(this.aktiv, abschluss.aktiv) &&
        Objects.equals(this.zusatzfrage, abschluss.zusatzfrage);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, abschluss, ausbildungskategorie, bildungskategorie, bildungsrichtung, bfsKategorie, berufsbefaehigenderAbschluss, ferien, bezeichnungDe, bezeichnungFr, aktiv, zusatzfrage);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AbschlussDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    abschluss: ").append(toIndentedString(abschluss)).append("\n");
    sb.append("    ausbildungskategorie: ").append(toIndentedString(ausbildungskategorie)).append("\n");
    sb.append("    bildungskategorie: ").append(toIndentedString(bildungskategorie)).append("\n");
    sb.append("    bildungsrichtung: ").append(toIndentedString(bildungsrichtung)).append("\n");
    sb.append("    bfsKategorie: ").append(toIndentedString(bfsKategorie)).append("\n");
    sb.append("    berufsbefaehigenderAbschluss: ").append(toIndentedString(berufsbefaehigenderAbschluss)).append("\n");
    sb.append("    ferien: ").append(toIndentedString(ferien)).append("\n");
    sb.append("    bezeichnungDe: ").append(toIndentedString(bezeichnungDe)).append("\n");
    sb.append("    bezeichnungFr: ").append(toIndentedString(bezeichnungFr)).append("\n");
    sb.append("    aktiv: ").append(toIndentedString(aktiv)).append("\n");
    sb.append("    zusatzfrage: ").append(toIndentedString(zusatzfrage)).append("\n");
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


}

