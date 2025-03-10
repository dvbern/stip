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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import jakarta.validation.Path.Node;
import org.apache.commons.lang3.NotImplementedException;

class ConstraintViolationParser {
    public Violation parse(ConstraintViolation<?> violation) {
        return Violation.of(
            buildPath(violation),
            violation.getConstraintDescriptor().getAnnotation().annotationType().getSimpleName(),
            violation.getMessage()
        );
    }

    public List<Violation> parse(Collection<ConstraintViolation<?>> violation) {
        return violation.stream()
            .map(this::parse)
            .toList();
    }

    private static List<Node> asList(Path propertyPath) {
        return StreamSupport.stream(propertyPath.spliterator(), false)
            .toList();
    }

    private String buildPath(ConstraintViolation<?> constraintViolation) {
        Path propertyPath = constraintViolation.getPropertyPath();
        Iterator<Node> iterator = propertyPath.iterator();

        if (!iterator.hasNext()) {
            throw new NotImplementedException(
                String.format(
                    "Not yet implemented: violations without path %s, %s",
                    propertyPath,
                    constraintViolation
                )
            );
        }

        Node first = iterator.next();
        switch (first.getKind()) {
            case BEAN:
                if (!iterator.hasNext()) {
                    return constraintViolation.getLeafBean().getClass().getSimpleName();
                }
                throw new NotImplementedException("BEAN");
            case PROPERTY:
                return buildNodePath(asList(propertyPath));
            case PARAMETER, METHOD:
                List<Node> nodes = asList(propertyPath);
                List<Node> justParam = nodes.subList(2, nodes.size());
                return buildNodePath(justParam);
            default:
                throw new NotImplementedException(
                    String.format(
                        "Not yet implemented validation exception for type %s, %s",
                        first.getKind(),
                        constraintViolation
                    )
                );
        }
    }

    @SuppressWarnings("java:S125")
    private String buildNodePath(List<Node> path) {
        // example:
        // parent.child[1].property
        // => entries:
        // parent (isInIterable=false,index=null)
        // child (isInIterable=false,index=null)
        // property (isInIterable=true,index=1);

        var pathEntries = new ArrayList<String>();
        for (int i = 0; i < path.size(); i++) {
            Node next = path.get(i);

            if (next.getKey() != null) {
                throw new NotImplementedException("Path with map keys is not implemented: " + path.toString());
            }

            pathEntries.add(i, next.getName());

            if (next.isInIterable()) {
                var prevIdx = i - 1;
                if (prevIdx < 0) {
                    continue;
                }
                String previous = pathEntries.get(prevIdx);
                String prevIndexed = previous + '[' + next.getIndex() + ']';
                pathEntries.set(prevIdx, prevIndexed);
            }
        }

        return String.join(".", pathEntries);
    }

}
