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

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.LazyRefEval;
import org.apache.poi.ss.formula.eval.BlankEval;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.formula.eval.NumberEval;
import org.apache.poi.ss.formula.eval.StringEval;
import org.apache.poi.ss.formula.eval.ValueEval;
import org.apache.poi.ss.formula.functions.Function;
import org.apache.poi.ss.usermodel.DateUtil;

/**
 * Apache POI doesn't have an implementation for DATEDIF
 *
 * This wrapper only works for DATEDIF(CELL_REF->DATE,DATE,"Y")
 */
@Slf4j
public class DemoDataDatedif implements Function {
    @Override
    public ValueEval evaluate(ValueEval[] args, int srcRowIndex, int srcColumnIndex) {
        if (args.length != 3) {
            LOG.error("This DATEDIF implementation only works with DATEDIF(CELL_REF->DATE,DATE,\"Y\")");
            return ErrorEval.FUNCTION_NOT_IMPLEMENTED;
        }

        final var date1RefRaw = args[0];
        final var date2Raw = args[1];
        final var datePartRaw = args[2];

        if (!(datePartRaw instanceof StringEval datePart) || !datePart.getStringValue().equalsIgnoreCase("y")) {
            LOG.error("This DATEDIF implementation only works with DATEDIF(CELL_REF->DATE,DATE,\"Y\")");
            return ErrorEval.FUNCTION_NOT_IMPLEMENTED;
        }
        if (!(date2Raw instanceof NumberEval date2Number)) {
            if (date2Raw instanceof BlankEval) {
                return NumberEval.ZERO;
            }
            LOG.error("This DATEDIF implementation only works with DATEDIF(CELL_REF->DATE,DATE,\"Y\")");
            return ErrorEval.FUNCTION_NOT_IMPLEMENTED;
        }
        if (!(date1RefRaw instanceof LazyRefEval date1Ref)) {
            return BlankEval.instance;
        }
        if (!(date1Ref.getInnerValueEvalForFirstSheet() instanceof NumberEval date1Number)) {
            if (date1Ref.getInnerValueEvalForFirstSheet() instanceof BlankEval) {
                return NumberEval.ZERO;
            }
            LOG.error("This DATEDIF implementation only works with DATEDIF(CELL_REF->DATE,DATE,\"Y\")");
            return ErrorEval.FUNCTION_NOT_IMPLEMENTED;
        }

        final var date1 = DateUtil.getLocalDateTime(date1Number.getNumberValue()).toLocalDate();
        final var date2 = DateUtil.getLocalDateTime(date2Number.getNumberValue()).toLocalDate();

        return new NumberEval(ChronoUnit.YEARS.between(date1, date2));
    }
}
