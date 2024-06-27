package ch.dvbern.stip.api.gesuch.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import ch.dvbern.stip.api.familiensituation.entity.Familiensituation;
import ch.dvbern.stip.api.steuerdaten.entity.Steuerdaten;
import ch.dvbern.stip.api.steuerdaten.service.SteuerdatenTabBerechnungsService;
import ch.dvbern.stip.api.steuerdaten.type.SteuerdatenTyp;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.Mockito;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class SteuerdatenTabsRequiredConstraintValidatorTest {
    @ParameterizedTest
    @ArgumentsSource(RequiresTabsTestArgumentsProvider.class)
    void requiresTabsTest(
        final Set<Steuerdaten> steuerdaten,
        final List<SteuerdatenTyp> required,
        final boolean expectedValid
    ) {
        final var service = Mockito.mock(SteuerdatenTabBerechnungsService.class);
        Mockito.when(service.calculateTabs(Mockito.any())).thenReturn(required);

        final var constraintMock = Mockito.mock(SteuerdatenTabsRequiredConstraint.class);
        Mockito.when(constraintMock.property()).thenReturn("foo");

        final var gesuchFormular = new GesuchFormular()
            .setSteuerdaten(steuerdaten)
            .setFamiliensituation(new Familiensituation());

        final var validator = new SteuerdatenTabsRequiredConstraintValidator();
        validator.initialize(constraintMock);
        validator.steuerdatenTabBerechnungsService = service;

        assertThat(validator.isValid(gesuchFormular, null), is(expectedValid));
    }

    private static class RequiresTabsTestArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
            return Stream.of(
                // 1
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.FAMILIE),
                    List.of(SteuerdatenTyp.FAMILIE),
                    true
                ),
                // 2
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.VATER),
                    List.of(SteuerdatenTyp.VATER),
                    true
                ),
                // 3
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER),
                    List.of(SteuerdatenTyp.FAMILIE),
                    false
                ),
                // 4
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER),
                    List.of(SteuerdatenTyp.MUTTER),
                    false
                ),
                // 5
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER),
                    List.of(SteuerdatenTyp.VATER),
                    false
                ),
                // 6
                Arguments.of(
                    createSteuerdatenOfType(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER),
                    List.of(SteuerdatenTyp.MUTTER, SteuerdatenTyp.VATER),
                    true
                )
            );
        }

        public Set<Steuerdaten> createSteuerdatenOfType(final SteuerdatenTyp... typs) {
            final var set = new HashSet<Steuerdaten>();
            for (final var typ : typs) {
                set.add(new Steuerdaten().setSteuerdatenTyp(typ));
            }

            return set;
        }
    }
}
