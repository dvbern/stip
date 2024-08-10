import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';

import { SachbearbeitungAppPatternVerfuegungLayoutComponent } from './sachbearbeitung-app-pattern-verfuegung-layout.component';

describe('SachbearbeitungAppPatternVerfuegungLayoutComponent', () => {
  let component: SachbearbeitungAppPatternVerfuegungLayoutComponent;
  let fixture: ComponentFixture<SachbearbeitungAppPatternVerfuegungLayoutComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SachbearbeitungAppPatternVerfuegungLayoutComponent,
        TranslateModule.forRoot(),
      ],
      providers: [
        GesuchAenderungStore,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideOAuthClient(),
        provideMockStore({
          initialState: {
            configs: {},
          },
        }),
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
