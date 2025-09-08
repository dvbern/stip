import { Injectable, effect, inject, signal } from '@angular/core';
import { toSignal } from '@angular/core/rxjs-interop';
import { Title } from '@angular/platform-browser';
import { RouterStateSnapshot, TitleStrategy } from '@angular/router';
import { TranslocoService } from '@jsverse/transloco';
import { getRouterSelectors } from '@ngrx/router-store';
import { Store } from '@ngrx/store';
import { map, withLatestFrom } from 'rxjs';

const { selectTitle, selectUrl } = getRouterSelectors();

const APP_NAME_I18N_KEY = 'app-name';

@Injectable()
export class SharedPatternI18nTitleStrategy extends TitleStrategy {
  private title = inject(Title);
  private translateService = inject(TranslocoService);
  private store = inject(Store);
  private titleSig = signal<{ title?: string; url: string } | null>(null);
  private hasInitializedSig = toSignal(
    this.translateService.events$.pipe(
      map((event) => event.type === 'translationLoadSuccess'),
    ),
  );

  constructor() {
    super();

    // update current  title on language change
    const langChangeWithTitleAndUrlSig = toSignal(
      this.translateService.langChanges$.pipe(
        withLatestFrom(
          this.store.select(selectTitle),
          this.store.select(selectUrl),
        ),
      ),
    );
    effect(() => {
      const [, title, url] = langChangeWithTitleAndUrlSig() ?? [];
      if (url && !title) {
        this.missingTranslationKey(url);
      }
      if (url !== undefined && title !== undefined) {
        this.titleSig.set({ title, url });
      } else {
        this.titleSig.set(null);
      }
    });

    effect(() => {
      const value = this.titleSig();
      const isInitialized = this.hasInitializedSig();
      if (value === null || !isInitialized) {
        return;
      }
      const { title, url } = value;

      if (title !== undefined) {
        const routeTitleTranslation = this.translateService.translate(title);
        const appNameTranslation =
          this.translateService.translate(APP_NAME_I18N_KEY);
        this.title.setTitle(`${routeTitleTranslation} - ${appNameTranslation}`);
      } else {
        this.missingTranslationKey(url);
      }
    });
  }

  // update title on navigation
  updateTitle(routerState: RouterStateSnapshot): void {
    const title = this.buildTitle(routerState);
    this.titleSig.set({ title, url: routerState.url });
  }

  private missingTranslationKey(url: string) {
    console.error(
      `[${SharedPatternI18nTitleStrategy.name}]`,
      `The title translation key for route "${url}" is not defined, please add it to the corresponding route config.`,
    );
  }
}

export function provideSharedPatternI18nTitleStrategy() {
  return [
    {
      provide: TitleStrategy,
      useClass: SharedPatternI18nTitleStrategy,
    },
  ];
}
