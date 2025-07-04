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
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonTypeName;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets NotificationType
 */
public enum NotificationTypeDtoSpec {
  
  GESUCH_EINGEREICHT("GESUCH_EINGEREICHT"),
  
  GESUCH_STATUS_CHANGE_WITH_COMMENT("GESUCH_STATUS_CHANGE_WITH_COMMENT"),
  
  FEHLENDE_DOKUMENTE_EINREICHEN("FEHLENDE_DOKUMENTE_EINREICHEN"),
  
  FEHLENDE_DOKUMENTE("FEHLENDE_DOKUMENTE"),
  
  FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT("FEHLENDE_DOKUMENTE_NICHT_EINGEREICHT"),
  
  AENDERUNG_ABGELEHNT("AENDERUNG_ABGELEHNT"),
  
  NEUE_VERFUEGUNG("NEUE_VERFUEGUNG");

  private String value;

  NotificationTypeDtoSpec(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static NotificationTypeDtoSpec fromValue(String value) {
    for (NotificationTypeDtoSpec b : NotificationTypeDtoSpec.values()) {
      if (b.value.equals(value)) {
        return b;
      }
    }
    throw new IllegalArgumentException("Unexpected value '" + value + "'");
  }
}

