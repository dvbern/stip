/*
 * Copyright (C) 2023 DV Bern AG, Switzerland
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.quarkus.exception;

import java.util.List;

import ch.dvbern.stip.api.common.exception.ExceptionId;
import ch.dvbern.stip.api.common.exception.FailureType;
import com.fasterxml.jackson.databind.JsonMappingException;
import lombok.Getter;

@Getter
class AppFailureErrorResponse extends AppErrorResponse {
    private final ExceptionId id;
    private final String summary;

    public AppFailureErrorResponse(
    ExceptionId id,
    String summary,
    List<JsonMappingException.Reference> referenceList
    ) {
        this(id, summary, JsonMappingReferenceParser.fromReferenceList(referenceList).parse());
    }

    public AppFailureErrorResponse(ExceptionId id, String summary) {
        super(FailureType.FAILURE);
        this.id = id;
        this.summary = summary;
    }

    public AppFailureErrorResponse(
    ExceptionId id,
    String summary,
    String detail
    ) {
        super(FailureType.FAILURE);
        this.id = id;
        this.summary = summary + ", detail: " + detail;
    }
}
