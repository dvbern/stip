import { provideHttpClient } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { GlobalNotificationStore } from '@dv/shared/global/notification';

import { ChangeGesuchsperiodeStore } from './shared-data-access-change-gesuchsperiode.store';

describe('ChangeGesuchsperiodeStore', () => {
  let store: ChangeGesuchsperiodeStore;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        ChangeGesuchsperiodeStore,
        provideMockStore(),
        GlobalNotificationStore,
        provideHttpClient(),
      ],
    });
    store = TestBed.inject(ChangeGesuchsperiodeStore);
  });

  it('is initializable', () => {
    expect(store).toBeTruthy();
  });

  it('has a initial remote data state', () => {
    expect(store.changeGesuchsperiode()).toEqual({
      type: 'initial',
      data: undefined,
      error: undefined,
    });
  });
});
