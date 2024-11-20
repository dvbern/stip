import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { provideTranslateService } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { provideSharedPatternJestTestSetup } from '@dv/shared/pattern/jest-test-setup';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from './sachbearbeitung-app-pattern-gesuch-header.component';

describe('SachbearbeitungAppPatternGesuchHeaderComponent', () => {
  let component: SachbearbeitungAppPatternGesuchHeaderComponent;
  let fixture: ComponentFixture<SachbearbeitungAppPatternGesuchHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SachbearbeitungAppPatternGesuchHeaderComponent],
      providers: [
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideOAuthClient(),
        provideTranslateService(),
        provideMockStore({
          initialState: {
            gesuchs: {
              cache: {},
            },
            configs: {
              compileTimeConfig: undefined,
            },
          },
        }),
        provideSharedPatternJestTestSetup(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(
      SachbearbeitungAppPatternGesuchHeaderComponent,
    );
    fixture.componentRef.setInput('currentGesuch', null);
    fixture.componentRef.setInput('gesuchPermissions', {});
    fixture.componentRef.setInput('isLoading', false);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
