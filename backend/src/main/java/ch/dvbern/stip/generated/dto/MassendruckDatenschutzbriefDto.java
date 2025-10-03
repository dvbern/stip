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



@JsonTypeName("MassendruckDatenschutzbrief")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class MassendruckDatenschutzbriefDto  implements Serializable {
  private @Valid UUID id;
  private @Valid Boolean isVersendet;
  private @Valid String nachname;
  private @Valid String vorname;
  private @Valid String gesuchNummer;
  private @Valid UUID gesuchId;
  private @Valid UUID gesuchTrancheId;
  private @Valid ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp;

  /**
   **/
  public MassendruckDatenschutzbriefDto id(UUID id) {
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
  public MassendruckDatenschutzbriefDto isVersendet(Boolean isVersendet) {
    this.isVersendet = isVersendet;
    return this;
  }

  
  @JsonProperty("isVersendet")
  @NotNull
  public Boolean getIsVersendet() {
    return isVersendet;
  }

  @JsonProperty("isVersendet")
  public void setIsVersendet(Boolean isVersendet) {
    this.isVersendet = isVersendet;
  }

  /**
   **/
  public MassendruckDatenschutzbriefDto nachname(String nachname) {
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
  public MassendruckDatenschutzbriefDto vorname(String vorname) {
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
  public MassendruckDatenschutzbriefDto gesuchNummer(String gesuchNummer) {
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
  public MassendruckDatenschutzbriefDto gesuchId(UUID gesuchId) {
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
  public MassendruckDatenschutzbriefDto gesuchTrancheId(UUID gesuchTrancheId) {
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
  public MassendruckDatenschutzbriefDto elternTyp(ch.dvbern.stip.api.eltern.type.ElternTyp elternTyp) {
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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MassendruckDatenschutzbriefDto massendruckDatenschutzbrief = (MassendruckDatenschutzbriefDto) o;
    return Objects.equals(this.id, massendruckDatenschutzbrief.id) &&
        Objects.equals(this.isVersendet, massendruckDatenschutzbrief.isVersendet) &&
        Objects.equals(this.nachname, massendruckDatenschutzbrief.nachname) &&
        Objects.equals(this.vorname, massendruckDatenschutzbrief.vorname) &&
        Objects.equals(this.gesuchNummer, massendruckDatenschutzbrief.gesuchNummer) &&
        Objects.equals(this.gesuchId, massendruckDatenschutzbrief.gesuchId) &&
        Objects.equals(this.gesuchTrancheId, massendruckDatenschutzbrief.gesuchTrancheId) &&
        Objects.equals(this.elternTyp, massendruckDatenschutzbrief.elternTyp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, isVersendet, nachname, vorname, gesuchNummer, gesuchId, gesuchTrancheId, elternTyp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class MassendruckDatenschutzbriefDto {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    isVersendet: ").append(toIndentedString(isVersendet)).append("\n");
    sb.append("    nachname: ").append(toIndentedString(nachname)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchTrancheId: ").append(toIndentedString(gesuchTrancheId)).append("\n");
    sb.append("    elternTyp: ").append(toIndentedString(elternTyp)).append("\n");
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

