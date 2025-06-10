import { provideHttpClient } from '@angular/common/http';
import { signal } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { provideTranslateService } from '@ngx-translate/core';

import { Land } from '@dv/shared/model/gesuch';
import { LandLookupService } from '@dv/shared/util-data-access/land-lookup';

import { SharedUiLandAutocompleteComponent } from './shared-ui-land-autocomplete.component';

// Mock land data
const mockLaender: Land[] = [
  {
    id: '1',
    deKurzform: 'Schweiz',
    frKurzform: 'Suisse',
    enKurzform: 'Switzerland',
    itKurzform: 'Svizzera',
    iso3code: 'CHE',
    laendercodeBfs: '8100',
    eintragGueltig: true,
    isEuEfta: true,
  },
  {
    id: '2',
    deKurzform: 'Deutschland',
    frKurzform: 'Allemagne',
    enKurzform: 'Germany',
    itKurzform: 'Germania',
    iso3code: 'DEU',
    laendercodeBfs: '8207',
    eintragGueltig: true,
    isEuEfta: true,
  },
  {
    id: '3',
    deKurzform: 'Österreich',
    frKurzform: 'Autriche',
    enKurzform: 'Austria',
    itKurzform: 'Austria',
    iso3code: 'AUT',
    laendercodeBfs: '8204',
    eintragGueltig: true,
    isEuEfta: true,
  },
];

// Mock LandLookupService
const mockLandLookupService = {
  getCachedLandLookup: jest.fn().mockReturnValue(signal(mockLaender)),
};

describe('SharedUiLandAutocompleteComponent', () => {
  let component: SharedUiLandAutocompleteComponent;
  let fixture: ComponentFixture<SharedUiLandAutocompleteComponent>;
  let formControl: FormControl<string | null | undefined>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiLandAutocompleteComponent,
        NoopAnimationsModule,
        ReactiveFormsModule,
      ],
      providers: [
        provideHttpClient(),
        provideTranslateService(),
        { provide: LandLookupService, useValue: mockLandLookupService },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiLandAutocompleteComponent);
    component = fixture.componentInstance;
    formControl = new FormControl<string | null | undefined>(undefined);

    // Set up the component inputs
    fixture.componentRef.setInput('languageSig', 'de');

    // Connect the component to a form control
    component.registerOnChange((value) => formControl.setValue(value));
    component.registerOnTouched(() => formControl.markAsTouched());

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display filtered countries when typing', async () => {
    const input = fixture.nativeElement.querySelector(
      '[data-testid="land-autocomplete-input"]',
    );

    // Type 'Sch' to filter for Switzerland
    input.value = 'Sch';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Check if the filtered options are displayed correctly
    const filteredLaender = component.laenderValuesSig();
    expect(filteredLaender).toHaveLength(2); // Switzerland and Germany
    expect(filteredLaender[0].deKurzform).toBe('Schweiz');
  });

  it('should select a country and emit its ID', async () => {
    const input = fixture.nativeElement.querySelector(
      '[data-testid="land-autocomplete-input"]',
    );

    // Type to filter for Germany
    input.value = 'Deut';
    input.dispatchEvent(new Event('input'));
    fixture.detectChanges();

    // Simulate selecting Germany from the autocomplete
    const deutschlandOption = mockLaender.find(
      (l) => l.deKurzform === 'Deutschland',
    );
    component.autocompleteControl.setValue(deutschlandOption);
    fixture.detectChanges();

    // Verify that the correct ID was emitted
    expect(formControl.value).toBe('2');
  });

  it('should display correct language-specific names', () => {
    // Test German display
    fixture.componentRef.setInput('languageSig', 'de');
    fixture.detectChanges();

    expect(component.languageDisplayFieldSig()).toBe('deKurzform');

    // Test French display
    fixture.componentRef.setInput('languageSig', 'fr');
    fixture.detectChanges();

    expect(component.languageDisplayFieldSig()).toBe('frKurzform');
  });

  it('should write value and display correct country', () => {
    // Write a country ID to the component
    component.writeValue('1');
    fixture.detectChanges();

    // Verify that Switzerland is selected
    const selectedLand = component.autocompleteControl.value as Land;
    expect(selectedLand?.id).toBe('1');
    expect(selectedLand?.deKurzform).toBe('Schweiz');
  });

  it('should clear value when undefined is written', () => {
    // First set a value
    component.writeValue('1');
    fixture.detectChanges();

    // Then clear it
    component.writeValue(undefined);
    fixture.detectChanges();

    expect(component.autocompleteControl.value).toBeUndefined();
  });

  it('should handle blur event correctly', () => {
    // Set an invalid string value
    component.autocompleteControl.setValue('invalid country');

    // Simulate blur
    component.onBlur();
    fixture.detectChanges();

    // Should clear the invalid value
    expect(component.autocompleteControl.value).toBeUndefined();
    expect(formControl.value).toBeUndefined();
  });

  it('should show zuvor hint when provided', () => {
    fixture.componentRef.setInput('zuvorHintValueSig', '2');
    fixture.detectChanges();

    const hintName = component.zuvorHintLandNameSig();
    expect(hintName).toBe('Deutschland');
  });

  it('should disable/enable control when setDisabledState is called', () => {
    // Initially enabled
    expect(component.autocompleteControl.enabled).toBe(true);

    // Disable
    component.setDisabledState(true);
    expect(component.autocompleteControl.disabled).toBe(true);

    // Enable again
    component.setDisabledState(false);
    expect(component.autocompleteControl.enabled).toBe(true);
  });
});
