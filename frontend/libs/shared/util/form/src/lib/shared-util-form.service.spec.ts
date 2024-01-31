import {
  ChangeDetectionStrategy,
  Component,
  Injector,
  inject,
  runInInjectionContext,
} from '@angular/core';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormGroup } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

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
    addEventListener: () => {},
    removeEventListener: () => {},
  };

  beforeEach(async () => {
    jest.spyOn(wndw, 'addEventListener');
    jest.spyOn(wndw, 'removeEventListener');
    await TestBed.configureTestingModule({
      imports: [TranslateModule.forRoot()],
      declarations: [TestComponent],
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
});
