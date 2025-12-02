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

package ch.dvbern.stip.api.benutzer.entity;

import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Execution(ExecutionMode.CONCURRENT)
class BuchstabenRangeUtilTest {
    @ParameterizedTest
    @CsvSource(
        {
            "A-Z,Schweizer,true",
            "AA-ZZ,Schweizer,true",
            "AAA-ZZZ,Schweizer,true",
            "A-Z,schweizer,true",
            "A-Z,Mueller,true",
            "A-A,Schweizer,false",
            "A-Z,Yi,true",
            "AA-ZZ,Yi,true",
            "AAA-ZZZ,Yi,true",
            "A-B,Yi,false",
            "AA-BB,Yi,false",
            "AAA-BBB,Yi,false",
            "A-AA,Aaron,true",
            "A-AB,Abbuehl,true",
            "'A,F',Federer,true",
            "'A',Federer,false",
            "'A',Abbuehl,true",
            "'A,F-X',Federer,true",
            "'C,F-X',Federer,true",
            "'A,F-X',Berchtold,false",
            "'SCA-SCG,SCI-Z',Schweizer,false",
            "A-Z,Äbi,true",
            "B-Z,Äbi,false",
            "'A,B',Äbi,true",
            "'B,C',Äbi,false",
            "AAA-Z,Äbi,true",
            "A-Z,Śchweizer,true",
            "'SCA-SCG,SCI-Z',Śchweizer,false",
            "O,Ølund,false"
        }
    )
    void containsTest(final String range, final String test, final boolean expected) {
        assertThat(BuchstabenRangeUtil.contains(range, test), is(expected));
    }

    @ParameterizedTest
    @CsvSource(
        {
            "A-Z,1",
            "AA-ZZ,1",
            "AAA-ZZZ,1",
            "AA-Z,1",
            "AAA-Z,1",
            "A-ZZ,1",
            "A-ZZZ,1",
            "AA-ZZ,1",
            "AAA-ZZ,1",
            "AA-ZZZ,1",
            "A-M,1",
            "A,1",
            "'A,B',2",
            "'A-F,I-X',2",
            "'A,B,C',3",
            "'A,B,D-X',3"
        }
    )
    void parseTest(final String range, final int expected) {
        assertThat(BuchstabenRangeUtil.BuchstabenRange.parse(range).getPairs().size(), is(expected));
    }
}
