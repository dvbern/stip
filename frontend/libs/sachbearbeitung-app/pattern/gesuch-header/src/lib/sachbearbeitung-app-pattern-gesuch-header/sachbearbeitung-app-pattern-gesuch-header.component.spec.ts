import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { TranslateModule } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { GesuchAenderungStore } from '@dv/shared/data-access/gesuch-aenderung';

import { SachbearbeitungAppPatternGesuchHeaderComponent } from './sachbearbeitung-app-pattern-gesuch-header.component';

describe('SachbearbeitungAppPatternGesuchHeaderComponent', () => {
  let component: SachbearbeitungAppPatternGesuchHeaderComponent;
  let fixture: ComponentFixture<SachbearbeitungAppPatternGesuchHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SachbearbeitungAppPatternGesuchHeaderComponent,
        TranslateModule.forRoot(),
      ],
      providers: [
        GesuchAenderungStore,
        provideRouter([]),
        provideHttpClient(),
        provideHttpClientTesting(),
        provideOAuthClient(),
        provideMockStore(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(
      SachbearbeitungAppPatternGesuchHeaderComponent,
    );
    fixture.componentRef.setInput('currentGesuch', null);
    fixture.componentRef.setInput('navClicked', { value: null });
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
