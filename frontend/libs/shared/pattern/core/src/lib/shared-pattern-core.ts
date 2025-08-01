import {
  HttpBackend,
  provideHttpClient,
  withInterceptors,
} from '@angular/common/http';
import {
  ApplicationConfig,
  ENVIRONMENT_INITIALIZER,
  importProvidersFrom,
  inject,
  isDevMode,
  provideZoneChangeDetection,
} from '@angular/core';
import { provideAnimations } from '@angular/platform-browser/animations';
import {
  Route,
  provideRouter,
  withComponentInputBinding,
  withDisabledInitialNavigation,
  withInMemoryScrolling,
  withRouterConfig,
} from '@angular/router';
import { provideEffects } from '@ngrx/effects';
import { provideRouterStore, routerReducer } from '@ngrx/router-store';
import { Store, provideState, provideStore } from '@ngrx/store';
import { provideStoreDevtools } from '@ngrx/store-devtools';
import {
  MissingTranslationHandler,
  MissingTranslationHandlerParams,
  TranslateLoader,
  provideTranslateService,
} from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';
import { MultiTranslateHttpLoader } from 'ngx-translate-multi-http-loader';

import {
  sharedDataAccessBenutzerEffects,
  sharedDataAccessBenutzersFeature,
} from '@dv/shared/data-access/benutzer';
import {
  SharedDataAccessConfigEvents,
  sharedDataAccessConfigEffects,
  sharedDataAccessConfigsFeature,
} from '@dv/shared/data-access/config';
import {
  SharedDataAccessLanguageEvents,
  sharedDataAccessLanguageEffects,
  sharedDataAccessLanguageFeature,
} from '@dv/shared/data-access/language';
import {
  CompileTimeConfig,
  SharedModelCompileTimeConfig,
} from '@dv/shared/model/config';
import { provideSharedPatternAppInitialization } from '@dv/shared/pattern/app-initialization';
import { provideSharedAppSettings } from '@dv/shared/pattern/app-settings';
import { withDvGlobalHttpErrorInterceptorFn } from '@dv/shared/pattern/http-error-interceptor';
import { provideSharedPatternI18nTitleStrategy } from '@dv/shared/pattern/i18n-title-strategy';
import { SharedPatternInterceptorDeploymentConfig } from '@dv/shared/pattern/interceptor-deployment-config';
import { provideSharedPatternRouteReuseStrategyConfigurable } from '@dv/shared/pattern/route-reuse-strategy-configurable';
import { provideMaterialDefaultOptions } from '@dv/shared/util/form';

export class ExplicitMissingTranslationHandler
  implements MissingTranslationHandler
{
  handle(params: MissingTranslationHandlerParams) {
    return `${params.key}`;
  }
}

export const metaReducers = [];

export function provideSharedPatternCore(
  appRoutes: Route[],
  compileTimeConfig: CompileTimeConfig,
): ApplicationConfig['providers'] {
  return [
    // providers
    provideOAuthClient(),
    provideSharedPatternAppInitialization(),
    provideSharedAppSettings(compileTimeConfig.appType),
    provideAnimations(),
    provideZoneChangeDetection({ eventCoalescing: true, runCoalescing: true }),
    provideHttpClient(
      withInterceptors([
        SharedPatternInterceptorDeploymentConfig,
        ...withDvGlobalHttpErrorInterceptorFn({ type: 'globalAndLocal' }),
        // STUB add global interceptors for auth, error handling, ...
      ]),
    ),
    provideRouter(
      appRoutes,
      withRouterConfig({
        onSameUrlNavigation: 'reload',
      }),
      withComponentInputBinding(),
      withDisabledInitialNavigation(),
      withInMemoryScrolling({
        anchorScrolling: 'enabled',
        scrollPositionRestoration: 'top',
      }),
    ),
    provideSharedPatternI18nTitleStrategy(),
    provideSharedPatternRouteReuseStrategyConfigurable(),
    provideMaterialDefaultOptions(),

    // state management
    provideStore(
      {
        router: routerReducer,
      },
      {
        metaReducers,
        runtimeChecks: {
          strictStateImmutability: true,
          strictActionImmutability: true,
          strictStateSerializability: true,
          strictActionSerializability: true,
          strictActionWithinNgZone: true,
          strictActionTypeUniqueness: true,
        },
      },
    ),
    provideState(sharedDataAccessBenutzersFeature),
    provideState(sharedDataAccessConfigsFeature),
    provideState(sharedDataAccessLanguageFeature),
    provideEffects(
      sharedDataAccessBenutzerEffects,
      sharedDataAccessConfigEffects,
      sharedDataAccessLanguageEffects,
    ),
    provideTranslateService({
      missingTranslationHandler: {
        provide: MissingTranslationHandler,
        useClass: ExplicitMissingTranslationHandler,
      },
      useDefaultLang: false, // easier to notice missing translations
      loader: {
        provide: TranslateLoader,
        useFactory: () =>
          new MultiTranslateHttpLoader(inject(HttpBackend), [
            { prefix: './assets/i18n/', suffix: '.json' },
            { prefix: './assets/i18n/shared.', suffix: '.json' },
          ]),
      },
    }),
    provideRouterStore(),
    ...(isDevMode() ? [provideStoreDevtools({ connectInZone: true })] : []),

    // modules which don't support Angular Standalone APIs yet
    importProvidersFrom([]),
    {
      provide: SharedModelCompileTimeConfig,
      useFactory: () => new SharedModelCompileTimeConfig(compileTimeConfig),
    },

    // init (has to be last, order matters)
    {
      provide: ENVIRONMENT_INITIALIZER,
      multi: true,
      useValue() {
        const store = inject(Store);
        // rework to ngrxOnEffectsInit once available for functional effects
        // https://twitter.com/MarkoStDev/status/1661094873116581901
        store.dispatch(
          SharedDataAccessConfigEvents.appInit({ compileTimeConfig }),
        );
        store.dispatch(SharedDataAccessLanguageEvents.appInit());
      },
    },
  ];
}
