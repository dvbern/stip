package ch.dvbern.stip.api.common.i18n.translations;

import ch.dvbern.oss.commons.i18nl10n.AppLanguage;
import ch.dvbern.oss.commons.i18nl10n.HttpAcceptLanguageHeaderParser;
import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.PreMatching;
import jakarta.ws.rs.ext.Provider;
import lombok.Getter;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

@Provider
@PreMatching
@RequestScoped
public class HttpAcceptLanguageRequestFilter implements ContainerRequestFilter {

    private static final String ACCEPT_LANGUAGE = "Accept-Language";

    @Getter
    private Optional<AppLanguage> appLanguage = Optional.empty();

    private final Function<@Nullable String, AppLanguage> headerParser;

    public HttpAcceptLanguageRequestFilter() {
        var parser = HttpAcceptLanguageHeaderParser.forAppLanguages(
            Set.of(AppLanguages.values()),
            AppLanguages.DEFAULT
        );
        this.headerParser = parser::parse;
    }

    @Override
    public void filter(ContainerRequestContext requestContext) {
        var acceptLanguageHeader = requestContext.getHeaderString(ACCEPT_LANGUAGE);

        appLanguage = Optional.of(headerParser.apply(acceptLanguageHeader));
    }

}
