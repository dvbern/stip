import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { SteuerdatenStore } from '@dv/sachbearbeitung-app/data-access/steuerdaten';
import { GesuchInfoStore } from '@dv/shared/data-access/gesuch-info';
import {
  getTranslocoModule,
  provideSharedPatternVitestTestSetup,
} from '@dv/shared/pattern/vitest-test-setup';
import { initial } from '@dv/shared/util/remote-data';

import { SachbearbeitungAppFeatureGesuchFormComponent } from './sachbearbeitung-app-feature-gesuch-form.component';

describe('SachbearbeitungAppFeatureGesuchFormComponent', () => {
  let component: SachbearbeitungAppFeatureGesuchFormComponent;
  let fixture: ComponentFixture<SachbearbeitungAppFeatureGesuchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SachbearbeitungAppFeatureGesuchFormComponent,
        getTranslocoModule(),
      ],
      providers: [
        GesuchStore,
        GesuchInfoStore,
        SteuerdatenStore,
        provideHttpClient(),
        provideMockStore({
          initialState: {
            gesuchs: {
              cache: {
                gesuchId: null,
                gesuchFormular: null,
              },
              steuerdatenTabs: initial(),
            },
            globalNotifications: { globalNotificationsById: {} },
            configs: {},
          },
        }),
        provideSharedPatternVitestTestSetup(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(
      SachbearbeitungAppFeatureGesuchFormComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
