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



@JsonTypeName("FailedAuszahlungBuchhaltung")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", comments = "Generator version: 7.25.0")@lombok.AllArgsConstructor
@org.eclipse.microprofile.openapi.annotations.media.Schema(hidden=true)
@org.jilt.Builder(style = org.jilt.BuilderStyle.STAGED)

public class FailedAuszahlungBuchhaltungDto  implements Serializable {
  private UUID fallId;
  private String fallNummer;
  private UUID gesuchId;
  private String gesuchNummer;
  private String name;
  private String vorname;
  private java.time.LocalDateTime lastTryDate;

  protected FailedAuszahlungBuchhaltungDto(FailedAuszahlungBuchhaltungDtoBuilder<?, ?> b) {
    this.fallId = b.fallId;
    this.fallNummer = b.fallNummer;
    this.gesuchId = b.gesuchId;
    this.gesuchNummer = b.gesuchNummer;
    this.name = b.name;
    this.vorname = b.vorname;
    this.lastTryDate = b.lastTryDate;
  }

  public FailedAuszahlungBuchhaltungDto() {
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto fallId(UUID fallId) {
    this.fallId = fallId;
    return this;
  }

  
  @JsonProperty(required = true, value = "fallId")
  @NotNull public UUID getFallId() {
    return fallId;
  }

  @JsonProperty(required = true, value = "fallId")
  public void setFallId(UUID fallId) {
    this.fallId = fallId;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto fallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "fallNummer")
  @NotNull public String getFallNummer() {
    return fallNummer;
  }

  @JsonProperty(required = true, value = "fallNummer")
  public void setFallNummer(String fallNummer) {
    this.fallNummer = fallNummer;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto gesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchId")
  @NotNull public UUID getGesuchId() {
    return gesuchId;
  }

  @JsonProperty(required = true, value = "gesuchId")
  public void setGesuchId(UUID gesuchId) {
    this.gesuchId = gesuchId;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto gesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
    return this;
  }

  
  @JsonProperty(required = true, value = "gesuchNummer")
  @NotNull public String getGesuchNummer() {
    return gesuchNummer;
  }

  @JsonProperty(required = true, value = "gesuchNummer")
  public void setGesuchNummer(String gesuchNummer) {
    this.gesuchNummer = gesuchNummer;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto name(String name) {
    this.name = name;
    return this;
  }

  
  @JsonProperty(required = true, value = "name")
  @NotNull public String getName() {
    return name;
  }

  @JsonProperty(required = true, value = "name")
  public void setName(String name) {
    this.name = name;
  }

  /**
   **/
  public FailedAuszahlungBuchhaltungDto vorname(String vorname) {
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
  public FailedAuszahlungBuchhaltungDto lastTryDate(java.time.LocalDateTime lastTryDate) {
    this.lastTryDate = lastTryDate;
    return this;
  }

  
  @JsonProperty(required = true, value = "lastTryDate")
  @NotNull public java.time.LocalDateTime getLastTryDate() {
    return lastTryDate;
  }

  @JsonProperty(required = true, value = "lastTryDate")
  public void setLastTryDate(java.time.LocalDateTime lastTryDate) {
    this.lastTryDate = lastTryDate;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FailedAuszahlungBuchhaltungDto failedAuszahlungBuchhaltung = (FailedAuszahlungBuchhaltungDto) o;
    return Objects.equals(this.fallId, failedAuszahlungBuchhaltung.fallId) &&
        Objects.equals(this.fallNummer, failedAuszahlungBuchhaltung.fallNummer) &&
        Objects.equals(this.gesuchId, failedAuszahlungBuchhaltung.gesuchId) &&
        Objects.equals(this.gesuchNummer, failedAuszahlungBuchhaltung.gesuchNummer) &&
        Objects.equals(this.name, failedAuszahlungBuchhaltung.name) &&
        Objects.equals(this.vorname, failedAuszahlungBuchhaltung.vorname) &&
        Objects.equals(this.lastTryDate, failedAuszahlungBuchhaltung.lastTryDate);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fallId, fallNummer, gesuchId, gesuchNummer, name, vorname, lastTryDate);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class FailedAuszahlungBuchhaltungDto {\n");
    
    sb.append("    fallId: ").append(toIndentedString(fallId)).append("\n");
    sb.append("    fallNummer: ").append(toIndentedString(fallNummer)).append("\n");
    sb.append("    gesuchId: ").append(toIndentedString(gesuchId)).append("\n");
    sb.append("    gesuchNummer: ").append(toIndentedString(gesuchNummer)).append("\n");
    sb.append("    name: ").append(toIndentedString(name)).append("\n");
    sb.append("    vorname: ").append(toIndentedString(vorname)).append("\n");
    sb.append("    lastTryDate: ").append(toIndentedString(lastTryDate)).append("\n");
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


  public static FailedAuszahlungBuchhaltungDtoBuilder<?, ?> builder() {
    return new FailedAuszahlungBuchhaltungDtoBuilderImpl();
  }

  private static final class FailedAuszahlungBuchhaltungDtoBuilderImpl extends FailedAuszahlungBuchhaltungDtoBuilder<FailedAuszahlungBuchhaltungDto, FailedAuszahlungBuchhaltungDtoBuilderImpl> {

    @Override
    protected FailedAuszahlungBuchhaltungDtoBuilderImpl self() {
      return this;
    }

    @Override
    public FailedAuszahlungBuchhaltungDto build() {
      return new FailedAuszahlungBuchhaltungDto(this);
    }
  }

  public static abstract class FailedAuszahlungBuchhaltungDtoBuilder<C extends FailedAuszahlungBuchhaltungDto, B extends FailedAuszahlungBuchhaltungDtoBuilder<C, B>>  {
    private UUID fallId;
    private String fallNummer;
    private UUID gesuchId;
    private String gesuchNummer;
    private String name;
    private String vorname;
    private java.time.LocalDateTime lastTryDate;
    protected abstract B self();

    public abstract C build();

    public B fallId(UUID fallId) {
      this.fallId = fallId;
      return self();
    }
    public B fallNummer(String fallNummer) {
      this.fallNummer = fallNummer;
      return self();
    }
    public B gesuchId(UUID gesuchId) {
      this.gesuchId = gesuchId;
      return self();
    }
    public B gesuchNummer(String gesuchNummer) {
      this.gesuchNummer = gesuchNummer;
      return self();
    }
    public B name(String name) {
      this.name = name;
      return self();
    }
    public B vorname(String vorname) {
      this.vorname = vorname;
      return self();
    }
    public B lastTryDate(java.time.LocalDateTime lastTryDate) {
      this.lastTryDate = lastTryDate;
      return self();
    }
  }
}
