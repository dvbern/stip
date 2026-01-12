import {
  Directive,
  ElementRef,
  Input,
  Renderer2,
  inject,
  output,
  signal,
} from '@angular/core';
import { FormGroupDirective } from '@angular/forms';

@Directive({
  selector: '[dvSharedUiFormReadonly],[dvSharedUiFormReadonlyToggle]',
  standalone: true,
})
export class SharedUiFormReadonlyDirective {
  formGroup = inject(FormGroupDirective);
  elementRef = inject(ElementRef);
  renderer = inject(Renderer2);
  isReadonly = signal<boolean>(false);
  gotReenabledSig = output<object>();
  private wasPreviouslyReadonly = false;

  @Input() set dvSharedUiFormReadonly(readonly: boolean | undefined) {
    if (readonly) {
      this.disableForm();
      this.wasPreviouslyReadonly = true;
    } else if (this.wasPreviouslyReadonly) {
      this.enableForm();
    }
  }

  @Input() set dvSharedUiFormReadonlyToggle(readonly: boolean | undefined) {
    if (readonly === true) {
      this.disableForm();
      this.wasPreviouslyReadonly = true;
    }
    if (readonly === false) {
      this.enableForm();
      this.wasPreviouslyReadonly = false;
    }
  }

  private disableForm() {
    this.isReadonly.set(true);
    this.formGroup.form.disable({ emitEvent: false });
    this.renderer.addClass(this.elementRef.nativeElement, 'readonly');
  }

  private enableForm() {
    this.isReadonly.set(false);
    this.formGroup.form.enable({ emitEvent: false });
    this.renderer.removeClass(this.elementRef.nativeElement, 'readonly');
    this.gotReenabledSig.emit({});
  }
}
