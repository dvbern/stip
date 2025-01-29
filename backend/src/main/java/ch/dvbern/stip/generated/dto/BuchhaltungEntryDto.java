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



@JsonTypeName("BuchhaltungEntry")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class BuchhaltungEntryDto  implements Serializable {
  private @Valid java.time.LocalDateTime timestampErstellt;
  private @Valid ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType buchhaltungType;
  private @Valid Integer saldoAenderung;
  private @Valid Integer saldo;
  private @Valid String comment;
  private @Valid Integer stipendienBetrag;
  private @Valid Integer auszahlung;
  private @Valid Integer rueckforderung;
  private @Valid Integer sapId;
  private @Valid ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus;
  private @Valid UUID verfuegungId;
  private @Valid UUID gesuchId;

  /**
   **/
  public BuchhaltungEntryDto timestampErstellt(java.time.LocalDateTime timestampErstellt) {
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
  public BuchhaltungEntryDto buchhaltungType(ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType buchhaltungType) {
    this.buchhaltungType = buchhaltungType;
    return this;
  }

  
  @JsonProperty("buchhaltungType")
  @NotNull
  public ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType getBuchhaltungType() {
    return buchhaltungType;
  }

  @JsonProperty("buchhaltungType")
  public void setBuchhaltungType(ch.dvbern.stip.api.buchhaltung.type.BuchhaltungType buchhaltungType) {
    this.buchhaltungType = buchhaltungType;
  }

  /**
   **/
  public BuchhaltungEntryDto saldoAenderung(Integer saldoAenderung) {
    this.saldoAenderung = saldoAenderung;
    return this;
  }

  
  @JsonProperty("saldoAenderung")
  @NotNull
  public Integer getSaldoAenderung() {
    return saldoAenderung;
  }

  @JsonProperty("saldoAenderung")
  public void setSaldoAenderung(Integer saldoAenderung) {
    this.saldoAenderung = saldoAenderung;
  }

  /**
   **/
  public BuchhaltungEntryDto saldo(Integer saldo) {
    this.saldo = saldo;
    return this;
  }

  
  @JsonProperty("saldo")
  @NotNull
  public Integer getSaldo() {
    return saldo;
  }

  @JsonProperty("saldo")
  public void setSaldo(Integer saldo) {
    this.saldo = saldo;
  }

  /**
   **/
  public BuchhaltungEntryDto comment(String comment) {
    this.comment = comment;
    return this;
  }

  
  @JsonProperty("comment")
  @NotNull
  public String getComment() {
    return comment;
  }

  @JsonProperty("comment")
  public void setComment(String comment) {
    this.comment = comment;
  }

  /**
   **/
  public BuchhaltungEntryDto stipendienBetrag(Integer stipendienBetrag) {
    this.stipendienBetrag = stipendienBetrag;
    return this;
  }

  
  @JsonProperty("stipendienBetrag")
  public Integer getStipendienBetrag() {
    return stipendienBetrag;
  }

  @JsonProperty("stipendienBetrag")
  public void setStipendienBetrag(Integer stipendienBetrag) {
    this.stipendienBetrag = stipendienBetrag;
  }

  /**
   **/
  public BuchhaltungEntryDto auszahlung(Integer auszahlung) {
    this.auszahlung = auszahlung;
    return this;
  }

  
  @JsonProperty("auszahlung")
  public Integer getAuszahlung() {
    return auszahlung;
  }

  @JsonProperty("auszahlung")
  public void setAuszahlung(Integer auszahlung) {
    this.auszahlung = auszahlung;
  }

  /**
   **/
  public BuchhaltungEntryDto rueckforderung(Integer rueckforderung) {
    this.rueckforderung = rueckforderung;
    return this;
  }

  
  @JsonProperty("rueckforderung")
  public Integer getRueckforderung() {
    return rueckforderung;
  }

  @JsonProperty("rueckforderung")
  public void setRueckforderung(Integer rueckforderung) {
    this.rueckforderung = rueckforderung;
  }

  /**
   **/
  public BuchhaltungEntryDto sapId(Integer sapId) {
    this.sapId = sapId;
    return this;
  }

  
  @JsonProperty("sapId")
  public Integer getSapId() {
    return sapId;
  }

  @JsonProperty("sapId")
  public void setSapId(Integer sapId) {
    this.sapId = sapId;
  }

  /**
   **/
  public BuchhaltungEntryDto sapStatus(ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus) {
    this.sapStatus = sapStatus;
    return this;
  }

  
  @JsonProperty("sapStatus")
  public ch.dvbern.stip.api.buchhaltung.type.SapStatus getSapStatus() {
    return sapStatus;
  }

  @JsonProperty("sapStatus")
  public void setSapStatus(ch.dvbern.stip.api.buchhaltung.type.SapStatus sapStatus) {
    this.sapStatus = sapStatus;
  }

  /**
   **/
  public BuchhaltungEntryDto verfuegungId(UUID verfuegungId) {
    this.verfuegungId = verfuegungId;
    return this;
  }

  
  @JsonProperty("verfuegungId")
  public UUID getVerfuegungId() {
    return verfuegungId;
  }

  @JsonProperty("verfuegungId")
  public void setVerfuegungId(UUID verfuegungId) {
    this.verfuegungId = verfuegungId;
  }

  /**
   **/
  public BuchhaltungEntryDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty("gesuchId")
  public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty("gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BuchhaltungEntryDto buchhaltungEntry = (BuchhaltungEntryDto) o;
    return Objects.equals(this.timestampErstellt, buchhaltungEntry.timestampErstellt) &&
        Objects.equals(this.buchhaltungType, buchhaltungEntry.buchhaltungType) &&
        Objects.equals(this.saldoAenderung, buchhaltungEntry.saldoAenderung) &&
        Objects.equals(this.saldo, buchhaltungEntry.saldo) &&
        Objects.equals(this.comment, buchhaltungEntry.comment) &&
        Objects.equals(this.stipendienBetrag, buchhaltungEntry.stipendienBetrag) &&
        Objects.equals(this.auszahlung, buchhaltungEntry.auszahlung) &&
        Objects.equals(this.rueckforderung, buchhaltungEntry.rueckforderung) &&
        Objects.equals(this.sapId, buchhaltungEntry.sapId) &&
        Objects.equals(this.sapStatus, buchhaltungEntry.sapStatus) &&
        Objects.equals(this.verfuegungId, buchhaltungEntry.verfuegungId) &&
        Objects.equals(this.gesuchId, buchhaltungEntry.gesuchId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestampErstellt, buchhaltungType, saldoAenderung, saldo, comment, stipendienBetrag, auszahlung, rueckforderung, sapId, sapStatus, verfuegungId, gesuchId);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BuchhaltungEntryDto {\n");
    
    sb.append("    timestampErstellt: ").append(toIndentedString(timestampErstellt)).append("\n");
    sb.append("    buchhaltungType: ").append(toIndentedString(buchhaltungType)).append("\n");
    sb.append("    saldoAenderung: ").append(toIndentedString(saldoAenderung)).append("\n");
    sb.append("    saldo: ").append(toIndentedString(saldo)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    stipendienBetrag: ").append(toIndentedString(stipendienBetrag)).append("\n");
    sb.append("    auszahlung: ").append(toIndentedString(auszahlung)).append("\n");
    sb.append("    rueckforderung: ").append(toIndentedString(rueckforderung)).append("\n");
    sb.append("    sapId: ").append(toIndentedString(sapId)).append("\n");
    sb.append("    sapStatus: ").append(toIndentedString(sapStatus)).append("\n");
    sb.append("    verfuegungId: ").append(toIndentedString(verfuegungId)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
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

