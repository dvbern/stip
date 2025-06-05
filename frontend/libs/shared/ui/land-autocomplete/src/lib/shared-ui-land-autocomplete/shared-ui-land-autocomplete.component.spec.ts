import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideTranslateService } from '@ngx-translate/core';

import { SharedUiLandAutocompleteComponent } from './shared-ui-land-autocomplete.component';

describe('SharedUiLandAutocompleteComponent', () => {
  let component: SharedUiLandAutocompleteComponent;
  let fixture: ComponentFixture<SharedUiLandAutocompleteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SharedUiLandAutocompleteComponent, NoopAnimationsModule],
      providers: [provideHttpClient(), provideTranslateService()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiLandAutocompleteComponent);
    component = fixture.componentInstance;
    fixture.componentRef.setInput('languageSig', 'de');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
