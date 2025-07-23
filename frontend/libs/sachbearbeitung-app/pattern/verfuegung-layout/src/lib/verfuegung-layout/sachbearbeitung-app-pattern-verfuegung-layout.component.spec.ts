import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { provideTranslateService } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { GesuchStore } from '@dv/sachbearbeitung-app/data-access/gesuch';
import { provideSharedPatternVitestTestSetup } from '@dv/shared/pattern/vitest-test-setup';

import { SachbearbeitungAppPatternVerfuegungLayoutComponent } from './sachbearbeitung-app-pattern-verfuegung-layout.component';

describe('SachbearbeitungAppPatternVerfuegungLayoutComponent', () => {
  let component: SachbearbeitungAppPatternVerfuegungLayoutComponent;
  let fixture: ComponentFixture<SachbearbeitungAppPatternVerfuegungLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        SachbearbeitungAppPatternVerfuegungLayoutComponent,
      ],
      providers: [
        GesuchStore,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideOAuthClient(),
        provideTranslateService(),
        provideMockStore({
          initialState: {
            gesuchs: {
              gesuch: null,
              gesuchFormular: null,
              cache: {
                gesuch: null,
                gesuchId: null,
                gesuchFormular: null,
              },
            },
            configs: {},
          },
        }),
        provideSharedPatternVitestTestSetup(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(
      SachbearbeitungAppPatternVerfuegungLayoutComponent,
    );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
