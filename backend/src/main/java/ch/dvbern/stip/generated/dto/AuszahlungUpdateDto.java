package ch.dvbern.stip.generated.dto;

import ch.dvbern.stip.generated.dto.ZahlungsverbindungDto;
import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.annotation.JsonTypeName;



@JsonTypeName("AuszahlungUpdate")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class AuszahlungUpdateDto  implements Serializable {
  private @Valid Boolean auszahlungAnSozialdienst;
  private @Valid ZahlungsverbindungDto zahlungsverbindung;

  protected AuszahlungUpdateDto(AuszahlungUpdateDtoBuilder<?, ?> b) {
    this.auszahlungAnSozialdienst = b.auszahlungAnSozialdienst;
    this.zahlungsverbindung = b.zahlungsverbindung;
  }

  public AuszahlungUpdateDto() {
  }

  /**
   **/
  public AuszahlungUpdateDto auszahlungAnSozialdienst(Boolean auszahlungAnSozialdienst) {
    this.auszahlungAnSozialdienst = auszahlungAnSozialdienst;
    return this;
  }

  
  @JsonProperty("auszahlungAnSozialdienst")
  @NotNull
  public Boolean getAuszahlungAnSozialdienst() {
    return auszahlungAnSozialdienst;
  }

  @JsonProperty("auszahlungAnSozialdienst")
  public void setAuszahlungAnSozialdienst(Boolean auszahlungAnSozialdienst) {
    this.auszahlungAnSozialdienst = auszahlungAnSozialdienst;
  }

  /**
   **/
  public AuszahlungUpdateDto zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
    return this;
  }

  
  @JsonProperty("zahlungsverbindung")
  public ZahlungsverbindungDto getZahlungsverbindung() {
    return zahlungsverbindung;
  }

  @JsonProperty("zahlungsverbindung")
  public void setZahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
    this.zahlungsverbindung = zahlungsverbindung;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    AuszahlungUpdateDto auszahlungUpdate = (AuszahlungUpdateDto) o;
    return Objects.equals(this.auszahlungAnSozialdienst, auszahlungUpdate.auszahlungAnSozialdienst) &&
        Objects.equals(this.zahlungsverbindung, auszahlungUpdate.zahlungsverbindung);
  }

  @Override
  public int hashCode() {
    return Objects.hash(auszahlungAnSozialdienst, zahlungsverbindung);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class AuszahlungUpdateDto {\n");
    
    sb.append("    auszahlungAnSozialdienst: ").append(toIndentedString(auszahlungAnSozialdienst)).append("\n");
    sb.append("    zahlungsverbindung: ").append(toIndentedString(zahlungsverbindung)).append("\n");
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


  public static AuszahlungUpdateDtoBuilder<?, ?> builder() {
    return new AuszahlungUpdateDtoBuilderImpl();
  }

  private static final class AuszahlungUpdateDtoBuilderImpl extends AuszahlungUpdateDtoBuilder<AuszahlungUpdateDto, AuszahlungUpdateDtoBuilderImpl> {

    @Override
    protected AuszahlungUpdateDtoBuilderImpl self() {
      return this;
    }

    @Override
    public AuszahlungUpdateDto build() {
      return new AuszahlungUpdateDto(this);
    }
  }

  public static abstract class AuszahlungUpdateDtoBuilder<C extends AuszahlungUpdateDto, B extends AuszahlungUpdateDtoBuilder<C, B>>  {
    private Boolean auszahlungAnSozialdienst;
    private ZahlungsverbindungDto zahlungsverbindung;
    protected abstract B self();

    public abstract C build();

    public B auszahlungAnSozialdienst(Boolean auszahlungAnSozialdienst) {
      this.auszahlungAnSozialdienst = auszahlungAnSozialdienst;
      return self();
    }
    public B zahlungsverbindung(ZahlungsverbindungDto zahlungsverbindung) {
      this.zahlungsverbindung = zahlungsverbindung;
      return self();
    }
  }
}

