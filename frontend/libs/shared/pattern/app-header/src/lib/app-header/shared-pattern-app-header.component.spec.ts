import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { provideTranslateService } from '@ngx-translate/core';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { SharedPatternAppHeaderComponent } from './shared-pattern-app-header.component';

describe('SharedPatternAppHeaderComponent', () => {
  let component: SharedPatternAppHeaderComponent;
  let fixture: ComponentFixture<SharedPatternAppHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedPatternAppHeaderComponent],
      providers: [
        provideHttpClient(),
        provideRouter([]),
        provideOAuthClient(),
        provideMockStore(),
        provideTranslateService(),
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedPatternAppHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
