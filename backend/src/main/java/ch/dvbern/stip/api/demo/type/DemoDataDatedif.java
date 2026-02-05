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

package ch.dvbern.stip.api.demo.type;

import java.time.temporal.ChronoUnit;

import jakarta.ws.rs.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.LazyRefEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellAddress;

/**
 * Apache POI doesn't have an implementation for DATEDIF
 *
 * This wrapper only works for DATEDIF(CELL_REF->DATE,DATE,"Y")
 */
@Slf4j
public class DemoDataDatedif implements Function {
    private static final String DATEDIF_ERROR =
        "Current DATEDIF implementation only works with DATEDIF(CELL_REF->DATE, DATE, \"Y\")";

    @Override
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 3) {
            throw new BadRequestException(DATEDIF_ERROR);
        }

        final var date1Raw = args[0];
        final var date2Raw = args[1];
        final var datePartRaw = args[2];

        if (!(datePartRaw instanceof StringEval datePart) || !datePart.getStringValue().equalsIgnoreCase("y")) {
            throw new BadRequestException(
                "%s\nCell (unknown sheet) [%s]".formatted(DATEDIF_ERROR, new CellAddress(srcRowIndex, srcColumnIndex))
            );
        }

        try {
            final var date1 = DateUtil.getLocalDateTime(tryGetNumberEvalOrRef(date1Raw).getNumberValue()).toLocalDate();
            final var date2 = DateUtil.getLocalDateTime(tryGetNumberEvalOrRef(date2Raw).getNumberValue()).toLocalDate();

            return new NumberEval(ChronoUnit.YEARS.between(date1, date2));
        } catch (BadRequestException e) {
            throw new BadRequestException(
                "%s\n%sCell (unknown sheet) [%s]"
                    .formatted(e.getMessage(), new CellAddress(srcRowIndex, srcColumnIndex))
            );
        }
    }

    private NumberEval tryGetNumberEval(ValueEval eval) {
        if (eval instanceof NumberEval numberEval) {
            return numberEval;
        }
        if (eval instanceof BlankEval) {
            return NumberEval.ZERO;
        }
        if (eval instanceof StringEval stringValue && stringValue.getStringValue().trim().isEmpty()) {
            return NumberEval.ZERO;
        }
        throw new BadRequestException(DATEDIF_ERROR);
    }

    private NumberEval tryGetNumberEvalOrRef(ValueEval eval) {
        if (eval instanceof LazyRefEval lazyRef) {
            try {
                return tryGetNumberEval(lazyRef.getInnerValueEvalForFirstSheet());
            } catch (BadRequestException e) {
                throw new BadRequestException(
                    "%s\nRef (unknown sheet) [%s]"
                        .formatted(DATEDIF_ERROR, new CellAddress(lazyRef.getRow(), lazyRef.getColumn()))
                );
            }
        }
        return tryGetNumberEval(eval);
    }
}
