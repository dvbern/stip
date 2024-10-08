/*
 * STIP API
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 1.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */


package ch.dvbern.stip.generated.dto;

import java.util.Objects;
import java.util.Arrays;
import ch.dvbern.stip.generated.dto.GesuchFormularDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheStatusDtoSpec;
import ch.dvbern.stip.generated.dto.GesuchTrancheTypDtoSpec;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonValue;
import java.time.LocalDate;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

/**
 * GesuchTrancheDtoSpec
 */
@JsonPropertyOrder({
  GesuchTrancheDtoSpec.JSON_PROPERTY_ID,
  GesuchTrancheDtoSpec.JSON_PROPERTY_GUELTIG_AB,
  GesuchTrancheDtoSpec.JSON_PROPERTY_GUELTIG_BIS,
  GesuchTrancheDtoSpec.JSON_PROPERTY_COMMENT,
  GesuchTrancheDtoSpec.JSON_PROPERTY_GESUCH_FORMULAR,
  GesuchTrancheDtoSpec.JSON_PROPERTY_STATUS,
  GesuchTrancheDtoSpec.JSON_PROPERTY_TYP
})
@JsonTypeName("GesuchTranche")
@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen")
public class GesuchTrancheDtoSpec {
  public static final String JSON_PROPERTY_ID = "id";
  private UUID id;

  public static final String JSON_PROPERTY_GUELTIG_AB = "gueltigAb";
  private LocalDate gueltigAb;

  public static final String JSON_PROPERTY_GUELTIG_BIS = "gueltigBis";
  private LocalDate gueltigBis;

  public static final String JSON_PROPERTY_COMMENT = "comment";
  private String comment;

  public static final String JSON_PROPERTY_GESUCH_FORMULAR = "gesuchFormular";
  private GesuchFormularDtoSpec gesuchFormular;

  public static final String JSON_PROPERTY_STATUS = "status";
  private GesuchTrancheStatusDtoSpec status;

  public static final String JSON_PROPERTY_TYP = "typ";
  private GesuchTrancheTypDtoSpec typ;

  public GesuchTrancheDtoSpec() {
  }

  public GesuchTrancheDtoSpec id(UUID id) {
    
    this.id = id;
    return this;
  }

   /**
   * Get id
   * @return id
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public UUID getId() {
    return id;
  }


  @JsonProperty(JSON_PROPERTY_ID)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setId(UUID id) {
    this.id = id;
  }


  public GesuchTrancheDtoSpec gueltigAb(LocalDate gueltigAb) {
    
    this.gueltigAb = gueltigAb;
    return this;
  }

   /**
   * Get gueltigAb
   * @return gueltigAb
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GUELTIG_AB)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGueltigAb() {
    return gueltigAb;
  }


  @JsonProperty(JSON_PROPERTY_GUELTIG_AB)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGueltigAb(LocalDate gueltigAb) {
    this.gueltigAb = gueltigAb;
  }


  public GesuchTrancheDtoSpec gueltigBis(LocalDate gueltigBis) {
    
    this.gueltigBis = gueltigBis;
    return this;
  }

   /**
   * Get gueltigBis
   * @return gueltigBis
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_GUELTIG_BIS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public LocalDate getGueltigBis() {
    return gueltigBis;
  }


  @JsonProperty(JSON_PROPERTY_GUELTIG_BIS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setGueltigBis(LocalDate gueltigBis) {
    this.gueltigBis = gueltigBis;
  }


  public GesuchTrancheDtoSpec comment(String comment) {
    
    this.comment = comment;
    return this;
  }

   /**
   * Get comment
   * @return comment
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_COMMENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public String getComment() {
    return comment;
  }


  @JsonProperty(JSON_PROPERTY_COMMENT)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setComment(String comment) {
    this.comment = comment;
  }


  public GesuchTrancheDtoSpec gesuchFormular(GesuchFormularDtoSpec gesuchFormular) {
    
    this.gesuchFormular = gesuchFormular;
    return this;
  }

   /**
   * Get gesuchFormular
   * @return gesuchFormular
  **/
  @jakarta.annotation.Nullable
  @JsonProperty(JSON_PROPERTY_GESUCH_FORMULAR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)

  public GesuchFormularDtoSpec getGesuchFormular() {
    return gesuchFormular;
  }


  @JsonProperty(JSON_PROPERTY_GESUCH_FORMULAR)
  @JsonInclude(value = JsonInclude.Include.USE_DEFAULTS)
  public void setGesuchFormular(GesuchFormularDtoSpec gesuchFormular) {
    this.gesuchFormular = gesuchFormular;
  }


  public GesuchTrancheDtoSpec status(GesuchTrancheStatusDtoSpec status) {
    
    this.status = status;
    return this;
  }

   /**
   * Get status
   * @return status
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchTrancheStatusDtoSpec getStatus() {
    return status;
  }


  @JsonProperty(JSON_PROPERTY_STATUS)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setStatus(GesuchTrancheStatusDtoSpec status) {
    this.status = status;
  }


  public GesuchTrancheDtoSpec typ(GesuchTrancheTypDtoSpec typ) {
    
    this.typ = typ;
    return this;
  }

   /**
   * Get typ
   * @return typ
  **/
  @jakarta.annotation.Nonnull
  @JsonProperty(JSON_PROPERTY_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)

  public GesuchTrancheTypDtoSpec getTyp() {
    return typ;
  }


  @JsonProperty(JSON_PROPERTY_TYP)
  @JsonInclude(value = JsonInclude.Include.ALWAYS)
  public void setTyp(GesuchTrancheTypDtoSpec typ) {
    this.typ = typ;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GesuchTrancheDtoSpec gesuchTranche = (GesuchTrancheDtoSpec) o;
    return Objects.equals(this.id, gesuchTranche.id) &&
        Objects.equals(this.gueltigAb, gesuchTranche.gueltigAb) &&
        Objects.equals(this.gueltigBis, gesuchTranche.gueltigBis) &&
        Objects.equals(this.comment, gesuchTranche.comment) &&
        Objects.equals(this.gesuchFormular, gesuchTranche.gesuchFormular) &&
        Objects.equals(this.status, gesuchTranche.status) &&
        Objects.equals(this.typ, gesuchTranche.typ);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, gueltigAb, gueltigBis, comment, gesuchFormular, status, typ);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class GesuchTrancheDtoSpec {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    gueltigAb: ").append(toIndentedString(gueltigAb)).append("\n");
    sb.append("    gueltigBis: ").append(toIndentedString(gueltigBis)).append("\n");
    sb.append("    comment: ").append(toIndentedString(comment)).append("\n");
    sb.append("    gesuchFormular: ").append(toIndentedString(gesuchFormular)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    typ: ").append(toIndentedString(typ)).append("\n");
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

