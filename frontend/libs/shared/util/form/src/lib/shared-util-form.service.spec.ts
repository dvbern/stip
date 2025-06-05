import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  inject,
  runInInjectionContext,
} from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { provideTranslateService } from '@ngx-translate/core';

import { SharedUtilFormService } from './shared-util-form.service';

@Component({
  selector: 'dv-test',
  template: '',
  changeDetection: ChangeDetectionStrategy.OnPush,
})
class TestComponent {
  injector = inject(Injector);
  form = new FormGroup({});
}

describe('SharedUtilFormService', () => {
  let service: SharedUtilFormService;
  let componentFixture: ComponentFixture<TestComponent>;
  let injector: Injector;
  const wndw = {
    addEventListener: () => undefined,
    removeEventListener: () => undefined,
  };

  beforeEach(async () => {
    jest.spyOn(wndw, 'addEventListener');
    jest.spyOn(wndw, 'removeEventListener');
    await TestBed.configureTestingModule({
      providers: [provideTranslateService()],
      imports: [TestComponent],
    }).compileComponents();
    service = TestBed.inject(SharedUtilFormService);
    componentFixture = TestBed.createComponent(TestComponent);
    injector = componentFixture.componentInstance.injector;
  });

  it('should correctly register and unregister to the beforeUnload event', () => {
    service['wndw'] = wndw as any;
    runInInjectionContext(injector, () => {
      service.registerFormForUnsavedCheck(componentFixture.componentInstance);
      componentFixture.componentRef.destroy();
    });
    expect(wndw.addEventListener).toHaveBeenCalled();
    expect(wndw.removeEventListener).toHaveBeenCalled();
  });

  it('should create a number converter for a form group', () => {
    const form = new FormGroup({
      preis: new FormControl<string | null>(null, [Validators.required]),
      menge: new FormControl<string | null>(null),
    });
    const state = {
      preis: 123,
      menge: 456,
    };
    form.patchValue({ preis: '123' });
    const { toNumber, toString } = service.createNumberConverter(form, [
      'preis',
      'menge',
    ]);
    const formValues = form.getRawValue();
    expect(toNumber(formValues).preis).toBe(123);
    expect(toNumber(formValues).menge).toBe(null);
    expect(toString(state).preis).toBe('123');
    expect(toString(state).menge).toBe('456');

    form.patchValue({ menge: '456' });
    expect(toNumber(form.getRawValue()).menge).toBe(456);
  });
});
