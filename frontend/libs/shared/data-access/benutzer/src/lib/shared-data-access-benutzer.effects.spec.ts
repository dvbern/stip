import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { OAuthService, provideOAuthClient } from 'angular-oauth2-oidc';
import { TestScheduler } from 'rxjs/testing';

import { BenutzerService } from '@dv/shared/model/gesuch';

import { loadCurrentBenutzer } from './shared-data-access-benutzer.effects';
import { SharedDataAccessBenutzerApiEvents } from './shared-data-access-benutzer.events';

describe('SharedDataAccessBenutzer Effects', () => {
  let scheduler: TestScheduler;
  let testBed: TestBed;
  let oauthService: OAuthService;

  beforeEach(() => {
    testBed = TestBed.configureTestingModule({
      providers: [provideHttpClient(), provideOAuthClient()],
    });
    scheduler = new TestScheduler((actual, expected) =>
      expect(actual).toEqual(expected),
    );
    oauthService = testBed.inject(OAuthService);
  });

  it('loads Benutzer effect - success', () => {
    scheduler.run(({ expectObservable, hot, cold }) => {
      const benutzerServiceMock = {
        getCurrentBenutzer$: () => cold('150ms a', { a: {} }),
      } as unknown as BenutzerService;

      jest.spyOn(oauthService, 'hasValidIdToken').mockReturnValue(true);
      const eventsMock$ = hot('10ms a', {
        a: SharedDataAccessBenutzerApiEvents.loadCurrentBenutzer(),
      });

      const effectStream$ = testBed.runInInjectionContext(() =>
        loadCurrentBenutzer(eventsMock$, benutzerServiceMock),
      );

      expectObservable(effectStream$).toBe('160ms a', {
        a: SharedDataAccessBenutzerApiEvents.currentBenutzerLoadedSuccess({
          benutzer: {} as any,
        }),
      });
    });
  });
});
