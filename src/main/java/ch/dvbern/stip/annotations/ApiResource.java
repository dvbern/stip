package ch.dvbern.stip.annotations;

import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Stereotype;
import jakarta.transaction.Transactional;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@RequestScoped
@Transactional
@Stereotype
@Target(TYPE)
@Retention(RUNTIME)
public @interface ApiResource {
}
