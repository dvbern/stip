import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideMockStore } from '@ngrx/store/testing';

import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';
import { initial } from '@dv/shared/util/remote-data';
import { SteuerdatenStore } from '@dv/shared-data-access-steuerdaten';

import { SachbearbeitungAppFeatureGesuchFormComponent } from './sachbearbeitung-app-feature-gesuch-form.component';

describe('SachbearbeitungAppFeatureGesuchFormComponent', () => {
  let component: SachbearbeitungAppFeatureGesuchFormComponent;
  let fixture: ComponentFixture<SachbearbeitungAppFeatureGesuchFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      providers: [
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
        provideSharedPatternJestTestSetup(),
      ],
      imports: [SachbearbeitungAppFeatureGesuchFormComponent],
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
