package ch.dvbern.stip.api.common.statemachines;

import java.lang.annotation.Annotation;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

import ch.dvbern.stip.api.common.exception.AppErrorException;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.GesuchStatusConfigProducer;
import ch.dvbern.stip.api.common.statemachines.gesuchstatus.handlers.StateChangeHandler;
import ch.dvbern.stip.api.gesuch.entity.Gesuch;
import ch.dvbern.stip.api.gesuch.service.GesuchStatusChangeEventTrigger;
import ch.dvbern.stip.api.gesuch.type.GesuchStatusChangeEvent;
import ch.dvbern.stip.api.gesuch.type.Gesuchstatus;
import com.github.oxo42.stateless4j.StateMachine;
import com.github.oxo42.stateless4j.transitions.Transition;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.util.TypeLiteral;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GesuchStatusStateMachineTest {
    @Test
    void failsWithoutGesuchAsParameter() {
        final var gesuch = new Gesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var config = new GesuchStatusConfigProducer(new InstanceMock(new ArrayList<>())).createStateMachineConfig();
        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                    .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );

        final var trigger = GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT);

        assertThrows(AppErrorException.class, () -> sm.fire(trigger, null));
    }

    @Test
    void transitionCallsCorrectHandler() {
        final var doesHandle = new StateChangeHandlerMock();
        final var doesNotHandle = new StateChangeDoesNotHandleMock();
        final var instance = new InstanceMock(new ArrayList<>() {{
            add(doesHandle);
            add(doesNotHandle);
        }});

        final var gesuch = new Gesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var config = new GesuchStatusConfigProducer(instance).createStateMachineConfig();
        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                    .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );
        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT), gesuch);

        assertThat(
                "The state machine did not find a handler to call",
                doesHandle.isHandled(),
                is(true)
        );

        assertThat(
                "The state machine called the wrong handler",
                doesNotHandle.isHandled(),
                is(false)
        );
    }

    @Test
    void transitionCallsHandler() {
        final var handler = new StateChangeHandlerMock();
        final var instance = new InstanceMock(new ArrayList<>() {{
			add(handler);
		}});

        final var gesuch = new Gesuch().setGesuchStatus(Gesuchstatus.IN_BEARBEITUNG_GS);
        final var config = new GesuchStatusConfigProducer(instance).createStateMachineConfig();
        final var sm = new StateMachine<>(
            gesuch.getGesuchStatus(),
            gesuch::getGesuchStatus,
            s -> gesuch.setGesuchStatus(s)
                .setGesuchStatusAenderungDatum(LocalDateTime.now()),
            config
        );
        sm.fire(GesuchStatusChangeEventTrigger.createTrigger(GesuchStatusChangeEvent.EINGEREICHT), gesuch);

        assertThat(
            "The state machine did not find a handler to call",
            handler.isHandled(),
            is(true)
        );
    }

    @Getter
    static class StateChangeDoesNotHandleMock implements StateChangeHandler {
        private boolean handled = false;

        @Override
        public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
            return false;
        }

        @Override
        public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
            handled = true;
        }
    }

    @Getter
	static class StateChangeHandlerMock implements StateChangeHandler {
        private boolean handled = false;

        @Override
        public boolean handles(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition) {
            return true;
        }

        @Override
        public void handle(Transition<Gesuchstatus, GesuchStatusChangeEvent> transition, Gesuch gesuch) {
            handled = true;
        }
    }

    static class InstanceMock implements Instance<StateChangeHandler> {
        private final List<StateChangeHandler> handlers;

        public InstanceMock(List<StateChangeHandler> handlers) {
            this.handlers = handlers;
        }

        @Override
        public Stream<StateChangeHandler> stream() {
            return handlers.stream();
        }

        @Override
        public Instance<StateChangeHandler> select(Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends StateChangeHandler> Instance<U> select(Class<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public <U extends StateChangeHandler> Instance<U> select(TypeLiteral<U> subtype, Annotation... qualifiers) {
            return null;
        }

        @Override
        public boolean isUnsatisfied() {
            return false;
        }

        @Override
        public boolean isAmbiguous() {
            return false;
        }

        @Override
        public void destroy(StateChangeHandler instance) {

        }

        @Override
        public Handle<StateChangeHandler> getHandle() {
            return null;
        }

        @Override
        public Iterable<? extends Handle<StateChangeHandler>> handles() {
            return null;
        }

        @Override
        public StateChangeHandler get() {
            return null;
        }

        @NotNull
        @Override
        public Iterator<StateChangeHandler> iterator() {
            return handlers.iterator();
        }
    }
}
