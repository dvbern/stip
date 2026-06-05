package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.GesuchTrancheSlimDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("GesuchAenderungs")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)

public class GesuchAenderungsDto  implements Serializable {
  private @Valid List<GesuchTrancheSlimDto> manuell = new ArrayList<>();
  private @Valid List<GesuchTrancheSlimDto> akzeptiert = new ArrayList<>();
  private @Valid List<GesuchTrancheSlimDto> abgelehnt = new ArrayList<>();
  private @Valid Boolean canAenderungEinreichen;
  private @Valid GesuchTrancheSlimDto offen;

  /**
   **/
  public GesuchAenderungsDto manuell(List<GesuchTrancheSlimDto> manuell) {
    this.manuell = manuell;
    return this;
  }

  
  @JsonProperty("manuell")
  @NotNull
  public List<GesuchTrancheSlimDto> getManuell() {
    return manuell;
  }

  @JsonProperty("manuell")
  public void setManuell(List<GesuchTrancheSlimDto> manuell) {
    this.manuell = manuell;
  }

  public GesuchAenderungsDto addManuellItem(GesuchTrancheSlimDto manuellItem) {
    if (this.manuell == null) {
      this.manuell = new ArrayList<>();
    }

    this.manuell.add(manuellItem);
    return this;
  }

  public GesuchAenderungsDto removeManuellItem(GesuchTrancheSlimDto manuellItem) {
    if (manuellItem != null && this.manuell != null) {
      this.manuell.remove(manuellItem);
    }

    return this;
  }
  /**
   **/
  public GesuchAenderungsDto akzeptiert(List<GesuchTrancheSlimDto> akzeptiert) {
    this.akzeptiert = akzeptiert;
    return this;
  }

  
  @JsonProperty("akzeptiert")
  @NotNull
  public List<GesuchTrancheSlimDto> getAkzeptiert() {
    return akzeptiert;
  }

  @JsonProperty("akzeptiert")
  public void setAkzeptiert(List<GesuchTrancheSlimDto> akzeptiert) {
    this.akzeptiert = akzeptiert;
  }

  public GesuchAenderungsDto addAkzeptiertItem(GesuchTrancheSlimDto akzeptiertItem) {
    if (this.akzeptiert == null) {
      this.akzeptiert = new ArrayList<>();
    }

    this.akzeptiert.add(akzeptiertItem);
    return this;
  }

  public GesuchAenderungsDto removeAkzeptiertItem(GesuchTrancheSlimDto akzeptiertItem) {
    if (akzeptiertItem != null && this.akzeptiert != null) {
      this.akzeptiert.remove(akzeptiertItem);
    }

    return this;
  }
  /**
   **/
  public GesuchAenderungsDto abgelehnt(List<GesuchTrancheSlimDto> abgelehnt) {
    this.abgelehnt = abgelehnt;
    return this;
  }

  
  @JsonProperty("abgelehnt")
  @NotNull
  public List<GesuchTrancheSlimDto> getAbgelehnt() {
    return abgelehnt;
  }

  @JsonProperty("abgelehnt")
  public void setAbgelehnt(List<GesuchTrancheSlimDto> abgelehnt) {
    this.abgelehnt = abgelehnt;
  }

  public GesuchAenderungsDto addAbgelehntItem(GesuchTrancheSlimDto abgelehntItem) {
    if (this.abgelehnt == null) {
      this.abgelehnt = new ArrayList<>();
    }

    this.abgelehnt.add(abgelehntItem);
    return this;
  }

  public GesuchAenderungsDto removeAbgelehntItem(GesuchTrancheSlimDto abgelehntItem) {
    if (abgelehntItem != null && this.abgelehnt != null) {
      this.abgelehnt.remove(abgelehntItem);
    }

    return this;
  }
  /**
   **/
  public GesuchAenderungsDto canAenderungEinreichen(Boolean canAenderungEinreichen) {
    this.canAenderungEinreichen = canAenderungEinreichen;
    return this;
  }

  
  @JsonProperty("canAenderungEinreichen")
  @NotNull
  public Boolean getCanAenderungEinreichen() {
    return canAenderungEinreichen;
  }

  @JsonProperty("canAenderungEinreichen")
  public void setCanAenderungEinreichen(Boolean canAenderungEinreichen) {
    this.canAenderungEinreichen = canAenderungEinreichen;
  }

  /**
   **/
  public GesuchAenderungsDto offen(GesuchTrancheSlimDto offen) {
    this.offen = offen;
    return this;
  }

  
  @JsonProperty("offen")
  public GesuchTrancheSlimDto getOffen() {
    return offen;
  }

  @JsonProperty("offen")
  public void setOffen(GesuchTrancheSlimDto offen) {
    this.offen = offen;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchAenderungsDto gesuchAenderungs = (GesuchAenderungsDto) o;
    return Objects.equals(this.manuell, gesuchAenderungs.manuell) &&
        Objects.equals(this.akzeptiert, gesuchAenderungs.akzeptiert) &&
        Objects.equals(this.abgelehnt, gesuchAenderungs.abgelehnt) &&
        Objects.equals(this.canAenderungEinreichen, gesuchAenderungs.canAenderungEinreichen) &&
        Objects.equals(this.offen, gesuchAenderungs.offen);
  }

  @Override
  public int hashCode() {
    return Objects.hash(manuell, akzeptiert, abgelehnt, canAenderungEinreichen, offen);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchAenderungsDto {\n");
    
    sb.append("    manuell: ").append(toIndentedString(manuell)).append("\n");
    sb.append("    akzeptiert: ").append(toIndentedString(akzeptiert)).append("\n");
    sb.append("    abgelehnt: ").append(toIndentedString(abgelehnt)).append("\n");
    sb.append("    canAenderungEinreichen: ").append(toIndentedString(canAenderungEinreichen)).append("\n");
    sb.append("    offen: ").append(toIndentedString(offen)).append("\n");
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

