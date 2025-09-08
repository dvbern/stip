import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { provideRouter } from '@angular/router';
import { provideMockStore } from '@ngrx/store/testing';
import { provideOAuthClient } from 'angular-oauth2-oidc';

import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedPatternAppHeaderComponent } from './shared-pattern-app-header.component';

describe('SharedPatternAppHeaderComponent', () => {
  let component: SharedPatternAppHeaderComponent;
  let fixture: ComponentFixture<SharedPatternAppHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedPatternAppHeaderComponent, getTranslocoModule()],
      providers: [
        provideHttpClient(),
        provideRouter([]),
        provideOAuthClient(),
        provideMockStore(),
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
