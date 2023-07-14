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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package ch.dvbern.stip.api.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Getter
@Setter
public class DateRange implements Serializable, Comparable<DateRange> {

    @NotNull
    @Column(nullable = false)
    private LocalDate gueltigAb;

    @NotNull
    @Column(nullable = false)
    private LocalDate gueltigBis;

    public DateRange(LocalDate gueltigAb, LocalDate gueltigBis) {
        this.gueltigAb = Objects.requireNonNull(gueltigAb);
        this.gueltigBis = Objects.requireNonNull(gueltigBis);
    }

    public DateRange() {
        this(LocalDate.now(), LocalDate.now());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DateRange)) {
            return false;
        }

        DateRange other = (DateRange) o;

        return 0 == this.compareTo(other);
    }

    @Override
    public int hashCode() {
        int result = getGueltigAb().hashCode();
        result = 31 * result + getGueltigBis().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("gueltigAb: " + gueltigAb.toString())
                .append("gueltigBis: "+ gueltigBis.toString())
                .toString();
    }

    @Override
    public int compareTo(DateRange o) {
        int cmp = getGueltigAb().compareTo(o.getGueltigAb());
        if (cmp == 0) {
            cmp = getGueltigBis().compareTo(o.getGueltigBis());
        }
        return cmp;
    }
}
