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

package ch.dvbern.stip.api.common.util;

import java.util.List;

import ch.dvbern.stip.api.common.authorization.AusbildungAuthorizer;
import ch.dvbern.stip.api.common.exception.ValidationsException;
import ch.dvbern.stip.api.gesuchformular.validation.GesuchEinreichenValidationGroup;
import ch.dvbern.stip.api.gesuchtranche.service.GesuchTrancheMapper;
import ch.dvbern.stip.api.gesuchtranche.util.GesuchTrancheCopyUtil;
import ch.dvbern.stip.api.util.TestUtil;
import com.savoirtech.json.JsonComparatorBuilder;
import io.quarkus.test.junit.QuarkusMock;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_DOCUMENTS_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMENKOSTEN_VERANLAGUNGSTATUS_INVALID_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_EINNAHMEN_KOSTEN_WG_WOHNEND_REQUIRED_MESSAGE;
import static ch.dvbern.stip.api.common.validation.ValidationsConstant.VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.oneOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class CopyUtilTest {
    @Inject
    Validator validator;

    @Inject
    GesuchTrancheMapper gesuchTrancheMapper;

    @BeforeAll
    static void setUp() {
        var ausbildungAuthorizerMock = Mockito.mock(AusbildungAuthorizer.class);
        Mockito.when(ausbildungAuthorizerMock.canUpdateCheck(Mockito.any())).thenReturn(true);
        QuarkusMock.installMockForType(ausbildungAuthorizerMock, AusbildungAuthorizer.class);
    }

    @Test
    void testCopy() throws JsonProcessingException {
        final var gesuch = TestUtil.getFullGesuch();

        try {
            ValidatorUtil.validate(validator, gesuch, List.of(GesuchEinreichenValidationGroup.class));
        } catch (ValidationsException e) {
            e.getViolations()
                .forEach(
                    constraintViolation -> assertThat(
                        constraintViolation.getMessageTemplate(),
                        is(
                            oneOf(
                                VALIDATION_DOCUMENTS_REQUIRED_MESSAGE,
                                VALIDATION_EINNAHMEN_KOSTEN_WG_WOHNEND_REQUIRED_MESSAGE,
                                VALIDATION_LEBENSLAUF_LUCKENLOS_MESSAGE,
                                VALIDATION_EINNAHMENKOSTEN_VERANLAGUNGSTATUS_INVALID_MESSAGE
                            )
                        )
                    )
                );
        }

        var tranche = gesuch.getNewestGesuchTranche().get();
        tranche.setId(null);
        tranche.setTyp(null);

        var trancheDto = gesuchTrancheMapper.toDto(tranche);
        var trancheCopy = GesuchTrancheCopyUtil.copyTranche(tranche, tranche.getGueltigkeit(), tranche.getComment());
        var copyDto = gesuchTrancheMapper.toDto(trancheCopy);

        final var expected = "{\"templateJson\":" + new ObjectMapper().writeValueAsString(trancheDto) + '}';
        final var actual = new ObjectMapper().writeValueAsString(copyDto);
        final var comparator = new JsonComparatorBuilder().build();

        final var result = comparator.compare(expected, actual);

        assertTrue(result.isMatch(), result.getErrorMessage());
        assertThat(trancheDto.toString(), equalTo(copyDto.toString()));
    }
}
