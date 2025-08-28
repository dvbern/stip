import { provideHttpClient } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NonNullableFormBuilder } from '@angular/forms';
import { NoopAnimationsModule } from '@angular/platform-browser/animations';

// eslint-disable-next-line @nx/enforce-module-boundaries
import { getTranslocoModule } from '@dv/shared/pattern/vitest-test-setup';

import { SharedUiFormAddressComponent } from './shared-ui-form-address.component';

describe('SharedUiFormAddressComponent', () => {
  let component: SharedUiFormAddressComponent;
  let fixture: ComponentFixture<SharedUiFormAddressComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        NoopAnimationsModule,
        SharedUiFormAddressComponent,
        getTranslocoModule(),
      ],
      providers: [provideHttpClient()],
    }).compileComponents();

    fixture = TestBed.createComponent(SharedUiFormAddressComponent);
    component = fixture.componentInstance;
    component.group = SharedUiFormAddressComponent.buildAddressFormGroup(
      TestBed.inject(NonNullableFormBuilder),
    );
    fixture.componentRef.setInput('languageSig', 'de');
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
