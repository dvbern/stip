import { provideHttpClient } from '@angular/common/http';
import { inject } from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateService, provideTranslateService } from '@ngx-translate/core';

import { SharedUiSelectSearchComponent } from './shared-ui-select-search.component';

type TestType = {
  id: string;
  testId: string;
  displayValueDe: string;
  displayValueFr: string;
};

describe('SharedUiSelectSearchComponent Unit Test', () => {
  let component: SharedUiSelectSearchComponent<TestType>;
  let fixture: ComponentFixture<SharedUiSelectSearchComponent<TestType>>;
  let formControl: FormControl<string | null | undefined>;
  const testId = 'autocomplete-test-id';
  const hintTestId = 'hint-autocomplete-test-id';
  const getElement = <T extends HTMLElement>(testId: string) =>
    (fixture.nativeElement as HTMLElement).querySelector<T>(
      `[data-testid="${testId}"]`,
    ) ??
    (() => {
      throw new Error(`Element with testId ${testId} not found`);
    })();
  const values: TestType[] = [
    {
      id: 'uuid1',
      testId: 'test1',
      displayValueDe: 'Wert 1',
      displayValueFr: 'Valeur 1',
    },
    {
      id: 'uuid2',
      testId: 'test2',
      displayValueDe: 'Ein anderer Wert 2',
      displayValueFr: 'Une autre valeur 2',
    },
    {
      id: 'uuid3',
      testId: 'test3',
      displayValueDe: 'Noch ein Wert 3',
      displayValueFr: 'Encore une valeur 3',
    },
  ];
  const hintValue = values[2];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SharedUiSelectSearchComponent,
        NoopAnimationsModule,
        ReactiveFormsModule,
      ],
      providers: [provideHttpClient(), provideTranslateService()],
    }).compileComponents();
    TestBed.runInInjectionContext(() => {
      inject(TranslateService).use('de');
    });

    fixture = TestBed.createComponent(SharedUiSelectSearchComponent<TestType>);
    component = fixture.componentInstance;
    formControl = new FormControl<string | null | undefined>(undefined);

    // Set up the component inputs
    fixture.componentRef.setInput('labelKeySig', 'label.test.key');
    fixture.componentRef.setInput('valuesSig', values);
    fixture.componentRef.setInput('isEntryValidSig', () => true);
    fixture.componentRef.setInput('invalidValueLabelKeySig', 'error.test.key');
    fixture.componentRef.setInput('languageSig', 'de');
    fixture.componentRef.setInput('zuvorValueSig', hintValue);
    fixture.componentRef.setInput('zuvorHintTestIdSig', hintTestId);
    fixture.componentRef.setInput('testIdSig', testId);

    // Connect the component to a form control
    component.registerOnChange((value) => formControl.setValue(value));
    component.registerOnTouched(() => formControl.markAsTouched());

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should select a value and emit its ID', async () => {
    // Simulate selecting values[1] from the select
    const firstValueOption = component
      .filteredValuesSig()
      .find((land) => land.displayValueDe.includes(values[1].displayValueDe));

    component.form.controls.select.setValue(firstValueOption);
    fixture.detectChanges();

    // Verify that the correct ID was emitted
    expect(formControl.value).toBe(values[1].id);
  });

  it('should display correct language-specific names', async () => {
    // Simulate selecting values[0] from the select
    const firstValueOption = component
      .filteredValuesSig()
      .find((land) => land.displayValueDe.includes(values[0].displayValueDe));
    component.form.controls.select.setValue(firstValueOption);

    expect(
      component.displayValueWithSig()(
        component.form.controls.select.value as TestType,
      ),
    ).toBe(values[0].displayValueDe);

    // Test French display
    fixture.componentRef.setInput('languageSig', 'fr');
    fixture.detectChanges();

    expect(
      component.displayValueWithSig()(
        component.form.controls.select.value as TestType,
      ),
    ).toBe(values[0].displayValueFr);
  });

  it('should write value and display correct value', () => {
    // Write a value ID to the component
    component.writeValue(values[1].id);
    fixture.detectChanges();

    const selectedLand = component.form.controls.select.value as TestType;
    expect(selectedLand?.id).toBe(values[1].id);
    expect(selectedLand?.displayValueDe).toBe(values[1].displayValueDe);
  });

  it('should clear value when undefined is written', () => {
    // First set a value
    component.writeValue(values[0].id);
    fixture.detectChanges();

    // Then clear it
    component.writeValue(undefined);
    fixture.detectChanges();

    expect(component.form.controls.select.value).toBeUndefined();
  });

  it('should show zuvor hint when provided', () => {
    fixture.componentRef.setInput('zuvorValueSig', hintValue.id);
    fixture.detectChanges();

    expect(getElement(hintTestId)?.textContent).toBe(hintValue.displayValueDe);
  });

  it('should disable/enable control when setDisabledState is called', () => {
    // Initially enabled
    expect(component.form.controls.select.enabled).toBe(true);

    // Disable
    component.setDisabledState(true);
    expect(component.form.controls.select.disabled).toBe(true);

    // Enable again
    component.setDisabledState(false);
    expect(component.form.controls.select.enabled).toBe(true);
  });
});
