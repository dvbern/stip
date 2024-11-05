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

package ch.dvbern.stip.api.common.repo;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

public abstract class BaseNummerSeqRepository {
    @Transactional(TxType.REQUIRES_NEW)
    protected void createSequenceIfNotExists(final EntityManager entityManager, final String seqName) {
        // Native queries with parameters result in a prepared statement, which cannot create a sequence
        // Which means we sadly have to String.format this...
        final var createSql = String.format("CREATE SEQUENCE IF NOT EXISTS %s AS INTEGER", seqName);
        final var createQuery = entityManager.createNativeQuery(createSql);
        createQuery.executeUpdate();
    }

    @Transactional(TxType.REQUIRES_NEW)
    protected int getNextValueFromSequence(final EntityManager entityManager, final String seqName) {
        final var selectSql = "SELECT nextval(:seqName)";
        final var selectQuery = entityManager.createQuery(selectSql);
        selectQuery.setParameter("seqName", seqName);

        final var result = selectQuery.getSingleResult();
        return ((Number) result).intValue();
    }
}
